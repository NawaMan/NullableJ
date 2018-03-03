//  ========================================================================
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
package nawaman.nullablej.nullabledata;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import lombok.val;
import nawaman.nullablej.NullableJ;
import nawaman.nullablej._internal.UReflection;
import nawaman.nullablej.nullable.IAsNullable;
import nawaman.nullablej.nullable.Nullable;
import nawaman.nullablej.nullvalue.NullValues;

/**
 * NullableData can create an instance of any interface that act as a null object.
 * 
 * The result object (called nullable data) implements both the data interface and {@link IAsNullable} interface.
 * This object holds the actual value of that interface type.
 * But if that value is null, this nullable data instance will act as null object.
 * The method {@code asNullable} from IAsNullable will return an instance of {@link Nullable} of the object.
 * If the methods has default implementation, the implementation will be called.
 * All other methods will do nothing and return null value of that method type
 *   (by calling {@code NullValues.nullValueOf(returnType)}).
 *   
 * The nullable data implements both the data interface and {@link IAsNullable} instance.
 * But the implement of {@link IAsNullable} is hidden meaning that
 *   the instance has to be casted to IAsNullable before it can be used as such.
 * You can also make the data interface to implement the {@link IAsNullable} interface.
 *   
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class NullableData {
    
    @SuppressWarnings("rawtypes")
    private static final Map<Class, Object> nullableObjects = new ConcurrentHashMap<>();
    
    @SuppressWarnings("rawtypes")
    private static final Supplier nullSupplier = ()->null;
    
    private NullableData() {
        
    }
    
    /**
     * Create a nullable data object.
     * 
     * This method is similar to {@link NullableData#from(Supplier, Class, Class)}
     *   but the value given as object which this method will convert to supplier.
     * 
     * @param dataValue              the value.
     * @param dataObjectClass        the data object class.
     * @param asNullableObjectClass  the combine data and iAsNullable class.
     * @return  the nullable data object.
     * 
     * @param <DATA>        the data type.
     * @param <ASNULLABLE>  the IAsNullable data type.
     */
    @SuppressWarnings("unchecked")
    public static <DATA, ASNULLABLE extends IAsNullable<DATA>> ASNULLABLE of(
            DATA              dataValue, 
            Class<DATA>       dataObjectClass, 
            Class<ASNULLABLE> asNullableObjectClass) {
        if (dataValue == null) {
            return (ASNULLABLE)nullableObjects.computeIfAbsent(asNullableObjectClass, clzz->{
                return from((Supplier<DATA>)nullSupplier, dataObjectClass, asNullableObjectClass, Nullable.empty());
            });
        }
        if ((dataValue instanceof IAsNullable)
                && dataObjectClass.isInstance(dataValue)
                && asNullableObjectClass.isInstance(dataValue))
            return asNullableObjectClass.cast(dataValue);
        
        return from(()->dataValue, dataObjectClass, asNullableObjectClass);
    }
    
    /**
     * Create a Nullable Data object without the combined class.
     * 
     * @param dataValue  the data value.
     * @param dataClass  the data class.
     * @return the nullable data object.
     * 
     * @param <DATA>  the data type.
     */
    @SuppressWarnings("unchecked")
    public static <DATA> DATA of(DATA dataValue, Class<DATA> dataClass) {
        if (dataValue == null) {
            return (DATA)nullableObjects.computeIfAbsent(dataClass, clzz->{
                return from((Supplier<DATA>)nullSupplier, dataClass, Nullable.empty());
            });
        }
        if ((dataValue instanceof IAsNullable)
                && dataClass.isInstance(dataValue))
            return dataClass.cast(dataValue);
        
        
        return from(()->dataValue, dataClass);
    }
    
    /**
     * Create a nullable data object.
     * 
     * A Nullable data object is an object that is the mix of the data and Nullable.
     * It contains both the method from the data interface as well as Nullable.
     * 
     * Exapmle: the data interface might look like this.
     * <pre>
     * public interface Data {
     *     public Value getValue();
     *     public void setValue(Value value);
     * }
     * </pre>
     * Then, the nullable object might look like this:
     * <pre>
     * public interface NullableData extends Data, Nullable&lt;Data&gt; {
     * }
     * </pre>
     * And this method can be used to create an instance of NullableData like this.
     * <pre>
     * NullableData nullableData = Nullable.createNullableObject(()-&gt;myValue, Data.class, NullableData.class);
     * System.out.println(nullableData.getValue());
     * System.out.println(nullableData.map(Data::getValue()).orElse(Value.NO_VALUE));
     * </pre>
     * 
     * @param valueSupplier          the supplier for the value.
     * @param dataObjectClass        the data object class.
     * @param asNullableObjectClass  the combine data and nullable class.
     * @return  the nullable data object.
     * 
     * @param <DATA>        the data type.
     * @param <ASNULLABLE>  the Nullable data type.
     */
    public static <DATA, ASNULLABLE extends IAsNullable<DATA>> ASNULLABLE from(
            Supplier<DATA>    valueSupplier, 
            Class<DATA>       dataObjectClass, 
            Class<ASNULLABLE> asNullableObjectClass) {
        return from(valueSupplier, dataObjectClass, asNullableObjectClass, null);
    }
    
    @SuppressWarnings("unchecked")
    private static <DATA, ASNULLABLE extends IAsNullable<DATA>> ASNULLABLE from(
            Supplier<DATA> valueSupplier, 
            Class<DATA> dataObjectClass, 
            Class<ASNULLABLE> asNullableObjectClass,
            Nullable<DATA> nullable) {
        if (!dataObjectClass.isInterface())
            throw new IllegalArgumentException("The data class must be an interface: " + dataObjectClass);
        
        val interfaces  = new Class<?>[] { dataObjectClass, asNullableObjectClass };
        val classLoader = dataObjectClass.getClassLoader();
        val handler     = createNullableInvocationHandler(valueSupplier, (Class<DATA>)asNullableObjectClass, nullable);
        val rawProxy    = Proxy.newProxyInstance(classLoader, interfaces, handler);
        val proxy       = asNullableObjectClass.cast(rawProxy);
        return proxy;
    }
    
    /**
     * Create a Nullable Data object without the combined class.
     * 
     * @param valueSupplier    the value supplier.
     * @param dataObjectClass  the data object class.
     * @return the nullable data object.
     * 
     * @param <DATA>  the data type.
     */
    public static <DATA> DATA from(Supplier<? extends DATA> valueSupplier, Class<DATA> dataObjectClass) {
        return from(valueSupplier, dataObjectClass, null);
    }
    
    @SuppressWarnings("unchecked")
    private static <DATA> DATA from(
            Supplier<? extends DATA> valueSupplier,
            Class<DATA> dataObjectClass,
            Nullable<DATA> nullable) {
        if (!dataObjectClass.isInterface())
            throw new IllegalArgumentException("The data class must be an interface: " + dataObjectClass);
        
        val interfaces  = new Class<?>[] { dataObjectClass, IAsNullable.class };
        val classLoader = dataObjectClass.getClassLoader();
        val handler     = createNullableInvocationHandler(valueSupplier, dataObjectClass, nullable);
        val rawProxy    = Proxy.newProxyInstance(classLoader, interfaces, handler);
        val proxy       = (DATA)rawProxy;
        return proxy;
    }
    
    
    private static Object invokeNullableProxy(Object proxy, Method method, Object[] args)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        @SuppressWarnings("rawtypes")
        val value = ((IAsNullable)proxy).asNullable().get();
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
    
    private static <DATA> InvocationHandler createNullableInvocationHandler(
            Supplier<? extends DATA> valueSupplier,
            Class<DATA> dataClass,
            Nullable<DATA> nullable) {
        val theNullable = NullableJ._orGet(nullable, ()->Nullable.from(valueSupplier));
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
            
            if ("asNullable".equals(method.getName()) && (method.getParameterCount() == 0))
                return theNullable;
            
            if (method.isDefault()) {
                return UReflection.invokeDefaultMethod(proxy, method, methodArgs);
            }
            
            boolean isICanBeNullableMethod = IAsNullable.class == method.getDeclaringClass();
            if (!isICanBeNullableMethod)
                return invokeNullableProxy(proxy, method, methodArgs);
            
            return null;
        };
        return handler;
    }
    
}
