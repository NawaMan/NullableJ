# NullableJ

![alt "Build status"](https://travis-ci.org/NawaMan/NullableJ.svg?branch=master)

Java Uitility used to deal with `null`.

`NullableJ` deploy multiple techniques to deal with `null`.

### `NullableJ`
`NullableJ` is a collection of extension methods that are null safe.
These methods are designed to be used with [Lombok's @ExtensionMethod](https://dzone.com/articles/lomboks-extension-methods).
They can be used as normal static methods but the naming is done with the intention that they will be used with Lombok's ExtensionMethod.
These are commonly used methods that are null safe.
This include simple method like `toString()` that return null if the object is null.
Null epecific methods like `_or(...)` to returns the given value if the object is null.
Collection methods like `Map.get(...)` that return null, if the map is null or the key is null.
Or even complex method like `when(...)` that allow you to discards the value if the condition is not met.
See [`NullableJ` page](https://github.com/NawaMan/NullableJ/blob/master/docs/NullableJ.md) for more info.

### `NullValues`
`NullValues` will try to find the best candidate value to use as null value for a given class.
Find more information [here](https://github.com/NawaMan/NullableJ/blob/master/NullValues.md).

### `Nullable`
`Nullable` is an implementation of Optional but it is extensible.
It also allow you to create a nullable object of interface data.


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
