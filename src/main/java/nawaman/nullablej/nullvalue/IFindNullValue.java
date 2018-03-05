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
package nawaman.nullablej.nullvalue;

import java.util.function.Function;

import nawaman.nullablej._internal.Default;
import nawaman.nullablej.nullvalue.processor.NullValue;

/**
 * Classes implementing this interface knows how to find null values given the class.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public interface IFindNullValue {
    
    /** The default value */
    @Default
    @NullValue
    public static final IFindNullValue instance = NullValues.instance;
    
    /**
     * Find the null value given the class.
     * 
     * @param clzz  the class.
     * @return  the null value.
     * @param <TYPE>  the type of the object.
     */
    public <TYPE> TYPE findNullValueOf(Class<TYPE> clzz);
    
    /**
     * Return this object as a function.
     * 
     * @return the function.
     * 
     * @param  <TYPE>  the type of data.
     */
    public default <TYPE> Function< Class<TYPE>, TYPE> asFunction() {
        return (Function< Class<TYPE>, TYPE>)this::findNullValueOf;
    }
    
}
