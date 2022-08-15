//  MIT License
//  
//  Copyright (c) 2017-2019 Nawa Manusitthipol
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
package nullablej.utils.reflection;

import static java.util.Arrays.stream;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Utility class relating to reflection.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class UReflection {
    
    /**
     * Check if the annotations contains one with the given name.
     * 
     * @param annotations     the annotation array.
     * @param annotationName  the expected annotation name.
     * @return  {@code true} if there is at least on annotation inthe reflect with the name.
     */
    public static boolean hasAnnotationWithName(Annotation[] annotations, String annotationName) {
        boolean hasAnnotation = stream(annotations)
                .filter(a-> a.annotationType().getSimpleName().equals(annotationName))
                .findAny()
                .isPresent();
        return hasAnnotation;
    }
    
    /**
     * Reflectively get the static field value for the given class - return null if fail for any reason.
     * 
     * @param clzz   the class.
     * @param field  the field.
     * @return  the field value or null if fail.
     * 
     * @param <T>  the type of the class.
     * @param <R>  the return type of the value.
     */
    @SuppressWarnings("unchecked")
    public static <T, R> R getValueFromStaticFieldOrNull(Class<T> clzz, Field field) {
        try { return (R)field.get(clzz); }
        catch (IllegalArgumentException | IllegalAccessException e) {
            return null;
        }
    }
    
    /**
     * Reflectively invoke the static method for the given class - return null if fail for any reason.
     * 
     * @param clzz    the class.
     * @param method  the method.
     * @return  the result value or null if fail.
     * 
     * @param <T>  the type of the class.
     * @param <R>  the return type of the method.
     */
    @SuppressWarnings("unchecked")
    public static <T, R> R invokeStaticMethodOrNull(Class<T> clzz, Method method) {
        try {
            return (R)method.invoke(clzz);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            return null;
        }
    }
    
    /**
     * Check if the given type and modifier indicate if they belong to 
     *   a public static final element that is compatible with the expected type.
     * 
     * @param expectedType  the expected type.
     * @param theElementType  the given type of the element (method or field) we are checking.
     * @param theElementModifiers     the modifier of the element.
     * @return  {@code true}  if the given type and modifier indicate that the element matches.
     * 
     * @param <T>  the expected type.
     */
    public static <T> boolean isPublicStaticFinalAndCompatible(
            Class<T> expectedType,
            Class<?> theElementType,
            int      theElementModifiers) {
        if (!Modifier.isStatic(theElementModifiers))
            return false;
        if (!Modifier.isPublic(theElementModifiers))
            return false;
        if (!Modifier.isFinal(theElementModifiers))
            return false;
        if (!expectedType.isAssignableFrom(theElementType))
            return false;
        
        return true;
    }
}
