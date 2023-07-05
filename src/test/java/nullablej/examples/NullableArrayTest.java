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

package nullablej.examples;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import lombok.val;
import nullablej.nullable.Nullable;

public class NullableArrayTest {
    
    public static interface NullableArray<T> extends Nullable<T[]> {
        public static <T> NullableArray<T> of(T[] array) {
            return (NullableArray<T>)()->array;
        }
        public default List<T> toList() {
            T[] array = get();
            if (array == null)
                return Collections.emptyList();
            
            return Arrays.asList((T[])array);
        }
    }
    
    @Test
    public void testNullableArray() {
        val nullableArray = NullableArray.of(new String[] { "one",  "two",  "three" });
        assertEquals("[one, two, three]", nullableArray.toList().toString());
        
        val nullableArray2 = NullableArray.of(null);
        assertEquals("[]", nullableArray2.toList().toString());
    }
    
}
