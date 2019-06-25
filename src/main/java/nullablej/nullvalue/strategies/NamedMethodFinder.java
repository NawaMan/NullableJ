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

import static nullablej.utils.reflection.UReflection.invokeStaticMethodOrNull;

import lombok.NonNull;
import lombok.val;
import nullablej.nullvalue.IFindNullValue;

/**
 * This finder finds by looking for compatible method with a specific name.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class NamedMethodFinder extends AbstractFromClassElementFinder implements IFindNullValue {
    
    private String methodName;
    
    /**
     * Construct the finder that looks for method with the given name.
     * 
     * @param methodName  the method name. 
     */
    public NamedMethodFinder(@NonNull  String methodName) {
        this.methodName = methodName;
    }
    
    @Override
    public <OBJECT> OBJECT findNullValueOf(Class<OBJECT> clzz) {
        return findNullValueFromAnnotatedMethod(clzz, methodName);
    }
    
    /**
     * Find null value by looking for named method of the given class.
     * 
     * @param clzz        the class.
     * @param methodName  the name of the annotation.
     * @return  the null value.
     * 
     * @param  <OBJECT>  the type of data.
     */
    public static final <OBJECT> OBJECT findNullValueFromAnnotatedMethod(Class<OBJECT> clzz, String methodName) {
        val valueFromAnnotatedMethod = getPublicStaticFinalCompatibleMethod(clzz, method->{
            if (!methodName.equals(method.getName()))
                return null;
            
            return invokeStaticMethodOrNull(clzz, method);
        });
        return valueFromAnnotatedMethod;
    }
    
}
