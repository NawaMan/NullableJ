package nawaman.nullablej.nullabledata;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.val;
import nawaman.nullablej.nullable.IAsNullable;
import nawaman.nullablej.nullable.Nullable;
import nawaman.nullablej.nullabledata.NullableData;

@SuppressWarnings("javadoc")
public class NullableDataTest {

    public static interface Person {
        
        public String getFirstName();
        public void setFirstName(String name);
        
        public String getLastName();
        public void setLastName(String name);
        public int getAge();
        
        public default String getFullName() {
            return (getFirstName() + " " + getLastName()).trim();
        }
        
        public default void printlnName(PrintStream out) {
            out.println("Person: " + this.getFirstName());
        }
        
    }
    
    @Data
    @AllArgsConstructor
    public static class PersonImpl implements Person {
        private String firstName;
        private String lastName;
        private int age = 25;
        public PersonImpl(String firstName) {
            this(firstName, "Smith", 25);
        }
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testBasic() {
        val nullablePerson1 = NullableData.from(()->new PersonImpl("Peter", "Pan", 25), Person.class);
        assertTrue(nullablePerson1 instanceof Person);
        assertEquals("Peter",     nullablePerson1.getFirstName());
        assertEquals("Pan",       nullablePerson1.getLastName());
        assertEquals("Peter Pan", nullablePerson1.getFullName());
        assertEquals(25, nullablePerson1.getAge());
        assertTrue(((IAsNullable<Person>)nullablePerson1).asNullable().isPresent());
        
        val nullablePerson2 = NullableData.from(()->null, Person.class);
        assertEquals("", nullablePerson2.getFirstName());
        assertEquals("", nullablePerson2.getLastName());
        assertEquals("", nullablePerson2.getFullName());
        assertEquals(0,  nullablePerson2.getAge());
        assertFalse(((IAsNullable<Person>)nullablePerson2).asNullable().isPresent());
    }
    
    public static interface AnotherNullablePerson extends Person, IAsNullable<Person> {
    }
    
    @Test
    public void testProxy() {
        val orgPerson = new PersonImpl("John");
        
        val person = NullableData.from(()->orgPerson, Person.class, AnotherNullablePerson.class);
        
        assertEquals("John", person.getFirstName());
        person.setFirstName("Jack");
        assertEquals("Jack", person.getFirstName());
        person.setFirstName("Jim");
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PrintStream           out  = new PrintStream(baos)) {
            person.printlnName(out);
            assertEquals("Person: Jim", baos.toString().trim());
        } catch (IOException e) {
        }
        assertEquals(3, person.asNullable().map(Person::getFirstName).map(String::length).get().intValue());
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
        val person    = NullableData.from(()->orgPerson, BuildInNullablePerson.class);
        
        assertEquals("John", person.get().getName());
        person.get().setName("Jack");
        assertEquals("Jack", person.get().getName());
        person.get().setName("Jim");
        assertEquals("Jim", person.map(BuildInNullablePerson::getName).get());
    }
    
    //== Test recursive ==
    
    public static interface Thing {
        
        String name();
        Thing anotherThing();
        
    }
    
    @Test
    public void testRecursiveProxy_andCash() {
        val thing = NullableData.of(null, Thing.class);
        assertEquals(thing, thing.anotherThing());
    }
    
    @Test
    public void testObjectMethods() {
        val thing = NullableData.of(null, Thing.class);
        assertEquals("Thing=null", thing.toString());
        assertEquals(Thing.class.hashCode(), thing.hashCode());
    }
    
    @Test
    public void testEqualsOfNull() {
        val thing1 = NullableData.of(null, Thing.class);
        val thing2 = NullableData.of(null, Thing.class);
        assertTrue(thing1.equals(null));
        assertTrue(thing1.equals(thing1));
        assertTrue(thing1.equals(thing2));
        assertTrue(thing1.equals(thing1.anotherThing()));
    }
    
    public static interface ThingAndMore extends Thing {
        String value();
    }
    
    @Test
    public void testNullOfSubClassNotEqualsToNullOfSuper() {
        val thing = NullableData.of(null, Thing.class);
        val thingAndMore = NullableData.of(null, ThingAndMore.class);
        Assert.assertFalse(thing.equals(thingAndMore));
    }
    
}
