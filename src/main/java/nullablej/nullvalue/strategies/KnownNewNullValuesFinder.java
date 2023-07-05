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

import static java.util.Collections.unmodifiableSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import nullablej.nullvalue.IFindNullValue;

/**
 * This finder finds from a list of known null values.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
@SuppressWarnings("rawtypes")
public class KnownNewNullValuesFinder implements IFindNullValue {
    
    private static final Set<Class> knownNewNullValues;
    static {
        Set<Class> set = new HashSet<>();
        set.add(ArrayList.class);
        set.add(HashSet.class);
        set.add(TreeSet.class);
        set.add(LinkedHashSet.class);
        set.add(HashMap.class);
        set.add(TreeMap.class);
        set.add(LinkedHashMap.class);
        set.add(ConcurrentHashMap.class);
        knownNewNullValues = unmodifiableSet(set);
    }
    
    @SuppressWarnings("javadoc")
	protected final DefaultConstructorFinder  defaultConstructorFinder  = new DefaultConstructorFinder();
    
    /**
     * Checks if this finder can find a null value for the given class.
     * 
     * @param <OBJECT>  the data type.
     * @param clzz      the data class.
     * @return {@code true} if this find can find null value for the class.
     */
    public <OBJECT> boolean canFindFor(Class<OBJECT> clzz) {
        return knownNewNullValues.contains(clzz);
    }
    
    @Override
    public <OBJECT> OBJECT findNullValueOf(Class<OBJECT> clzz) {
        if (knownNewNullValues.contains(clzz))
            return defaultConstructorFinder.findNullValueOf(clzz);
        
        return null;
    }
    
}
