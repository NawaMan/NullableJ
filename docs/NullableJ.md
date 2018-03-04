# NullableJ

Java Uitility used to deal with 'null'.
It is designed to used with Lombok @ExtensionMethod (see [here](https://dzone.com/articles/lomboks-extension-methods) and [here](https://projectlombok.org/features/experimental/ExtensionMethod)).
This means it will be the most beneficial when used with @ExtensionMethod
  although you can use the utilities without that.
The use of Lomlok's extension methods particularly magnify the value of null-safty done to all the utility methods.

For example, you can write code like this...

```Java

    System.out.println("String: " + str._or(" <not-given>"));
//                                      ^^^ and Elvis operator, any one?
```

A slightly bigger example, consider the following code.

```Java

    public class SaleReport {
        public BigDecimal totalYearlySaleByPart(String partNumber, Color color, int year) {
            val item        = itemService.findItem(partNumber, color);
            val salesByYear = saleStatService.findItemSalesByYear(item);
           return salesByYear.get(year).stream().map(Sale::getTotal).collect(reducing(ZERO, BigDecimal::add));
        }
    }
```

This method is very readable but a lots of things can go wrong.
* there might be no item for the `partNumber` and `color`.
* the legacy `saleStatService.findItemSalesByYear(item)` may return `null` if item is `null`.
* there might be no item in `salesByYear` map.

With `NullableJ` methods, we may adjust the method like this.

```Java

    @ExtensionMethod({ NullableJ.class })
    public class SaleReport {
        public BigDecimal totalYearlySaleByPart(String partNumber, Color color, int year) {
            val item        = itemService.findItem(partNumber, color);
            val salesByYear = item._isNotNull() ? saleStatService.findItemSalesByYear(item) : null;
            return salesByYear._get(year)._stream$().map(Sale::getTotal).collect(reducing(ZERO, BigDecimal::add));
        }
    }
```

Spot the differences?
That exactly the point.
`NullableJ` methods are designed to be used with Lombok's `@ExtensionMethod` to provide natural way to handle `null`.
`NullableJ` has may often-used methods that are ready.
If you need more, you can create yourown.

## The methods

Most of the methods in NullableJ is very straightforward.

Methods that returns primitive data are the easiest to reason with.
For example: `_equalsTo` returns `true` if the given object equals to another given object (it basically uses `Objects.equals(...)` method).
Non-primitive type methods that will not returns null are `_orXXX()` method (unless you call `_or(null)` of course - but don't do it).

The methods that return non-primitive might return `null`.
This might be surprise to many as the library to help dealing with `null` should not be returning `null` for any reason.
But there is a reason and that is to allow you to chain the call.
For example: `_toString` will returns a string representation of the given object or null.
Then, the method like `_or()` can be used to further specify what to do next.
Like, in case of array or collection,
  you may want to use empty square brackets.

```Java
  System.out.println(obj.getStrings()._toString()._or("[]"));
```

Methods that deal with collections such as `_get()` or `_first()` are also return `null`.
If the methods, however, returns array, collection or stream such as `_toList()`, `stream$()`, they will returns empty value.

The last group of methods are those `_whenXXX` and `_as` methods.
These methods allow us to test the given value if it fit a certain criteria.
These methods return `Otherwise` object.
The otherwise value acts very similar to `Nullable` (or `Optional`).
The value is present or not depending on whether or not the criteria was met.
The different between `Nullable` and `Otherwise` is that the later still have access to the original value.
See [`Otherwise` page](https://github.com/NawaMan/NullableJ/blob/master/docs/Otherwise.md) for more info.

Here are some more examples of methods:

**Methods on Object `O`**
- `O._isNull()`/`O._isNotNull()`: checks if the object is `null` or not.
- `O._equalsTo(O)`/`O._notEqualsTo(O)`: checks if the object equals to another object or not (use `Objects::equals`).
- `O._toString()`: calls the `toString()` method of the object if not `null` and return `null` otherwise.
- `O._or(O)`: returns the object if not `null` otherwise return another object.
- `O._orGet(Supplier<O>)`: returns the object if not `null` otherwise return the value from the supplier.
- `O._orNullValue(Class<O>)`: returns the object if not `null` otherwise call NullValues to get the null value of the given class.
- `O._orNullValue(Class<O>)`: returns the object if not `null` otherwise call NullValues to get the null value of the given class.
- `O._toNullable()`: wraps the object with `Nullable`.
- `O._toOptional()`: wraps the object with `Optional`.
- `O._whenNotNull()`: wraps the object with `Otherwise`.
- `O._when(Predicate<O>)`: returns the`Otherwise` of the object if the predicate returns `true` otherwise returns `Otherwise` of `null` with the original value.
- `O._as(Class<O>)`: returns the`Otherwise` of the object if it is an instance of the given type otherwise returns `Otherwise` of `null` with the object.
- `O._map(Function<O, T>)`, `_mapTo(Function<O, T>)`, `_mapBy(Function<O, T>)`, `_mapFrom(Function<O, T>)`: if not `null`, uses the given function to transform the object. Otherwise, return `null`.

**Methods on number object `N`**
- `N._or(P)`: returns the number object as primitive number type if not `null` otherwise returns the given primitive number.

**Methods on String `S`**
- `S._length()`: returns length of the string or 0 if the string is `null`.
- `S._isEmpty()`: returns whether the string is `null` or have 0 length.
- `S._isBlank()`: returns whether the trimmed value if the string is `null` or have 0 length.
- `S._trimToNull()`: returns the trimmed value of the string if the trimmed value is not `null` or empty. If the string is `null` or the trimmed value is empty, return `null`.
- `S._trimToEmpty()`: returns the trimmed value of the string if the trimmed value is not `null`. If the string is `null`, return an empty string.
- `S._contains(S)`: returns if the string contains the given string. Return false if the string is `null`.
- `S._matches(S)`: returns if the string matches the given regular expression string. Return false if the string is `null`.
- `S._matches(S)`/`S._notMatches(S)`: returns if the string matches the given regular expression string. Return false if the string is `null`.
- `S._whenContains(C)`/`S._whenNotContains(C)`, `S._whenMatches(S)`/`S._whenNotMatches(S)`, `S._whenMatches(P)`/`S._whenNotMatches(P)`: returns the `Otherwise` of the string if the condition met, returns `Otherwise` of `null` with the string  if the condition does not met.
- `C._whenNotEmpty()`: returns the `Otherwise` of the `CharSequence` if it is not empty, returns `Otherwise` of `null` with the `CharSequence` if it was `null`.

**Methods on Array `A`, Collection `C`, Stream `S` and Map `M`**
- `A._stream$()`, `C._stream$()`: returns the stream of the array/collection otherwise return empty stream if the array/collection is `null`.
- `A._length()`: returns the length of the array otherwise return 0 if the array is `null`.
- `A._size()`, `C._size()`: returns the length of the array/collection otherwise return 0 if the array/collection is `null`.
- `A._isEmpty()`, `C._isEmpty()`, `M._isEmpty()`: returns if the array/collection is empty otherwise returns true if the array/collection is `null`.
- `A._contains(O)`, `C._contains(O)`: returns if the array/collection contains the given object otherwise returns false if the array/collection is `null`.
- `M._containsKey(O)`: returns if the map contains the given object as a key otherwise returns false if the map is `null`.
- `M._containsValue(O)`: returns if the map contains the given object as a value otherwise returns false if the map is `null`.
- `A._whenNotEmpty()`,`C._whenNotEmpty()`,`M._whenNotEmpty()`: returns the `Otherwise` of the Array/Collection/Map if it is not empty, returns `Otherwise` of `null` with the Array/Collection/Map if it was `null`.
- `A._toList$()`, `C._toList$()`, `S._toList$()`: returns the list of the array/collection/Stream otherwise returns empty list if the array/collection/Stream is `null`.
- `A._get(index)`, `List._get(index)`, `M._get(key)`: returns if the value inside the array/collection/map otherwise returns `null` if the array/collection/map is `null`.
- `A._get(index, orFunction)`, `List._get(index, orFunction)`, `M._get(key, orFunction)`: returns if the value inside the array/collection/map otherwise returns value from the given `orFunction` with the index or key as the input if the array/collection/map is `null`.
- `A._first()`, `List._first()`: returns if the first value inside the array/collection otherwise returns `null` if the array/collection/map is `null`.
- `A._last()`, `List._last()`: returns if the last value inside the array/collection otherwise returns `null` if the array/collection/map is `null`.
- `A._hasAll(Predicate<O>)`, `C._hasAll(Predicate<O>)`: returns if all of the elements in the array/collection pass the predicate test.
- `A._hasSome(Predicate<O>)`, `C._hasSome(Predicate<O>)`: returns if the array/collection has at least one element that pass the predicate test.
- `A._butOnly$(Predicate<O>)`, `C._butOnly$(Predicate<O>)`: returns the stream after the filter with the given predicate. If the array/collection is `null`, the empty stream is returned.
- `A._butOnly(Predicate<O>)`, `C._butOnly(Predicate<O>)`: returns the list after the filter with the given predicate. If the array/collection is `null`, the empty list is returned.
- `A._butOnlyNonNull$$(Predicate<O>)`, `C._butOnlyNonNull$$(Predicate<O>)`, `S._butOnlyNonNull$$(Predicate<O>)`: returns the stream after the filter our any null element. If the array/collection/stream is `null`, the empty stream is returned.
- `S._flatMap$(Function<O Collection<T>>)`: perform the flatMap using the given mapper and automatically convert to stream. Other word, do a flatMap of the result collection. Otherwise, return empty stream if null.

**Methods on Supplier `S` and Function `F`**
- `S._get()`: returns the value from the supplier otherwise returns `null` if the supplier is `null`.
- `F._get(key)` and `F._apply(key)`: returns the value from the function using the key otherwise returns `null` if the function is `null`.


## Documentation

Althought list above, the full list of methods may change the synchronization between this list that actual one might not be perfect.
So instead, it is recommended to find out about all the methods from looking at the test code and the source code.
Since `NullableJ` methods are very small, mostly a few lines, and very easy to understand.
They also comes with JavaDoc and well covered unit tests.
It won't be painful to just looking at its test and source code.

- [Test](https://github.com/NawaMan/NullableJ/blob/master/src/test/java/nawaman/nullable/NullableJTest.java)
- [Source](https://github.com/NawaMan/NullableJ/blob/master/src/main/java/nawaman/nullable/NullableJ.java)

