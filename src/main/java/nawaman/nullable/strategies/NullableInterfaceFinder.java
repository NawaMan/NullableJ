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
package nawaman.nullable.strategies;

import nawaman.nullable.IFindNullValue;
import nawaman.nullable.Nullable;
import nawaman.nullable.NullableData;

/**
 * This finder use {@link Nullable} to create the null value if possible.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class NullableInterfaceFinder implements IFindNullValue {

    @Override
    public <OBJECT> OBJECT findNullValueOf(Class<OBJECT> clzz) {
        return createNullValueForInterface(clzz);
    }
    
    /**
     * Find a nullable object of the given class.
     * 
     * @param clzz  the given class.
     * @return  the nullable object of the type.
     */
    public static <OBJECT, NULLABLE extends Nullable<OBJECT>> OBJECT createNullValueForInterface(Class<OBJECT> clzz) {
        if (!clzz.isInterface())
            return null;
        
        return (OBJECT)NullableData.of(null, clzz);
    }
    
}
