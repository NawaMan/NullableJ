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
package nawaman.nullable.strategies;

import static java.util.Arrays.stream;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Function;

import lombok.val;
import lombok.experimental.ExtensionMethod;
import nawaman.nullable.NullableJ;

/**
 * This abstract class contains many useful methods for finders that look into field and methods.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
@ExtensionMethod({ NullableJ.class })
public class AbstractFromClassElementFinder {
    
    protected static final <T> boolean isPublicStaticFinalCompatible(Class<T> clzz, final java.lang.Class<?> type,
            final int modifiers) {
        boolean isPublicStaticFinalCompatible
            =  !Modifier.isStatic(modifiers)
            || !Modifier.isPublic(modifiers)
            || !Modifier.isFinal(modifiers)
            || !clzz.isAssignableFrom(type);
        return isPublicStaticFinalCompatible;
    }
    
    @SuppressWarnings("unchecked")
    protected static <T> T getPublicStaticFinalCompatibleField(Class<T> clzz, Function<Field, Object> supplier) {
        for (val field : clzz.getDeclaredFields()) {
            val type      = field.getType();
            val modifiers = field.getModifiers();
            if (isPublicStaticFinalCompatible(clzz, type, modifiers))
                continue;
            
            val value = supplier.apply(field);
            if (value._isNotNull())
                return (T)value;
        }
        return null;
    }
    
    protected static final boolean checkAnnotationForName(Annotation[] annotations, String annotationName) {
        boolean hasAnnotation = stream(annotations)
                .filter(a-> a.annotationType().getSimpleName().equals(annotationName))
                .findAny()
                .isPresent();
        return hasAnnotation;
    }
    
    @SuppressWarnings("unchecked")
    protected static final <T> Object getValueFromField(Class<T> clzz, Field field) {
        try { return (T)field.get(clzz); }
        catch (IllegalArgumentException | IllegalAccessException e) {
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
    protected static final <T> Object getValueFromMethod(Class<T> clzz, Method method) {
        try {
            return (T)method.invoke(clzz);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
    protected static final <T> T getPublicStaticFinalCompatibleMethod(Class<T> clzz, Function<Method, Object> supplier) {
        for (val method : clzz.getDeclaredMethods()) {
            val type      = method.getReturnType();
            val modifiers = method.getModifiers();
            if (isPublicStaticFinalCompatible(clzz, type, modifiers))
                continue;
            if (!clzz.getTypeParameters()._isEmpty())
                continue;
            
            val value = supplier.apply(method);
            if (value._isNotNull())
                return (T)value;
        }
        return null;
    }
}
