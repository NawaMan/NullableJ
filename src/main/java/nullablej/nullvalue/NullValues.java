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

import lombok.val;
import nullablej.nullvalue.strategies.AnnotatedFieldFinder;
import nullablej.nullvalue.strategies.AnnotatedMethodFinder;
import nullablej.nullvalue.strategies.DefaultConstructorFinder;
import nullablej.nullvalue.strategies.KnownNewNullValuesFinder;
import nullablej.nullvalue.strategies.KnownNullValuesFinder;
import nullablej.nullvalue.strategies.NamedFieldFinder;
import nullablej.nullvalue.strategies.NamedMethodFinder;
import nullablej.nullvalue.strategies.NullableInterfaceFinder;

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
    
    
    protected final KnownNullValuesFinder    knownNullFinder          = new KnownNullValuesFinder();
    protected final KnownNewNullValuesFinder KnownNewNullValuesFinder = new KnownNewNullValuesFinder();
    protected final AnnotatedFieldFinder     annotatedFieldFinder     = new AnnotatedFieldFinder(NULL_VALUE_ANNOTTION_NAME);
    protected final AnnotatedMethodFinder    annotatedMethodFinder    = new AnnotatedMethodFinder(NULL_VALUE_ANNOTTION_NAME);
    protected final NamedFieldFinder         namedFieldFinder1        = new NamedFieldFinder(NULL_VALUE_FIELD_NAME1);
    protected final NamedFieldFinder         namedFieldFinder2        = new NamedFieldFinder(NULL_VALUE_FIELD_NAME2);
    protected final NamedMethodFinder        namedMethodFinder1       = new NamedMethodFinder(NULL_VALUE_METHOD_NAME1);
    protected final NamedMethodFinder        namedMethodFinder2       = new NamedMethodFinder(NULL_VALUE_METHOD_NAME2);
    protected final DefaultConstructorFinder defaultConstructorFinder = new DefaultConstructorFinder();
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
