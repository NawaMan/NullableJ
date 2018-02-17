package nawaman.nullable;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.Test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.val;

@SuppressWarnings("javadoc")
public class NullableObjectTest {

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
