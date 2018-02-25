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

import java.util.function.Supplier;

/**
 * This class signify that it is value ({@code get(...)} is live.)
 * 
 * @param <TYPE>  the data type of the value wrapped by this Nullable.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public interface LiveNullable<TYPE> extends Nullable<TYPE> {
    
    /**
     * Returns the Nullable value from the value of the given supplier.
     * 
     * @param theSupplier  the supplier of the value.
     * @return  the Nullable of the value.
     * 
     * @param <OBJECT>  the data type.
     */
    public static <OBJECT> LiveNullable<OBJECT> from(Supplier<? extends OBJECT> theSupplier) {
        return ()-> {
            try {
                return theSupplier.get();
            } catch (NullPointerException e) {
                return null;
            }
        };
    }
    
}
