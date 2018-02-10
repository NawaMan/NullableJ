package nullablej;

import static org.junit.Assert.*;

import java.util.List;
import java.util.function.Predicate;

import org.junit.Test;

import lombok.experimental.ExtensionMethod;
import nawaman.nullable.NullableJ;

@SuppressWarnings("javadoc")
@ExtensionMethod({ NullableJ.class })
public class AutoCompleteSearch {
    
    private static Predicate<String> startsWith(String prefix) {
        return str->str.startsWith(prefix);
    }
    
    public static List<String> autocompleteSearchCountries(String term) {
        return term._whenNotEmpty()
                .map(t->Countries.list._butOnlyThat(startsWith(term)))
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
