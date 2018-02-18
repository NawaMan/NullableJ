//  Copyright (c) 2017 Nawapunth Manusitthipol (NawaMan).
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
package nawaman.nullable._internal;

import static nawaman.utils.reflection.UReflection.invokeDefaultMethod;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Supplier;

import lombok.val;
import nawaman.nullable.NullValues;
import nawaman.nullable.Nullable;

/**
 * For internal use only, I will turn around if I were you.
 */
@SuppressWarnings("javadoc")
public class ReflectionUtil {
    
    private ReflectionUtil() {}
    
    public static Object invokeNullableProxy(Object proxy, Method method, Object[] args)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        @SuppressWarnings("rawtypes")
        val value = ((Nullable)proxy).get();
        if (value == null) {
            Class<?> returnType = method.getReturnType();
            Object rawNullValue = NullValues.nullValueOf(returnType);
            if (returnType.isPrimitive()) {
                if (returnType == int.class)
                    return ((Integer)rawNullValue).intValue();
                if (returnType == boolean.class)
                    return ((Boolean)rawNullValue).booleanValue();
                if (returnType == long.class)
                    return ((Long)rawNullValue).longValue();
                if (returnType == double.class)
                    return ((Double)rawNullValue).doubleValue();
                
                if (returnType == byte.class)
                    return ((Byte)rawNullValue).byteValue();
                if (returnType == short.class)
                    return ((Short)rawNullValue).shortValue();
                if (returnType == float.class)
                    return ((Float)rawNullValue).floatValue();
                if (returnType == char.class)
                    return ((Character)rawNullValue).charValue();
            }
            
            return returnType.cast(rawNullValue);
        }
        
        return method.invoke(value, args);
    }
    
    public static <T> InvocationHandler createNullableInvocationHandler(Supplier<? extends T> valueSupplier, Class<T> dataClass) {
        val handler = (InvocationHandler)(proxy, method, methodArgs) -> {
            if ("get".equals(method.getName()))
                return valueSupplier.get();
            if ("toString".equals(method.getName()) && (method.getParameterCount() == 0))
                return dataClass.getSimpleName() + "=null";
            if ("hashCode".equals(method.getName()) && (method.getParameterCount() == 0))
                return dataClass.hashCode();
            if ("equals".equals(method.getName()) && (method.getParameterCount() == 1)) {
                if (methodArgs[0] == null)
                    return true;
                if (methodArgs[0] == proxy)
                    return true;
                if (!(dataClass == methodArgs[0].getClass()))
                    return false;
                if (!Nullable.class.isAssignableFrom(methodArgs[0].getClass()))
                    return false;
                @SuppressWarnings("rawtypes")
                boolean isPresent = ((Nullable)proxy).isPresent();
                return !isPresent;
            }
            
            boolean isNullableMethod = Nullable.class == method.getDeclaringClass();
            if (!isNullableMethod)
                return invokeNullableProxy(proxy, method, methodArgs);
            
            if (method.isDefault()) {
                return invokeDefaultMethod(proxy, method, methodArgs);
            }
            
            // TODO - Might have to deal with hashCode toString and equals.
            
            return null;
        };
        return handler;
    }
    
}
