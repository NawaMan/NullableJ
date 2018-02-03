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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.junit.Test;

import lombok.val;
import lombok.experimental.ExtensionMethod;

@SuppressWarnings("javadoc")
@ExtensionMethod({ Nullable.class })
public class NullableTest {
    
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
        val someString = "String";
        val nullString = (String)null;
        assertFalse(someString._isNull());
        assertTrue(nullString._isNull());
    }
    
    @Test
    public void testIsNotNull() {
        val someString = "String";
        val nullString = (String)null;
        assertTrue(someString._isNotNull());
        assertFalse(nullString._isNotNull());
    }
    
    @Test
    public void testOr() {
        val someString = "String";
        val nullString = (String)null;
        assertEquals("String",         someString._or("Another String"));
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
        Supplier<String> erorrMessage =  ()->"Another" + " " + "String";
        val someString = "String";
        val nullString = (String)null;
        assertEquals("String",         someString._orGet(erorrMessage));
        assertEquals("Another String", nullString._orGet(erorrMessage));
    }
    
    @Test
    public void testToOptional() {
        val someString = "String";
        val nullString = (String)null;
        assertTrue(someString._toOptional() instanceof Optional);
        assertTrue(someString._toOptional().isPresent());
        assertEquals("String", someString._toOptional().get());
        
        assertTrue(nullString._toOptional() instanceof Optional);
        assertFalse(nullString._toOptional().isPresent());
    }
    
    @Test
    public void testWhenNotNull() {
        val someString = "String";
        val nullString = (String)null;
        assertTrue(someString._whenNotNull() instanceof Optional);
        assertTrue(someString._whenNotNull().isPresent());
        assertEquals("String", someString._whenNotNull().get());
        
        assertTrue(nullString._whenNotNull() instanceof Optional);
        assertFalse(nullString._whenNotNull().isPresent());
    }
    
    private static Consumer<String> saveStringTo(AtomicReference<String> ref) {
        return str -> ref.set(str);
    }
    
    @Test
    public void testWhenNotNull_action() {
        val someString = "String";
        val nullString = (String)null;
        val someResult = new AtomicReference<String>("NULL");
        val nullResult = new AtomicReference<String>("NULL");
        someString._whenNotNull(saveStringTo(someResult));
        nullString._whenNotNull(saveStringTo(nullResult));
        
        assertEquals("String", someResult.get());
        assertEquals("NULL",   nullResult.get());
    }
    
    @Test
    public void testWhen() {
        val theOriginalString = "The original string";
        val nullString = (String)null;
        
        assertEquals("The original string", theOriginalString._when(contains("original"))._or("Another string"));
        assertEquals("Another string",      theOriginalString._when(contains("another" ))._or("Another string"));
        assertEquals("Another string",      nullString       ._when(contains("original"))._or("Another string"));
        assertEquals("Another string",      nullString       ._when(contains("another" ))._or("Another string"));
    }
    
    @Test
    public void testAs() {
        val i = IntOf(1234);
        assertEquals(IntOf(1234), i._as(Integer.class));
        assertEquals(null,        i._as(Double.class));
        assertEquals(0.0,         i._as(Double.class)._or(0.0), 0.0);
    }
    
    @Test
    public void testMapTo() {
        val normalString = "String";
        val nullString   = (String)null;
        val itsLength    = (Function<String, Integer>)String::length;
        assertEquals( 6, normalString._mapTo(itsLength)._or(-1));
        assertEquals(-1, nullString  ._mapTo(itsLength)._or(-1));
    }
    
    @Test
    public void testMapBy() {
        val normalString = "String";
        val nullString   = (String)null;
        val surroundingWithQuotes = (Function<String, String>)(str->("\'" + str + "\'"));
        assertEquals("\'String\'", normalString._mapBy(surroundingWithQuotes)._or("null"));
        assertEquals("null",       nullString  ._mapBy(surroundingWithQuotes)._or("null"));
    }
    
    @Test
    public void testMapFrom() {
        val normalString = "42";
        val nullString   = (String)null;
        val stringToInt = (Function<String, Integer>)(str->Integer.parseInt(str));
        assertEquals(42, normalString._mapFrom(stringToInt)._or(0));
        assertEquals( 0, nullString  ._mapFrom(stringToInt)._or(0));
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
        val str = "Str";
        assertFalse(str._isEmpty());
        
        val nullStr = (String)null;
        assertTrue(nullStr._isEmpty());
        
        val emptyStr = "";
        assertTrue(emptyStr._isEmpty());
    }
    
    @Test
    public void testIsBlank() {
        val str = "Str";
        assertFalse(str._isBlank());
        
        val nullStr = (String)null;
        assertTrue(nullStr._isBlank());
        
        val emptyStr = "";
        assertTrue(emptyStr._isBlank());
        
        val whiteSpaceStr = " ";
        assertTrue(whiteSpaceStr._isBlank());
    }
    
    @Test
    public void testTrimToNull() {
        val str = "\t Str ";
        assertEquals("Str", str._trimToNull());
        
        val nullStr = (String)null;
        assertNull(nullStr._trimToNull());
        
        val emptyStr = "";
        assertNull(emptyStr._trimToNull());
        
        val whiteSpaceStr = " ";
        assertNull(whiteSpaceStr._trimToNull());
    }
    
    @Test
    public void testTrimToEmpty() {
        val str = "\t Str ";
        assertEquals("Str", str._trimToEmpty());
        
        val nullStr = (String)null;
        assertTrue(nullStr._trimToEmpty().isEmpty());
        
        val emptyStr = "";
        assertTrue(emptyStr._trimToEmpty().isEmpty());
        
        val whiteSpaceStr = " ";
        assertTrue(whiteSpaceStr._trimToEmpty().isEmpty());
    }
    
}
