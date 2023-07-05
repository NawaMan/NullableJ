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

import static java.util.Collections.EMPTY_LIST;
import static java.util.Collections.EMPTY_MAP;
import static java.util.Collections.EMPTY_SET;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import lombok.val;
import nullablej.nullvalue.IFindNullValue;

/**
 * This finder finds from a list of known null values.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
@SuppressWarnings("rawtypes")
public class KnownNullValuesFinder implements IFindNullValue {

    private static final Map<Class, Object> knownNullValues = new ConcurrentHashMap<>();
    static {
        Map<Class, Object> map = knownNullValues;
        map.put(byte.class,    (byte)0);
        map.put(Byte.class,    Byte.valueOf((byte) 0));
        map.put(short.class,   (short)0);
        map.put(Short.class,   Short.valueOf((short) 0));
        map.put(int.class,     0);
        map.put(Integer.class, Integer.valueOf(0));
        map.put(long.class,    0L);
        map.put(Long.class,    Long.valueOf(0L));
        
        map.put(float.class,  0.0f);
        map.put(Float.class,  0.0f);
        map.put(double.class, 0.0);
        map.put(Double.class, 0.0);
        
        map.put(char.class,         ' ');
        map.put(Character.class,    ' ');
        map.put(String.class,       "");
        map.put(CharSequence.class, "");
        
        map.put(boolean.class, false);
        map.put(Boolean.class, Boolean.FALSE);
        
        map.put(Runnable.class,   ((Runnable)()->{}));
        map.put(Supplier.class,   ((Supplier)()->null));
        map.put(Function.class,   ((Function)input->null));
        map.put(BiFunction.class, ((BiFunction)(a,b)->null));
        map.put(Consumer.class,   ((Consumer)input->{}));
        
        map.put(Collection.class, EMPTY_LIST);
        map.put(List.class,       EMPTY_LIST);
        map.put(Set.class,        EMPTY_SET);
        map.put(Map.class,        EMPTY_MAP);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <OBJECT> OBJECT findNullValueOf(Class<OBJECT> clzz) {
        val nullFromKnown = (OBJECT)knownNullValues.get(clzz);
        if (nullFromKnown != null)
            return nullFromKnown;
        
        if (clzz.isEnum()) {
            val enums = clzz.getEnumConstants();
            if (enums.length != 0) {
                val value = (OBJECT)enums[0];
                knownNullValues.put(clzz, value);
                return value;
            }
        }
        if (clzz.isArray()) {
            val componentType = clzz.getComponentType();
            val value         = (OBJECT)Array.newInstance(componentType, 0);
            knownNullValues.put(clzz, value);
            return value;
        }
        
        return null;
    }
    
}
