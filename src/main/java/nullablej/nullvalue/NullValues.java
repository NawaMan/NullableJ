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
package nawaman.nullablej.nullvalue;

import lombok.val;
import nawaman.nullablej.nullvalue.strategies.AnnotatedFieldFinder;
import nawaman.nullablej.nullvalue.strategies.AnnotatedMethodFinder;
import nawaman.nullablej.nullvalue.strategies.DefaultConstructorFinder;
import nawaman.nullablej.nullvalue.strategies.KnownNewNullValuesFinder;
import nawaman.nullablej.nullvalue.strategies.KnownNullValuesFinder;
import nawaman.nullablej.nullvalue.strategies.NamedFieldFinder;
import nawaman.nullablej.nullvalue.strategies.NamedMethodFinder;
import nawaman.nullablej.nullvalue.strategies.NullableInterfaceFinder;

/**
 * Default implementation of {@link IFindNullValue}.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class NullValues implements IFindNullValue {
    
    
    /** The name of the annotation to make null value. */
    public static final String NULL_VALUE_ANNOTTION_NAME = "NullValue";
    
    /** The name of the null value method. */
    public static final String NULL_VALUE_METHOD_NAME1 = "nullValue";
    
    /** The name of the null value method. */
    public static final String NULL_VALUE_METHOD_NAME2 = "getNullValue";
    
    /** The name of the null value field. */
    public static final String NULL_VALUE_FIELD_NAME1 = "nullValue";
    
    /** The name of the null value field. */
    public static final String NULL_VALUE_FIELD_NAME2 = "NULL_VALUE";
    
    
    /** The ready to use instance of NullValues.  */
    public static final NullValues instance = new NullValues();
    

    @SuppressWarnings("javadoc")
	protected final KnownNullValuesFinder    knownNullFinder          = new KnownNullValuesFinder();
    @SuppressWarnings("javadoc")
    protected final KnownNewNullValuesFinder KnownNewNullValuesFinder = new KnownNewNullValuesFinder();
    @SuppressWarnings("javadoc")
    protected final AnnotatedFieldFinder     annotatedFieldFinder     = new AnnotatedFieldFinder(NULL_VALUE_ANNOTTION_NAME);
    @SuppressWarnings("javadoc")
    protected final AnnotatedMethodFinder    annotatedMethodFinder    = new AnnotatedMethodFinder(NULL_VALUE_ANNOTTION_NAME);
    @SuppressWarnings("javadoc")
    protected final NamedFieldFinder         namedFieldFinder1        = new NamedFieldFinder(NULL_VALUE_FIELD_NAME1);
    @SuppressWarnings("javadoc")
    protected final NamedFieldFinder         namedFieldFinder2        = new NamedFieldFinder(NULL_VALUE_FIELD_NAME2);
    @SuppressWarnings("javadoc")
    protected final NamedMethodFinder        namedMethodFinder1       = new NamedMethodFinder(NULL_VALUE_METHOD_NAME1);
    @SuppressWarnings("javadoc")
    protected final NamedMethodFinder        namedMethodFinder2       = new NamedMethodFinder(NULL_VALUE_METHOD_NAME2);
    @SuppressWarnings("javadoc")
    protected final DefaultConstructorFinder defaultConstructorFinder = new DefaultConstructorFinder();
    @SuppressWarnings("javadoc")
    protected final NullableInterfaceFinder  nullableInterfaceFinder  = new NullableInterfaceFinder();
    
    /**
     * Find the null value of the given class.
     * 
     * @param clzz  the class.
     * @return  the null value found. This method return null if it cannot find some.
     * @param <OBJECT>  the type of the object.
     */
    public static final <OBJECT> OBJECT nullValueOf(Class<OBJECT> clzz) {
        val nullObj = instance.findNullValueOf(clzz);
        return nullObj;
    }
    
    /**
     * Find the null value of the given class.
     * 
     * @param clzz  the class.
     * @return  the null value found. This method return null if it cannot find some.
     * @param <OBJECT>  the type of the object.
     */
    public static final <OBJECT> OBJECT of(Class<OBJECT> clzz) {
        val nullObj = instance.findNullValueOf(clzz);
        return nullObj;
    }
    
    @Override
    public <T> T findNullValueOf(Class<T> clzz) {
        val nullFromKnown = knownNullFinder.findNullValueOf(clzz);
        if (nullFromKnown != null)
            return nullFromKnown;
        
        val newKnowNullValue = KnownNewNullValuesFinder.findNullValueOf(clzz);
        if (newKnowNullValue != null)
            return newKnowNullValue;
        
        val valueFromAnnotatedField = annotatedFieldFinder.findNullValueOf(clzz);
        if (valueFromAnnotatedField != null)
            return valueFromAnnotatedField;
        
        val valueFromAnnotatedMethod = annotatedMethodFinder.findNullValueOf(clzz);
        if (valueFromAnnotatedMethod != null)
            return valueFromAnnotatedMethod;
        
       val valueFromNamedField1 = namedFieldFinder1.findNullValueOf(clzz);
        if (valueFromNamedField1 != null)
            return valueFromNamedField1;
        
        val valueFromNamedField2 = namedFieldFinder2.findNullValueOf(clzz);
         if (valueFromNamedField2 != null)
             return valueFromNamedField2;
        
        val valueFromNamedMethod1 = namedMethodFinder1.findNullValueOf(clzz);
        if (valueFromNamedMethod1 != null)
            return valueFromNamedMethod1;
        
        val valueFromNamedMethod2 = namedMethodFinder2.findNullValueOf(clzz);
        if (valueFromNamedMethod2 != null)
            return valueFromNamedMethod2;
        
        val valueFromDefaultConstructor = defaultConstructorFinder.findNullValueOf(clzz);
        if (valueFromDefaultConstructor != null)
            return valueFromDefaultConstructor;
        
        val valueFromNullableInterface = nullableInterfaceFinder.findNullValueOf(clzz);
        if (valueFromNullableInterface != null)
            return valueFromNullableInterface;
        
        return null;
    }
    
}
