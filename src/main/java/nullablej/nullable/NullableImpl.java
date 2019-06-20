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

/**
 * An implementation of Nullable that store the value as value (as opposed to supplier).
 * 
 * NullableImpl is an implementation of Nullable that is truely a monad.
 * 
 * @param <TYPE>  the data type of the value wrapped by this Nullable.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class NullableImpl<TYPE> implements Nullable<TYPE> {
    
    private final TYPE value;
    
    /**
     * Constructs a NullableImple for the given value.
     * 
     * @param  value  the value.
     */
    public NullableImpl(TYPE value) {
        this.value = value;
    }
    
    @Override
    public TYPE get() {
        return value;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if ((obj.getClass() == Nullable.class) || (obj.getClass() == Optional.class))
            return false;
        @SuppressWarnings({ "rawtypes", "unchecked" })
        Object otherValue
                = (Nullable.class.isAssignableFrom(obj.getClass())) ? ((Nullable)obj).get()
                : (Optional.class.isAssignableFrom(obj.getClass())) ? ((Optional)obj).orElse(null)
                : null;
        if (value == null) {
            if (otherValue != null)
                return false;
        } else if (!value.equals(otherValue))
            return false;
        return true;
    }
    
    public String toString() {
        if (value == null)
            return "Nullable.EMPTY";
        
        return "Nullable.of(" + value + ")";
    }
    
}
