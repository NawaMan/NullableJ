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
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.Test;

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
    public void testIsNull() {
        assertFalse("String"._isNull());
        assertTrue( nullString._isNull());
    }
    
    @Test
    public void testIsNotNull() {
        assertTrue( "String"._isNotNull());
        assertFalse(nullString._isNotNull());
    }
    
    @Test
    public void testEqualsTo() {
        assertTrue( "String" ._equalsTo("String"));
        assertTrue( "Integer"._equalsTo("Integer"));
        assertFalse(nullString._equalsTo("String"));
    }
    
    @Test
    public void testNotEqualsTo() {
        assertFalse("String" ._notEqualsTo("String"));
        assertFalse("Integer"._notEqualsTo("Integer"));
        assertTrue( nullString._notEqualsTo("String"));
    }
    
    @Test
    public void testOr() {
        assertEquals("String",         "String"  ._or("Another String"));
        assertEquals("Another String", nullString._or("Another String"));
    }
    
    @Test
    public void testOr_subClass() {
        StringBuffer  someBuffer = new StringBuffer("String");
        StringBuffer  nullBuffer = null;
        CharSequence  someResult = someBuffer._or("Another String");
        CharSequence  nullResult = nullBuffer._or("Another String");
        assertEquals("String",           someResult.toString());
        assertEquals("Another String",   nullResult.toString());
        // Notice that <StringBuffer>.or(<String>) is <CharSequence> which is the closest common ancestor.
    }
    
    @Test
    public void testOrGet() {
        Supplier<String> erorrMessage = ()->"Another" + " " + "String";
        assertEquals("String",        "String"._orGet(erorrMessage));
        assertEquals("Another String", nullString._orGet(erorrMessage));
    }
    
    @Test
    public void testOrNullObject() {
        assertEquals("String", "String"  ._orNullObject(String.class));
        assertEquals("",       nullString._orNullObject(String.class));
    }
    
    @Test
    public void testOrElseNullObject() {
        assertEquals("String", Optional.of("String")          ._orElseNullObject(String.class).orElse(null));
        assertEquals("",       Optional.ofNullable(nullString)._orElseNullObject(String.class).orElse(null));
    }
    
    @Test
    public void testToOptional() {
        assertTrue("String"._toOptional() instanceof Optional);
        assertTrue("String"._toOptional().isPresent());
        assertEquals("String", "String"._toOptional().get());
        
        assertTrue(nullString._toOptional() instanceof Optional);
        assertFalse(nullString._toOptional().isPresent());
    }
    
    @Test
    public void testWhenNotNull() {
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
    public void testWhenNotNull_action() {
        val someResult = new AtomicReference<String>("NULL");
        val nullResult = new AtomicReference<String>("NULL");
        "String"  ._whenNotNull(saveStringTo(someResult));
        nullString._whenNotNull(saveStringTo(nullResult));
        
        assertEquals("String", someResult.get());
        assertEquals("NULL",   nullResult.get());
    }
    
    @Test
    public void testWhen() {
        assertEquals("The original string", "The original string"._when(contains("original")).orElse("Another string"));
        assertEquals("Another string",      "The original string"._when(contains("another" )).orElse("Another string"));
        assertEquals("Another string",      nullString           ._when(contains("original")).orElse("Another string"));
        assertEquals("Another string",      nullString           ._when(contains("another" )).orElse("Another string"));
    }
    
    @Test
    public void testAs() {
        assertEquals(IntOf(1234), IntOf(1234)._as(Integer.class));
        assertEquals(null,        IntOf(1234)._as(Double.class));
        assertEquals(0.0,         IntOf(1234)._as(Double.class)._or(0.0), 0.0);
    }
    
    @Test
    public void testMapTo() {
        val itsLength    = (Function<String, Integer>)String::length;
        assertEquals( 6, "String"  ._mapTo(itsLength)._or(-1));
        assertEquals(-1, nullString._mapTo(itsLength)._or(-1));
    }
    
    @Test
    public void testMapBy() {
        val surroundingWithQuotes = (Function<String, String>)(str->("\'" + str + "\'"));
        assertEquals("\'String\'", "String"  ._mapBy(surroundingWithQuotes)._or("null"));
        assertEquals("null",       nullString._mapBy(surroundingWithQuotes)._or("null"));
    }
    
    @Test
    public void testMapFrom() {
        val stringToInt = (Function<String, Integer>)(str->Integer.parseInt(str));
        assertEquals(42, "42"      ._mapFrom(stringToInt)._or(0));
        assertEquals( 0, nullString._mapFrom(stringToInt)._or(0));
    }
    
    @Test
    public void testOrPrimitive() {
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
    public void testIsEmpty() {
        assertFalse("String"   ._isEmpty());
        assertTrue( nullString ._isEmpty());
        assertTrue( emptyString._isEmpty());
    }
    
    @Test
    public void testIsBlank() {
        assertFalse("String"   ._isBlank());
        assertTrue( nullString ._isBlank());
        assertTrue( emptyString._isBlank());
        assertTrue( blankString._isBlank());
    }
    
    @Test
    public void testTrimToNull() {
        assertEquals("String", "\t String "._trimToNull());
        assertNull(nullString ._trimToNull());
        assertNull(emptyString._trimToNull());
        assertNull(blankString._trimToNull());
    }
    
    @Test
    public void testTrimToEmpty() {
        assertFalse("String", "\t String "._trimToNull()._isEmpty());
        assertTrue(nullString ._trimToNull()._isEmpty());
        assertTrue(emptyString._trimToNull()._isEmpty());
        assertTrue(blankString._trimToNull()._isEmpty());
    }
    
    @Test
    public void testContains() {
        assertTrue( "String"  ._contains("ring"));
        assertFalse(nullString._contains("ring"));
    }
    
    @Test
    public void testNotContains() {
        assertTrue("String"  ._notContains("round"));
        assertTrue(nullString._notContains("round"));
    }
    
    @Test
    public void testMatches() {
        assertTrue("String"   ._matches("[Sa]tring"));
        assertFalse(nullString._matches("[Sa]tring"));
    }
    
    @Test
    public void testNotMatches() {
        assertTrue("String"  ._notMatches("[Ii]nteger"));
        assertTrue(nullString._notMatches("[Ii]nteger"));
    }
    
    @Test
    public void testWhenContains() {
        assertEquals("",    "AB"          ._whenContains(",").map(String::toLowerCase).orElse(""));
        assertEquals("a,b", "A,B"         ._whenContains(",").map(String::toLowerCase).orElse(""));
        assertEquals("",    ((String)null)._whenContains(",").map(String::toLowerCase).orElse(""));
    }
    
    @Test
    public void testWhenNotContains() {
        assertEquals("ab", "AB"          ._whenNotContains(",").map(String::toLowerCase).orElse(""));
        assertEquals("",   "A,B"         ._whenNotContains(",").map(String::toLowerCase).orElse(""));
        assertEquals("",   ((String)null)._whenNotContains(",").map(String::toLowerCase).orElse(""));
    }
    
    @Test
    public void testWhenMatches() {
        assertEquals(42, "42"      ._whenMatches("^[0-9]+$").map(Integer::parseInt).orElse(-1).intValue());
        assertEquals(-1, "Blue"    ._whenMatches("^[0-9]+$").map(Integer::parseInt).orElse(-1).intValue());
        assertEquals(-1, nullString._whenMatches("^[0-9]+$").map(Integer::parseInt).orElse(-1).intValue());
    }
    
    @Test
    public void testWhenNotMatches() {
        // This variable is needed as Lombok ExtensionMethods has some problem when compile with Oracle JDK.
        val toNotNumber = (Function<String, String>)(s->"NotNumber");
        assertEquals("Number",    "42"      ._whenNotMatches("^[0-9]+$").map(toNotNumber).orElse("Number"));
        assertEquals("NotNumber", "Blue"    ._whenNotMatches("^[0-9]+$").map(toNotNumber).orElse("Number"));
        // This does not useful but it is logical.
        assertEquals("Number",    nullString._whenNotMatches("^[0-9]+$").map(toNotNumber).orElse("Number"));
    }
    
    //== Array and Collection ==
    
    @Test
    public void testStreamArray() {
        String[] array1 = new String[] { "One", "Two" };
        assertEquals("OneTwo", array1._stream$().collect(joining()));
        
        String[] array2 = null;
        assertEquals("", array2._stream$().collect(joining()));
    }
    
    @Test
    public void testStreamList() {
        List<String> list1 = asList("One", "Two");
        assertEquals("OneTwo", list1._stream$().collect(joining()));
        
        String[] list2 = null;
        assertEquals("", list2._stream$().collect(joining()));
    }
    
    @Test
    public void testToListArray() {
        String[] array1 = new String[] { "One", "Two" };
        assertEquals(2, array1._toList().size());
        
        String[] array2 = null;
        assertEquals(0, array2._toList().size());
    }
    
    @Test
    public void testToListList() {
        List<String> list1 = asList("One", "Two");
        assertEquals(2, list1._toList().size());
        assertFalse(list1 == list1._toList());
        
        List<String> list2 = null;
        assertEquals(0, list2._toList().size());
    }
    
    @Test
    public void testToListStream() {
        List<String> stream1 = asList("One", "Two");
        assertEquals(2, stream1._stream$()._toList().size());
        
        Stream<String> stream2 = null;
        assertEquals(0, stream2._toList().size());
    }
    
    @Test
    public void testGetArray() {
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
    public void testGetList() {
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
    public void testGetOrArray() {
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
    public void testGetOrList() {
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
    public void testGetOrSupplierArray() {
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
    public void testGetOrSupplierList() {
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
    public void testGetOrFunctionArray() {
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
    public void testGetOrFunctionList() {
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
    public void testGetFirstArray() {
        String[] array1 = new String[] { "One", "Two" };
        assertEquals("One", array1._first());
        
        String[] array2 = null;
        assertEquals(null, array2._first());
    }
    
    @Test
    public void testGetFirstList() {
        List<String> list1 = asList("One", "Two");
        assertEquals("One", list1._first());
        
        List<String> list2 = null;
        assertEquals(null, list2._first());
    }
    
    @Test
    public void testGetLastArray() {
        String[] array1 = new String[] { "One", "Two" };
        assertEquals("Two", array1._last());
        
        String[] array2 = null;
        assertEquals(null, array2._last());
    }
    
    @Test
    public void testGetLastList() {
        List<String> list1 = asList("One", "Two");
        assertEquals("Two", list1._last());
        
        List<String> list2 = null;
        assertEquals(null, list2._last());
    }
    
    @Test
    public void testHasAllWithArray() {
        val length3 = (Predicate<String>)s->s.length() == 3;
        
        String[] array1 = new String[] { "One", "Two" };
        assertTrue(array1._hasAllWith(length3));
        
        String[] array2 = new String[] { "One", "Two", "Three" };
        assertFalse(array2._hasAllWith(length3));
        
        String[] arrayNull = null;
        assertFalse(arrayNull._hasAllWith(length3));
    }
    
    @Test
    public void testHasAllWithList() {
        val length3 = (Predicate<String>)s->s.length() == 3;
        
        List<String> list1 = asList("One", "Two");
        assertTrue(list1._hasAllWith(length3));
        
        List<String> list2 = asList("One", "Two", "Three");
        assertFalse(list2._hasAllWith(length3));
        
        String[] listNull = null;
        assertFalse(listNull._hasAllWith(length3));
    }
    
    @Test
    public void testHasSomeWithArray() {
        val length5 = (Predicate<String>)s->s.length() == 5;
        
        String[] array1 = new String[] { "One", "Two" };
        assertFalse(array1._hasSomeWith(length5));
        
        String[] array2 = new String[] { "One", "Two", "Three" };
        assertTrue(array2._hasSomeWith(length5));
        
        String[] arrayNull = null;
        assertFalse(arrayNull._hasSomeWith(length5));
    }
    
    @Test
    public void testHasSomeWithList() {
        val length5 = (Predicate<String>)s->s.length() == 5;
        
        List<String> list1 = asList("One", "Two");
        assertFalse(list1._hasSomeWith(length5));
        
        List<String> list2 = asList("One", "Two", "Three");
        assertTrue(list2._hasSomeWith(length5));
        
        String[] listNull = null;
        assertFalse(listNull._hasSomeWith(length5));
    }
    
    @Test
    public void testButOnlyWithArray() {
        val length5 = (Predicate<String>)s->s.length() == 5;
        
        String[] array1 = new String[] { "One", "Two" };
        assertEquals(0, array1._butOnlyWith(length5).length);
        
        String[] array2 = new String[] { "One", "Two", "Three" };
        assertEquals(1, array2._butOnlyWith(length5).length);
        
        String[] arrayNull = null;
        assertNull(arrayNull._butOnlyWith(length5));
    }
    
    @Test
    public void testButOnlyWithList() {
        val length5 = (Predicate<String>)s->s.length() == 5;
        
        List<String> list1 = asList("One", "Two");
        assertEquals(0, list1._butOnlyWith(length5).size());
        
        List<String> list2 = asList("One", "Two", "Three");
        assertEquals(1, list2._butOnlyWith(length5).size());
        
        String[] listNull = null;
        assertNull(listNull._butOnlyWith(length5));
    }
    
}
