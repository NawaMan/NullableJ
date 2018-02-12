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

import lombok.val;
import nawaman.nullable._internal.Nextable;

/**
 * This functional interface represent nextable of Nullable.
 * 
 * Admittedly, I don't know what is a good use of these but after trying around for a while,
 *   I don't want to remove it yet. :-p
 * 
 * @author NawaMan -- nawa@nawaman.net
 *
 * @param <TYPE>  the data type.
 */
@FunctionalInterface
public interface Nullables<TYPE> extends Nullable<TYPE> {
    
    /**
     * Create a Nullables from the given iterable.
     * 
     * @param persons  the person iterable.
     * @return  the Nullables.
     */
    @SuppressWarnings("unchecked")
    public static <TYPE> Nullables<TYPE> from(Iterable<TYPE> persons) {
        @SuppressWarnings("rawtypes")
        val iterator = new Nextable(persons.iterator());
        return (Nullables<TYPE>)()->iterator;
    }
    
    /**
     * Returns the Nextable of the value. This is only used internally.
     * 
     * @return  the nextable.
     */
    public Nextable<TYPE> nextable();
    
    /**
     * @return  the current value.
     **/
    public default TYPE get() {
        return nextable().current();
    }
    
    /**
     * Advance to the next element.
     * 
     * @return  the next element.
     */
    public default TYPE next() {
        return nextable().next();
    }
    
}