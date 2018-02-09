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

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import lombok.Getter;
import lombok.val;
import lombok.experimental.ExtensionMethod;

@SuppressWarnings("javadoc")
@ExtensionMethod({ NullableJ.class })
public class NullableJTest {
    
    private final String nullString = (String)null;
    private final String emptyString = "";
    private final String blankString = " \t\n";
    
    private static Predicate<String> contains(String needle) {
        return heystack-> { 
            return heystack._isNotNull()
                && heystack.contains(needle);
        };
    }
    
    private static Integer IntOf(int i) {
        return Integer.valueOf(i);
    }
    
    @Test
    public void test_isNull() {
        assertFalse("String"._isNull());
        assertTrue( nullString._isNull());
    }
    
    @Test
    public void test_isNotNull() {
        assertTrue( "String"._isNotNull());
        assertFalse(nullString._isNotNull());
    }
    
    @Test
    public void test_equalsTo() {
        assertTrue( "String" ._equalsTo("String"));
        assertTrue( "Integer"._equalsTo("Integer"));
        assertFalse(nullString._equalsTo("String"));
    }
    
    @Test
    public void test_notEqualsTo() {
        assertFalse("String" ._notEqualsTo("String"));
        assertFalse("Integer"._notEqualsTo("Integer"));
        assertTrue( nullString._notEqualsTo("String"));
    }
    
    @Test
    public void test_or() {
        assertEquals("String",         "String"  ._or("Another String"));
        assertEquals("Another String", nullString._or("Another String"));
    }
    
    @Test
    public void test_or__subClass() {
        StringBuffer  someBuffer = new StringBuffer("String");
        StringBuffer  nullBuffer = null;
        CharSequence  someResult = someBuffer._or("Another String");
        CharSequence  nullResult = nullBuffer._or("Another String");
        assertEquals("String",           someResult.toString());
        assertEquals("Another String",   nullResult.toString());
        // Notice that <StringBuffer>.or(<String>) is <CharSequence> which is the closest common ancestor.
    }
    
    @Test
    public void test_orGet() {
        Supplier<String> erorrMessage = ()->"Another" + " " + "String";
        assertEquals("String",        "String"._orGet(erorrMessage));
        assertEquals("Another String", nullString._orGet(erorrMessage));
    }
    
    @Test
    public void test_orNullObject() {
        assertEquals("String", "String"  ._orNullObject(String.class));
        assertEquals("",       nullString._orNullObject(String.class));
    }
    
    @Test
    public void test_orElseNullObject() {
        assertEquals("String", Optional.of("String")          ._orElseNullObject(String.class).orElse(null));
        assertEquals("",       Optional.ofNullable(nullString)._orElseNullObject(String.class).orElse(null));
    }
    
    @Test
    public void test_toOptional() {
        assertTrue("String"._toOptional() instanceof Optional);
        assertTrue("String"._toOptional().isPresent());
        assertEquals("String", "String"._toOptional().get());
        
        assertTrue(nullString._toOptional() instanceof Optional);
        assertFalse(nullString._toOptional().isPresent());
    }
    
    @Test
    public void test_whenNotNull() {
        assertTrue("String"._whenNotNull() instanceof Optional);
        assertTrue("String"._whenNotNull().isPresent());
        assertEquals("String", "String"._whenNotNull().get());
        
        assertTrue(nullString._whenNotNull() instanceof Optional);
        assertFalse(nullString._whenNotNull().isPresent());
    }
    
    private static Consumer<String> saveStringTo(AtomicReference<String> ref) {
        return str -> ref.set(str);
    }
    
    @Test
    public void test_whenNotNull_action() {
        val someResult = new AtomicReference<String>("NULL");
        val nullResult = new AtomicReference<String>("NULL");
        "String"  ._whenNotNull(saveStringTo(someResult));
        nullString._whenNotNull(saveStringTo(nullResult));
        
        assertEquals("String", someResult.get());
        assertEquals("NULL",   nullResult.get());
    }
    
    @Test
    public void test_when() {
        assertEquals("The original string", "The original string"._when(contains("original")).orElse("Another string"));
        assertEquals("Another string",      "The original string"._when(contains("another" )).orElse("Another string"));
        assertEquals("Another string",      nullString           ._when(contains("original")).orElse("Another string"));
        assertEquals("Another string",      nullString           ._when(contains("another" )).orElse("Another string"));
    }
    
    @Test
    public void test_as() {
        assertEquals(IntOf(1234), IntOf(1234)._as(Integer.class));
        assertEquals(null,        IntOf(1234)._as(Double.class));
        assertEquals(0.0,         IntOf(1234)._as(Double.class)._or(0.0), 0.0);
    }
    
    @Test
    public void test_mapTo() {
        val itsLength    = (Function<String, Integer>)String::length;
        assertEquals( 6, "String"  ._mapTo(itsLength)._or(-1));
        assertEquals(-1, nullString._mapTo(itsLength)._or(-1));
    }
    
    @Test
    public void test_mapBy() {
        val surroundingWithQuotes = (Function<String, String>)(str->("\'" + str + "\'"));
        assertEquals("\'String\'", "String"  ._mapBy(surroundingWithQuotes)._or("null"));
        assertEquals("null",       nullString._mapBy(surroundingWithQuotes)._or("null"));
    }
    
    @Test
    public void test_mapFrom() {
        val stringToInt = (Function<String, Integer>)(str->Integer.parseInt(str));
        assertEquals(42, "42"      ._mapFrom(stringToInt)._or(0));
        assertEquals( 0, nullString._mapFrom(stringToInt)._or(0));
    }
    
    @Test
    public void test_or__primitive() {
        val nullInteger        = (Integer)null;
        val nullLong           = (Long)null;
        val nullDouble         = (Double)null;
        val nullBigInteger     = (BigInteger)null;
        val minusOneBigInteger = BigInteger.valueOf(-1);
        val nullBigDecimal     = (BigDecimal)null;
        val minusOneBigDecimal = BigDecimal.valueOf(-1);
        assertEquals(-1,   nullInteger._or(-1));
        assertEquals(-1L,  nullLong._or(-1L));
        assertEquals(-1L,  nullLong._or(-1));
        assertEquals(-1.0, nullDouble._or(-1.0), 0.0);
        assertEquals(-1.0, nullDouble._or(-1),   0.0);
        assertEquals(minusOneBigInteger, nullBigInteger._or(-1));
        assertEquals(minusOneBigInteger, nullBigInteger._or(-1L));
        assertEquals(minusOneBigDecimal, nullBigDecimal._or(-1));
        assertEquals(minusOneBigDecimal, nullBigDecimal._or(-1L));
        assertEquals(minusOneBigDecimal.doubleValue(), nullBigDecimal._or(-1.0).doubleValue(), 0.0);
    }
    
    @Test
    public void test_isEmpty() {
        assertFalse("String"   ._isEmpty());
        assertTrue( nullString ._isEmpty());
        assertTrue( emptyString._isEmpty());
    }
    
    @Test
    public void test_isBlank() {
        assertFalse("String"   ._isBlank());
        assertTrue( nullString ._isBlank());
        assertTrue( emptyString._isBlank());
        assertTrue( blankString._isBlank());
    }
    
    @Test
    public void test_trimToNull() {
        assertEquals("String", "\t String "._trimToNull());
        assertNull(nullString ._trimToNull());
        assertNull(emptyString._trimToNull());
        assertNull(blankString._trimToNull());
    }
    
    @Test
    public void test_trimToEmpty() {
        assertFalse("String", "\t String "._trimToNull()._isEmpty());
        assertTrue(nullString ._trimToNull()._isEmpty());
        assertTrue(emptyString._trimToNull()._isEmpty());
        assertTrue(blankString._trimToNull()._isEmpty());
    }
    
    @Test
    public void test_contains() {
        assertTrue( "String"  ._contains("ring"));
        assertFalse(nullString._contains("ring"));
    }
    
    @Test
    public void test_notContains() {
        assertTrue("String"  ._notContains("round"));
        assertTrue(nullString._notContains("round"));
    }
    
    @Test
    public void test_matches() {
        assertTrue("String"   ._matches("[Sa]tring"));
        assertFalse(nullString._matches("[Sa]tring"));
    }
    
    @Test
    public void test_notMatches() {
        assertTrue("String"  ._notMatches("[Ii]nteger"));
        assertTrue(nullString._notMatches("[Ii]nteger"));
    }
    
    @Test
    public void test_whenContains() {
        assertEquals("",    "AB"          ._whenContains(",").map(String::toLowerCase).orElse(""));
        assertEquals("a,b", "A,B"         ._whenContains(",").map(String::toLowerCase).orElse(""));
        assertEquals("",    ((String)null)._whenContains(",").map(String::toLowerCase).orElse(""));
    }
    
    @Test
    public void test_whenNotContains() {
        assertEquals("ab", "AB"          ._whenNotContains(",").map(String::toLowerCase).orElse(""));
        assertEquals("",   "A,B"         ._whenNotContains(",").map(String::toLowerCase).orElse(""));
        assertEquals("",   ((String)null)._whenNotContains(",").map(String::toLowerCase).orElse(""));
    }
    
    @Test
    public void test_whenMatches() {
        assertEquals(42, "42"      ._whenMatches("^[0-9]+$").map(Integer::parseInt).orElse(-1).intValue());
        assertEquals(-1, "Blue"    ._whenMatches("^[0-9]+$").map(Integer::parseInt).orElse(-1).intValue());
        assertEquals(-1, nullString._whenMatches("^[0-9]+$").map(Integer::parseInt).orElse(-1).intValue());
    }
    
    @Test
    public void test_whenNotMatches() {
        // This variable is needed as Lombok ExtensionMethods has some problem when compile with Oracle JDK.
        val toNotNumber = (Function<String, String>)(s->"NotNumber");
        assertEquals("Number",    "42"      ._whenNotMatches("^[0-9]+$").map(toNotNumber).orElse("Number"));
        assertEquals("NotNumber", "Blue"    ._whenNotMatches("^[0-9]+$").map(toNotNumber).orElse("Number"));
        // This does not useful but it is logical.
        assertEquals("Number",    nullString._whenNotMatches("^[0-9]+$").map(toNotNumber).orElse("Number"));
    }
    
    //== Array and Collection ==
    
    @Test
    public void test_stream$__array() {
        String[] array1 = new String[] { "One", "Two" };
        assertEquals("OneTwo", array1._stream$().collect(joining()));
        
        String[] array2 = null;
        assertEquals("", array2._stream$().collect(joining()));
    }
    
    @Test
    public void test_stream$__list() {
        List<String> list1 = asList("One", "Two");
        assertEquals("OneTwo", list1._stream$().collect(joining()));
        
        String[] list2 = null;
        assertEquals("", list2._stream$().collect(joining()));
    }
    
    @Test
    public void test_butOnlyNonNull__array() {
        String[] array1 = new String[] {"One", null, "Two"};
        assertEquals(2, array1._butOnlyNonNull$()._toList().size());
        
        String[] arrayNull = null;
        assertEquals(0, arrayNull._butOnlyNonNull$()._toList().size());
    }
    
    @Test
    public void test_butOnlyNonNull__list() {
        List<String> list1 = asList("One", null, "Two");
        assertEquals(2, list1._butOnlyNonNull$()._toList().size());
        
        List<String> listNull = null;
        assertEquals(0, listNull._butOnlyNonNull$()._toList().size());
    }
    
    @Test
    public void test_butOnlyNonNull__stream() {
        Stream<String> stream1 = asList("One", null, "Two").stream();
        assertEquals(2, stream1._butOnlyNonNull$()._toList().size());
        
        Stream<String> stream2 = null;
        assertEquals(0, stream2._butOnlyNonNull$()._toList().size());
        
        // extra
        
        assertEquals("3,3", asList("One", null, "Two").stream()
                ._butOnlyNonNull$()
                .map(String::length)
                .map(Object::toString)
                .collect(Collectors.joining(",")));
    }
    
    @Test
    public void test_toList__array() {
        String[] array1 = new String[] { "One", "Two" };
        assertEquals(2, array1._toList().size());
        
        String[] array2 = null;
        assertEquals(0, array2._toList().size());
    }
    
    @Test
    public void test_toList__list() {
        List<String> list1 = asList("One", "Two");
        assertEquals(2, list1._toList().size());
        assertFalse(list1 == list1._toList());
        
        List<String> list2 = null;
        assertEquals(0, list2._toList().size());
    }
    
    @Test
    public void test_toList__steam() {
        List<String> stream1 = asList("One", "Two");
        assertEquals(2, stream1._stream$()._toList().size());
        
        Stream<String> stream2 = null;
        assertEquals(0, stream2._toList().size());
    }
    
    @Test
    public void test_get__supplier() {
        Supplier<String> oneSupplier  = ()->"One";
        Supplier<String> nullSupplier = null;
        assertEquals("One", oneSupplier ._get());
        assertEquals(null,  nullSupplier._get());
    }
    
    @Test
    public void test_get__function() {
        Function<String, String> oneFunction  = key->"One:"+key;
        Function<String, String> nullFunction = null;
        assertEquals("One:1", oneFunction ._get("1"));
        assertEquals(null,    nullFunction._get("1"));
    }
    
    @Test
    public void test_apply__function() {
        Function<String, String> oneFunction  = key->"One:"+key;
        Function<String, String> nullFunction = null;
        assertEquals("One:1", oneFunction ._apply("1"));
        assertEquals(null,    nullFunction._apply("1"));
    }
    
    @Test
    public void test_get__Array() {
        String[] array1 = new String[] { "One", "Two" };
        assertEquals("One", array1._get(0));
        assertEquals("Two", array1._get(1));
        assertEquals(null,  array1._get(2));
        assertEquals(null,  array1._get(-1));
        
        String[] array2 = null;
        assertEquals(null, array2._get(0));
        assertEquals(null, array2._get(1));
        assertEquals(null, array2._get(2));
        assertEquals(null, array2._get(-1));
    }
    
    @Test
    public void test_get__list() {
        List<String> list1 = asList("One", "Two");
        assertEquals("One", list1._get(0));
        assertEquals("Two", list1._get(1));
        assertEquals(null,  list1._get(2));
        assertEquals(null,  list1._get(-1));
        
        List<String> list2 = null;
        assertEquals(null, list2._get(0));
        assertEquals(null, list2._get(1));
        assertEquals(null, list2._get(2));
        assertEquals(null, list2._get(-1));
    }
    
    @Test
    public void test_get__map() {
        Map<String, String> map1 = Collections.singletonMap("1", "One");
        assertEquals("One", map1._get("1"));
        assertEquals(null,  map1._get("2"));
        assertEquals(null,  map1._get(null));
        
        Map<String, String> map2 = null;
        assertEquals(null,  map2._get("1"));
        assertEquals(null,  map2._get("2"));
        assertEquals(null,  map2._get(null));
    }
    
    @Test
    public void test_get__or__array() {
        String[] array1 = new String[] { "One", "Two" };
        assertEquals("One",  array1._get(0, "none"));
        assertEquals("Two",  array1._get(1, "none"));
        assertEquals("none", array1._get(2, "none"));
        assertEquals("none", array1._get(-1, "none"));
        
        String[] array2 = null;
        assertEquals("none", array2._get(0, "none"));
        assertEquals("none", array2._get(1, "none"));
        assertEquals("none", array2._get(2, "none"));
        assertEquals("none", array2._get(-1, "none"));
    }
    @Test
    public void test_get__or__list() {
        List<String> list1 = asList("One", "Two");
        assertEquals("One",  list1._get(0, "none"));
        assertEquals("Two",  list1._get(1, "none"));
        assertEquals("none", list1._get(2, "none"));
        assertEquals("none", list1._get(-1, "none"));
        
        List<String> list2 = null;
        assertEquals("none", list2._get(0, "none"));
        assertEquals("none", list2._get(1, "none"));
        assertEquals("none", list2._get(2, "none"));
        assertEquals("none", list2._get(-1, "none"));
    }
    
    @Test
    public void test_get__or__map() {
        Map<String, String> map1 = Collections.singletonMap("1", "One");
        assertEquals("One",  map1._get("1", "Else"));
        assertEquals("Else", map1._get("2", "Else"));
        assertEquals("Else", map1._get(null, "Else"));
        
        Map<String, String> map2 = null;
        assertEquals("Else", map2._get("1", "Else"));
        assertEquals("Else", map2._get("2", "Else"));
        assertEquals("Else", map2._get(null, "Else"));
    }
    
    @Test
    public void test_get__orSupplier__array() {
        String[] array1 = new String[] { "One", "Two" };
        Supplier<String> returnNone = ()->"none";
        assertEquals("One",  array1._get(0, returnNone));
        assertEquals("Two",  array1._get(1, returnNone));
        assertEquals("none", array1._get(2, returnNone));
        assertEquals("none", array1._get(-1, returnNone));
        
        String[] array2 = null;
        assertEquals("none", array2._get(0, returnNone));
        assertEquals("none", array2._get(1, returnNone));
        assertEquals("none", array2._get(2, returnNone));
        assertEquals("none", array2._get(-1, returnNone));
    }
    @Test
    public void test_get__orSupplier__list() {
        List<String> list1 = asList("One", "Two");
        Supplier<String> returnNone = ()->"none";
        assertEquals("One",  list1._get(0, returnNone));
        assertEquals("Two",  list1._get(1, returnNone));
        assertEquals("none", list1._get(2, returnNone));
        assertEquals("none", list1._get(-1, returnNone));
        
        List<String> list2 = null;
        assertEquals("none", list2._get(0, returnNone));
        assertEquals("none", list2._get(1, returnNone));
        assertEquals("none", list2._get(2, returnNone));
        assertEquals("none", list2._get(-1, returnNone));
    }
    
    @Test
    public void test_get__orSupplier__map() {
        Map<String, String> map1 = Collections.singletonMap("1", "One");
        Supplier<String> orElse = ()->"Else";
        assertEquals("One",  map1._get("1",  orElse));
        assertEquals("Else", map1._get("2",  orElse));
        assertEquals("Else", map1._get(null, orElse));
        
        Map<String, String> map2 = null;
        assertEquals("Else", map2._get("1",  orElse));
        assertEquals("Else", map2._get("2",  orElse));
        assertEquals("Else", map2._get(null, orElse));
    }
    
    @Test
    public void test_get__orFunction__array() {
        String[] array1 = new String[] { "One", "Two" };
        Function<Integer, String> returnNone = index->("none: " + index);
        assertEquals("One",  array1._get(0, returnNone));
        assertEquals("Two",  array1._get(1, returnNone));
        assertEquals("none: 2", array1._get(2, returnNone));
        assertEquals("none: -1", array1._get(-1, returnNone));
        
        String[] array2 = null;
        assertEquals("none: 0", array2._get(0, returnNone));
        assertEquals("none: 1", array2._get(1, returnNone));
        assertEquals("none: 2", array2._get(2, returnNone));
        assertEquals("none: -1", array2._get(-1, returnNone));
    }
    @Test
    public void test_get__orFunction__list() {
        List<String> list1 = asList("One", "Two");
        Function<Integer, String> returnNone = index->("none: " + index);
        assertEquals("One",  list1._get(0, returnNone));
        assertEquals("Two",  list1._get(1, returnNone));
        assertEquals("none: 2", list1._get(2, returnNone));
        assertEquals("none: -1", list1._get(-1, returnNone));
        
        List<String> list2 = null;
        assertEquals("none: 0", list2._get(0, returnNone));
        assertEquals("none: 1", list2._get(1, returnNone));
        assertEquals("none: 2", list2._get(2, returnNone));
        assertEquals("none: -1", list2._get(-1, returnNone));
    }
    
    @Test
    public void test_get__orFunction__map() {
        Map<String, String> map1 = Collections.singletonMap("1", "One");
        Function<String,String> orElse = key->"Else-"+key;
        assertEquals("One",      map1._get("1",  orElse));
        assertEquals("Else-2",   map1._get("2",  orElse));
        assertEquals("Else-null", map1._get(null, orElse));
        
        Map<String, String> map2 = null;
        assertEquals("Else-1",    map2._get("1",  orElse));
        assertEquals("Else-2",    map2._get("2",  orElse));
        assertEquals("Else-null", map2._get(null, orElse));
    }
    
    @Test
    public void test_first__array() {
        String[] array1 = new String[] { "One", "Two" };
        assertEquals("One", array1._first());
        
        String[] array2 = null;
        assertEquals(null, array2._first());
    }
    
    @Test
    public void test_first__list() {
        List<String> list1 = asList("One", "Two");
        assertEquals("One", list1._first());
        
        List<String> list2 = null;
        assertEquals(null, list2._first());
    }
    
    @Test
    public void tes_last__array() {
        String[] array1 = new String[] { "One", "Two" };
        assertEquals("Two", array1._last());
        
        String[] array2 = null;
        assertEquals(null, array2._last());
    }
    
    @Test
    public void test_last__LastList() {
        List<String> list1 = asList("One", "Two");
        assertEquals("Two", list1._last());
        
        List<String> list2 = null;
        assertEquals(null, list2._last());
    }
    
    @Test
    public void test_hasAllWith__array() {
        val length3 = (Predicate<String>)s->s.length() == 3;
        
        String[] array1 = new String[] { "One", "Two" };
        assertTrue(array1._hasAllWith(length3));
        
        String[] array2 = new String[] { "One", "Two", "Three" };
        assertFalse(array2._hasAllWith(length3));
        
        String[] arrayNull = null;
        assertFalse(arrayNull._hasAllWith(length3));
    }
    
    @Test
    public void test_hasAllWith__list() {
        val length3 = (Predicate<String>)s->s.length() == 3;
        
        List<String> list1 = asList("One", "Two");
        assertTrue(list1._hasAllWith(length3));
        
        List<String> list2 = asList("One", "Two", "Three");
        assertFalse(list2._hasAllWith(length3));
        
        String[] listNull = null;
        assertFalse(listNull._hasAllWith(length3));
    }
    
    @Test
    public void test_hasSomeWith__array() {
        val length5 = (Predicate<String>)s->s.length() == 5;
        
        String[] array1 = new String[] { "One", "Two" };
        assertFalse(array1._hasSomeWith(length5));
        
        String[] array2 = new String[] { "One", "Two", "Three" };
        assertTrue(array2._hasSomeWith(length5));
        
        String[] arrayNull = null;
        assertFalse(arrayNull._hasSomeWith(length5));
    }
    
    @Test
    public void test_hasSomeWith__list() {
        val length5 = (Predicate<String>)s->s.length() == 5;
        
        List<String> list1 = asList("One", "Two");
        assertFalse(list1._hasSomeWith(length5));
        
        List<String> list2 = asList("One", "Two", "Three");
        assertTrue(list2._hasSomeWith(length5));
        
        String[] listNull = null;
        assertFalse(listNull._hasSomeWith(length5));
    }
    
    @Test
    public void test_butOnlyWith__array() {
        val length5 = (Predicate<String>)s->s.length() == 5;
        
        String[] array1 = new String[] { "One", "Two" };
        assertEquals(0, array1._butOnlyWith(length5).length);
        
        String[] array2 = new String[] { "One", "Two", "Three" };
        assertEquals(1, array2._butOnlyWith(length5).length);
        
        String[] arrayNull = null;
        assertNull(arrayNull._butOnlyWith(length5));
    }
    
    @Test
    public void test_butOnlyWith__list() {
        val length5 = (Predicate<String>)s->s.length() == 5;
        
        List<String> list1 = asList("One", "Two");
        assertEquals(0, list1._butOnlyWith(length5).size());
        
        List<String> list2 = asList("One", "Two", "Three");
        assertEquals(1, list2._butOnlyWith(length5).size());
        
        String[] listNull = null;
        assertNull(listNull._butOnlyWith(length5));
    }
    
    @Test
    public void testflatMap$() {
        
        @Getter
        class Person {
            String name;
            List<Person> children = new ArrayList<>();
            Person(String name) {
                this.name = name;
            }
        }
        
        Person p    = new Person("p");
        Person p1   = new Person("p1");
        Person p11  = new Person("p11");
        Person p12  = new Person("p12");
        Person p2   = new Person("p2");
        Person p21  = new Person("p21");
        Person p22  = new Person("p22");
        
        p.children.add(p1);
        p.children.add(p2);
        
        p1.children.add(p11);
        p1.children.add(p12);
        
        p2.children.add(p21);
        p2.children.add(p22);
        
        val mapper = (Function<Person, List<Person>>)Person::getChildren;
        assertEquals("[p11, p12, p21, p22]", p.children.stream()._flatMap$(mapper).map(Person::getName)._toList().toString());
        
        p2.children.add(null);
        assertEquals("[p11, p12, p21, p22]", p.children.stream()._flatMap$(mapper).map(Person::getName)._toList().toString());
        
        Stream<Person> nullStream = null;
        assertEquals("[]", nullStream._flatMap$(mapper).map(Person::getName)._toList().toString());
    }
    
}
