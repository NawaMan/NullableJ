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

import lombok.val;
import nawaman.nullable.strategies.AnnotatedFieldFinder;
import nawaman.nullable.strategies.AnnotatedMethodFinder;
import nawaman.nullable.strategies.DefaultConstructorFinder;
import nawaman.nullable.strategies.KnownNewNullObjectsFinder;
import nawaman.nullable.strategies.KnownNullObjectsFinder;
import nawaman.nullable.strategies.NamedFieldFinder;
import nawaman.nullable.strategies.NamedMethodFinder;

/**
 * Default implementation of {@link IFindNullObject}.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class NullObjects implements IFindNullObject {
    
    
    /** The name of the annotation to make null object. */
    public static final String NULL_OBJET_ANNOTTION_NAME = "NullObject";
    
    /** The name of the null object method. */
    public static final String NULL_OBJECT_METHOD_NAME1 = "nullObject";
    
    /** The name of the null object method. */
    public static final String NULL_OBJECT_METHOD_NAME2 = "getNullObject";
    
    /** The name of the null object field. */
    public static final String NULL_OBJECT_FIELD_NAME1 = "nullObject";
    
    /** The name of the null object field. */
    public static final String NULL_OBJECT_FIELD_NAME2 = "NULL_OBJECT";
    
    
    /** The ready to use instance of NullObjects.  */
    public static final NullObjects instance = new NullObjects();
    
    /**
     * Find the null object of the given class.
     * 
     * @param clzz  the class.
     * @return  the null object found. This method return null if it cannot find some.
     * @param <OBJECT>  the type of the object.
     */
    public static final <OBJECT> OBJECT nullObjectOf(Class<OBJECT> clzz) {
        val nullObj = instance.findNullObjectOf(clzz);
        return nullObj;
    }
    
    protected final KnownNullObjectsFinder    knownNullFinder           = new KnownNullObjectsFinder();
    protected final KnownNewNullObjectsFinder KnownNewNullObjectsFinder = new KnownNewNullObjectsFinder();
    protected final AnnotatedFieldFinder      annotatedFieldFinder      = new AnnotatedFieldFinder(NULL_OBJET_ANNOTTION_NAME);
    protected final AnnotatedMethodFinder     annotatedMethodFinder     = new AnnotatedMethodFinder(NULL_OBJET_ANNOTTION_NAME);
    protected final NamedFieldFinder          namedFieldFinder1         = new NamedFieldFinder(NULL_OBJECT_FIELD_NAME1);
    protected final NamedFieldFinder          namedFieldFinder2         = new NamedFieldFinder(NULL_OBJECT_FIELD_NAME2);
    protected final NamedMethodFinder         namedMethodFinder1        = new NamedMethodFinder(NULL_OBJECT_METHOD_NAME1);
    protected final NamedMethodFinder         namedMethodFinder2        = new NamedMethodFinder(NULL_OBJECT_METHOD_NAME2);
    protected final DefaultConstructorFinder  defaultConstructorFinder  = new DefaultConstructorFinder();
    
    @Override
    public <T> T findNullObjectOf(Class<T> clzz) {
        val nullFromKnown = knownNullFinder.findNullObjectOf(clzz);
        if (nullFromKnown != null)
            return nullFromKnown;
        
        val newKnowNullObject = KnownNewNullObjectsFinder.findNullObjectOf(clzz);
        if (newKnowNullObject != null)
            return newKnowNullObject;
        
        val valueFromAnnotatedField = annotatedFieldFinder.findNullObjectOf(clzz);
        if (valueFromAnnotatedField != null)
            return valueFromAnnotatedField;
        
        val valueFromAnnotatedMethod = annotatedMethodFinder.findNullObjectOf(clzz);
        if (valueFromAnnotatedMethod != null)
            return valueFromAnnotatedMethod;
        
       val valueFromNamedField1 = namedFieldFinder1.findNullObjectOf(clzz);
        if (valueFromNamedField1 != null)
            return valueFromNamedField1;
        
        val valueFromNamedField2 = namedFieldFinder2.findNullObjectOf(clzz);
         if (valueFromNamedField2 != null)
             return valueFromNamedField2;
        
        val valueFromNamedMethod1 = namedMethodFinder1.findNullObjectOf(clzz);
        if (valueFromNamedMethod1 != null)
            return valueFromNamedMethod1;
        
        val valueFromNamedMethod2 = namedMethodFinder2.findNullObjectOf(clzz);
        if (valueFromNamedMethod2 != null)
            return valueFromNamedMethod2;
        
        val valueFromDefaultConstructor = defaultConstructorFinder.findNullObjectOf(clzz);
        if (valueFromDefaultConstructor != null)
            return valueFromDefaultConstructor;
        
        return null;
    }
    
}
