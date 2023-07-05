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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.val;
import nullablej.nullvalue.IFindNullValue;

/**
 * This finder finds by looking for compatible method with annotation.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class DefaultConstructorFinder extends AbstractFromClassElementFinder implements IFindNullValue {
    
    @SuppressWarnings("rawtypes")
    private static final Map<Class, Constructor> constructors = new ConcurrentHashMap<>();
    
    @Override
    public <OBJECT> OBJECT findNullValueOf(Class<OBJECT> clzz) {
        return findNullValueFromDefaultConstructor(clzz);
    }
    
    /**
     * Find null value by looking for its default constructor.
     * 
     * @param clzz  the class.
     * @return  the null value.
     * 
     * @param  <OBJECT>  the type of data.
     */
    @SuppressWarnings("unchecked")
    public static final <OBJECT> OBJECT findNullValueFromDefaultConstructor(Class<OBJECT> clzz) {
        Constructor<OBJECT> constructor = null;
        try {
            if (constructors.containsKey(clzz)) {
                constructor = (Constructor<OBJECT>)constructors.get(clzz);
            } else {
                constructor = clzz.getConstructor();
                constructors.put(clzz, constructor);
            }
        } catch (Exception e) {
        }
        
        if (constructor == null)
            return null;
        
        try {
            val value = constructor.newInstance();
            constructors.put(clzz, constructor);
            return value;
        } catch (SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
