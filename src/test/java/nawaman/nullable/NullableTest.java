package nawaman.nullable;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import lombok.val;

@SuppressWarnings("javadoc")
public class NullableTest {
    
    public static interface Person {
        
        public String getName();
        public void setName(String name);
        
    }
    
    public static class PersonImpl implements Person {
        private String name;
        public PersonImpl(String name) {
            this.name = name;
        }
        public String getName() { return name; }
        public void setName(String name) {
            this.name = name;
        }
    }
    
    @FunctionalInterface
    public static interface NullablePerson extends Person, Nullable<Person>{
        
        public static NullablePerson of(Person person) {
            return ()->person;
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
    public void testOr() {
        Nullable<CharSequence> blah = Nullable.of((CharSequence)"ONE");
        Nullable<CharSequence> or = blah.or(()->Nullable.of("TWO"));
        assertEquals("ONE", or.get());
        
        Nullable<CharSequence> blah2 = Nullable.empty();
        Nullable<CharSequence> or2 = blah2.or(()->Nullable.of("TWO"));
        assertEquals("TWO", or2.get());
    }
    
}
