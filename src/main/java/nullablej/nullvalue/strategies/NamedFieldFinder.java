//  MIT License
//  
//  Copyright (c) 2017-2019 Nawa Manusitthipol
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
package nullablej.nullvalue.strategies;

import static nullablej.utils.reflection.UReflection.getValueFromStaticFieldOrNull;

import lombok.NonNull;
import lombok.val;
import nullablej.nullvalue.IFindNullValue;

/**
 * This finder finds by looking for compatible field with annotation.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class NamedFieldFinder extends AbstractFromClassElementFinder implements IFindNullValue {
    
    private String fieldName;
    
    /**
     * Construct the finder that looks for a compatible field with the given name.
     * 
     * @param fieldName  the field name. 
     */
    public NamedFieldFinder(@NonNull String fieldName) {
        this.fieldName = fieldName;
    }
    
    @Override
    public <OBJECT> OBJECT findNullValueOf(Class<OBJECT> clzz) {
        return findNullValueFromNamedField(clzz, fieldName);
    }
    
    /**
     * Find null value by looking for a field with specific name of the given class.
     * 
     * @param clzz       the class.
     * @param fieldName  the name of the field.
     * @return  the null value.
     * 
     * @param  <OBJECT>  the type of data.
     */
    public static final <OBJECT> OBJECT findNullValueFromNamedField(Class<OBJECT> clzz, String fieldName) {
        val valueFromNamedField = getPublicStaticFinalCompatibleField(clzz, field->{
            if (!fieldName.equals(field.getName()))
                return null;
            
            return getValueFromStaticFieldOrNull(clzz, field);
        });
        return valueFromNamedField;
    }
    
}
