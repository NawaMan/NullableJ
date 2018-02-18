# NullableJ

![alt "Build status"](https://travis-ci.org/NawaMan/NullableJ.svg?branch=master)

Java Uitility used to deal with `null`.

`NullableJ` deploy multiple techniques to deal with `null`.
* `NullableJ` is a collection of extension methods that are null safe.
* `NullValues` is a utility to get null value of a class (value of a type that represent null).
* `Nullable` is a wrapper of a value that might be null. This is very similar to Optional.
* `NullableData` is a utility to create a null data of any data interface.

## `NullableJ`
`NullableJ` is a collection of extension methods that are null safe.
These methods are designed to be used with [Lombok's @ExtensionMethod](https://dzone.com/articles/lomboks-extension-methods).
They can be used as normal static methods but the naming is done with the intention that they will be used with Lombok's ExtensionMethod.
These are commonly used methods that are null safe.
This include simple method like `toString()` that return null if the object is null.
Null specific methods like `_or(...)` to returns the given value if the object is null.
Collection methods like `Map.get(...)` that return null, if the map is null or the key is null.
Or even complex method like `when(...)` that allow you to discards the value if the condition is not met.

For example, the code below will parse the string to an int or retur 0 if the string does not contain valid int stirng.
```java
	return intString._whenMatches("\\-?[0-9]+").map(Integer::parseInt).orElse(0);
```
See [`NullableJ` page](https://github.com/NawaMan/NullableJ/blob/master/docs/NullableJ.md) for more info.

## `NullValues`
`NullValues` is a utility to get null value of a class.
`NullValues` will try to find the best candidate value to use as null value for a given class.
It deploys many strategies to obtain the value such as from know list, single field with name or anncation and so on.

The example code below show PhoneNumber class that specifies its null value using annotation.
```java
	@Value
	public class PhoneNumber {
		@NullValue	// Specify that this value is a null value.
		public static final PhoneNumber nullPhoneNumber = new PhoneNumber("xxx-xxx-xxxx");
		
		private String number;
	}
	
	...
	// NullValues figured out that the nullPhoneNumber is the null value of the type.
	assertEquals("xxx-xxx-xxxx", NullValues.of(PhoneNumber.class).getNumber());
```

Find more information [here](https://github.com/NawaMan/NullableJ/blob/master/docs/NullValues.md).

## `Nullable`
`Nullable` is an implementation of Optional but it is extensible.
It also allow you to create a nullable object of interface data.
Its benefit (as for dealing with `null`) is very similar to Optional.
However, since it is extensible,
  more methods are added and sub typing is done to expands its utilities.
Also, fix some of the problems of Optional.

For example, the following code will return the value associated with the string key or empty string if the key is null.
```java
	return Nullable.of(key).map(valueMap::get).orElse("");
```
Find more information [here](https://github.com/NawaMan/NullableJ/blob/master/docs/Nullable.md).

## `NullableData`
`NullableData` is a utility to create nullable object of any interface (only work with interfaces).
The nullable data created is an hybridge object between the data interface and Nullable.
When invoking the method of the data class, nothing is done and null values are returned.
This nullable data will look just other instance of that interface so it can be passed along and used just like other instance.

For example, the following code will create a nullable of EmailService depending on whether if the service was enabled.
```java
	public interface EmailService {
		public void sendEmail(Email email);
	}
	...
	
	// NullableData implements the interface 
	EmailService emailService = NullableData.of(emailServiceEnabled ? actualEmailService : null, EmailService.class);
	...
	emailService.sendEmail(email);
```
Find more information [here](https://github.com/NawaMan/NullableJ/blob/master/docs/NullableData.md).


## Using NullableJ in Gradle project

Add the maven repository ...

```Groovy
    maven { url 'https://raw.githubusercontent.com/nawmaman/nawaman-maven-repository/master/' }
```

and the dependencies to Lombok and NullableJ.

```Groovy
    compileOnly 'org.projectlombok:lombok:1.16.16'
    compile     'nawaman:nullablej:0.4.0'
```

See [UseNullableJGradle](https://github.com/NawaMan/UseNullableJGradle) for more information.

## Using NullableJ in Maven project

Adding the required maven repository (hosted by github).

```xml
<repository>
	<id>Nullable-mvn-repo</id>
	<url>https://raw.githubusercontent.com/nawaman/nawaman-maven-repository/master/</url>
	<snapshots>
		<enabled>true</enabled>
		<updatePolicy>always</updatePolicy>
	</snapshots>
</repository>
```

and the dependencies to Lombok and NullableJ.

```xml
<dependency>
	<groupId>nawaman</groupId>
	<artifactId>nullablej</artifactId>
	<version>0.4.0</version>
</dependency>
<dependency>
	<groupId>junit</groupId>
	<artifactId>junit</artifactId>
	<version>4.11</version>
	<scope>test</scope>
</dependency>
```

See [UseNullableJMaven](https://github.com/NawaMan/UseNullableJMaven) for more information.

## Issues

Please use our [issues tracking page](https://github.com/NawaMan/NullableJ/issues) to report any issues.

## Constribute

Feel free to help out.
Report problem, suggest solutions, suggest more functionality ... anything is appreciated.
Or if this is useful to you and want to buy me a [coffee](https://www.paypal.me/NawaMan/2.00)
 or [lunch](https://www.paypal.me/NawaMan/10.00) ... that would be great :-)

