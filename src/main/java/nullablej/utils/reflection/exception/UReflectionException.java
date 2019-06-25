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
package nullablej.utils.reflection.exception;

/**
 * Exception originated from UReflection.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class UReflectionException extends RuntimeException {
    
    private static final long serialVersionUID = -3759163482561478914L;
    
    /** Construct the exception. */
    public UReflectionException() {
        
    }
    
    /**
     * Construct the exception with a message.
     * 
     * @param message  the exception message.
     **/
    public UReflectionException(String message) {
        super(message);
    }
    
    /**
     * Construct the exception with a message and the root cause.
     * 
     * @param message  the exception message.
     * @param cause    the cause exception.
     **/
    public UReflectionException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Construct the exception with the root cause.
     * 
     * @param cause  the cause exception.
     **/
    public UReflectionException(Throwable cause) {
        super(cause);
    }
    
}
