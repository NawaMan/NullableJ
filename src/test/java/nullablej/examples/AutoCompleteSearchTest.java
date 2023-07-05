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

import java.util.List;
import java.util.function.Predicate;

import org.junit.Test;

import lombok.experimental.ExtensionMethod;
import nullablej.NullableJ;

@ExtensionMethod({ NullableJ.class })
public class AutoCompleteSearchTest {
    
    private static Predicate<String> thatStartsWith(String prefix) {
        return str->str.startsWith(prefix);
    }
    
    // If term is empty, return the whole list, otherwise, use the search term to filter.
    public static List<String> autocompleteSearchCountries(String term) {
        return term._whenNotEmpty()
                .map(t->Countries.list._butOnly(thatStartsWith(term)))
                .orElse(Countries.list);
    }
    
    @Test
    public void test() {
        assertEquals(197, autocompleteSearchCountries(null)._size());
        assertEquals(197, autocompleteSearchCountries("")._size());
        assertEquals(1,   autocompleteSearchCountries("Th")._size());
        assertEquals(1,   autocompleteSearchCountries("Tha")._size());
        assertEquals(19,  autocompleteSearchCountries("M")._size());
        assertEquals(6,   autocompleteSearchCountries("Mo")._size());
        assertEquals(3,   autocompleteSearchCountries("Mon")._size());
    }
    
}
