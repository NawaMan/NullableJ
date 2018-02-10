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

import java.lang.annotation.Annotation;

import lombok.NonNull;
import lombok.val;
import nawaman.nullable.IFindNullObject;

/**
 * This finder finds by looking for compatible field with annotation.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class AnnotatedFieldFinder extends AbstractFromClassElementFinder implements IFindNullObject {
    
    private String annotationName;
    
    /**
     * Construct the finder that looks for field with the given annotation name.
     * 
     * @param annotationName the annotation name.
     */
    public AnnotatedFieldFinder(@NonNull String annotationName) {
        this.annotationName = annotationName;
    }
    
    @Override
    public <OBJECT> OBJECT findNullObjectOf(Class<OBJECT> clzz) {
        return findNullObjectFromAnnotatedField(clzz, annotationName);
    }
    
    /**
     * Find null object by looking for annotated field of the given class.
     * 
     * @param clzz            the class.
     * @param annotationName  the name of the annotation.
     * @return  the null-object value.
     */
    public static final <OBJECT> OBJECT findNullObjectFromAnnotatedField(Class<OBJECT> clzz, String annotationName) {
        val valueFromAnnotatedField = getPublicStaticFinalCompatibleField(clzz, field->{
            Annotation[] annotations = field.getAnnotations();
            boolean hasAnnotation = checkAnnotationForName(annotations, annotationName);
            if (!hasAnnotation)
                return null;
            
            return getValueFromField(clzz, field);
        });
        return valueFromAnnotatedField;
    }
}
