//  MIT License
//  
//  Copyright (c) 2017-2023 Nawa Manusitthipol
//  
//  Permission is hereby granted, free of charge, to any person obtaining a copy
//  of this software and associated documentation files (the "Software"), to deal
//  in the Software without restriction, including without limitation the rights
//  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//  copies of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions:
//  
//  The above copyright notice and this permission notice shall be included in all
//  copies or substantial portions of the Software.
//  
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
//  SOFTWARE.

package nullablej.nullvalue.strategies;

import static nullablej.utils.reflection.UReflection.hasAnnotationWithName;
import static nullablej.utils.reflection.UReflection.invokeStaticMethodOrNull;

import java.lang.annotation.Annotation;

import lombok.NonNull;
import lombok.val;
import nullablej.nullvalue.IFindNullValue;

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
