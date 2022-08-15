//  MIT License
//  
//  Copyright (c) 2017-2019 Nawa Manusitthipol
//  
//  Permission is hereby granted, free of charge, to any person obtaining a copy
//  of this software and associated documentation files (the "Software"), to deal
//  in the Software without restriction, including without limitation the rights
//  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//  copies of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions:
//  
//  The above copyright notice and this permission notice shall be included in all
//  copies or substantial portions of the Software.
//  
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
//  SOFTWARE.
package nullablej.utils.reflection;

import static java.util.Arrays.asList;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.val;
import nullablej.utils.reflection.exception.NotDefaultMethodException;

/**
 * Utility class relating to dynamic proxy.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class UProxy {
    
    private static final Random random = new Random();
    
    /**
     * Create a dynamic proxy for the given interface that call all default method.
     * 
     * @param <OBJECT>              the main interface type.
     * @param theGivenInterface     the main interface class.
     * @param additionalInterfaces  additional interfaces.
     * @return  the newly created dyamic proxy for the interface.
     */
    @SuppressWarnings("unchecked")
    public static <OBJECT> OBJECT createDefaultProxy(@NonNull Class<OBJECT> theGivenInterface, Class<?> ... additionalInterfaces) {
        val interfaces  = prepareInterfaces(theGivenInterface, additionalInterfaces);
        val classLoader = theGivenInterface.getClassLoader();
        val randomHash  = Math.abs(random.nextInt() / 2);
        val theProxy    = (OBJECT)Proxy.newProxyInstance(classLoader, interfaces, (proxy, method, args)->{
            // TODO Redirect this somewhere.
            if ("toString".equals(method.getName()) && (method.getParameterCount() == 0)) {
                if (proxy instanceof WithToStringHashCodeEquals)
                    return ((WithToStringHashCodeEquals)proxy)._toString();
                return theGivenInterface.getSimpleName() + "@" + randomHash;
            }
            if ("hashCode".equals(method.getName()) && (method.getParameterCount() == 0)) {
                if (proxy instanceof WithToStringHashCodeEquals)
                    return ((WithToStringHashCodeEquals)proxy)._hashCode();
                return randomHash;
            }
            if ("equals".equals(method.getName()) && (method.getParameterCount() == 1)) {
                if (proxy instanceof WithToStringHashCodeEquals)
                    return ((WithToStringHashCodeEquals)proxy).equals(args[0]);
                return proxy == args[0];
            }
            
            return invokeDefaultMethod(proxy, method, args);
        });
        return (OBJECT)theProxy;
    }
    
    private static <OBJECT> java.lang.Class<?>[] prepareInterfaces(Class<OBJECT> theGivenInterface,
            Class<?>... additionalInterfaces) {
        if (!theGivenInterface.isInterface())
            throw new IllegalArgumentException("Interface is required: " + theGivenInterface);
        
        val interfaceList = new ArrayList<Class<?>>();
        interfaceList.add(theGivenInterface);
        if (additionalInterfaces != null) {
            val additionalList = asList(additionalInterfaces);
            additionalList.forEach(each->{
                if (!each.isInterface())
                    throw new IllegalArgumentException("Interface is required: " + each);
            });
            interfaceList.addAll(additionalList);
        }
        
        val interfaces  = interfaceList.toArray(new Class<?>[interfaceList.size()]);
        return interfaces;
    }
    
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
    
    /**
     * Returns the map of method signature and the interface class it was in for all the non-default methods.
     * 
     * @param <T>           the interface data type.
     * @param theInterface  the interface data class.
     * @return  the map of method signature to interface full name or {@code null} if the class is not an interface.
     */
    public static <T> Map<String, String> getNonDefaultMethods(Class<T> theInterface) {
        if (!theInterface.isInterface())
            return null;
        
        return new InterfaceChecker<T>(theInterface).ensureDefaultInterface();
    }
    
    @AllArgsConstructor
    private static class InterfaceChecker<T> {
        
        private Class<T> orgInterface;
        
        private final Map<String, String> abstracts = new TreeMap<String, String>();
        
        private final Set<String> defaults = new TreeSet<String>();
        
        private Map<String, String> ensureDefaultInterface() {
            ensureDefaultInterface(orgInterface);
            defaults.forEach(m -> abstracts.remove(m));
            return abstracts;
        }
        
        private void ensureDefaultInterface(Class<?> element) {
            for (Method method : element.getDeclaredMethods()) {
                if (method.isDefault() || !java.lang.reflect.Modifier.isAbstract(method.getModifiers()))
                     defaults.add(methodSignature(method));
                else abstracts.put(methodSignature(method), element.getCanonicalName());
            }
            
            for (Class<?> intf : element.getInterfaces()) {
                ensureDefaultInterface(intf);
            }
        }
        
        private static String methodSignature(Method method) {
            return method.getName() + "(" + Arrays.toString(method.getParameterTypes()) + "): " + method.getReturnType();
        }
    }
    
}
