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
package nullablej.nullable;

import java.util.function.Supplier;

/**
 * Classes implementing this interface knows hot to turn itself into a nullable.
 * 
 * @param <TYPE> the type of the data.
 * @author NawaMan -- nawa@nawaman.net
 */

public interface IAsNullable<TYPE> {
    
    /** Reusable value of empty. */
    @SuppressWarnings({ "rawtypes", "unchecked"})
    public static final IAsNullable EMPTY = new IAsNullable<Object>() {
        @Override
        public Nullable<Object> asNullable() {
            return Nullable.EMPTY;
        }
        public String toString() {
            return "IAsNullable.EMPTY";
        }
    };
    
    /**
     * Returns the nullable representation of this object.
     * 
     * @return the nullable.
     */
    public Nullable<TYPE> asNullable();
    
    
    // == Factory methods. ==
    
    /**
     * Get the empty value 
     * 
     * @return  the empty IAsNullable.
     * @param <TYPE>  the data type.
     **/
    @SuppressWarnings("unchecked")
    public static <TYPE> IAsNullable<TYPE> empty() {
        return (IAsNullable<TYPE>)EMPTY;
    }
    /**
     * Create an IAsNullable of the given value.
     * 
     * @param value  the data value.
     * @return the IAsNullable value.
     * 
     * @param <TYPE>  the data type.
     */
    public static <TYPE> IAsNullable<TYPE> of(TYPE value) {
        return (IAsNullable<TYPE>)()->Nullable.of(value);
    }
    
    /**
     * Create an IAsNullable of the given value.
     * 
     * @param supplier  the data supplier.
     * @return the IAsNullable value.
     * 
     * @param <TYPE>  the data type.
     */
    public static <TYPE> IAsNullable<TYPE> from_(Supplier<? extends TYPE> supplier) {
        return (IAsNullable<TYPE>)()->Nullable.from(supplier);
    }
    
}
