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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;

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
        val result = (theGivenObject == null) ? elseSupplier.get() : theGivenObject;
        return result;
    }
    
    /**
     * Returns the null object.
     * 
     * @param  theGivenObject  the given object.
     * @param  theObjectClass  the class of the given data.
     * @param  <OBJECT>        the data type of the given object.
     * @return theGivenObject if not null or value from the elseSupplier if null.
     **/
    public static <OBJECT> OBJECT _orNullObject(OBJECT theGivenObject, Class<OBJECT> theObjectClass) {
        val result = (theGivenObject == null) ? NullObjects.nullObjectOf(theObjectClass) : theGivenObject;
        return result;
    }
    
    /**
     * Returns the null object.
     * 
     * @param  theOptionalObject  the optional object.
     * @param  theObjectClass     the class of the given data.
     * @param  <OBJECT>           the data type of the given object.
     * @return theGivenObject if not null or value from the elseSupplier if null.
     **/
    public static <OBJECT> Optional<OBJECT> _orElseNullObject(Optional<OBJECT> theOptionalObject, Class<OBJECT> theObjectClass) {
        val result = theOptionalObject.isPresent() ? theOptionalObject : Optional.ofNullable(NullObjects.nullObjectOf(theObjectClass));
        return result;
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
    public static <OBJECT> Optional<OBJECT> _whenNotNull(OBJECT theGivenObject) {
        return Optional.ofNullable(theGivenObject);
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
     * Return the Optional of the given object if the test yields {@code true} or else return {@code Optional.empty}.
     * 
     * @param  theGivenObject  the given object.
     * @param  theTest         the test.
     * @param  <OBJECT>        the data type of the given object.
     * @return  the Optional of the original object or {@code Optional.empty}.
     */
    public static <OBJECT> Optional<OBJECT> _when(OBJECT theGivenObject, Predicate<OBJECT> theTest) {
        if (theGivenObject == null)
            return Optional.empty();
        
        val conditionResult = theTest.test(theGivenObject);
        if (!conditionResult)
            return Optional.empty();
        
        return Optional.of(theGivenObject);
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
     * @return  the Optional of the original string if it contains the value otherwise return {@code Optional.empty()}.
     */
    public static Optional<String> _whenContains(String theGivenString, CharSequence theNeedle) {
        if (theGivenString == null)
            return Optional.empty();
        
        val isContains = theGivenString.contains(theNeedle);
        val theResult  = isContains ? theGivenString : null;
        return Optional.ofNullable(theResult);
    }
    
    /**
     * Check if the given string contains the needle.
     * 
     * @param theGivenString  the given string.
     * @param theNeedle       the needle.
     * @return  the Optional of the original string if it DOES NOT contain the needle otherwise return {@code null}.
     */
    public static Optional<String> _whenNotContains(String theGivenString, CharSequence theNeedle) {
        if (theGivenString == null)
            return Optional.empty();
        
        val isContains = theGivenString.contains(theNeedle);
        val theResult  = isContains ? null : theGivenString;
        return Optional.ofNullable(theResult);
    }
    
    /**
     * Check if the given string matches the regular expression.
     * 
     * @param theGivenString  the given string.
     * @param theRegex        the regular expression.
     * @return  the Optional of the original string if it matches the needle otherwise return {@code Optional.empty()}.
     */
    public static Optional<String> _whenMatches(String theGivenString, String theRegex) {
        if (theGivenString == null)
            return Optional.empty();
        
        val isMatches = theGivenString.matches(theRegex);
        val theResult  = isMatches ? theGivenString : null;
        return Optional.ofNullable(theResult);
    }
    
    /**
     * Check if the given string matches the regular expression.
     * 
     * @param theGivenString  the given string.
     * @param theRegex        the regular expression.
     * @return  the Optional of the original string if it DOES NOT contain the needle otherwise return {@code Optional.empty()}.
     */
    public static Optional<String> _whenNotMatches(String theGivenString, String theRegex) {
        if (theGivenString == null)
            return Optional.empty();
        
        val isMatches = theGivenString.matches(theRegex);
        val theResult  = isMatches ? null : theGivenString;
        return Optional.ofNullable(theResult);
    }
    
    /**
     * Check if the given string matches the regular expression.
     * 
     * @param theGivenString  the given string.
     * @param theRegex        the regular expression.
     * @return  the Optional of the original string if it matches the needle otherwise return {@code Optional.empty()}.
     */
    public static Optional<String> _whenMatches(String theGivenString, Pattern theRegex) {
        if (theGivenString == null)
            return Optional.empty();
        
        val isMatches = theRegex.matcher(theGivenString).find();
        val theResult  = isMatches ? theGivenString : null;
        return Optional.ofNullable(theResult);
    }
    
    /**
     * Check if the given string matches the regular expression.
     * 
     * @param theGivenString  the given string.
     * @param theRegex        the regular expression.
     * @return  the Optional of the original string if it DOES NOT contain the needle otherwise return {@code Optional.empty()}.
     */
    public static Optional<String> _whenNotMatches(String theGivenString, Pattern theRegex) {
        if (theGivenString == null)
            return Optional.empty();
        
        val isMatches = theRegex.matcher(theGivenString).find();
        val theResult  = isMatches ? null : theGivenString;
        return Optional.ofNullable(theResult);
    }
    
    //== Array and Collection ==
    
    /**
     * Get the element in the array at the index and return {@code null} if fail.
     * 
     * @param array  the array.
     * @param index  the index.
     * @return  the value at the index or null.
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
     * Get the element in the array at the index and return orValue if fail.
     * 
     * @param array    the array.
     * @param index    the index.
     * @param orValue  the value to return if getting the value fail.
     * @return  the value at the index or null.
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
     * Get the element in the array at the index and return the value from orSupplier if fail.
     * 
     * @param array       the array.
     * @param index       the index.
     * @param orSupplier  the supplier of the value to return if getting the value fail.
     * @return  the value at the index or null.
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
     * Get the element in the array at the index and return the value from orFunction if fail.
     * 
     * @param array       the array.
     * @param index       the index.
     * @param orFunction  the function that create the value to return if getting the value fail.
     * @return  the value at the index or null.
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
     * Get the element in the array at the index and return the value from orSupplier if fail.
     * 
     * @param list        the list.
     * @param index       the index.
     * @param orFunction  the function that create the value to return if getting the value fail.
     * @return  the value at the index or null.
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
     * Get the first element in the array and return {@code null} if fail.
     * 
     * @param array  the array.
     * @return  the value at the 0th index or null.
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
     */
    public static <OBJECT> OBJECT _last(List<OBJECT> list) {
        if (list == null)
            return null;
        if (list.isEmpty())
            return null;
        return list.get(list.size() - 1);
    }
    
}
