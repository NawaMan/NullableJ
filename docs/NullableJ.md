# NullableJ

`NullableJ` contains methods for dealing with 'null'.
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


## Documentation

`NullableJ` methods are very small, mostly a few lines, and very easy to understand.
They also comes with JavaDoc and well covered unit tests.
It does not seems productive to replicate them here.
So instead, it is recommended to find out about all the methods from looking at the test code and the source code.

- [Test](https://github.com/NawaMan/NullableJ/blob/master/src/test/java/nawaman/nullable/NullableJTest.java)
- [Source](https://github.com/NawaMan/NullableJ/blob/master/src/main/java/nawaman/nullable/NullableJ.java)

