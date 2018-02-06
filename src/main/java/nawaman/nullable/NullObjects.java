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
package nawaman.nullable;

import static java.util.Arrays.stream;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

import lombok.val;

/**
 * Default implementation of {@link IFindNullObject}.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class NullObjects implements IFindNullObject {
    
    
    /** The name of the annotation to make null object. */
    public static final String NULL_OBJET_ANNOTTION_NAME = "NullObject";
    
    /** The name of the null object method. */
    public static final String NULL_OBJECT_METHOD_NAME = "nullObject";
    
    /** The name of the null object field. */
    public static final String NULL_OBJECT_FIELD_NAME1 = "nullObject";
    
    /** The name of the null object field. */
    public static final String NULL_OBJECT_FIELD_NAME2 = "NULL_OBJECT";
    
    
    /** The ready to use instance of NullObjects.  */
    public static final NullObjects instance = new NullObjects();
    
    @SuppressWarnings("rawtypes")
    private static final Map<Class, Supplier> knownNullObjects;
    static {
        @SuppressWarnings("rawtypes")
        Map<Class, Supplier> map = new ConcurrentHashMap<>();
        map.put(byte.class,    ()->0);
        map.put(Byte.class,    ()->Byte.valueOf((byte) 0));
        map.put(short.class,   ()->0);
        map.put(Short.class,   ()->Short.valueOf((short) 0));
        map.put(int.class,     ()->0);
        map.put(Integer.class, ()->Integer.valueOf(0));
        map.put(long.class,    ()->0);
        map.put(Long.class,    ()->Long.valueOf(0L));
        
        map.put(float.class,  ()->0.0);
        map.put(Float.class,  ()->0.0f);
        map.put(double.class, ()->0.0);
        map.put(Double.class, ()->0.0);
        
        map.put(char.class,         ()->' ');
        map.put(Character.class,    ()->' ');
        map.put(String.class,       ()->"");
        map.put(CharSequence.class, ()->"");
        
        map.put(boolean.class,   ()->false);
        map.put(Character.class, ()->Boolean.FALSE);
        
        map.put(Runnable.class, ()->((Runnable)()->{}));
        
        map.put(Collection.class, ()->Collections.EMPTY_LIST);
        map.put(List.class,       ()->Collections.EMPTY_LIST);
        map.put(Set.class,        ()->Collections.EMPTY_SET);
        map.put(Map.class,        ()->Collections.EMPTY_MAP);
        
        knownNullObjects = Collections.unmodifiableMap(map);
    }
    
    @SuppressWarnings("rawtypes")
    private static final Set<Class> knownNewNullObjects;
    static {
        @SuppressWarnings("rawtypes")
        Set<Class> set = new HashSet<>();
        set.add(ArrayList.class);
        set.add(HashSet.class);
        set.add(TreeSet.class);
        set.add(LinkedHashSet.class);
        set.add(HashMap.class);
        set.add(TreeMap.class);
        set.add(LinkedHashMap.class);
        set.add(ConcurrentHashMap.class);
        knownNewNullObjects = Collections.unmodifiableSet(set);
    }
    
    /**
     * Find the null object of the given class.
     * 
     * @param clzz  the class.
     * @return  the null object found. This method return null if it cannot find some.
     */
    public static final <T> T nullObjectOf(Class<T> clzz) {
        val nullObj = instance.findNullObjectOf(clzz);
        return nullObj;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> T findNullObjectOf(Class<T> clzz) {
        val nullFromKnown = knownNullObjects.get(clzz);
        if (nullFromKnown != null)
            return (T)(nullFromKnown.get());
        
        val newKnowNullObject = newKnowNullObject(clzz);
        if (newKnowNullObject != null)
            return (T)newKnowNullObject;
        
        val valueFromAnnotatedField = getValueFromAnnotatedField(clzz);
        if (valueFromAnnotatedField != null)
            return valueFromAnnotatedField;
        
        val valueFromAnnotatedMethod = getValueFromAnnotatedMethod(clzz);
        if (valueFromAnnotatedMethod != null)
            return valueFromAnnotatedMethod;
        
       val valueFromNamedField = getValueFromNamedField(clzz);
        if (valueFromNamedField != null)
            return valueFromNamedField;
        
        val valueFromNamedMethod = getValueFromNamedMethod(clzz);
        if (valueFromNamedMethod != null)
            return valueFromNamedMethod;
        
        return null;
    }
    
    @SuppressWarnings("unchecked")
    private <T> T newKnowNullObject(Class<T> clzz) {
        if (knownNewNullObjects.contains(clzz))
            try {
                return (T)clzz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                // Just keep going...
            }
        
        if (clzz.isArray())
            return (T)Array.newInstance(clzz.getComponentType(), 0);
        
        if (clzz.isEnum()) {
            val enums = clzz.getEnumConstants();
            if (enums.length != 0)
                return (T)enums[0];
        }
        
        return null;
    }
    
    private <T> T getValueFromNamedField(Class<T> clzz) {
        val valueFromNamedField = getPublicStaticFinalCompatibleField(clzz, field->{
            if (!NULL_OBJECT_FIELD_NAME1.equals(field.getName())
             && !NULL_OBJECT_FIELD_NAME2.equals(field.getName()))
                return null;
            
            return getValueFromField(clzz, field);
        });
        return valueFromNamedField;
    }
    
    private <T> T getValueFromAnnotatedField(Class<T> clzz) {
        val valueFromAnnotatedField = getPublicStaticFinalCompatibleField(clzz, field->{
            Annotation[] annotations = field.getAnnotations();
            boolean hasAnnotation = checkAnnotationForName(annotations, NULL_OBJET_ANNOTTION_NAME);
            if (!hasAnnotation)
                return null;
            
            return getValueFromField(clzz, field);
        });
        return valueFromAnnotatedField;
    }
    
    private boolean checkAnnotationForName(Annotation[] annotations, String annotationName) {
        boolean hasAnnotation = stream(annotations)
                .filter(a-> a.annotationType().getSimpleName().equals(annotationName))
                .findAny()
                .isPresent();
        return hasAnnotation;
    }
    
    @SuppressWarnings("unchecked")
    private <T> T getPublicStaticFinalCompatibleField(Class<T> clzz, Function<Field, Object> supplier) {
        for (val field : clzz.getDeclaredFields()) {
            val type      = field.getType();
            val modifiers = field.getModifiers();
            if (isPublicStaticFinalCompatible(clzz, type, modifiers))
                continue;
            
            val value = supplier.apply(field);
            if (value != null)
                return (T)value;
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    private <T> Object getValueFromField(Class<T> clzz, Field field) {
        try { return (T)field.get(clzz); }
        catch (IllegalArgumentException | IllegalAccessException e) {
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
    private <T> T getPublicStaticFinalCompatibleMethod(Class<T> clzz, Function<Method, Object> supplier) {
        for (val method : clzz.getDeclaredMethods()) {
            val type      = method.getReturnType();
            val modifiers = method.getModifiers();
            if (isPublicStaticFinalCompatible(clzz, type, modifiers))
                continue;
            
            val value = supplier.apply(method);
            if (value != null)
                return (T)value;
        }
        return null;
    }
    
    private <T> T getValueFromAnnotatedMethod(Class<T> clzz) {
        val valueFromAnnotatedMethod = getPublicStaticFinalCompatibleMethod(clzz, method->{
            Annotation[] annotations = method.getAnnotations();
            boolean hasAnnotation = checkAnnotationForName(annotations, NULL_OBJET_ANNOTTION_NAME);
            if (!hasAnnotation)
                return null;
            
            return getValueFromMethod(clzz, method);
        });
        return valueFromAnnotatedMethod;
    }
    
    private <T> T getValueFromNamedMethod(Class<T> clzz) {
        val valueFromAnnotatedMethod = getPublicStaticFinalCompatibleMethod(clzz, method->{
            if (!NULL_OBJECT_METHOD_NAME.equals(method.getName()))
                return null;
            
            return getValueFromMethod(clzz, method);
        });
        return valueFromAnnotatedMethod;
    }
    
    @SuppressWarnings("unchecked")
    private <T> Object getValueFromMethod(Class<T> clzz, Method method) {
        try {
            return (T)method.invoke(clzz);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            return null;
        }
    }
    
    private <T> boolean isPublicStaticFinalCompatible(Class<T> clzz, final java.lang.Class<?> type,
            final int modifiers) {
        boolean isPublicStaticFinalCompatible
            =  !Modifier.isStatic(modifiers)
            || !Modifier.isPublic(modifiers)
            || !Modifier.isFinal(modifiers)
            || !clzz.isAssignableFrom(type);
        return isPublicStaticFinalCompatible;
    }
    
}
