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

import static nawaman.utils.reflection.UReflection.getValueFromStaticFieldOrNull;

import lombok.NonNull;
import lombok.val;
import nawaman.nullable.IFindNullValue;

/**
 * This finder finds by looking for compatible field with annotation.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class NamedFieldFinder extends AbstractFromClassElementFinder implements IFindNullValue {
    
    private String fieldName;
    
    /**
     * Construct the finder that looks for a compatible field with the given name.
     * 
     * @param fieldName  the field name. 
     */
    public NamedFieldFinder(@NonNull String fieldName) {
        this.fieldName = fieldName;
    }
    
    @Override
    public <OBJECT> OBJECT findNullValueOf(Class<OBJECT> clzz) {
        return findNullValueFromNamedField(clzz, fieldName);
    }
    
    /**
     * Find null value by looking for a field with specific name of the given class.
     * 
     * @param clzz       the class.
     * @param fieldName  the name of the field.
     * @return  the null value.
     * 
     * @param  <OBJECT>  the type of data.
     */
    public static final <OBJECT> OBJECT findNullValueFromNamedField(Class<OBJECT> clzz, String fieldName) {
        val valueFromNamedField = getPublicStaticFinalCompatibleField(clzz, field->{
            if (!fieldName.equals(field.getName()))
                return null;
            
            return getValueFromStaticFieldOrNull(clzz, field);
        });
        return valueFromNamedField;
    }
    
}
