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

import lombok.NonNull;
import lombok.val;
import nawaman.nullable.IFindNullObject;

/**
 * This finder finds by looking for compatible method with a specific name.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class NamedMethodFinder extends AbstractFromClassElementFinder implements IFindNullObject {
    
    private String methodName;
    
    /**
     * Construct the finder that looks for method with the given name.
     * 
     * @param annotationName 
     */
    public NamedMethodFinder(@NonNull  String annotationName) {
        this.methodName = annotationName;
    }
    
    @Override
    public <OBJECT> OBJECT findNullObjectOf(Class<OBJECT> clzz) {
        return findNullObjectFromAnnotatedMethod(clzz, methodName);
    }
    
    /**
     * Find null object by looking for named method of the given class.
     * 
     * @param clzz        the class.
     * @param methodName  the name of the annotation.
     * @return  the null-object value.
     */
    public static final <OBJECT> OBJECT findNullObjectFromAnnotatedMethod(Class<OBJECT> clzz, String methodName) {
        val valueFromAnnotatedMethod = getPublicStaticFinalCompatibleMethod(clzz, method->{
            if (!methodName.equals(method.getName()))
                return null;
            
            return getValueFromMethod(clzz, method);
        });
        return valueFromAnnotatedMethod;
    }
}
