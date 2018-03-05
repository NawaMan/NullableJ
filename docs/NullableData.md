# NullableData

NullableData can create an instance of any interface that act as a null object.

The result object (called nullable-data object) implements the data interface
  and holds the actual value of that interface type.
But if the provided value is null, this nullable data instance will act as null object.
If the methods has default implementation, the implementation will be called.
All other methods will do nothing and return null value of that method return type
  (by calling `@code NullValues.nullValueOf(returnType)`).
Here is the example.

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

        Person nullablePerson = NullableData.of(new PersonImpl("Peter", "Pan"), Person.class);
        assertTrue(nullablePerson instanceof Person);
        assertEquals("Peter",     nullablePerson.getFirstName());
        assertEquals("Pan",       nullablePerson.getLastName());
        assertEquals("Peter Pan", nullablePerson.getFullName());
```

If the data is null, the exact same operation can be used but it will acts as a null object of that type.

```Java

        Person nullablePerson = NullableData.of(null, Person.class);
        assertTrue(nullablePerson instanceof Person);
        assertEquals("", nullablePerson.getFirstName());
        assertEquals("", nullablePerson.getLastName());
        assertEquals("", nullablePerson.getFullName());
```

What if you want to know if the underline value is `null`,
  NullableData got you covered.
The nulldata instance secretly implements the `IAsNullable` interface.
The method `asNullable` from `IAsNullable` will return an instance of `Nullable` of the underline object.
Because the implement of `IAsNullable` is hidden,
  the instance has to be casted to `IAsNullable` before it can be used as such.
You can also make the data interface to implement the `IAsNullable` interface.

```Java

    public static interface NullablePerson extends Person, IAsNullable<Person> {}
    
    ...
        val nullablePerson = NullableData.of(new PersonImpl("Peter", "Pan"), NullablePerson.class);
        assertTrue(nullablePerson instanceof Person);
        assertTrue(nullablePerson.asNullable().isPresent());
        
        val nullablePerson2 = NullableData.of(null, NullablePerson.class);
        assertTrue(nullablePerson2 instanceof Person);
        assertFalse(nullablePerson2.asNullable().isPresent());
```

`NullableData` is currently implemented using DynamicProxy is sufficient in most case.
However, the interface and the implemented class has to be separated which is not exactly convenient.
So, to having `Immutable` style code generation for this is under consideration.



