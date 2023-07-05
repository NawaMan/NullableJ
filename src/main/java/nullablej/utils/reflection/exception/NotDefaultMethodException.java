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

package nullablej.utils.reflection.exception;

import static java.lang.String.format;

import java.lang.reflect.Method;

import lombok.Getter;
import lombok.NonNull;

/**
 * This exception is thrown only when a default method is needed but the one that is not default is given,
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class NotDefaultMethodException extends UReflectionException {
    
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
