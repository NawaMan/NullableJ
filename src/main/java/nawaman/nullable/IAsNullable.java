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

import java.util.function.Supplier;

/**
 * Classes implementing this interface knows hot to turn itself into a nullable.
 * 
 * @param <OBJECT> the type of the data.
 * @author NawaMan -- nawa@nawaman.net
 */

public interface IAsNullable<OBJECT> {
    
    /**
     * Returns the nullable representation of this object.
     * 
     * @return the nullable.
     */
    public Nullable<OBJECT> asNullable();
    
    // Factory methods.
    
    /** Reusable value of empty. */
    @SuppressWarnings("rawtypes")
    public static final IAsNullable EMPTY = ()->Nullable.EMPTY;
    
    /**
     * Get the empty value 
     * 
     * @return  the empty IAsNullable.
     * @param <T>  the data type.
     **/
    @SuppressWarnings("unchecked")
    public static <T> IAsNullable<T> empty() {
        return (IAsNullable<T>)EMPTY;
    }
    /**
     * Create an IAsNullable of the given value.
     * 
     * @param value  the data value.
     * @return the IAsNullable value.
     * 
     * @param <T>  the data type.
     */
    public static <T> IAsNullable<T> of(T value) {
        return (IAsNullable<T>)()->Nullable.of(value);
    }
    
    /**
     * Create an IAsNullable of the given value.
     * 
     * @param supplier  the data supplier.
     * @return the IAsNullable value.
     * 
     * @param <T>  the data type.
     */
    public static <T> IAsNullable<T> from(Supplier<? extends T> supplier) {
        return (IAsNullable<T>)()->Nullable.from(supplier);
    }
    
}
