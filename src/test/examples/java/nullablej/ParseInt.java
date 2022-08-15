package nullablej;

import static org.junit.Assert.*;

import org.junit.Test;

import lombok.experimental.ExtensionMethod;
import nawaman.nullablej.NullableJ;

@SuppressWarnings("javadoc")
@ExtensionMethod({ NullableJ.class })
public class ParseInt {
    
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
