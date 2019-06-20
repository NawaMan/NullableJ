package nawaman.nullablej.nullable;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import static org.junit.Assert.*;

import lombok.val;
import nullablej.nullable.LiveNullable;

@SuppressWarnings("javadoc")
public class LiveNullableTest {
    
    @Test
    public void testLiveness() {
        val ref = new AtomicReference<String>(null);
        val nullableRef = LiveNullable.from(ref::get);
        assertFalse(nullableRef.isPresent());
        
        ref.set("Hi");
        assertTrue(nullableRef.isPresent());
    }
    
}
