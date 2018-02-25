# Nullable

`Nullable` is a copy-cat of Java 8 Optional.
There are a few differences but if you know Optional,
  you will almost know `Nullable`.
So basically, `Nullable` is a wrapper to an object.
That object can be null.
You can check if the object is null (`isPresent()`).
You can transform the object using `map(...)`.
And almost everything you do with Optional,
  you can do it with Nullable.

As mentioned there are a few differences between `Optional` and `Nullable`.

## `Nullable` not `Optional`
The biggest difference (in the perspective of the user) is that
  `Nullable` embraces `null`.
As the name imply,
  the value wrapped inside `Nullable` can be null.
You can put null into it with `Nullable.of()`
  -- there is no need for a separete `ofNullable(...)`.
You can also get the value out using `get()` method
  without worry that it will throw an exception if the value is not present.
`get()` will simply return `null`.
This consolidate the way to get value in and out of `Nullable` or both `null` and non-`null`.
These make `Nullable` a true "maybe monad". 

## Complex nullable
`Nullable` can be created using factory methods.
Two of which (`Nullable.from(Supplier<T> supplier)` or `Nullable.nullable(Supplier<T> supplier)`),
  accept a supplier.
When called, the supplier will be executed right away and if it throw NullPointerException,
  an empty Nullable is returned.
This is very useful in case of a complex supplier to get the value such as long call chain expression.
For example.

```Java

        Nullable.from(()->myMap.get(theKey.toUpperCase()).replaceAll("^PREFIX: ", "")).ifPresent(System.out::println);
```

In the above code,
  if `theKey` or `mapByString` is `null`,
    or `mapByString` has no value associated with `theKey`,
    the `Nullable.from(...)` will return `Nullable.empty()`.
Without this feature,
  we need to put that expression combinations of map and flatMap.
A small trick for those that are OK with static import,
  you can use static factory method `Nullable.nullable()` to make a it more readable.
Like this ...

```Java

        nullable(()->myMap.get(theKey.toUpperCase()).replaceAll("^PREFIX: ", "")).ifPresent(out::println);
```


## Interface vs Class
Behind the scene, the differences between `Nullable` and `Optional` are even more profound.
`Optional` is a final class while `Nullable` is an interface -- a functional interface to be exact.
As a functional interface,
  `Nullable` has only one abstract method -- `get()` (note: `Nullable` extends `Supplier` so it can be used as such).
All of the rest of the methods are default.
So, you can create a nullable using functional interface assign (thought not recommended).

```Java

	Nullable<String> nullableStr = ()->myString;
```

Then, you can use it like this.

```Java

	nullableStr.map(s->"\"" + s + "\" is not null.").orElse("The string is null");
```

That being said, the recommended way to create `Nullable` is the factory methods `of(...)`, `from(...)` and `nullable(...)`.

```Java

	Nullable.of(myString).map(s->"\"" + s + "\" is not null.").orElse("The string is null");
```

## Extensibility I
Because `Nullable` is an interface so it is extensible and expansible.
This allow us to adapt `Nullable` to be used in many different ways.
Just within NullableJ project,
  we have `Nullable`, `LivNullable`, `NullableImpl`, nullable data and `Otherwise`.

We also expands `Nullable` to add more functionality than what comes with `Optional`.
One good example is `orElseThrow()` which throw the NPE if not present.
This one is useful to allow additional filter or map but still throw NPE when not present.

```Java
nullable(str).filter(s->s.startsWith(S)).map(String::toUpperCase).orElseThrow();
```

Another is `or(Supplier<Nullable<T>> supplier)` which return itself if present or return the result from the supplier essentially combine the two.

There are also a set of overload `ifPresent(...)` methods that can accept both `Consumer` and `Runnable`
  (if you do not care about the value).
It also can have another `Runnable` to run as else case.

## Extensibility II
Since `Nullable` is extensible, you can add more methods there if you need and used often.
For example, if you often received nullable array but use them as list,
  you may create NullableArray interface with a method to quickly convert to list.

```Java

    public static interface NullableArray<T> extends Nullable<T[]> {
        public static <T> NullableArray<T> of(T[] array) {
            return (NullableArray)()->array;
        }
        public default List<T> toList() {
            T[] array = get();
            if (array == null)
                return Collections.emptyList();
            
            return Arrays.asList((T[])array);
        }
    }
    
    @Test
    public void testNullableArray() {
        val nullableArray = NullableArray.of(new String[] { "one",  "two",  "three" });
        assertEquals("[one, two, three]", nullableArray.toList().toString());
        
        val nullableArray2 = NullableArray.of(null);
        assertEquals("[]", nullableArray2.toList().toString());
    }
```

## Liveness
As mention `Nullable` is a functional interface with `get()` as the functional method.
As an interface, it can't hold a state on itsown so the value is obtain from calling `get()` every time.
This can be useful in some cases but in most case it can cause confusion.
So it is recommended to use `Nullable.of(T value)`, `Nullable.nullable(T value)`, `Nullable.from(Supplier<T> value)` and `Nullable.nullable(Supplier<T> value)` to create a `Nullable` for most cases as it will actually instantiate a concrete class `NullableImpl` for it.

If liveness is desirable, use `LiveNullable.from(...)` should be used or utilize functional-interface casting to create one -- `(Nullable<String>)()->getCurrentString()`.



