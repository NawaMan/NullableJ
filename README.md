# NullableJ

![alt "Build status"](https://travis-ci.org/NawaMan/NullableJ.svg?branch=master)

Java Uitility used to deal with 'null'.
It is designed to used with Lombok's Extension.
This means it will be the most beneficial when used with Lombok's extension methods
  although you can use the utilities without that.
The use of Lomlok's extension methods particularly magnify the value of null-safty done to all the utility methods.

For example, you can write code like this...

```Java
// string CAN BE NULL.
return string._whenMatches("^[0-9]+$").map(Integer::parseInt).orElse(-1);	// <-- see '_when(...)'
```

The above code parse the string to an int **or** return -1 if the string is **null** or does not contains an interger.

## Using NullableJ

The example code above made use of [https://dzone.com/articles/lomboks-extension-methods](Lombok's Extension methods).
So for the above code to complie, the following annotation must be added to the class the above code it on using.

```Java
import nawaman.nullable.NullableJ;

...

@ExtensionMethod({ NullableJ.class })
```

If you do not like using Lombok's ExtensionMethods,
  use can straight out calling those method from NullableJ

```Java
import nawaman.nullable.NullableJ;

...

// string CAN BE NULL.
return NullableJ._whenMatches(string, "^[0-9]+$").map(Integer::parseInt).orElse(-1);	// <-- see '_when(...)'
```

or with static import ...

```Java
import static nawaman.nullable.NullableJ._as;

...

// string CAN BE NULL.
return _whenMatches(string, "^[0-9]+$").map(Integer::parseInt).orElse(-1);	// <-- see '_when(...)'
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


See [UseNullableJMaven](https://github.com/NawaMan/UseNullableJMaven) for more information.
