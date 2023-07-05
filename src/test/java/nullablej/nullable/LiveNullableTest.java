package nullablej.nullable;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import lombok.val;

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
