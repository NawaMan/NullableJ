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
package nawaman.nullablej.utils.reflection.exception;

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
