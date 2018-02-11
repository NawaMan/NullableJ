package nawaman.nullable;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.function.Supplier;

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
    
    // TODO - Proxy
    
    public static interface Nullables<TYPE> extends Nullable<TYPE> {
        
        public PassiveIterator<TYPE> iterator();
        
        public TYPE get();
        
        public boolean next();
        
    }
    
    public static class PassiveIterator<T> implements Iterator<T> {
        private Iterator<T> iterator;
        private T current = null;
        
        public PassiveIterator(Iterator<T> iterator) {
            this.iterator = iterator;
            this.hasNext();
        }
        
        @Override
        public boolean hasNext() {
            boolean hasNext = iterator.hasNext();
            if (hasNext) next();
            else current = null;
            return hasNext;
        }
        @Override
        public T next() {
            return (current = iterator.next());
        }
        public T current() {
            return current;
        }
    }
    
    @FunctionalInterface
    public static interface NullablePersons extends NullablePerson, Nullables<Person> {
        
        @SuppressWarnings("unchecked")
        public static NullablePersons from(Iterable<Person> persons) {
            @SuppressWarnings("rawtypes")
            val iterator = new PassiveIterator(persons.iterator());
            return (NullablePersons)()->iterator;
        }
        
        public PassiveIterator<Person> iterator();
        
        public default Person get() {
            return iterator().current();
        }
        
        public default boolean next() {
            return iterator().hasNext();
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
       nullable.next();
    }
    
}
