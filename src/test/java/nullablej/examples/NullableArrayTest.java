package nullablej.examples;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import lombok.val;
import nullablej.nullable.Nullable;

public class NullableArrayTest {
    
    public static interface NullableArray<T> extends Nullable<T[]> {
        public static <T> NullableArray<T> of(T[] array) {
            return (NullableArray<T>)()->array;
        }
        public default List<T> toList() {
            T[] array = get();
            if (array == null)
                return Collections.emptyList();
            
            return Arrays.asList((T[])array);
        }
    }
    
    @Test
    public void testNullableArray() {
        val nullableArray = NullableArray.of(new String[] { "one",  "two",  "three" });
        assertEquals("[one, two, three]", nullableArray.toList().toString());
        
        val nullableArray2 = NullableArray.of(null);
        assertEquals("[]", nullableArray2.toList().toString());
    }
    
}
