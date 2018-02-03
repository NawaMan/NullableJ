# NullableJ

Java Uitility used to deal with 'null'.
It is designed to used with Lombok's Extension.
This means it will be the most beneficial when used with Lombok's extension methods
  although you can use the utilities without that.
The use of Lomlok's extension methods particularly magnify the value of null-safty done to all the utility methods.

For example, you can write code like this...

```Java
@ExtensionMethod({ UNulls.class })
public class Main {
	/** This method parse number from the given string, return 0 if the string does not contains a value number. */
	public int parseNumber(String theGivenString) {
		return theGivenString.when(matches("^[0-9]+$")).or(-1);	// <-- see 'when(...)' and 'or(...)'
	}
	private Predicate<String> matches(String regEx) {
		return str->(str == null) ? null : str.matches(regEx);
	}
}
```

