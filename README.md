# NullableJ

![alt "Build status"](https://travis-ci.org/NawaMan/NullableJ.svg?branch=master)

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

The above code parse the string to an int **or** return -1 if the string is **null** or does not contains an integer.

## Using NullableJ

The example code above makes use of @ExtensionMethod.
So for the above code to compile, the following annotation must be added to the class the above code it on using.

```Java
import nawaman.nullable.NullableJ;

...

@ExtensionMethod({ NullableJ.class })
```

@ExtensionMethod do have some limitations,
  see [the "Limitations" section in my article](https://dzone.com/articles/lomboks-extension-methods) for more information.
If you do not like using @ExtensionMethod,
  use can straight out calling those method from NullableJ

```Java
import nawaman.nullable.NullableJ;

...

return NullableJ._whenMatches(string, "^[0-9]+$").map(Integer::parseInt).orElse(-1);
```

or with static import ...

```Java
import static nawaman.nullable.NullableJ._as;

...

return _whenMatches(string, "^[0-9]+$").map(Integer::parseInt).orElse(-1);
```

See below on how to setup a [Gradle](#use-in-gradle-project) or [Maven](#use-in-maven-project) to use NullableJ.

## API

The best and most up-to-date way to see all the methods and how to use them is to see [the unit tests](https://github.com/NawaMan/NullableJ/blob/master/src/test/java/nawaman/nullable/NullableJTest.java).
It is easy to read, promise!

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

See [UseNullableJGradle](https://github.com/NawaMan/UseNullableJGradle) for more information.

## Use in Maven project

Add the maven repository ...

It basically boils down to adding NawaMan maven repository (hosted by github).

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
