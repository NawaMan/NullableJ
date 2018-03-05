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

import static nawaman.nullablej._internal.UReflection.getValueFromStaticFieldOrNull;
import static nawaman.nullablej._internal.UReflection.hasAnnotationWithName;

import java.lang.annotation.Annotation;

import lombok.NonNull;
import lombok.val;
import nawaman.nullablej.nullvalue.IFindNullValue;

/**
 * This finder finds by looking for compatible field with annotation.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class AnnotatedFieldFinder extends AbstractFromClassElementFinder implements IFindNullValue {
    
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
    public <OBJECT> OBJECT findNullValueOf(Class<OBJECT> clzz) {
        return findNullValueFromAnnotatedField(clzz, annotationName);
    }
    
    /**
     * Find null value by looking for annotated field of the given class.
     * 
     * @param clzz            the class.
     * @param annotationName  the name of the annotation.
     * @return  the null value.
     * 
     * @param  <OBJECT>  the type of data.
     */
    public static final <OBJECT> OBJECT findNullValueFromAnnotatedField(Class<OBJECT> clzz, String annotationName) {
        val valueFromAnnotatedField = getPublicStaticFinalCompatibleField(clzz, field->{
            Annotation[] annotations = field.getAnnotations();
            boolean hasAnnotation = hasAnnotationWithName(annotations, annotationName);
            if (!hasAnnotation)
                return null;
            
            return getValueFromStaticFieldOrNull(clzz, field);
        });
        return valueFromAnnotatedField;
    }
    
}
