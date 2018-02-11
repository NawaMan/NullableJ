package nawaman.nullable;

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
public interface Nullable<TYPE> {
    
    @SuppressWarnings({ "rawtypes", "javadoc" })
    public static final Nullable EMPTY = (Nullable)()->null;
    
    /**
     * Returns the Nullable value of the given value.
     * 
     * @param theGivenValue  the given value.
     * @return  the Nullable of the given value.
     */
    public static <OBJECT> Nullable<OBJECT> of(OBJECT theGivenValue) {
        return (Nullable<OBJECT>)()->theGivenValue;
    }
    
    /**
     * Return Nullable which has no value.
     * 
     * @return  the Nullable of the no value.
     */
    @SuppressWarnings("unchecked")
    public static <OBJECT> Nullable<OBJECT> empty() {
        return (Nullable<OBJECT>)EMPTY;
    }
    
    
    /**
     * Returns the value.
     * 
     * @return  the value of hold by this nullable.
     */
    public TYPE get();
    
    
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
     * @param theCondition 
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
     */
    public default <TARGET> Nullable<TARGET> flatMap(Function<? super TYPE, Nullable<TARGET>> mapper) {
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
     */
    public default <TARGET> Nullable<TARGET> map(Function<? super TYPE,? extends TARGET> mapper) {
        val value = get();
        if (value == null)
            return Nullable.empty();
            
        val newValue = mapper.apply(value);
        return Nullable.of(newValue);
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
    public default TYPE ifPresent(Runnable theAction) {
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
    public default TYPE ifPresent(Runnable theAction, Runnable elseRunnable) {
        val value = get();
        if (value != null)
            theAction.run();
        
        elseRunnable.run();
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
