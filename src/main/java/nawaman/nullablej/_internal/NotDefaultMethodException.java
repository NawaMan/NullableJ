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
package nawaman.nullablej._internal;

import static java.lang.String.format;

import java.lang.reflect.Method;

import lombok.Getter;
import lombok.NonNull;

/**
 * This exception is thrown only when a default method is needed but the one that is not default is given,
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class NotDefaultMethodException extends RuntimeException {
    
    /** The default tempalte. */
    public static final String DEFAULT_MESSAGE_TEMPLATE = "Method is not a default interface method: %s";
    
    private static final long serialVersionUID = 3423534513645245L;
    
    @Getter
    private Method method;
    
    /**
     * Construct the exception with the given template and the method.
     * 
     * @param messageTemplate   the template of the message to be used with {@code String.format(...)} method
     *                            with notDefaultMethod as a parameter.
     * @param notDefaultMethod  the method that is not a default method.
     */
    public NotDefaultMethodException(@NonNull String messageTemplate, @NonNull Method notDefaultMethod) {
        super(format(messageTemplate, notDefaultMethod));
        this.method = notDefaultMethod;
    }
    
    /**
     * Construct the exception using the default template.
     * 
     * @param notDefaultMethod  the method that is not a default method.
     */
    public NotDefaultMethodException(Method notDefaultMethod) {
        this(DEFAULT_MESSAGE_TEMPLATE, notDefaultMethod);
    }
    
}
