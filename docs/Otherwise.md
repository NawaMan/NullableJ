# Otherwise

Otherwise ...
They are generally returned from `NullableJ.whenXXX()` and `NullableJ._as()` methods.

Otherwise behave like a regular Nullable when the condition is true.
When the condition is not true, Otherwise behave like Nullable of null.
But it store the original value so that it can later be used.


```Java
	str._when(startsWith("prefix")).map(str->str.subString("prefix"))
```