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

package nullablej.nullvalue;

import java.util.function.Function;

import nullablej._internal.Default;
import nullablej.nullvalue.processor.NullValue;

/**
 * Classes implementing this interface knows how to find null values given the class.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public interface IFindNullValue {
    
    /**
     * The default value.
     * 
     * @return the default instance.
     **/
    @Default
    @NullValue
    public static IFindNullValue instance() {
        return NullValues.instance;
    }
    
    /**
     * Find the null value given the class.
     * 
     * @param clzz  the class.
     * @return  the null value.
     * @param <TYPE>  the type of the object.
     */
    public <TYPE> TYPE findNullValueOf(Class<TYPE> clzz);
    
    /**
     * Return this object as a function.
     * 
     * @return the function.
     * 
     * @param  <TYPE>  the type of data.
     */
    public default <TYPE> Function< Class<TYPE>, TYPE> asFunction() {
        return (Function< Class<TYPE>, TYPE>)this::findNullValueOf;
    }
    
}
