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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.Test;

import lombok.AllArgsConstructor;
import lombok.Data;

@SuppressWarnings("javadoc")
public class NullObjectsTest {
    
    private IFindNullObject nullObjects = NullObjects.instance;
    
    @Test
    public void testPrimitive() {
        assertEquals(0, nullObjects.findNullObjectOf(int.class).intValue());
        assertEquals("", nullObjects.findNullObjectOf(String.class));
    }
    
    @Test
    public void testArray() {
        String[] nObj = nullObjects.findNullObjectOf(String[].class);
        assertEquals(0,            nObj.length);
        assertEquals(String.class, nObj.getClass().getComponentType());
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD, ElementType.METHOD })
    public static @interface NullObject {
        
    }
    
    @Data
    @AllArgsConstructor
    public static class Person1 {
        @NullObject
        public static final Person1 nullPerson1 = new Person1(null);
        private String name;
    }
    
    @Test
    public void testAnnotatedField() {
        Person1 nullPerson1 = nullObjects.findNullObjectOf(Person1.class);
        assertEquals(nullPerson1, Person1.nullPerson1);
    }

    private static Person2 nullObjectOfPerson2 = new Person2(null);
    @Data
    @AllArgsConstructor
    public static class Person2 {
        @NullObject
        public static final Person2 nullPerson2() { return nullObjectOfPerson2; }
        private String name;
    }
    
    @Test
    public void testAnnotatedMethod() {
        Person2 nullPerson2 = nullObjects.findNullObjectOf(Person2.class);
        assertEquals(nullPerson2, nullObjectOfPerson2);
    }
    
    @Data
    @AllArgsConstructor
    public static class Person3 {
        public static final Person3 nullObject = new Person3(null);
        private String name;
    }
    
    @Test
    public void testNamedField() {
        Person3 nullPerson3 = nullObjects.findNullObjectOf(Person3.class);
        assertEquals(nullPerson3, Person3.nullObject);
    }
    
    private static Person4 nullObjectOfPerson4 = new Person4(null);
    @Data
    @AllArgsConstructor
    public static class Person4 {
        public static final Person4 nullObject() { return nullObjectOfPerson4; }
        private String name;
    }
    
    @Test
    public void testNamedMethod() {
        Person4 nullPerson4 = nullObjects.findNullObjectOf(Person4.class);
        assertEquals(nullPerson4, nullObjectOfPerson4);
    }
    
}
