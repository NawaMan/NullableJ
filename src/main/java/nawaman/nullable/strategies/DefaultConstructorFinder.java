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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.val;
import nawaman.nullable.IFindNullValue;

/**
 * This finder finds by looking for compatible method with annotation.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class DefaultConstructorFinder extends AbstractFromClassElementFinder implements IFindNullValue {
    
    @SuppressWarnings("rawtypes")
    private static final Map<Class, Constructor> constructors = new ConcurrentHashMap<>();
    
    @Override
    public <OBJECT> OBJECT findNullValueOf(Class<OBJECT> clzz) {
        return findNullValueFromDefaultConstructor(clzz);
    }
    
    /**
     * Find null value by looking for its default constructor.
     * 
     * @param clzz  the class.
     * @return  the null value.
     * 
     * @param  <OBJECT>  the type of data.
     */
    @SuppressWarnings("unchecked")
    public static final <OBJECT> OBJECT findNullValueFromDefaultConstructor(Class<OBJECT> clzz) {
        Constructor<OBJECT> constructor = null;
        try {
            if (constructors.containsKey(clzz)) {
                constructor = (Constructor<OBJECT>)constructors.get(clzz);
            } else {
                constructors.put(clzz, null);
                constructor = clzz.getConstructor();
            }
        } catch (Exception e) {
        }
        
        if (constructor == null)
            return null;
        
        try {
            val value = constructor.newInstance();
            constructors.put(clzz, constructor);
            return value;
        } catch (SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
