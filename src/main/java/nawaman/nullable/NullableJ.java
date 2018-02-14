//  ========================================================================
//  Copyright (c) 2017 Nawapunth Manusitthipol (NawaMan).
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

import static java.lang.reflect.Array.newInstance;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.val;

/**
 * This utility class contains useful methods to deal with null.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class NullableJ {
    
    //== Objects ==
    
    /**
     * Returns {@code true} if theGivenObject is null. 
     * 
     * @param  theGivenObject the given object.
     * @return {@code true} if theGivenObject is null.
     **/
    public static boolean _isNull(Object theGivenObject) {
        return (theGivenObject == null);
    }
    
    /**
     * Returns {@code true} if theGivenObject is not null. 
     * 
     * @param  theGivenObject  the given object.
     * @return {@code true} if theGivenObject is not null.
     **/
    public static boolean _isNotNull(Object theGivenObject) {
        return (theGivenObject != null);
    }
    
    /**
     * Returns {@code true} if theGivenObject equals to the expected value. 
     * 
     * @param  theGivenObject   the given object.
     * @param theExpectedValue  the expected value.
     * @return {@code true} if theGivenObject is null.
     **/
    public static boolean _equalsTo(Object theGivenObject, Object theExpectedValue) {
        val equalsTo = Objects.equals(theGivenObject, theExpectedValue);
        return equalsTo;
    }
    
    /**
     * Returns {@code true} if theGivenObject is not null. 
     * 
     * @param  theGivenObject    the given object.
     * @param  theExpectedValue  the expected value.
     * @return {@code true} if theGivenObject is not null.
     **/
    public static boolean _notEqualsTo(Object theGivenObject, Object theExpectedValue) {
        val equalsTo = Objects.equals(theGivenObject, theExpectedValue);
        return !equalsTo;
    }
    
    /**
     * Returns the toString of the give value or null if the given value is null. 
     * 
     * @param  theGivenObject   the given object.
     * @return the toString of the given object.
     **/
    public static String _toString(Object theGivenObject) {
        val toString = (theGivenObject != null) ? theGivenObject.toString() : null;
        return toString;
    }
    
    /**
     * Returns the toString of the give array or null if the given value is null. 
     * 
     * @param  theGivenArray   the given array.
     * @return the toString of the given object.
     * 
     * @param <OBJECT>  the data type of the element in the array.
     **/
    public static <OBJECT> String _toString(OBJECT[] theGivenArray) {
        val toString = (theGivenArray != null) ? Arrays.toString((Object[])theGivenArray) : null;
        return toString;
    }
    
    /**
     * Returns elseValue if theGivenObject is null. 
     * 
     * @param  theGivenObject  the given object.
     * @param  elseValue       the return value for when the given object is null.
     * @param  <OBJECT>        the data type of the given object.
     * @return theGivenObject if not null or elseValue if null.
     **/
    public static <OBJECT> OBJECT _or(OBJECT theGivenObject, OBJECT elseValue) {
        val result = (theGivenObject == null) ? elseValue : theGivenObject;
        return result;
    }
    
    /**
     * Returns the result of elseSupplier if the given object is null. 
     * 
     * @param  theGivenObject  the given object.
     * @param  elseSupplier    the supplier for when the given object is null.
     * @param  <OBJECT>        the data type of the given object.
     * @return theGivenObject if not null or value from the elseSupplier if null.
     **/
    public static <OBJECT> OBJECT _orGet(OBJECT theGivenObject, Supplier<? extends OBJECT> elseSupplier) {
        val result = (theGivenObject == null) ? _get(elseSupplier) : theGivenObject;
        return result;
    }
    
    /**
     * Returns the null value.
     * 
     * @param  theGivenObject  the given object.
     * @param  theObjectClass  the class of the given data.
     * @param  <OBJECT>        the data type of the given object.
     * @return theGivenObject if not null or value from the elseSupplier if null.
     **/
    public static <OBJECT> OBJECT _orNullValue(OBJECT theGivenObject, Class<OBJECT> theObjectClass) {
        val result = (theGivenObject == null) ? NullValues.nullValueOf(theObjectClass) : theGivenObject;
        return result;
    }
    
    /**
     * Returns the null value.
     * 
     * @param  theNullableObject  the optional object.
     * @param  theObjectClass     the class of the given data.
     * @param  <OBJECT>           the data type of the given object.
     * @return theGivenObject if not null or value from the elseSupplier if null.
     **/
    public static <OBJECT> Nullable<OBJECT> _orElseNullValue(Nullable<OBJECT> theNullableObject, Class<OBJECT> theObjectClass) {
        val result = theNullableObject.isPresent() ? theNullableObject : Nullable.of(NullValues.nullValueOf(theObjectClass));
        return result;
    }
    
    /**
     * Extension method to create optional of theGivenObject. 
     * 
     * @param  theGivenObject  the given object.
     * @param  <OBJECT>        the data type of the given object.
     * @return the optional value of the theGivenObject.
     **/
    public static <OBJECT> Nullable<OBJECT> _toNullable(OBJECT theGivenObject) {
        return Nullable.of(theGivenObject);
    }
    
    /**
     * Extension method to create optional of theGivenObject. 
     * 
     * @param  theGivenObject  the given object.
     * @param  <OBJECT>        the data type of the given object.
     * @return the optional value of the theGivenObject.
     **/
    public static <OBJECT> Optional<OBJECT> _toOptional(OBJECT theGivenObject) {
        return Optional.ofNullable(theGivenObject);
    }
    
    /**
     * Extension method to create optional of theGivenObject. 
     * 
     * @param  theGivenObject  the given object.
     * @param  <OBJECT>        the data type of the given object.
     * @return the optional value of the theGivenObject.
     **/
    public static <OBJECT> Nullable<OBJECT> _whenNotNull(OBJECT theGivenObject) {
        return Nullable.of(theGivenObject);
    }
    
    /**
     * Perform the theAction of theGivenObject is not null. 
     * 
     * @param  theGivenObject  the given object.
     * @param  theAction       the action.
     * @param  <OBJECT>        the data type of the given object.
     **/
    public static <OBJECT> void _whenNotNull(OBJECT theGivenObject, Consumer<OBJECT> theAction) {
        if (theGivenObject == null)
            return;
        theAction.accept(theGivenObject);
    }
    
    /**
     * Return the Nullable of the given object if the test yields {@code true} or else return {@code Nullable.empty}.
     * 
     * @param  theGivenObject  the given object.
     * @param  theTest         the test.
     * @param  <OBJECT>        the data type of the given object.
     * @return  the Nullable of the original object or {@code Nullable.empty}.
     */
    public static <OBJECT> Nullable<OBJECT> _when(OBJECT theGivenObject, Predicate<OBJECT> theTest) {
        if (theGivenObject == null)
            return Nullable.empty();
        
        val conditionResult = theTest.test(theGivenObject);
        if (!conditionResult)
            return Nullable.empty();
        
        return Nullable.of(theGivenObject);
    }
    
    /**
     * Return the given object if it is of the given class or else return null.
     * 
     * @param  theGivenObject  the given object.
     * @param  theClass        the class.
     * @param  <OBJECT>        the data type of the given object.
     * @param  <CLASS>         the data type of the returned object.
     * @return  the original object as the type class or null.
     */
    public static <OBJECT, CLASS> CLASS _as(OBJECT theGivenObject, Class<CLASS> theClass) {
        val isInstanceOf = theClass.isInstance(theGivenObject);
        if (!isInstanceOf)
            return null;
        
        return theClass.cast(theGivenObject);
    }
    
    /**
     * Map the given object using the transformation if the given object is not null or else return null.
     * 
     * This method is the alias of mapBy and mapFrom.
     * 
     * @param  theGivenObject  the given object.
     * @param  transformation  the transformation function.
     * @param  <OBJECT>        the data type of the given object.
     * @param  <TARGET>        the data type of the target object.
     * @return  the transformed value.
     */
    public static <OBJECT, TARGET> TARGET _mapTo(OBJECT theGivenObject, Function<OBJECT, TARGET> transformation) {
        return (theGivenObject != null) ? transformation.apply(theGivenObject) : null;
    }
    
    /**
     * Map the given object using the transformation if the given object is not null or else return null.
     * 
     * This method is the alias of mapTo and mapFrom.
     * 
     * @param  theGivenObject  the given object.
     * @param  transformation  the transformation function.
     * @param  <OBJECT>        the data type of the given object.
     * @param  <TARGET>        the data type of the target object.
     * @return  the transformed value.
     */
    public static <OBJECT, TARGET> TARGET _mapBy(OBJECT theGivenObject, Function<OBJECT, TARGET> transformation) {
        return (theGivenObject != null) ? transformation.apply(theGivenObject) : null;
    }
    
    /**
     * Map the given object using the transformation if the given object is not null or else return null.
     * 
     * This method is the alias of mapTo and mapBy.
     * 
     * @param  theGivenObject  the given object.
     * @param  transformation  the transformation function.
     * @param  <OBJECT>        the data type of the given object.
     * @param  <TARGET>        the data type of the target object.
     * @return the transformed value.
     */
    public static <OBJECT, TARGET> TARGET _mapFrom(OBJECT theGivenObject, Function<OBJECT, TARGET> transformation) {
        return (theGivenObject != null) ? transformation.apply(theGivenObject) : null;
    }
    
    //== Primitive types 'or' ==
    
    /**
     * Returns elseValue if theGivenInteger is null. 
     * 
     * @param  theGivenInteger  the given integer.
     * @param  elseValue        the return value for when the given object is null.
     * @return theGivenObject if not null or elseValue if null.
     **/
    public static int _or(Integer theGivenInteger, int elseValue) {
        return (theGivenInteger == null) ? elseValue : theGivenInteger.intValue();
    }
    
    /**
     * Returns elseValue if theGivenLong is null. 
     * 
     * @param  theGivenLong  the given long.
     * @param  elseValue     the return value for when the given object is null.
     * @return theGivenObject if not null or elseValue if null.
     **/
    public static long _or(Long theGivenLong, long elseValue) {
        return (theGivenLong == null) ? elseValue : theGivenLong.longValue();
    }
    
    /**
     * Returns elseValue if theGivenLong is null. 
     * 
     * @param  theGivenLong  the given long.
     * @param  elseValue     the return value for when the given object is null.
     * @return theGivenObject if not null or elseValue if null.
     **/
    public static long _or(Long theGivenLong, int elseValue) {
        return (theGivenLong == null) ? elseValue : theGivenLong.longValue();
    }
    
    /**
     * Returns elseValue if theGivenDouble is null. 
     * 
     * @param  theGivenDouble  the given double.
     * @param  elseValue       the return value for when the given object is null.
     * @return theGivenObject if not null or elseValue if null.
     **/
    public static double _or(Double theGivenDouble, double elseValue) {
        return (theGivenDouble == null) ? elseValue : theGivenDouble.doubleValue();
    }
    
    /**
     * Returns elseValue if theGivenDouble is null. 
     * 
     * @param  theGivenDouble  the given double.
     * @param  elseValue       the return value for when the given object is null.
     * @return theGivenObject if not null or elseValue if null.
     **/
    public static double _or(Double theGivenDouble, int elseValue) {
        return (theGivenDouble == null) ? elseValue : theGivenDouble.doubleValue();
    }
    
    /**
     * Returns elseValue if theGivenDouble is null. 
     * 
     * @param  theGivenBigInteger  the given BigInteger.
     * @param  elseValue           the return value for when the given object is null.
     * @return theGivenObject if not null or elseValue if null.
     **/
    public static BigInteger _or(BigInteger theGivenBigInteger, int elseValue) {
        return (theGivenBigInteger == null) ? BigInteger.valueOf(elseValue) : theGivenBigInteger;
    }
    
    /**
     * Returns elseValue if theGivenDouble is null. 
     * 
     * @param  theGivenBigInteger  the given BigInteger.
     * @param  elseValue           the return value for when the given object is null.
     * @return theGivenObject if not null or elseValue if null.
     **/
    public static BigInteger _or(BigInteger theGivenBigInteger, long elseValue) {
        return (theGivenBigInteger == null) ? BigInteger.valueOf(elseValue) : theGivenBigInteger;
    }
    
    /**
     * Returns elseValue if theGivenDouble is null. 
     * 
     * @param  theGivenBigDecimal  the given BigInteger.
     * @param  elseValue           the return value for when the given object is null.
     * @return theGivenObject if not null or elseValue if null.
     **/
    public static BigDecimal _or(BigDecimal theGivenBigDecimal, int elseValue) {
        return (theGivenBigDecimal == null) ? BigDecimal.valueOf(elseValue) : theGivenBigDecimal;
    }
    
    /**
     * Returns elseValue if theGivenDouble is null. 
     * 
     * @param  theGivenBigDecimal  the given BigInteger.
     * @param  elseValue           the return value for when the given object is null.
     * @return theGivenObject if not null or elseValue if null.
     **/
    public static BigDecimal _or(BigDecimal theGivenBigDecimal, long elseValue) {
        return (theGivenBigDecimal == null) ? BigDecimal.valueOf(elseValue) : theGivenBigDecimal;
    }
    
    /**
     * Returns elseValue if theGivenDouble is null. 
     * 
     * @param  theGivenBigDecimal  the given BigInteger.
     * @param  elseValue           the return value for when the given object is null.
     * @return theGivenObject if not null or elseValue if null.
     **/
    public static BigDecimal _or(BigDecimal theGivenBigDecimal, double elseValue) {
        return (theGivenBigDecimal == null) ? BigDecimal.valueOf(elseValue) : theGivenBigDecimal;
    }
    
    //== Strings ==
    
    /**
     * Checks if theGivenString is empty (null or 0-length).
     * 
     * @param  theGivenString  the given string.
     * @return {@code true} if theGivenString is empty.
     */
    public static boolean _isEmpty(String theGivenString) {
        return (theGivenString == null) || theGivenString.isEmpty();
    }
    
    /**
     * Checks if theGivenString is blank (null or its trim is empty).
     * 
     * @param  theGivenString  the given string.
     * @return {@code true} if theGivenString is blank.
     */
    public static boolean _isBlank(String theGivenString) {
        return (theGivenString == null) || theGivenString.trim().isEmpty();
    }
    
    /**
     * Trim the given string and if the result is an empty string, return null.
     * 
     * @param theGivenString  the given string.
     * @return  {@code null} if the given string is null or its trimmed value is empty.
     */
    public static String _trimToNull(String theGivenString) {
        if (theGivenString == null)
            return null;
        
        val trimmedString = theGivenString.trim();
        if (trimmedString.isEmpty())
            return null;
        
        return trimmedString;
    }
    
    /**
     * Trim and return the given string. If the string is null, return empty string.
     * 
     * @param theGivenString  the given string.
     * @return  {@code ""} if the given string is null or its trimmed value.
     */
    public static String _trimToEmpty(String theGivenString) {
        if (theGivenString == null)
            return "";
        
        val trimmedString = theGivenString.trim();
        return trimmedString;
    }
    
    /**
     * Check if the given string contains the needle.
     * 
     * @param theGivenString  the given string.
     * @param theNeedle       the needle.
     * @return  if the given string contains the needle or {@code false} if it is null.
     */
    public static boolean _contains(String theGivenString, CharSequence theNeedle) {
        if (theGivenString == null)
            return false;
        
        val theResult = theGivenString.contains(theNeedle);
        return theResult;
    }
    
    /**
     * Check if the given string contains the needle.
     * 
     * @param theGivenString  the given string.
     * @param theNeedle       the needle.
     * @return  if the given string DOES NOT contain the needle or {@code true} if it is null.
     */
    public static boolean _notContains(String theGivenString, CharSequence theNeedle) {
        if (theGivenString == null)
            return true;
        
        val theResult = !theGivenString.contains(theNeedle);
        return theResult;
    }
    
    /**
     * Check if the given string matches the regular expression.
     * 
     * @param theGivenString  the given string.
     * @param theRegex        the regular expression.
     * @return  if the given string matches the regular expression or false if it is null.
     */
    public static boolean _matches(String theGivenString, String theRegex) {
        if (theGivenString == null)
            return false;
        
        val theResult = theGivenString.matches(theRegex);
        return theResult;
    }
    
    /**
     * Check if the given string matches the regular expression.
     * 
     * @param theGivenString  the given string.
     * @param theRegex        the regular expression.
     * @return  if the given string DOES NOT match the regular expression or true if it is null.
     */
    public static boolean _notMatches(String theGivenString, String theRegex) {
        if (theGivenString == null)
            return true;
        
        val theResult = !theGivenString.matches(theRegex);
        return theResult;
    }
    
    /**
     * Check if the given string matches the regular expression.
     * 
     * @param theGivenString  the given string.
     * @param theRegex        the regular expression.
     * @return  if the given string matches the regular expression or false if it is null.
     */
    public static boolean _matches(String theGivenString, Pattern theRegex) {
        if (theGivenString == null)
            return false;
        
        val theResult = theRegex.matcher(theGivenString).find();
        return theResult;
    }
    
    /**
     * Check if the given string matches the regular expression.
     * 
     * @param theGivenString  the given string.
     * @param theRegex        the regular expression.
     * @return  if the given string DOES NOT match the regular expression or true if it is null.
     */
    public static boolean _notMatches(String theGivenString, Pattern theRegex) {
        if (theGivenString == null)
            return true;
        
        val theResult = !theRegex.matcher(theGivenString).find();
        return theResult;
    }
    
    /**
     * Check if the given string contains the needle.
     * 
     * @param theGivenString  the given string.
     * @param theNeedle       the needle.
     * @return  the Nullable of the original string if it contains the value otherwise return {@code Nullable.empty()}.
     */
    public static Nullable<String> _whenContains(String theGivenString, CharSequence theNeedle) {
        if (theGivenString == null)
            return Nullable.empty();
        
        val isContains = theGivenString.contains(theNeedle);
        val theResult  = isContains ? theGivenString : null;
        return Nullable.of(theResult);
    }
    
    /**
     * Check if the given string contains the needle.
     * 
     * @param theGivenString  the given string.
     * @param theNeedle       the needle.
     * @return  the Nullable of the original string if it DOES NOT contain the needle otherwise return {@code null}.
     */
    public static Nullable<String> _whenNotContains(String theGivenString, CharSequence theNeedle) {
        if (theGivenString == null)
            return Nullable.empty();
        
        val isContains = theGivenString.contains(theNeedle);
        val theResult  = isContains ? null : theGivenString;
        return Nullable.of(theResult);
    }
    
    /**
     * Check if the given string matches the regular expression.
     * 
     * @param theGivenString  the given string.
     * @param theRegex        the regular expression.
     * @return  the Nullable of the original string if it matches the needle otherwise return {@code Nullable.empty()}.
     */
    public static Nullable<String> _whenMatches(String theGivenString, String theRegex) {
        if (theGivenString == null)
            return Nullable.empty();
        
        val isMatches = theGivenString.matches(theRegex);
        val theResult  = isMatches ? theGivenString : null;
        return Nullable.of(theResult);
    }
    
    /**
     * Check if the given string matches the regular expression.
     * 
     * @param theGivenString  the given string.
     * @param theRegex        the regular expression.
     * @return  the Nullable of the original string if it DOES NOT contain the needle otherwise return {@code Nullable.empty()}.
     */
    public static Nullable<String> _whenNotMatches(String theGivenString, String theRegex) {
        if (theGivenString == null)
            return Nullable.empty();
        
        val isMatches = theGivenString.matches(theRegex);
        val theResult  = isMatches ? null : theGivenString;
        return Nullable.of(theResult);
    }
    
    /**
     * Check if the given string matches the regular expression.
     * 
     * @param theGivenString  the given string.
     * @param theRegex        the regular expression.
     * @return  the Nullable of the original string if it matches the needle otherwise return {@code Nullable.empty()}.
     */
    public static Nullable<String> _whenMatches(String theGivenString, Pattern theRegex) {
        if (theGivenString == null)
            return Nullable.empty();
        
        val isMatches = theRegex.matcher(theGivenString).find();
        val theResult  = isMatches ? theGivenString : null;
        return Nullable.of(theResult);
    }
    
    /**
     * Check if the given string matches the regular expression.
     * 
     * @param theGivenString  the given string.
     * @param theRegex        the regular expression.
     * @return  the Nullable of the original string if it DOES NOT contain the needle otherwise return {@code Nullable.empty()}.
     */
    public static Nullable<String> _whenNotMatches(String theGivenString, Pattern theRegex) {
        if (theGivenString == null)
            return Nullable.empty();
        
        val isMatches = theRegex.matcher(theGivenString).find();
        val theResult  = isMatches ? null : theGivenString;
        return Nullable.of(theResult);
    }
    
    //== Array and Collection ==
    
    /**
     * Returns the stream of the given array.
     * 
     * @param array  the array.
     * @return  the stream.
     * 
     * @param <OBJECT> the type of the data in the array.
     */
    public static <OBJECT> Stream<OBJECT> _stream$(OBJECT[] array) {
        if (array == null)
            return Stream.empty();
        if (array.length == 0)
            return Stream.empty();
        return stream(array);
    }
    
    /**
     * Returns the stream of the given object.
     * 
     * @param list  the list.
     * @return  the stream.
     * 
     * @param <OBJECT> the type of the data in the list.
     */
    public static <OBJECT> Stream<OBJECT> _stream$(List<OBJECT> list) {
        if (list == null)
            return Stream.empty();
        return list.stream();
    }
    
    /**
     * Returns the length of the given string.
     * 
     * @param string  the string.
     * @return  the lenght of the string.
     * 
     * @param <OBJECT> the type of the data in the list.
     */
    public static <OBJECT> int _length(String string) {
        if (string == null)
            return 0;
        return string.length();
    }
    
    /**
     * Returns the length of the given array.
     * 
     * @param array  the array.
     * @return  the length of the array.
     * 
     * @param <OBJECT> the type of the data in the list.
     */
    public static <OBJECT> int _length(OBJECT[] array) {
        if (array == null)
            return 0;
        return array.length;
    }
    
    /**
     * Returns the size of the given list.
     * 
     * @param list  the list.
     * @return  the size of the list.
     * 
     * @param <OBJECT> the type of the data in the list.
     */
    public static <OBJECT> int _size(List<OBJECT> list) {
        if (list == null)
            return 0;
        return list.size();
    }
    
    /**
     * Returns the size of the given map.
     * 
     * @param map  the map.
     * @return  the size of the map.
     * 
     * @param <KEY>   the type of the key of the function.
     * @param <VALUE> the type of the value of the function.
     */
    public static <KEY, VALUE> int _size(Map<KEY, VALUE> map) {
        if (map == null)
            return 0;
        return map.size();
    }
    
    /**
     * Returns the given array is empty.
     * 
     * @param array  the array.
     * @return  {@code true}  if the array is empty.
     * 
     * @param <OBJECT> the type of the data in the array.
     */
    public static <OBJECT> boolean _isEmpty(OBJECT[] array) {
        if (array == null)
            return true;
        return array.length == 0;
    }
    
    /**
     * Returns the given list is empty.
     * 
     * @param list  the list.
     * @return  {@code true}  if the list is empty.
     * 
     * @param <OBJECT> the type of the data in the list.
     */
    public static <OBJECT> boolean _isEmpty(List<OBJECT> list) {
        if (list == null)
            return true;
        return list.isEmpty();
    }
    
    /**
     * Returns the given map is empty.
     * 
     * @param map  the map.
     * @return  {@code true}  if the map is empty.
     * 
     * @param <KEY>   the type of the key of the function.
     * @param <VALUE> the type of the value of the function.
     */
    public static <KEY, VALUE> boolean _isEmpty(Map<KEY, VALUE> map) {
        if (map == null)
            return true;
        return map.isEmpty();
    }
    
    /**
     * Returns Nullable.empty() the given CharSequence is null or empty..
     * 
     * @param charSequence  the CharSequence such as an array.
     * @return  Nullable value if the charSequence.
     * 
     * @param <CHARSEQUENCE>  the CharSequence type.
     */
    public static <CHARSEQUENCE extends CharSequence> Nullable<CHARSEQUENCE> _whenNotEmpty(CHARSEQUENCE charSequence) {
        if (charSequence == null)
            return Nullable.empty();
        if (charSequence.length() == 0)
            return Nullable.empty();
        return Nullable.of(charSequence);
    }
    
    /**
     * Returns Nullable.empty if the given array is null or empty. Otherwise, return the Nullable of the array.
     * 
     * @param array  the array.
     * @return  Nullable value if the array.
     * 
     * @param <OBJECT> the type of the data in the array.
     */
    public static <OBJECT> Nullable<OBJECT[]> _whenNotEmpty(OBJECT[] array) {
        if (array == null)
            return Nullable.empty();
        if (array.length == 0)
            return Nullable.empty();
        return Nullable.of(array);
    }
    
    /**
     * Returns Nullable.empty if the given list is null or empty. Otherwise, return the Nullable of the list.
     * 
     * @param list  the list.
     * @return  Nullable value if the list.
     * 
     * @param <OBJECT> the type of the data in the list.
     */
    public static <OBJECT> Nullable<List<OBJECT>> _whenNotEmpty(List<OBJECT> list) {
        if (list == null)
            return Nullable.empty();
        if (list.isEmpty())
            return Nullable.empty();
        return Nullable.of(list);
    }
    
    /**
     * Returns Nullable.empty if the given map is null or empty. Otherwise, return the Nullable of the map.
     * 
     * @param map  the map.
     * @return  Nullable value if the map.
     * 
     * @param <KEY>   the type of the key of the function.
     * @param <VALUE> the type of the value of the function.
     */
    public static <KEY, VALUE> Nullable<Map<KEY, VALUE>> _whenNotEmpty(Map<KEY, VALUE> map) {
        if (map == null)
            return Nullable.empty();
        if (map.isEmpty())
            return Nullable.empty();
        return Nullable.of(map);
    }
    
    /**
     * Returns the stream of from the given list but does not contains null value.
     * 
     * @param array  the array.
     * @return  the stream.
     * 
     * @param <OBJECT> the type of the data in the array.
     */
    public static <OBJECT> Stream<OBJECT> _butOnlyNonNull$(OBJECT[] array) {
        if (array == null)
            return Stream.empty();
        if (array.length == 0)
            return Stream.empty();
        return stream(array).filter(Objects::nonNull);
    }
    
    /**
     * Returns the stream of from the given list but does not contains null value.
     * 
     * @param list  the list.
     * @return  the stream.
     * 
     * @param <OBJECT> the type of the data in the list.
     */
    public static <OBJECT> Stream<OBJECT> _butOnlyNonNull$(List<OBJECT> list) {
        if (list == null)
            return Stream.empty();
        return list.stream().filter(Objects::nonNull);
    }
    
    /**
     * Returns the stream of from the given stream but does not contains null value.
     * 
     * @param stream  the stream.
     * @return  the stream.
     * 
     * @param <OBJECT> the type of the data in the stream.
     */
    public static <OBJECT> Stream<OBJECT> _butOnlyNonNull$(Stream<OBJECT> stream) {
        if (stream == null)
            return Stream.empty();
        return stream.filter(Objects::nonNull);
    }
    
    /**
     * Returns the list of the given array.
     * 
     * @param array  the array.
     * @return  the list.
     * 
     * @param <OBJECT> the type of the data in the list.
     */
    public static <OBJECT> List<OBJECT> _toList(OBJECT[] array) {
        if (array == null)
            return Collections.emptyList();
        if (array.length == 0)
            return Collections.emptyList();
        return stream(array).collect(Collectors.toList());
    }
    
    /**
     * Returns the list of the given list (a copy using stream().collect(toList())).
     * 
     * @param list  the list.
     * @return  the list.
     * 
     * @param <OBJECT> the type of the data in the list.
     */
    public static <OBJECT> List<OBJECT> _toList(List<OBJECT> list) {
        if (list == null)
            return Collections.emptyList();
        return list.stream().collect(Collectors.toList());
    }
    
    /**
     * Returns the list of the given object.
     * 
     * @param stream  the stream.
     * @return  the list.
     * 
     * @param <OBJECT> the type of the data in the stream.
     */
    public static <OBJECT> List<OBJECT> _toList(Stream<OBJECT> stream) {
        if (stream == null)
            return Collections.emptyList();
        return stream.collect(Collectors.toList());
    }
    
    /**
     * Get value from the supplier and return null if the supplier is null.
     * 
     * @param supplier  the supplier.
     * @return  the value at the index or null.
     * 
     * @param <OBJECT> the type of the data in the supplier.
     */
    public static <OBJECT> OBJECT _get(Supplier<OBJECT> supplier) {
        if (supplier == null)
            return null;
        return supplier.get();
    }
    
    /**
     * Get value from the function with the key and return null fail.
     * 
     * @param function  the function.
     * @param key       the key.
     * @return  the value at the index or null.
     * 
     * @param <KEY>   the type of the key of the function.
     * @param <VALUE> the type of the value of the function.
     */
    public static <KEY, VALUE> VALUE _get(Function<KEY, VALUE> function, KEY key) {
        if (function == null)
            return null;
        return function.apply(key);
    }
    
    /**
     * Get value from the function with the key and return null fail.
     * 
     * @param function  the function.
     * @param key       the key.
     * @return  the value at the index or null.
     * 
     * @param <KEY>   the type of the key of the function.
     * @param <VALUE> the type of the value of the function.
     */
    public static <KEY, VALUE> VALUE _apply(Function<KEY, VALUE> function, KEY key) {
        if (function == null)
            return null;
        return function.apply(key);
    }
    
    /**
     * Get the element in the array at the index and return {@code null} if fail.
     * 
     * @param array  the array.
     * @param index  the index.
     * @return  the value at the index or null.
     * 
     * @param <OBJECT> the type of the data in the array.
     */
    public static <OBJECT> OBJECT _get(OBJECT[] array, int index) {
        if (array == null)
            return null;
        if (array.length == 0)
            return null;
        if (index < 0)
            return null;
        if (index >= array.length)
            return null;
        return array[index];
    }
    
    /**
     * Get the element in the array at the index and return {@code null} if fail.
     * 
     * @param list   the list.
     * @param index  the index.
     * @return  the value at the index or null.
     * 
     * @param <OBJECT> the type of the data in the list.
     */
    public static <OBJECT> OBJECT _get(List<OBJECT> list, int index) {
        if (list == null)
            return null;
        if (list.isEmpty())
            return null;
        if (index < 0)
            return null;
        if (index >= list.size())
            return null;
        return list.get(index);
    }
    
    /**
     * Get the element in the map associating with the key and return {@code null} if fail.
     * 
     * @param map  the map.
     * @param key  the key.
     * @return  the value at the index or null.
     * 
     * @param <KEY>   the type of the key of the map.
     * @param <VALUE> the type of the value of the map.
     */
    public static <KEY, VALUE> VALUE _get(Map<KEY, VALUE> map, KEY key) {
        if (map == null)
            return null;
        if (map.isEmpty())
            return null;
        try {
            if (!map.containsKey(key))
                return null;
        } catch (NullPointerException e) {
            // Some map throws exception for null key.
            return null;
        }
        return map.get(key);
    }
    
    /**
     * Get the element in the array at the index and return orValue if fail.
     * 
     * @param array    the array.
     * @param index    the index.
     * @param orValue  the value to return if getting the value fail.
     * @return  the value at the index or null.
     * 
     * @param <OBJECT> the type of the data in the array.
     */
    public static <OBJECT> OBJECT _get(OBJECT[] array, int index, OBJECT orValue) {
        if (array == null)
            return orValue;
        if (array.length == 0)
            return orValue;
        if (index < 0)
            return orValue;
        if (index >= array.length)
            return orValue;
        
        return array[index];
    }
    /**
     * Get the element in the array at the index and return orValue if fail.
     * 
     * @param list   the list.
     * @param index  the index.
     * @param orValue  the value to return if getting the value fail.
     * @return  the value at the index or null.
     * 
     * @param <OBJECT> the type of the data in the list.
     */
    public static <OBJECT> OBJECT _get(List<OBJECT> list, int index, OBJECT orValue) {
        if (list == null)
            return orValue;
        if (list.isEmpty())
            return orValue;
        if (index < 0)
            return orValue;
        if (index >= list.size())
            return orValue;
        
        return list.get(index);
    }
    
    /**
     * Get the element in the map associating with the key and return the default value if fail.
     * 
     * @param map      the map.
     * @param key      the key.
     * @param orValue  the default value.
     * @return  the value at the index or null.
     * 
     * @param <KEY>   the type of the key of the map.
     * @param <VALUE> the type of the value of the map.
     */
    public static <KEY, VALUE> VALUE _get(Map<KEY, VALUE> map, KEY key, VALUE orValue) {
        if (map == null)
            return orValue;
        if (map.isEmpty())
            return orValue;
        try {
            if (!map.containsKey(key))
                return orValue;
        } catch (NullPointerException e) {
            // Some map throws exception for null key.
            return orValue;
        }
        return _or(map.get(key), orValue);
    }
    
    /**
     * Get the element in the array at the index and return the value from orSupplier if fail.
     * 
     * @param array       the array.
     * @param index       the index.
     * @param orSupplier  the supplier of the value to return if getting the value fail.
     * @return  the value at the index or null.
     * 
     * @param <OBJECT> the type of the data in the array.
     */
    public static <OBJECT> OBJECT _get(OBJECT[] array, int index, Supplier<OBJECT> orSupplier) {
        if (array == null)
            return orSupplier.get();
        if (array.length == 0)
            return orSupplier.get();
        if (index < 0)
            return orSupplier.get();
        if (index >= array.length)
            return orSupplier.get();
        
        return array[index];
    }
    /**
     * Get the element in the array at the index and return the value from orSupplier if fail.
     * 
     * @param list        the list.
     * @param index       the index.
     * @param orSupplier  the supplier of the value to return if getting the value fail.
     * @return  the value at the index or null.
     * 
     * @param <OBJECT> the type of the data in the list.
     */
    public static <OBJECT> OBJECT _get(List<OBJECT> list, int index, Supplier<OBJECT> orSupplier) {
        if (list == null)
            return orSupplier.get();
        if (list.isEmpty())
            return orSupplier.get();
        if (index < 0)
            return orSupplier.get();
        if (index >= list.size())
            return orSupplier.get();
        
        return list.get(index);
    }
    
    /**
     * Get the element in the map associating with the key and return the orSupplier if fail.
     * 
     * @param map      the map.
     * @param key      the key.
     * @param orSupplier  the supplier of the value to return if getting the value fail.
     * @return  the value at the index or null.
     * 
     * @param <KEY>   the type of the key of the map.
     * @param <VALUE> the type of the value of the map.
     */
    public static <KEY, VALUE> VALUE _get(Map<KEY, VALUE> map, KEY key, Supplier<VALUE> orSupplier) {
        if (map == null)
            return _get(orSupplier);
        if (map.isEmpty())
            return _get(orSupplier);
        try {
            if (!map.containsKey(key))
                return _get(orSupplier);
        } catch (NullPointerException e) {
            // Some map throws exception for null key.
            return _get(orSupplier);
        }
        return _orGet(map.get(key), orSupplier);
    }
    
    /**
     * Get the element in the array at the index and return the value from orFunction if fail.
     * 
     * @param array       the array.
     * @param index       the index.
     * @param orFunction  the function that create the value to return if getting the value fail.
     * @return  the value at the index or null.
     * 
     * @param <OBJECT> the type of the data in the array.
     */
    public static <OBJECT> OBJECT _get(OBJECT[] array, int index, Function<Integer, OBJECT> orFunction) {
        if (array == null)
            return orFunction.apply(index);
        if (array.length == 0)
            return orFunction.apply(index);
        if (index < 0)
            return orFunction.apply(index);
        if (index >= array.length)
            return orFunction.apply(index);
        
        return array[index];
    }
    /**
     * Get the element in the array at the index and return the value from orFunction if fail.
     * 
     * @param list        the list.
     * @param index       the index.
     * @param orFunction  the function that create the value to return if getting the value fail.
     * @return  the value at the index or null.
     * 
     * @param <OBJECT> the type of the data in the list.
     */
    public static <OBJECT> OBJECT _get(List<OBJECT> list, int index, Function<Integer, OBJECT> orFunction) {
        if (list == null)
            return orFunction.apply(index);
        if (list.isEmpty())
            return orFunction.apply(index);
        if (index < 0)
            return orFunction.apply(index);
        if (index >= list.size())
            return orFunction.apply(index);
        
        return list.get(index);
    }
    
    /**
     * Get the element in the map associating with the key and return the orFunction if fail.
     * 
     * @param map      the map.
     * @param key      the key.
     * @param orFunction  the function that create the value to return if getting the value fail.
     * @return  the value at the index or null.
     * 
     * @param <KEY>   the type of the key of the map.
     * @param <VALUE> the type of the value of the map.
     */
    public static <KEY, VALUE> VALUE _get(Map<KEY, VALUE> map, KEY key, Function<KEY, VALUE> orFunction) {
        if (map == null)
            return _get(orFunction, key);
        if (map.isEmpty())
            return _get(orFunction, key);
        try {
            if (!map.containsKey(key))
                return _get(orFunction, key);
        } catch (NullPointerException e) {
            // Some map throws exception for null key.
            return _get(orFunction, key);
        }
        return _orGet(map.get(key), ()->_get(orFunction, key));
    }
    
    /**
     * Get the first element in the array and return {@code null} if fail.
     * 
     * @param array  the array.
     * @return  the value at the 0th index or null.
     * 
     * @param <OBJECT> the type of the data in the array.
     */
    public static <OBJECT> OBJECT _first(OBJECT[] array) {
        if (array == null)
            return null;
        if (array.length == 0)
            return null;
        return array[0];
    }
    /**
     * Get the first element in the array at the index and return {@code null} if fail.
     * 
     * @param list   the list.
     * @return  the value at the 0th index or null.
     * 
     * @param <OBJECT> the type of the data in the list.
     */
    public static <OBJECT> OBJECT _first(List<OBJECT> list) {
        if (list == null)
            return null;
        if (list.isEmpty())
            return null;
        return list.get(0);
    }
    
    /**
     * Get the last element in the array and return {@code null} if fail.
     * 
     * @param array  the array.
     * @return  the value at the last index or null.
     * 
     * @param <OBJECT> the type of the data in the array.
     */
    public static <OBJECT> OBJECT _last(OBJECT[] array) {
        if (array == null)
            return null;
        if (array.length == 0)
            return null;
        return array[array.length - 1];
    }
    /**
     * Get the last element in the array at the index and return {@code null} if fail.
     * 
     * @param list   the list.
     * @return  the value at the last index or null.
     * 
     * @param <OBJECT> the type of the data in the list.
     */
    public static <OBJECT> OBJECT _last(List<OBJECT> list) {
        if (list == null)
            return null;
        if (list.isEmpty())
            return null;
        return list.get(list.size() - 1);
    }
    
    /**
     * Check if all elements in the the given array pass all the check by the condition.
     * 
     * @param array      the array.
     * @param condition  the condition.
     * @return  {@code true} if all elements pass the condition.
     * 
     * @param <OBJECT> the type of the data in the array.
     */
    public static <OBJECT> boolean _hasAllThat(OBJECT[] array, Predicate<OBJECT> condition) {
        if (array == null)
            return false;
        if (array.length == 0)
            return false;
        return Arrays.stream(array).allMatch(condition);
    }
    /**
     * Check if all elements in the the given list pass all the check by the condition.
     * 
     * @param list       the list.
     * @param condition  the condition.
     * @return  {@code true} if all elements pass the condition.
     * 
     * @param <OBJECT> the type of the data in the list.
     */
    public static <OBJECT> boolean _hasAllThat(List<OBJECT> list, Predicate<OBJECT> condition) {
        if (list == null)
            return false;
        if (list.isEmpty())
            return false;
        return list.stream().allMatch(condition);
    }
    
    /**
     * Check if at lease one element in the the given array pass all the check by the condition.
     * 
     * @param array      the array.
     * @param condition  the condition.
     * @return  {@code true} if some elements pass the condition.
     * 
     * @param <OBJECT> the type of the data in the array.
     */
    public static <OBJECT> boolean _hasSomeThat(OBJECT[] array, Predicate<OBJECT> condition) {
        if (array == null)
            return false;
        if (array.length == 0)
            return false;
        return Arrays.stream(array).anyMatch(condition);
    }
    /**
     * Check if at lease one element in the the given list pass all the check by the condition.
     * 
     * @param list       the list.
     * @param condition  the condition.
     * @return  {@code true} if some elements pass the condition.
     * 
     * @param <OBJECT> the type of the data in the list.
     */
    public static <OBJECT> boolean _hasSomeThat(List<OBJECT> list, Predicate<OBJECT> condition) {
        if (list == null)
            return false;
        if (list.isEmpty())
            return false;
        return list.stream().anyMatch(condition);
    }
    
    /**
     * Returns the array contains the element that match the given condition.
     * 
     * @param array      the array.
     * @param condition  the condition.
     * @return  the element with only the matching elements.
     * 
     * @param <OBJECT> the type of the data in the array.
     */
    public static <OBJECT> Stream<OBJECT> _butOnlyThat$(OBJECT[] array, Predicate<OBJECT> condition) {
        if (array == null)
            return Stream.empty();
        return stream(array).filter(condition);
    }
    /**
     * Check if at lease one element in the the given list pass all the check by the condition.
     * 
     * @param list       the list.
     * @param condition  the condition.
     * @return  the element with only the matching elements.
     * 
     * @param <OBJECT> the type of the data in the list.
     */
    public static <OBJECT> Stream<OBJECT> _butOnlyThat$(List<OBJECT> list, Predicate<OBJECT> condition) {
        if (list == null)
            return Stream.empty();
        return list.stream().filter(condition);
    }
    
    /**
     * Returns the array contains the element that match the given condition.
     * 
     * @param array      the array.
     * @param condition  the condition.
     * @return  the element with only the matching elements or null if the given element is null.
     * 
     * @param <OBJECT> the type of the data in the array.
     */
    @SuppressWarnings("unchecked")
    public static <OBJECT> OBJECT[] _butOnlyThat(OBJECT[] array, Predicate<OBJECT> condition) {
        if (array == null)
            return null;
        val list = _butOnlyThat$(array, condition).collect(toList());
        val newArray = (OBJECT[])newInstance(array.getClass().getComponentType(), list.size());
        return list.toArray(newArray);
    }
    
    /**
     * Check if at lease one element in the the given list pass all the check by the condition.
     * 
     * @param list       the list.
     * @param condition  the condition.
     * @return  the element with only the matching elements or null if the given element is null.
     * 
     * @param <OBJECT> the type of the data in the list.
     */
    public static <OBJECT> List<OBJECT> _butOnlyThat(List<OBJECT> list, Predicate<OBJECT> condition) {
        if (list == null)
            return null;
        return _butOnlyThat$(list, condition).collect(toList());
    }
    
    /**
     * Access to collection with the mapper and flatmap them.
     * 
     * @param stream  the stream.
     * @param mapper  the mapper.
     * @return  the element with only the matching elements or null if the given element is null.
     * @param <OBJECT>     the type of the data in the stream.
     * @param <COLLECTION> the type of the collection.
     */
    public static <OBJECT, COLLECTION extends Collection<OBJECT>> Stream<OBJECT> _flatMap$(Stream<OBJECT> stream, Function<OBJECT, COLLECTION> mapper) {
        if (stream == null)
            return Stream.empty();
        return _butOnlyNonNull$(_butOnlyNonNull$(stream.map(mapper)).flatMap(Collection::stream));
    }
    
}
