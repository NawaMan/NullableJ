//  Copyright (c) 2017-2018 Nawapunth Manusitthipol (NawaMan).
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
package nullablej.utils.reflection;

/**
 * Implement this interface if it is desired to have toString, hashCode and equals for dynamic proxy.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public interface WithToStringHashCodeEquals {
    
    /**
     * Returns the desired toString.
     * 
     * @return the toString.
     */
    public default String _toString() {
        return this.getClass().getSimpleName() + "@" + this.getClass().getSimpleName().hashCode();
    }
    
    /**
     * Returns the desired hashCode.
     * 
     * @return the hashCode.
     */
    public default int _hashCode() {
        return this._toString().hashCode();
    }
    
    /**
     * Checks if the given object equals this one.
     * 
     * @param theGivenObject  the given object.
     * @return  {@code true} if the objects equal.
     */
    public default boolean _equals(Object theGivenObject) {
        return this == theGivenObject;
    }
    
}
