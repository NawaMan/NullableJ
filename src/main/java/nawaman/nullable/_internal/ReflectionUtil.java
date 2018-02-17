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
import nawaman.nullable.Nullable;
import nawaman.nullable.NullableObject;

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
            return returnType.cast(NullableObject.of(null, returnType));
        }
        
        return method.invoke(value, args);
    }
    
    public static <T> InvocationHandler createNullableInvocationHandler(Supplier<T> valueSupplier) {
        val handler = (InvocationHandler)(proxy, method, methodArgs) -> {
            if ("get".equals(method.getName()))
                return valueSupplier.get();
            
            boolean isNullableMethod = Nullable.class == method.getDeclaringClass();
            if (!isNullableMethod)
                return invokeNullableProxy(proxy, method, methodArgs);
            
            if (method.isDefault()) {
                return invokeDefaultMethod(proxy, method, methodArgs);
            }
            
            return null;
        };
        return handler;
    }
    
}
