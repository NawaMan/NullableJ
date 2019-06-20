//  Copyright (c) 2017-2018 Nawapunth Manusitthipol (NawaMan).
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
package nullablej.utils.reflection;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import lombok.NonNull;
import lombok.val;
import nullablej.utils.reflection.exception.NotDefaultMethodException;

/**
 * Utility class relating to dynamic proxy.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class UProxy {
    
    /**
     * Invoke the default of an interface method of the proxy object given the methodArgs.
     * 
     * @param proxy       the proxy object.
     * @param method      the method -- this has to be a default object.
     * @param methodArgs  the arguments for the invocation.
     * @return  the invocation result.
     * @throws NotDefaultMethodException  if the method is not a default method - to avoid this use {@code Method.isDefault()}.
     * @throws Throwable                  any exception that might occur.
     */
    public static Object invokeDefaultMethod(@NonNull Object proxy, @NonNull Method method, Object[] methodArgs) 
                    throws NotDefaultMethodException, Throwable {
        // TODO - See if we can avoid this in case it is already done.
        val defaultMethod  = getDefaultMethod(method);
        val declaringClass = defaultMethod.getDeclaringClass();
        val lookup         = getLookup(declaringClass);
        return invoke(lookup, declaringClass, method, proxy, methodArgs);
    }
    
    // Thanks to https://blog.jooq.org/2018/03/28/correct-reflective-access-to-interface-default-methods-in-java-8-9-10
    private static Object invoke(Lookup lookup, Class<?> type, Method method, Object proxy, Object[] methodArgs)
            throws IllegalAccessException, Throwable {
        val mthdName       = method.getName();
        val mthdReturnType = method.getReturnType();
        val mthdParamTypes = method.getParameterTypes();
        val mthdType       = MethodType.methodType(mthdReturnType, mthdParamTypes);
        val result = lookup
                .findSpecial(type, mthdName, mthdType, type)
                .bindTo(proxy)
                .invokeWithArguments(methodArgs);
        return result;
    }
    
    private static Method getPrivateLookupInMethod() throws NoSuchMethodException {
        try {
            return MethodHandles.class.getMethod("privateLookupIn", new Class[] { Class.class, Lookup.class });
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
    
    private static Lookup getLookup(Class<?> type) throws Exception {
        val rawLookup = MethodHandles.lookup();
        val method = getPrivateLookupInMethod();
        if (method != null) {
            val lookup = (Lookup)method.invoke(MethodHandles.class, type, rawLookup);
            return lookup;
        } else {
            Constructor<Lookup> constructor = Lookup.class.getDeclaredConstructor(Class.class);
            constructor.setAccessible(true);
            val lookup = constructor.newInstance(type);
            return lookup;
        }
    }
    
    private static Method getDefaultMethod(Method method) {
        if (method.isDefault())
            return method;
        
        for (Class<?> superInterface : method.getDeclaringClass().getInterfaces()) {
            Method defaultMethod = getDefaultMethodFrom(method, superInterface);
            if (defaultMethod != null)
                return defaultMethod;
        }
        throw new NotDefaultMethodException(method);
    }
    
    private static Method getDefaultMethodFrom(Method method, Class<?> thisInterface) {
        try {
            Method foundMethod = thisInterface.getDeclaredMethod(method.getName(), method.getParameterTypes());
            if (foundMethod.isDefault())
                return foundMethod;
        } catch (NoSuchMethodException | SecurityException e) {
            
        }
        
        for (Class<?> superInterface : thisInterface.getInterfaces()) {
            Method defaultMethod = getDefaultMethodFrom(method, superInterface);
            if (defaultMethod != null)
                return defaultMethod;
        }
        
        return null;
    }
    
}
