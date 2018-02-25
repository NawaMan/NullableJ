# NullableJ

![alt "Build status"](https://travis-ci.org/NawaMan/NullableJ.svg?branch=master)

Java Uitility used to deal with `null`.

`NullableJ` deploy multiple techniques to deal with `null`.
* `NullableJ` is a collection of extension methods that are null safe.
* `Nullable` is a wrapper of a value that might be null. This is very similar to Optional.
* `NullValues` is a utility to get null value of a class (value of a type that represent null).
* `NullableData` is a utility to create a null data of any data interface.

## Functionalities

### `NullableJ`
`NullableJ` is a collection of extension methods that are null safe.
These methods are designed to be used with [Lombok's @ExtensionMethod](https://dzone.com/articles/lomboks-extension-methods).
They can be used as normal static methods but the naming is done with the intention that they will be used with Lombok's `@ExtensionMethod`.
These are commonly used methods that are null safe.
This include simple method like `_toString()` that return `null` if the object is `null`.
Null specific methods like `_or(...)` to returns the given value if the object is `null`.
Collection methods like `Map.get(...)` that return `null`, if the map is null or the key is `null`.
Or even complex method like `when(...)` that allow you to discards the value if the condition is not met.

For example, the code below returns the total sale of the given year of the specified item.
```java

    val salesByYear = saleStatService.findItemSalesByYear(item);
    return salesByYear._get(year)._stream$().map(Sale::getTotal).collect(reducing(ZERO, BigDecimal::add));
```

In this code, `item` can be `null`; `salesByYear` can be `null`; `salesByYear._get(year)` can also return `null`,
  then the method will return `BigDecimal.ZERO`.

See [`NullableJ` page](https://github.com/NawaMan/NullableJ/blob/master/docs/NullableJ.md) for more info.

### `Nullable`
`Nullable` is an copy-cat of Java 8 `Optional` but it is extensible and with improvements.
It also allow you to create a nullable object of interface data.
Its benefits (as for dealing with `null`) are very similar to `Optional`.
However, since it is extensible,
  more methods are added and sub typing is done to expands its utilities,
  as well as, fixing some of the problems of `Optional`.
For instance, `Nullable.of(...)` can accept `null` without throwing any exception.
Its `get()` method, similarly, will return `null` instead of throw exception.
These makes `Nullable` a true "maybe monad".

For example, the following code will return the value associated with the string key or empty string if the key is null.
```java

	return Nullable.of(key).map(valueMap::get).orElse("");
```

Another handy feature of `Nullable` is a factory method `Nullable.from(...)` which accept a supplier.
This method will get the value out of the supplier but if `NullPointerException` is thrown in this process,
  the factory method will return `Nullable.empty()`.
That means you can write any expression with in that supplier without having to deal with NPE.

Find more information [here](https://github.com/NawaMan/NullableJ/blob/master/docs/Nullable.md).

### `NullValues`
`NullValues` is a utility to get null value of a class.
`NullValues` will try to find the best candidate value to use as null value for a given class.
It deploys many strategies to obtain the value such as from know list, single field with name or annotation and so on.

The example code below show `PhoneNumber` class that specifies its null value using annotation.
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

With this, you can specify exactly what make sense as a null value of your class.

Find more information [here](https://github.com/NawaMan/NullableJ/blob/master/docs/NullValues.md).

### `NullableData`
`NullableData` is a utility to create nullable object of any interface (only work with interfaces).
The nullable data created is a hybridge object between the data interface and `IAsNullable` (to quickly get it s `Nullable`).
When invoking the method of the data class, nothing is done and null values are returned.
This nullable data will look just like other instance of that data interface so it can be passed along and used just like other instance, hence, the null data object.

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

If the email service is not enabled, the last line will do nothing.

Find more information [here](https://github.com/NawaMan/NullableJ/blob/master/docs/NullableData.md).


## Usage

### Using NullableJ in Gradle project

This project binary is published on [my maven repo](https://github.com/NawaMan/nawaman-maven-repository) hosted on GitHub. So to use NullableJ you will need to ...

Add the maven repository ...

```Groovy
    maven { url 'https://raw.githubusercontent.com/nawmaman/nawaman-maven-repository/master/' }
```

and the dependencies to Lombok and NullableJ.

```Groovy
    compileOnly 'org.projectlombok:lombok:1.16.16'	// Include this if @ExtensionMethod is nedded.
    compile     'nawaman:nullablej:2.0.0'			// Please fine the lastest version.
```

See [UseNullableJGradle](https://github.com/NawaMan/UseNullableJGradle) for more information.

### Using NullableJ in Maven project

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
	<version>2.0.0</version> <!-- Please fine the lastest version. -->
</dependency>
<dependency>
	<groupId>org.projectlombok</groupId>
	<artifactId>lombok</artifactId>
	<version>1.16.16</version> <!-- Please fine the lastest version. -->
</dependency>
<dependency>
	<groupId>junit</groupId>
	<artifactId>junit</artifactId>
	<version>4.11</version>
	<scope>test</scope>
</dependency>
```

See [UseNullableJMaven](https://github.com/NawaMan/UseNullableJMaven) for more information.

## Build

This project is developed as a gradle project on Eclipse
  so you can just clone and import it to your Eclipse.
Although, never tried, but I think it should be easy to import into IntelliJ.
Simply run `gradle clean build` to build the project (or use the build-in gradle wrapper).

## Issues

Please use our [issues tracking page](https://github.com/NawaMan/NullableJ/issues) to report any issues.

## Take what you need

You can import and use this library as you needed.
But if you just need a small part of it, feel free to fork it or just copy the part that you need. :-)


## Contribute

Feel free to help out.
Report problems, suggest solutions, suggest more functionality ... anything is appreciated (please do it in [issues tracking page](https://github.com/NawaMan/NullableJ/issues) or email me directly).

If this is useful to you and want to buy me a [coffee](https://www.paypal.me/NawaMan/2.00)
 or [lunch](https://www.paypal.me/NawaMan/10.00) or [help with my kid college fund](https://www.paypal.me/NawaMan/100.00) ... that would be great :-p

