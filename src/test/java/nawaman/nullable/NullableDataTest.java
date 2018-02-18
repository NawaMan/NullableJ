package nawaman.nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.val;

@SuppressWarnings("javadoc")
public class NullableDataTest {

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
    
    public static interface AnotherNullablePerson extends Person, Nullable<Person> {
    }
    
    @Test
    public void testProxy() {
        val orgPerson = new PersonImpl("John");
        
        val person = NullableData.from(()->orgPerson, Person.class, AnotherNullablePerson.class);
        
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
