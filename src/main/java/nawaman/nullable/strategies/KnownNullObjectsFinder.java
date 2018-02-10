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

import static java.util.Collections.EMPTY_LIST;
import static java.util.Collections.EMPTY_MAP;
import static java.util.Collections.EMPTY_SET;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

import lombok.val;
import nawaman.nullable.IFindNullObject;

/**
 * This finder finds from a list of known null objects.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
@SuppressWarnings("rawtypes")
public class KnownNullObjectsFinder implements IFindNullObject {

    private static final Map<Class, Object> knownNullObjects = new ConcurrentHashMap<>();
    static {
        Map<Class, Object> map = knownNullObjects;
        map.put(byte.class,    0);
        map.put(Byte.class,    Byte.valueOf((byte) 0));
        map.put(short.class,   0);
        map.put(Short.class,   Short.valueOf((short) 0));
        map.put(int.class,     0);
        map.put(Integer.class, Integer.valueOf(0));
        map.put(long.class,    0);
        map.put(Long.class,    Long.valueOf(0L));
        
        map.put(float.class,  0.0);
        map.put(Float.class,  0.0f);
        map.put(double.class, 0.0);
        map.put(Double.class, 0.0);
        
        map.put(char.class,         ' ');
        map.put(Character.class,    ' ');
        map.put(String.class,       "");
        map.put(CharSequence.class, "");
        
        map.put(boolean.class,   false);
        map.put(Character.class, Boolean.FALSE);
        
        map.put(Runnable.class, ((Runnable)()->{}));
        map.put(Supplier.class, ((Supplier)()->null));
        map.put(Function.class, ((Function)input->null));
        
        map.put(Collection.class, EMPTY_LIST);
        map.put(List.class,       EMPTY_LIST);
        map.put(Set.class,        EMPTY_SET);
        map.put(Map.class,        EMPTY_MAP);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <OBJECT> OBJECT findNullObjectOf(Class<OBJECT> clzz) {
        val nullFromKnown = (OBJECT)knownNullObjects.get(clzz);
        if (nullFromKnown != null)
            return nullFromKnown;
        
        if (clzz.isEnum()) {
            val enums = clzz.getEnumConstants();
            if (enums.length != 0) {
                val value = (OBJECT)enums[0];
                knownNullObjects.put(clzz, value);
                return value;
            }
        }
        if (clzz.isArray()) {
            val componentType = clzz.getComponentType();
            val value         = (OBJECT)Array.newInstance(componentType, 0);
            knownNullObjects.put(clzz, value);
            return value;
        }
        
        return null;
    }
    
}
