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

package nullablej;

import static java.util.function.Function.identity;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import lombok.val;

public class OtherwiseTest {
    
    @Test
    public void test_otherwise_returnsTheValueIfNotNull() {
        val o = new Otherwise<String, String>("a", "b");
        assertEquals("a", o.get());
        assertEquals("a", o.otherwise(identity()));
    }
    
    @Test
    public void test_orElse_returnsValueIfNotNull_returnsElseIfNull() {
        val o = new Otherwise<String, String>("a", "b");
        assertEquals("a", o.orElse("Z"));
        
        val nullO = new Otherwise<String, String>(null, "b");
        assertEquals("Z", nullO.orElse("Z"));
    }
    
    @Test
    public void test_getOtherwise_returnsOtherwise() {
        val o = new Otherwise<String, String>("a", "b");
        assertEquals("b", o.getOtherwise());
    }
    
    @Test
    public void test_otherwise_returnsOtherwiseIfNull() {
        val nullO = new Otherwise<String, String>(null, "b");
        assertEquals(null, nullO.get());
        assertEquals("b", nullO.otherwise(identity()));
    }
    
    @Test
    public void test_mapToUppercase_otherwiseMapToUpperCase() {
        val o = new Otherwise<String, String>("a", "B");
        assertEquals("A", o.map(String::toUpperCase).otherwise(String::toLowerCase));
        
        val nullO = new Otherwise<String, String>(null, "B");
        assertEquals("b", nullO.map(String::toUpperCase).otherwise(String::toLowerCase));
    }
    
    @Test
    public void test_withSameType_otherwise_returnsValueIfNotNull_returnsOtherwiseIfNull() {
        val o = new Otherwise.WithMatchTypes<String>("a", "b");
        assertEquals("a", o.otherwise());
        
        val nullO = new Otherwise.WithMatchTypes<String>(null, "b");
        assertEquals("b", nullO.otherwise());
    }
    
    @Test
    public void test_withSameType_mapBothWillMapBothValues() {
        val o = new Otherwise.WithMatchTypes<String>("a", "b");
        assertEquals("A", o.mapBoth(String::toUpperCase).otherwise());
        
        val nullO = new Otherwise.WithMatchTypes<String>(null, "B");
        assertEquals("B", nullO.mapBoth(String::toUpperCase).otherwise());
    }
    
    @Test
    public void test_withSameType_mapBecomeDifferentType_soOtherwiseWillHaveToMapTo() {
        val o = new Otherwise.WithMatchTypes<String>("a", "b");
        assertEquals("A", o.map(String::toUpperCase).otherwise(String::toLowerCase));
        
        val nullO = new Otherwise.WithMatchTypes<String>(null, "B");
        assertEquals("b", nullO.map(String::toUpperCase).otherwise(String::toLowerCase));
    }
    
    @Test
    public void test_differntTypesCanMapTowithSameType() {
        val o = new Otherwise.WithMatchTypes<String>("a", "b");
        assertEquals("A", o.map(String::toUpperCase).mapToMatch(identity()).otherwise());
        
        val nullO = new Otherwise.WithMatchTypes<String>(null, "b");
        assertEquals("b", nullO.map(String::toUpperCase).mapToMatch(identity()).otherwise());
    }
    
}
