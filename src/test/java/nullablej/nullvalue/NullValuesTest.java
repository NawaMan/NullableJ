//  ========================================================================
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.Test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.val;

public class NullValuesTest {
    
    private IFindNullValue nullValues = NullValues.instance;
    
    @Test
    public void testPrimitive() {
        assertEquals(0, nullValues.findNullValueOf(int.class).intValue());
        assertEquals("", nullValues.findNullValueOf(String.class));
    }
    
    @Test
    public void testArray() {
        val nObj = nullValues.findNullValueOf(String[].class);
        assertEquals(0,            nObj.length);
        assertEquals(String.class, nObj.getClass().getComponentType());
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD, ElementType.METHOD })
    public static @interface NullValue {
        
    }
    
    @Data
    @AllArgsConstructor
    public static class Person1 {
        @NullValue
        public static final Person1 nullPerson1 = new Person1(null);
        private String name;
    }
    
    @Test
    public void testAnnotatedField() {
        Person1 nullPerson1 = nullValues.findNullValueOf(Person1.class);
        assertEquals(nullPerson1, Person1.nullPerson1);
    }

    private static Person2 nullValueOfPerson2 = new Person2(null);
    @Data
    @AllArgsConstructor
    public static class Person2 {
        @NullValue
        public static final Person2 nullPerson2() { return nullValueOfPerson2; }
        private String name;
    }
    
    @Test
    public void testAnnotatedMethod() {
        Person2 nullPerson2 = nullValues.findNullValueOf(Person2.class);
        assertEquals(nullPerson2, nullValueOfPerson2);
    }
    
    @Data
    @AllArgsConstructor
    public static class Person3 {
        public static final Person3 nullValue = new Person3(null);
        private String name;
    }
    
    @Test
    public void testNamedField() {
        Person3 nullPerson3 = nullValues.findNullValueOf(Person3.class);
        assertEquals(nullPerson3, Person3.nullValue);
    }
    
    private static Person4 nullValueOfPerson4 = new Person4(null);
    @Data
    @AllArgsConstructor
    public static class Person4 {
        public static final Person4 nullValue() { return nullValueOfPerson4; }
        private String name;
    }
    
    @Test
    public void testNamedMethod() {
        Person4 nullPerson4 = nullValues.findNullValueOf(Person4.class);
        assertEquals(nullPerson4, nullValueOfPerson4);
    }
    
    public static interface IPerson {
        public String name();
        public void setName(String name);
    }
    
    @Test
    public void testDataInteface() {
        IPerson nullIPerson = nullValues.findNullValueOf(IPerson.class);
        assertNotNull(nullIPerson);
    }
    
}
