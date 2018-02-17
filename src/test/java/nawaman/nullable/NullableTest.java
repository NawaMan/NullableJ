package nawaman.nullable;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
    public static interface NullablePerson extends Person, Nullable<Person> {
        
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
    
    //== Proxy related testing ========================================================================================
    
    public static interface AnotherNullablePerson extends Person, Nullable<Person> {
    }
    
    @Test
    public void testProxy() {
        val orgPerson = new PersonImpl("John");
        
        val person = NullableObject.from(()->orgPerson, Person.class, AnotherNullablePerson.class);
        
        assertEquals("John", person.getName());
        person.setName("Jack");
        assertEquals("Jack", person.getName());
        person.setName("Jim");
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PrintStream           out  = new PrintStream(baos)) {
            person.printlnName(out);
            assertEquals("Person: Jim", baos.toString().trim());
        } catch (IOException e) {
        }
        assertEquals(3, person.map(Person::getName).map(String::length).get().intValue());
    }
    
    public static interface BuildInNullablePerson extends Nullable<BuildInNullablePerson> {
        
        public String getName();
        public void setName(String name);
        
    }
    
    @Data
    @AllArgsConstructor
    public static class BuildInPersonImpl implements BuildInNullablePerson {
        private String name;
        public BuildInNullablePerson get() {
            return this;
        }
    }
    
    @Test
    public void testProxy_buildin() {
        val orgPerson = new BuildInPersonImpl("John");
        val person    = NullableObject.from(()->orgPerson, BuildInNullablePerson.class);
        
        assertEquals("John", person.get().getName());
        person.get().setName("Jack");
        assertEquals("Jack", person.get().getName());
        person.get().setName("Jim");
        assertEquals("Jim", person.map(BuildInNullablePerson::getName).get());
    }
    
}
