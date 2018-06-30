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
package nawaman.nullablej.nullable;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import lombok.val;

/**
 * An implementation of Optional using interface and default methods.
 * 
 * This interface fully implements Java Optional exception for a few behaviours
 * Being an interface allowing it to be extended -- look at an example in the unit test.
 * 
 * Differences from Optional.
 * <ul>
 *   <li>Method `of` accepts both `null` and non `null` value.</li>
 *   <li>Method `get` does not throw NullPointerException if the value is null.</li>
 *   <li>Methods from Object class are not override (hashCode, equals, toString).</li>
 * </ul>
 * 
 * @param <TYPE>  the data type of the value wrapped by this Nullable.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
@FunctionalInterface
public interface Nullable<TYPE> extends Supplier<TYPE>, IAsNullable<TYPE> {
    
    @SuppressWarnings({ "rawtypes", "javadoc", "unchecked" })
    public static final Nullable EMPTY = new NullableImpl(null);
    
    //== Factory method ===============================================================================================
    
    /**
     * Returns the Nullable value of the given value.
     * 
     * @param theGivenValue  the given value.
     * @return  the Nullable of the given value.
     * 
     * @param <TYPE>  the data type.
     */
    public static <TYPE> Nullable<TYPE> of(TYPE theGivenValue) {
        if (theGivenValue == null)
            return empty();
        
        return new NullableImpl<TYPE>(theGivenValue);
    }
    
    /**
     * Returns the Nullable value from the value of the given supplier.
     * 
     * @param theSupplier  the supplier of the value.
     * @return  the Nullable of the value.
     * 
     * @param <TYPE>  the data type.
     */
    public static <TYPE> Nullable<TYPE> from(Supplier<? extends TYPE> theSupplier) {
        try {
            return new NullableImpl<TYPE>(theSupplier.get());
        } catch (NullPointerException e) {
            return empty();
        }
    }
    
    /**
     * Returns the Nullable value of the given value.
     * 
     * @param theGivenValue  the given value.
     * @return  the Nullable of the given value.
     * 
     * @param <TYPE>  the data type.
     */
    public static <TYPE> Nullable<TYPE> nullable(TYPE theGivenValue) {
        if (theGivenValue == null)
            return empty();
        
        return new NullableImpl<TYPE>(theGivenValue);
    }
    
    /**
     * Returns the Nullable value from the value of the given supplier.
     * 
     * @param theSupplier  the supplier of the value.
     * @return  the Nullable of the value.
     * 
     * @param <TYPE>  the data type.
     */
    public static <TYPE> Nullable<TYPE> nullable(Supplier<? extends TYPE> theSupplier) {
        try {
            return new NullableImpl<TYPE>(theSupplier.get());
        } catch (NullPointerException e) {
            return empty();
        }
    }
    
    /**
     * Return Nullable which has no value.
     * 
     * NOTE: This is alias for {@code Nullable#ofNull()}.
     * 
     * @return  the Nullable of the no value.
     * 
     * @param <TYPE>  the data type.
     */
    @SuppressWarnings("unchecked")
    public static <TYPE> Nullable<TYPE> empty() {
        return (Nullable<TYPE>)EMPTY;
    }
    
    /**
     * Return Nullable which has no value.
     * 
     * NOTE: This is alias for {@code Nullable#empty()}.
     * 
     * @return  the Nullable of the no value.
     * 
     * @param <TYPE>  the data type.
     */
    @SuppressWarnings("unchecked")
    public static <TYPE> Nullable<TYPE> ofNull() {
        return (Nullable<TYPE>)EMPTY;
    }
    
    //== Functional method ============================================================================================
    
    /**
     * Returns the value.
     * 
     * @return  the value of hold by this nullable.
     */
    public TYPE get();
    
    //== Default methods ==============================================================================================
    
    @Override
    public default Nullable<TYPE> asNullable() {
        return this;
    }
    
    /**
     * Returns the value if it is not null or the fallbackValue otherwise
     * 
     * @param fallbackValue  the fallback value.
     * @return  the value or the fallback value.
     */
    public default TYPE orElse(TYPE fallbackValue) {
        val value = get();
        return (value != null) ? value : fallbackValue;
    }
    
    /**
     * Returns the value if it is not null or the value from the fallbackSupplier otherwise
     * 
     * @param fallbackSupplier  the fallbackSupplier.
     * @return  the value or the fallback value.
     */
    public default TYPE orElseGet(Supplier<? extends TYPE> fallbackSupplier) {
        val value = get();
        return (value != null) ? value : fallbackSupplier.get();
    }
    
    /**
     * Returns the value if it is not null or throw the exception from the exceptionSupplier
     * 
     * @param exceptionSupplier  the exception supplier.
     * @return  the value.
     * @param <THROWABLE> the exception to be thrown if the value is null.
     * @throws THROWABLE  if the value is null.
     */
    public default <THROWABLE extends Throwable> TYPE orElseThrow(Supplier<? extends THROWABLE> exceptionSupplier) throws THROWABLE {
        val value = get();
        if (value == null)
            throw exceptionSupplier.get();
        
        return get();
    }
    
    /**
     * Returns the value if it is not null or throw the exception from the exceptionSupplier
     * 
     * @return  the value.
     * @throws NullPointerException when the value is null.
     */
    public default TYPE orElseThrow() throws NullPointerException {
        val value = get();
        if (value == null)
            throw new NullPointerException();
        
        return get();
    }
    
    /**
     * Returns the value if it is not null or the value from the fallbackSupplier otherwise
     * 
     * @param fallbackSupplier  the fallbackSupplier.
     * @return  the value or the fallback value.
     */
    @SuppressWarnings("unchecked")
    public default Nullable<TYPE> or(Supplier<? extends Nullable<? extends TYPE>> fallbackSupplier) {
        val value = get();
        return (value != null) ? this : (Nullable<TYPE>) fallbackSupplier.get();
    }
    
    /**
     * Returns this Nullable object if the value is null or fail the condition test otherwise return empty Nullable.
     * 
     * @param theCondition  the condition to be filter in.
     * @return  this object or empty Nullable.
     */
    public default Nullable<TYPE> filter(Predicate<? super TYPE> theCondition) {
        val value = get();
        if (value == null)
            return this;
        
        val isPass = theCondition.test(value);
        if (!isPass)
            return Nullable.empty();
        
        return this;
    }
    
    /**
     * Apply the mapper if the value is not null then return the result Nullable otherwise return empty Nullable.
     * 
     * @param mapper  the mapper.
     * @return  the result Nullable or empty Nullable.
     * @param <TARGET>  the target of the mapping.
     */
    public default <TARGET> Nullable<TARGET> flatMap(Function<? super TYPE, ? extends Nullable<TARGET>> mapper) {
        val value = get();
        if (value == null)
            return Nullable.empty();
            
        val newNullableValue = mapper.apply(value);
        return newNullableValue;
    }
    
    /**
     * Apply the mapper if the value is not null then return the result wrapped with Nullable otherwise return empty Nullable.
     * 
     * @param mapper  the mapper.
     * @return  the result Nullable or empty Nullable.
     * @param <TARGET>  the target of the mapping.
     */
    public default <TARGET> Nullable<TARGET> map(Function<? super TYPE, TARGET> mapper) {
        val value = get();
        if (value == null)
            return Nullable.empty();
            
        val newValue = mapper.apply(value);
        return Nullable.of(newValue);
    }
    
    /**
     * Run a body of code with the value is not null. Then returns itself.
     * 
     * @param theConsumer  the consumer.
     * @return  the value.
     */
    public default Nullable<TYPE> peek(Consumer<? super TYPE> theConsumer) {
        val value = get();
        if (value != null)
            theConsumer.accept(value);
        
        return this;
    }
    
    /**
     * Check if the value is not null.
     * 
     * @return {@code true} if the value is not null.
     */
    public default boolean isPresent() {
        val value = get();
        return (value != null);
    }
    
    /**
     * Check if the value is null.
     * 
     * @return {@code true} if the value is not null.
     */
    public default boolean isNull() {
        val value = get();
        return (value == null);
    }
    
    /**
     * Check if the value is not null.
     * 
     * @return {@code true} if the value is not null.
     */
    public default boolean isNotNull() {
        val value = get();
        return (value != null);
    }
    
    /**
     * If the value is not null, pass it to the consumer.
     * 
     * @param theConsumer  the consumer.
     * @return  the value.
     */
    public default TYPE ifPresent(Consumer<? super TYPE> theConsumer) {
        val value = get();
        if (value != null)
            theConsumer.accept(value);
        
        return value;
    }
    
    /**
     * If the value is not null, pass it to the consumer otherwise run elseRunnable.
     * 
     * @param theConsumer   the consumer.
     * @param elseRunnable  runnable in case the value is null.
     * @return  the value.
     */
    public default TYPE ifPresent(Consumer<? super TYPE> theConsumer, Runnable elseRunnable) {
        val value = get();
        if (value != null)
            theConsumer.accept(value);
        
        elseRunnable.run();
        return value;
    }
    
    /**
     * If the value is not null, run the action.
     * 
     * @param theAction  the action.
     * @return  the value.
     */
    public default TYPE ifPresentRun(Runnable theAction) {
        val value = get();
        if (value != null)
            theAction.run();
        
        return value;
    }
    
    /**
     * If the value is not null, run the action otherwise run elseRunnable.
     * 
     * @param theAction  the action.
     * @param elseRunnable  runnable in case the value is null.
     * @return  the value.
     */
    public default TYPE ifPresentRun(Runnable theAction, Runnable elseRunnable) {
        val value = get();
        if (value != null)
            theAction.run();
        
        elseRunnable.run();
        return value;
    }
    
    /**
     * If the value is not null, pass it to the consumer.
     * 
     * @param theConsumer  the consumer.
     * @return  the value.
     */
    public default TYPE ifNotNull(Consumer<? super TYPE> theConsumer) {
        val value = get();
        if (value != null)
            theConsumer.accept(value);
        
        return value;
    }
    
    /**
     * If the value is not null, pass it to the consumer otherwise run elseRunnable.
     * 
     * @param theConsumer   the consumer.
     * @param elseRunnable  runnable in case the value is null.
     * @return  the value.
     */
    public default TYPE ifNotNull(Consumer<? super TYPE> theConsumer, Runnable elseRunnable) {
        val value = get();
        if (value != null)
            theConsumer.accept(value);
        
        elseRunnable.run();
        return value;
    }
    
    /**
     * If the value is not null, run the action.
     * 
     * @param theAction  the action.
     * @return  the value.
     */
    public default TYPE ifNotNullRun(Runnable theAction) {
        val value = get();
        if (value != null)
            theAction.run();
        
        return value;
    }
    
    /**
     * If the value is not null, run the action otherwise run elseRunnable.
     * 
     * @param theAction  the action.
     * @param elseRunnable  runnable in case the value is null.
     * @return  the value.
     */
    public default TYPE ifNotNullRun(Runnable theAction, Runnable elseRunnable) {
        val value = get();
        if (value != null)
            theAction.run();
        
        elseRunnable.run();
        return value;
    }
    
    /**
     * If the value is null, run the action. If the value is not null, return the value.
     * 
     * @param theAction  the action.
     * @return  the value.
     */
    public default TYPE ifNull(Runnable theAction) {
        val value = get();
        if (value == null)
            theAction.run();
        
        return value;
    }
    
    /**
     * Returns this Nullable as an Optional.
     * 
     * @return the Optional value.
     */
    public default Optional<TYPE> toOptional() {
        return Optional.ofNullable(this.get());
    }
    
    /**
     * Returns the stream containing the value or empty stream if the value is null.
     * 
     * @return the stream.
     */
    public default Stream<TYPE> stream() {
        val value = get();
        return (value != null) ? Stream.of(value) : Stream.empty();
    }
    
}
