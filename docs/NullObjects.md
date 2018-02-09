# NullObjects

`NullObjects` implements `IFindNullObject` interface.
It can find/create null object for a given type.
This is most useful to create return object for other code that might not expect null.

NullObjects has default instance ready to use (`NullObjects.instance`).
It also has a static method (`nullObjectOf(...)`) that basically call the default instance.

# Finding null object.
`NullObjects` has a preset sets of strategies to find null objects.
Here are the strategies in the order.

1. KnownNullObjects
2. KnownNewNullObjects
3. Empty array
4. AnnotatedField
5. AnnotatedMethod
5. NamedField
6. NamedMethod
7. DefaultConstructor

## KnownNullObjects
`NullObjects` maintains a list of classes its know exactly what the null object are.
Most of the classes in this list are primitive or primitive wrapper types.
The list also include abstract collection and map classes where empty immutable instances are their null objects.
The list also include nullable of `Runnable` which does nothing.
Notable values are:

- 0 for all numeric types
- false for boolean
- empty string for `String`
- space character for `char` and `Character`
- do-nothing for `Runnable`

## KnownNewNullObjects
`NullObjects` also has a list of classes that it knows for sure how to create null-value instances.
All of these classes are collections and maps.
The null-value of these classes are its new instances.

## Empty array
If the class is an array class,
	NullObjects` will create an empty array of the same type. 

## AnnotatedField
If a class have a public-static-final field with compatible type,
  `NullObjects` will check if such a field has an annotation with a simple name `NullObject`.
You can use the annotation that comes with package 
  or you can make your own.
As long as it is named `NullObject` and is accessible at runtime, it can be used.
This method is only recommended if the object is immutable
  as the null object will be accessible by everyone.

** Example: **
```Java
@Value
public class Person {
	@NullObject
	public static final Person instance = new Person();
	
	private String name;
}
```

## AnnotatedMethod
Similar to AnnotatedField,
  `NullObjects` will check any public-static-final method with the compatible type and with no parameter to see if any is annotated with `@NullObject`.

```Java
@Value
public class Person {
	@NullObject
	public static final Person newInstance() {
		return new Person();
	}
	
	private String name;
}
```

## AnnotatedMethod
Similar to AnnotatedField,
  `NullObjects` will check any public-static-final method with the compatible type and with no parameter to see if any is annotated with `@NullObject`.

```Java
@Value
public class Person {
	@NullObject
	public static final Person newInstance() {
		return new Person();
	}
	
	private String name;
}
```

## NamedField
`NullObjects` will look for a public-static-final field with compatible type
  and the field is named `nullObject` or `NULL_OBJECT` (whatever comes first).
  
** Example: **
```Java
@Value
public class Person {
	public static final Person nullObject = new Person();
	private String name;
}
```

## NamedMethod
`NullObjects` will look for a public-static-final method with compatible type
  with no parameter and the method is named `nullObject`.
  
** Example: **
```Java
@Value
public class Person {
	public static final Person nullObject() { return new Person(); }
	private String name;
}
```

## DefaultConstructor
If the above strategies fail,
  `NullObjects` will try to see if it can create a new value using default constructor.

## null
That is right, `NullObjects` fails all above, it just return null.

# Conclusion
Hope this article explains what `NullObjects` does and how it does it.






