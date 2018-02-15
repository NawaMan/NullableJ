//  ========================================================================
//Copyright (c) 2017 Nawapunth Manusitthipol (NawaMan).
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
package nawaman.nullable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.Test;

import lombok.AllArgsConstructor;
import lombok.Data;

@SuppressWarnings("javadoc")
public class NullValuesTest {
    
    private IFindNullValue nullValues = NullValues.instance;
    
    @Test
    public void testPrimitive() {
        assertEquals(0, nullValues.findNullValueOf(int.class).intValue());
        assertEquals("", nullValues.findNullValueOf(String.class));
    }
    
    @Test
    public void testArray() {
        String[] nObj = nullValues.findNullValueOf(String[].class);
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
