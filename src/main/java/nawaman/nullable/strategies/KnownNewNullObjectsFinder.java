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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import nawaman.nullable.IFindNullObject;

/**
 * This finder finds from a list of known null objects.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
@SuppressWarnings("rawtypes")
public class KnownNewNullObjectsFinder implements IFindNullObject {
    
    private static final Set<Class> knownNewNullObjects;
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
        knownNewNullObjects = Collections.unmodifiableSet(set);
    }
    
    protected final DefaultConstructorFinder  defaultConstructorFinder  = new DefaultConstructorFinder();
    
    @Override
    public <OBJECT> OBJECT findNullObjectOf(Class<OBJECT> clzz) {
        if (knownNewNullObjects.contains(clzz))
            return defaultConstructorFinder.findNullObjectOf(clzz);
        
        return null;
    }
    
}
