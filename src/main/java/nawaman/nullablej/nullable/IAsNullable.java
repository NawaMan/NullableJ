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
