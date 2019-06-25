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
package nullablej.nullable;

import java.util.Optional;

/**
 * An implementation of Nullable that store the value as value (as opposed to supplier).
 * 
 * NullableImpl is an implementation of Nullable that is truely a monad.
 * 
 * @param <TYPE>  the data type of the value wrapped by this Nullable.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class NullableImpl<TYPE> implements Nullable<TYPE> {
    
    private final TYPE value;
    
    /**
     * Constructs a NullableImple for the given value.
     * 
     * @param  value  the value.
     */
    public NullableImpl(TYPE value) {
        this.value = value;
    }
    
    @Override
    public TYPE get() {
        return value;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if ((obj.getClass() == Nullable.class) || (obj.getClass() == Optional.class))
            return false;
        @SuppressWarnings({ "rawtypes", "unchecked" })
        Object otherValue
                = (Nullable.class.isAssignableFrom(obj.getClass())) ? ((Nullable)obj).get()
                : (Optional.class.isAssignableFrom(obj.getClass())) ? ((Optional)obj).orElse(null)
                : null;
        if (value == null) {
            if (otherValue != null)
                return false;
        } else if (!value.equals(otherValue))
            return false;
        return true;
    }
    
    public String toString() {
        if (value == null)
            return "Nullable.EMPTY";
        
        return "Nullable.of(" + value + ")";
    }
    
}
