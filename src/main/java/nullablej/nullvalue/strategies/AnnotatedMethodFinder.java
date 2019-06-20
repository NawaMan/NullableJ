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

import static nawaman.nullablej.utils.reflection.UReflection.hasAnnotationWithName;
import static nawaman.nullablej.utils.reflection.UReflection.invokeStaticMethodOrNull;

import java.lang.annotation.Annotation;

import lombok.NonNull;
import lombok.val;
import nawaman.nullablej.nullvalue.IFindNullValue;

/**
 * This finder finds by looking for compatible method with annotation.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class AnnotatedMethodFinder extends AbstractFromClassElementFinder implements IFindNullValue {
    
    private String annotationName;
    
    /**
     * Construct the finder that looks for method with the given name.
     * 
     * @param annotationName the name of the annotation.
     */
    public AnnotatedMethodFinder(@NonNull String annotationName) {
        this.annotationName = annotationName;
    }
    
    @Override
    public <OBJECT> OBJECT findNullValueOf(Class<OBJECT> clzz) {
        return findNullValueFromAnnotatedMethod(clzz, annotationName);
    }
    
    /**
     * Find null value by looking for annotated method of the given class.
     * 
     * @param clzz            the class.
     * @param annotationName  the name of the annotation.
     * @return  the null value.
     * 
     * @param  <OBJECT>  the type of data.
     */
    public static final <OBJECT> OBJECT findNullValueFromAnnotatedMethod(Class<OBJECT> clzz, String annotationName) {
        val valueFromAnnotatedMethod = getPublicStaticFinalCompatibleMethod(clzz, method->{
            Annotation[] annotations = method.getAnnotations();
            boolean hasAnnotation = hasAnnotationWithName(annotations, annotationName);
            if (!hasAnnotation)
                return null;
            
            return invokeStaticMethodOrNull(clzz, method);
        });
        return valueFromAnnotatedMethod;
    }
    
}
