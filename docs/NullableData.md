# NullableData

NullableData can create an instance of any interface that act as a null object.

The result object (called nullable-data object) implements both the data interface and `IAsNullable` interface.
This object holds the actual value of that interface type.
But if that value is null, this nullable data instance will act as null object.
The method {@code asNullable} from IAsNullable will return an instance of `Nullable` of the object.
If the methods has default implementation, the implementation will be called.
All other methods will do nothing and return null value of that method type
  (by calling `@code NullValues.nullValueOf(returnType)`).

The nullable data implements both the data interface and `IAsNullable` instance.
But the implement of `IAsNullable` is hidden -- meaning that
  the instance has to be casted to `IAsNullable` before it can be used as such.
You can also make the data interface to implement the `IAsNullable` interface.

For example, if we have a Person interface.

```Java

    public static interface Person {
        public String getFirstName();
        public String getLastName();
        
        public default String getFullName() {
            return (getFirstName() + " " + getLastName()).trim();
        }
        
    }
```

with the following implementation...
```Java

    @Value
    public static class PersonImpl implements Person {
        private String firstName;
        private String lastName;
    }
```

then a nullable data can be created and used like this ...

```Java

        val nullablePerson = NullableData.of(new PersonImpl("Peter", "Pan"), Person.class);
        assertTrue(nullablePerson1 instanceof Person);
        assertEquals("Peter",     nullablePerson.getFirstName());
        assertEquals("Pan",       nullablePerson.getLastName());
        assertEquals("Peter Pan", nullablePerson.getFullName());
        assertTrue(((IAsNullable<Person>)nullablePerson).asNullable().isPresent());
```

If the data is null, the exact same operation can be used but it will acts as a null object of that type.

```Java

        val nullablePerson = NullableData.of(null, Person.class);
        assertTrue(nullablePerson1 instanceof Person);
        assertEquals("", nullablePerson.getFirstName());
        assertEquals("", nullablePerson.getLastName());
        assertEquals("", nullablePerson.getFullName());
        assertFalse(((IAsNullable<Person>)nullablePerson).asNullable().isPresent());
```

As mentioned, if the `asNullable()` is accessed often, you can have `Person` interface extends `IAsNullable`.
You can also create another interface that both implements `Person` and `IAsNullable`
  then use it to create the nullable data.

```Java

    public static interface NullablePerson extends Person, IAsNullable<Person> {}
    
    ...
        val nullablePerson = NullableData.of(new PersonImpl("Peter", "Pan"), NullablePerson.class);
        assertTrue(nullablePerson1 instanceof Person);
        assertEquals("Peter",     nullablePerson.getFirstName());
        assertEquals("Pan",       nullablePerson.getLastName());
        assertEquals("Peter Pan", nullablePerson.getFullName());
        assertTrue(nullablePerson.asNullable().isPresent());
```

`NullableData` uses dynamic proxy to create these instances.
