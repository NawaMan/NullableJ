package nawaman.nullablej.nullable;

import static org.junit.Assert.*;

import java.util.function.Function;

import org.junit.Test;

import nullablej.nullable.Nullable;

@SuppressWarnings("javadoc")
public class NullableImplTest {
    
    @Test
    public void testMonad() {
        Nullable<Integer> int11 = Nullable.of(4);
        Nullable<Integer> int12 = Nullable.of(2);
        Nullable<Integer> sum1
                = int11.flatMap(i1 ->
                  int12.flatMap(i2 -> 
                      Nullable.of(i1 + i2)));
        assertEquals(Integer.valueOf(6), sum1.get());
        
        Nullable<Integer> int21 = Nullable.empty();
        Nullable<Integer> int22 = Nullable.of(2);
        Nullable<Integer> sum2
                = int21.flatMap(i1 ->
                  int22.flatMap(i2 -> 
                      Nullable.of(i1 + i2)));
        assertEquals(null, sum2.get());
        
        // Functions
        
        Function<Integer, Nullable<Integer>> ok_ = integer -> Nullable.of(integer);
        Function<Integer, Nullable<Integer>> f = val -> Nullable.of(val +1);
        Function<Integer, Nullable<Integer>> g = val -> Nullable.of(val +3);
        
        // leftIdentity: unit(a) flatMap f === f(a)
        Integer intVar = 2;
        assertEquals(Nullable.of(intVar).flatMap(f), f.apply(intVar));
        
        // rightIdentity: m flatMap unit === m
        Nullable<Integer> result = Nullable.of(2);
        assertEquals(result.flatMap(ok_), result);
        
        // associativity: (m flatMap f) flatMap g === m flatMap ( f(x) flatMap g )
        Nullable<Integer> result2 = Nullable.of(2);
        Nullable<Integer> left = (result2.flatMap(f)).flatMap(g);
        Nullable<Integer> right = result2.flatMap(val -> f.apply(val).flatMap(g));
        assertEquals(left, right);
    }
    
}
