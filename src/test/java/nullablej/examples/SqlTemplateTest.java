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

package nullablej.examples;

import static org.junit.Assert.assertEquals;

import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.Test;

import lombok.val;
import lombok.experimental.ExtensionMethod;
import nullablej.NullableJ;

@ExtensionMethod({ NullableJ.class })
public class SqlTemplateTest {
    
    public static final Function<String, String> equalsStringValue = strValue -> {
        val escValue 
                = strValue
                .replaceAll("\\\\", "\\\\")
                .replaceAll("\'", "\\\'")
                .replaceAll("\"", "\\\"")
                .replaceAll("\n", "\\n")
                .replaceAll("\r", "\\r")
                .replaceAll("\t", "\\t")
                ;
        return "=\"" + escValue + "\"";
    };
    
    public static final Supplier<String> isNullValue = ()->" is null";
    
    public static String selectByName(String value) {
        return "select * from Table where name" + value._mapTo(equalsStringValue)._orGet(isNullValue);
    }
    
    @Test
    public void test() {
        assertEquals("select * from Table where name=\"John\"", selectByName("John"));
        assertEquals("select * from Table where name is null",  selectByName(null));
    }
    
}
