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
