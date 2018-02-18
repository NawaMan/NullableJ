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
package nawaman.nullable;

import static nawaman.nullable._internal.ReflectionUtil.createNullableInvocationHandler;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import lombok.val;

class NullableObjectCache {
    
    @SuppressWarnings("rawtypes")
    static final Map<Class, Object> nullableObjects = new ConcurrentHashMap<>();
    
}

/**
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class NullableData {
    
    private NullableData() {
        
    }
    
    /**
     * Create a nullable data object.
     * 
     * This method is similar to {@link NullableData#from(Supplier, Class, Class)}
     *   but the value given as object which this method will convert to supplier.
     * 
     * @param value                the value.
     * @param dataObjectClass      the data object class.
     * @param nullableObjectClass  the combine data and nullable class.
     * @return  the nullable data object.
     * 
     * @param <T>  the data type.
     * @param <N>  the Nullable data type.
     */
    @SuppressWarnings("unchecked")
    public static <T, N extends Nullable<T>> N of(
            T value, 
            Class<T> dataObjectClass, 
            Class<N> nullableObjectClass) {
        if (value == null) {
            return (N)NullableObjectCache.nullableObjects.computeIfAbsent(nullableObjectClass, clzz->{
                return from(()->value, dataObjectClass, nullableObjectClass);
            });
        }
        return from(()->value, dataObjectClass, nullableObjectClass);
    }
    
    /**
     * Create a Nullable Data object without the combined class.
     * 
     * @param value
     * @param dataObjectClass
     * @return the nullable data object.
     * 
     * @param <T>  the data type.
     */
    @SuppressWarnings("unchecked")
    public static <T> T of(T value, Class<T> dataObjectClass) {
        if (value == null) {
            return (T)NullableObjectCache.nullableObjects.computeIfAbsent(dataObjectClass, clzz->{
                return from(()->value, dataObjectClass);
            });
        }
        
        return from(()->value, dataObjectClass);
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
     * public interface NullableData extends Data, Nullable<Data> {
     * }
     * </pre>
     * And this method can be used to create an instance of NullableData like this.
     * <pre>
     * NullableData nullableData = Nullable.createNullableObject(()->myValue, Data.class, NullableData.class);
     * System.out.println(nullableData.getValue());
     * System.out.println(nullableData.map(Data::getValue()).orElse(Value.NO_VALUE));
     * </pre>
     * 
     * @param valueSupplier        the supplier for the value.
     * @param dataObjectClass      the data object class.
     * @param nullableObjectClass  the combine data and nullable class.
     * @return  the nullable data object.
     * 
     * @param <T>  the data type.
     * @param <N>  the Nullable data type.
     */
    @SuppressWarnings("unchecked")
    public static <T, N extends Nullable<T>> N from(
            Supplier<T> valueSupplier, 
            Class<T> dataObjectClass, 
            Class<N> nullableObjectClass) {
        if (!dataObjectClass.isInterface())
            throw new IllegalArgumentException("The data class must be an interface: " + dataObjectClass);
        
        val interfaces  = new Class<?>[] { nullableObjectClass };
        val classLoader = dataObjectClass.getClassLoader();
        val handler     = createNullableInvocationHandler(valueSupplier, (Class<T>)nullableObjectClass);
        val rawProxy    = Proxy.newProxyInstance(classLoader, interfaces, handler);
        val proxy       = nullableObjectClass.cast(rawProxy);
        return proxy;
    }
    
    /**
     * Create a Nullable Data object without the combined class.
     * 
     * @param valueSupplier    the value supplier.
     * @param dataObjectClass  the data object class.
     * @return the nullable data object.
     * 
     * @param <T>  the data type.
     */
    @SuppressWarnings("unchecked")
    public static <T> T from(Supplier<? extends T> valueSupplier, Class<T> dataObjectClass) {
        if (!dataObjectClass.isInterface())
            throw new IllegalArgumentException("The data class must be an interface: " + dataObjectClass);
        
        val interfaces  = new Class<?>[] { dataObjectClass, Nullable.class };
        val classLoader = dataObjectClass.getClassLoader();
        val handler     = createNullableInvocationHandler(valueSupplier, dataObjectClass);
        val rawProxy    = Proxy.newProxyInstance(classLoader, interfaces, handler);
        val proxy       = (T)rawProxy;
        return proxy;
    }
    
}
