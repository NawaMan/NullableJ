package nullablej.examples;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import lombok.experimental.ExtensionMethod;
import nullablej.NullableJ;

@ExtensionMethod({ NullableJ.class })
public class ParseIntTest {
    
    public static int intOf(String intString) {
        return intString._whenMatches("\\-?[0-9]+").map(Integer::parseInt).orElse(0);
    }
    
    @Test
    public void test() {
        assertEquals(42, intOf("42"));
        assertEquals(0,  intOf(null));
        assertEquals(-5, intOf("-5"));
        assertEquals(0,  intOf("Five"));
    }
    
}
