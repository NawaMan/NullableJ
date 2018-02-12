package nawaman.nullable;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

import java.util.function.Supplier;

import org.junit.Test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.val;
import nawaman.nullable._internal.Nextable;

@SuppressWarnings("javadoc")
public class NullablesTest {
    
    public static interface Person {
        public String getName();
        public void setName(String name);
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
    
    @FunctionalInterface
    public static interface NullablePersons extends Nullables<Person>, NullablePerson {
        
        @SuppressWarnings("unchecked")
        public static NullablePersons from(Iterable<Person> persons) {
            @SuppressWarnings("rawtypes")
            val iterator = new Nextable(persons.iterator());
            return (NullablePersons)()->iterator;
        }
        
        public Nextable<Person> nextable();
        
        @Override
        public default Person get() {
            return nextable().current();
        }
    }
    
    @Test
    public void testIterator() { 
       val persons = asList("Jack", "Jim", null, "John").stream().map(PersonImpl::new).map(Person.class::cast).collect(toList());
       val nullable = NullablePersons.from(persons);
       
       assertEquals("Jack", nullable.getName());
       assertEquals(4, nullable.map(Person::getName).map(String::length).orElse(0).intValue());
       nullable.next();
       
       assertEquals("Jim", nullable.getName());
       assertEquals(3, nullable.map(Person::getName).map(String::length).orElse(0).intValue());
       nullable.next();
       
       assertEquals(null, nullable.getName());
       assertEquals(0, nullable.map(Person::getName).map(String::length).orElse(0).intValue());
       nullable.next();
       
       assertEquals("John", nullable.getName());
       assertEquals(4, nullable.map(Person::getName).map(String::length).orElse(0).intValue());
    }
    
}
