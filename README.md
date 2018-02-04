# NullableJ

![alt "Build status"](https://travis-ci.org/NawaMan/NullableJ.svg?branch=master)

Java Uitility used to deal with 'null'.
It is designed to used with Lombok's Extension.
This means it will be the most beneficial when used with Lombok's extension methods
  although you can use the utilities without that.
The use of Lomlok's extension methods particularly magnify the value of null-safty done to all the utility methods.

For example, you can write code like this...

```Java
@ExtensionMethod({ NullableJ.class })
public class Main {
	/** This method parse number from the given string, return -1 if the string does not contains a value number or null. */
	public int parseNumber(String theGivenString) {
		// theGivenString CAN BE NULL.
		return theGivenString._whenMatches("^[0-9]+$").map(Integer::parseInt).orElse(-1);	// <-- see '_when(...)'
	}
}
```

## Use in Gradle project

Add the maven repository ...

```Groovy
    maven { url 'https://raw.githubusercontent.com/nawmaman/nawaman-maven-repository/master/' }
```

and the dependencies to Lombok and NullableJ.

```Groovy
    compileOnly 'org.projectlombok:lombok:1.16.16'
    compile     'nawaman:nullablej:0.4.0'
```

See [UseNullableJGradle!](https://github.com/NawaMan/UseNullableJGradle) for more information.
