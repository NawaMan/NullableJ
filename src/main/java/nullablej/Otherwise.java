//  MIT License
//  
//  Copyright (c) 2017-2023 Nawa Manusitthipol
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
package nullablej;

import java.util.function.Function;
import java.util.function.UnaryOperator;

import lombok.val;
import nullablej.nullable.Nullable;

/**
 * Otherwise holds two alternative values -- main value and otherwise value.
 * On the surface, it acts as if it is a Nullable of the main value.
 * When desired (such as when the main value is null), the otherwise value can recovered.
 * 
 * There are two flavours of Otherwise, the one with match types and the ones that are not.
 * When the type matches, the method {@code otherwise()} will return the main value if not null,
 *   or the otherwise value if the main value is null.
 * When the type are not matches, there has to be a conversion either to the main value or
 *   to the otherwise value to be able to get the value selectively by null check.
 * To convert the main value to match the otherwise value, see the method {@code mapToMatch(...)}.
 * To convert the otherwise value to match the main value, see the method {@code otherwise(...)}.
 * 
 * There is also ability to map both values, see {@code mapBoth()}.
 * Lastly, there is also a method to map only the main value but maintain the type matching,
 *   see {@code mapValue()}
 * 
 * @author NawaMan -- nawa@nawaman.net
 * 
 * @param <VALUE>      the type of the main value.
 * @param <OTHERWISE>  the type of the otherwise value.
 */
public class Otherwise<VALUE, OTHERWISE> implements Nullable<VALUE> {
    
    private final VALUE value;
    private final OTHERWISE otherwise;
    
    /**
     * Constructs a non-type-match Otherwise object.
     * 
     * @param value      the main value.
     * @param otherwise  the otherwise value.
     */
    public Otherwise(VALUE value, OTHERWISE otherwise) {
        this.value = value;
        this.otherwise = otherwise;
    }
    
    @Override
    public VALUE get() {
        return value;
    }
    
    /**
     * Returns the otherwise value.
     * 
     * @return  the otherwise value.
     */
    public OTHERWISE getOtherwise() {
        return otherwise;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <TARGET> Otherwise<TARGET, OTHERWISE> map(Function<? super VALUE, ? extends TARGET> mapper) {
        VALUE value = get();
        if (value == null)
            return (Otherwise<TARGET, OTHERWISE>)this;
            
        val newValue = mapper.apply(value);
        return new Otherwise<TARGET, OTHERWISE>(newValue, otherwise);
    }
    
    /**
     * Map the main value so that its type will map there otherwise value.
     * 
     * @param mapper  the mapper.
     * @return  the Otherwise with map type.
     */
    public WithMatchTypes<OTHERWISE> mapToMatch(Function<VALUE, OTHERWISE> mapper) {
        VALUE value = get();
        if (value == null)
            return new WithMatchTypes<OTHERWISE>(null, otherwise);
            
        val newValue = mapper.apply(value);
        return new WithMatchTypes<OTHERWISE>(newValue, otherwise);
    }
    
    /**
     * Returns the main value if not null or the mapped value of the otherwise value.
     * 
     * @param mapper  the mapper.
     * @return  the main value if not null or the mapped valued of the otherwise value.
     */
    public VALUE otherwise(Function<OTHERWISE, VALUE> mapper) {
        VALUE value = get();
        if (value == null)
            return mapper.apply(otherwise);
        
        return value;
    }
    
    /**
     * Returns the value if not null otherwise using the throwableFunction to create a throable.
     * Then throw the throwable if not null. Returns null otherwise.
     * 
     * @param <THROWABLE>  the throwable type.
     * @param throwableFunction  the function to produce the throwable.
     * @return  the value if not null.
     * @throws THROWABLE  if the value is null.
     */
    public <THROWABLE extends Throwable> VALUE otherwiseThrow(Function<OTHERWISE, THROWABLE> throwableFunction)
            throws THROWABLE {
        VALUE value = get();
        if (value != null)
            return value;
        
        val throwable = throwableFunction.apply(otherwise);
        if (throwable == null)
            return null;
        
        throw throwable;
    }
    
    @Override
    public String toString() {
        return "[" + this.value + " otherwise " + this.otherwise + "]";
    }
    
    // == Sub class ==
    
    /**
     * Otherwise value with matched types.
     * 
     * @param <OBJECT>  the data type.
     */
    public static class WithMatchTypes<OBJECT> extends Otherwise<OBJECT, OBJECT> {
        
        /**
         * Constructs using single value -- so both main and otherwise is the same value.
         * 
         * @param value  the value.
         */
        public WithMatchTypes(OBJECT value) {
            super(value, value);
        }
        
        /**
         * Constructs using separated value.
         * 
         * @param value      the main value.
         * @param otherwise  the otherwise value.
         */
        public WithMatchTypes(OBJECT value, OBJECT otherwise) {
            super(value, otherwise);
        }
        
        /**
         * Map the main value using the given mapper while maintain the same types.
         * 
         * @param mapper  the mapper.
         * @return  another otherwise with the new value.
         */
        public Otherwise.WithMatchTypes<OBJECT> mapValue(UnaryOperator<? super OBJECT> mapper) {
            val value = get();
            if (value == null)
                return (Otherwise.WithMatchTypes<OBJECT>)this;
                
            @SuppressWarnings("unchecked")
            val newValue = (OBJECT)mapper.apply(value);
            return new Otherwise.WithMatchTypes<OBJECT>(newValue, getOtherwise());
        }
        
        /**
         * Map both values using the given mapper thus maintain the same types.
         * 
         * @param mapper  the mapper.
         * @return  another otherwise with the new values.
         * 
         * @param <TARGET>  the target type of the map.
         */
        public <TARGET> WithMatchTypes<TARGET> mapBoth(Function<? super OBJECT,? extends TARGET> mapper) {
            val value        = this.get();
            val otherwise    = this.getOtherwise();
            val newValue     = (value     == null) ? null : mapper.apply(value);
            val newOtherwise = (otherwise == null) ? null : mapper.apply(otherwise);
            return new WithMatchTypes<TARGET>(newValue, newOtherwise);
        }
        
        /**
         * Returns the main value if not null or the otherwise value if the main value is null.
         * 
         * @return  the final value.
         */
        public OBJECT otherwise() {
            val value = this.get();
            return (value != null) ? value : this.getOtherwise();
        }
        
    }
    
}
