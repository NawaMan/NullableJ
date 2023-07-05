package nullablej;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.function.Predicate;

import org.junit.Test;

import lombok.experimental.ExtensionMethod;

@ExtensionMethod({ NullableJ.class })
public class AutoCompleteSearch {
    
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
