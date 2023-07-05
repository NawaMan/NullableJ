package nullablej;

import static org.junit.Assert.assertEquals;

import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.Test;

import lombok.experimental.ExtensionMethod;

@ExtensionMethod({ NullableJ.class })
public class SqlTemplate {
    
    public static final Function<String, String> equalsStringValue = strValue -> {
        // TODO - do proper escape and encoding.
        return "=\"" + strValue + "\"";
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
