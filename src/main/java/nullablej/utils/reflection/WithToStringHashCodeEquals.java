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

package nullablej.utils.reflection;

/**
 * Implement this interface if it is desired to have toString, hashCode and equals for dynamic proxy.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public interface WithToStringHashCodeEquals {
    
    /**
     * Returns the desired toString.
     * 
     * @return the toString.
     */
    public default String _toString() {
        return this.getClass().getSimpleName() + "@" + this.getClass().getSimpleName().hashCode();
    }
    
    /**
     * Returns the desired hashCode.
     * 
     * @return the hashCode.
     */
    public default int _hashCode() {
        return this._toString().hashCode();
    }
    
    /**
     * Checks if the given object equals this one.
     * 
     * @param theGivenObject  the given object.
     * @return  {@code true} if the objects equal.
     */
    public default boolean _equals(Object theGivenObject) {
        return this == theGivenObject;
    }
    
}
