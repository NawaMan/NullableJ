# NullableJ

Java Uitility used to deal with 'null'.
It is designed to used with Lombok @ExtensionMethod (see [here](https://dzone.com/articles/lomboks-extension-methods) and [here](https://projectlombok.org/features/experimental/ExtensionMethod)).
This means it will be the most beneficial when used with @ExtensionMethod
  although you can use the utilities without that.
The use of Lomlok's extension methods particularly magnify the value of null-safty done to all the utility methods.

For example, you can write code like this...

```Java
// string CAN BE NULL.
return string._whenMatches("^[0-9]+$").map(Integer::parseInt).orElse(-1);
//            ^^^^^^^^^^^^
```

`NullableJ` methods are very small, mostly a few lines, and very easy to understand.
They also comes with JavaDoc and well covered unit tests.
It does not seems productive to replicate them here.
So instead, it is recommended to find out about all the methods from looking at the test code and the source code.

- [Test](https://github.com/NawaMan/NullableJ/blob/master/src/test/java/nawaman/nullable/NullableJTest.java)
- [Source](https://github.com/NawaMan/NullableJ/blob/master/src/main/java/nawaman/nullable/NullableJ.java)

That being said, some example codes that highlight benefits of using NullableJ will be put below. :-)

...

