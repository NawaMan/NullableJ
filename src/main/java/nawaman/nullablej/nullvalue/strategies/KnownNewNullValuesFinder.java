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
package nawaman.nullablej.nullvalue.strategies;

import static java.util.Collections.unmodifiableSet;

import nawaman.nullablej.nullvalue.IFindNullValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This finder finds from a list of known null values.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
@SuppressWarnings("rawtypes")
public class KnownNewNullValuesFinder implements IFindNullValue {
    
    private static final Set<Class> knownNewNullValues;
    static {
        Set<Class> set = new HashSet<>();
        set.add(ArrayList.class);
        set.add(HashSet.class);
        set.add(TreeSet.class);
        set.add(LinkedHashSet.class);
        set.add(HashMap.class);
        set.add(TreeMap.class);
        set.add(LinkedHashMap.class);
        set.add(ConcurrentHashMap.class);
        knownNewNullValues = unmodifiableSet(set);
    }
    
    protected final DefaultConstructorFinder  defaultConstructorFinder  = new DefaultConstructorFinder();
    
    /**
     * Checks if this finder can find a null value for the given class.
     * 
     * @param <OBJECT>  the data type.
     * @param clzz      the data class.
     * @return {@code true} if this find can find null value for the class.
     */
    public <OBJECT> boolean canFindFor(Class<OBJECT> clzz) {
        return knownNewNullValues.contains(clzz);
    }
    
    @Override
    public <OBJECT> OBJECT findNullValueOf(Class<OBJECT> clzz) {
        if (knownNewNullValues.contains(clzz))
            return defaultConstructorFinder.findNullValueOf(clzz);
        
        return null;
    }
    
}
