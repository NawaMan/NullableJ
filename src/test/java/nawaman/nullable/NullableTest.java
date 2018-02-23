package nawaman.nullable;

import static org.junit.Assert.assertEquals;

import static nawaman.nullable.Nullable.nullable;

import java.io.PrintStream;
import java.util.Optional;
import java.util.function.Supplier;

import org.junit.Test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.val;

@SuppressWarnings("javadoc")
public class NullableTest {
    
    public static interface Person {
        
        public String getName();
        public void setName(String name);
        
        public default void printlnName(PrintStream out) {
            out.println("Person: " + this.getName());
        }
        
    }
    
    @Data
    @AllArgsConstructor
    public static class PersonImpl implements Person {
        private String name;
    }
    
    @FunctionalInterface
    public static interface NullablePerson extends Person, LiveNullable<Person> {
        
        public static NullablePerson of(Person person) {
            return ()->person;
        }
        public static NullablePerson from(Supplier<? extends Person> nullablePerson) {
            return ()->nullablePerson.get();
        }
        
        public default String getName() {
            return this.map(Person::getName).orElse(null);
        }
        
        public default void setName(String name) {
            this.ifPresent(()->{
                this.get().setName(name);
            });
        }
        
    }
    
    @Test
    public void test() {
        val person1 = NullablePerson.of(new PersonImpl("Jack"));
        val person2 = NullablePerson.of(null);
        
        assertEquals("Jack", person1.getName());
        assertEquals(null,   person2.getName());
        
        person1.setName("John");
        person2.setName("John");
        
        assertEquals("John", person1.getName());
        assertEquals(null,   person2.getName());
    }
    
    @Test
    public void testCast() {
        val            nullablePerson = Nullable.of(new PersonImpl("Jack"));
        NullablePerson nullable       = NullablePerson.from(nullablePerson);
        assertEquals("Jack", nullable.getName());
        
        val supplier = (Supplier<Person>)()->new PersonImpl("John");
        val nullable2 = Nullable.from(supplier);
        assertEquals("John", nullable2.map(Person::getName).get());
        
        val nullable3 = NullablePerson.from(supplier);
        assertEquals("John", nullable3.getName());
        
        val nullable4 = NullablePerson.from(nullable2);
        assertEquals("John", nullable4.getName());
        
        assertEquals("Jack", Optional.of(new PersonImpl("Jack")).map(Nullable::of).get().map(Person::getName).get());
        
        val nullable5 = (Nullable<Person>)((Supplier<Person>)()->new PersonImpl("James"))::get;
        assertEquals("James", nullable5.map(Person::getName).get());
    }
    
    @Test
    public void testOr() {
        Nullable<CharSequence> blah = Nullable.of((CharSequence)"ONE");
        Nullable<CharSequence> or = blah.or(()->Nullable.of("TWO"));
        assertEquals("ONE", or.get());
        
        Nullable<CharSequence> blah2 = Nullable.empty();
        Nullable<CharSequence> or2 = blah2.or(()->Nullable.of("TWO"));
        assertEquals("TWO", or2.get());
    }
    
    private String getString(int i) {
        if (i <= 0)
            return null;
        return String.valueOf(i);
    }
    
    @Test
    public void testFrom_withException() {
        Nullable<Integer> _plus42 = Nullable.from(()->getString(42).length());
        Nullable<Integer> _minus5 = Nullable.from(()->getString(-1).length());
        assertEquals(Integer.valueOf(2), _plus42.get());
        assertEquals(null,               _minus5.get());
    }
    
    @Test
    public void testNullable_overload() {
        Nullable<String>           _42      = nullable(getString(42));
        Nullable<String>           string   = nullable(()->getString(42));
        Nullable<Supplier<String>> supplier = Nullable.<Supplier<String>>nullable(()->getString(42));
        
        assertEquals("42", _42.get());
        assertEquals("42", string.get());
        assertEquals("42", supplier.get().get());
    }
    
}
