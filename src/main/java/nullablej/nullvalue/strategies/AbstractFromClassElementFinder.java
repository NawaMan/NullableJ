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
package nullablej.nullvalue.strategies;


import static nullablej.utils.reflection.UReflection.isPublicStaticFinalAndCompatible;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.Function;

import lombok.val;
import lombok.experimental.ExtensionMethod;
import nullablej.NullableJ;

/**
 * This abstract class contains many useful methods for finders that look into field and methods.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
@ExtensionMethod({ NullableJ.class })
public class AbstractFromClassElementFinder {
    
    @SuppressWarnings({ "unchecked", "javadoc" })
    protected static <T> T getPublicStaticFinalCompatibleField(Class<T> clzz, Function<Field, Object> supplier) {
        for (val field : clzz.getDeclaredFields()) {
            val type      = field.getType();
            val modifiers = field.getModifiers();
            if (!isPublicStaticFinalAndCompatible(clzz, type, modifiers))
                continue;
            
            val value = supplier.apply(field);
            if (value._isNotNull())
                return (T)value;
        }
        return null;
    }
    
    @SuppressWarnings({ "unchecked", "javadoc" })
    protected static final <T> T getPublicStaticFinalCompatibleMethod(Class<T> clzz, Function<Method, Object> supplier) {
        for (val method : clzz.getDeclaredMethods()) {
            val type      = method.getReturnType();
            val modifiers = method.getModifiers();
            if (!isPublicStaticFinalAndCompatible(clzz, type, modifiers))
                continue;
            if (!clzz.getTypeParameters()._isEmpty())
                continue;
            
            val value = supplier.apply(method);
            if (value._isNotNull())
                return (T)value;
        }
        return null;
    }
}
