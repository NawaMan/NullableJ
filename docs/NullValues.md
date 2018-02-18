# NullValues

`NullValues` implements `IFindNullValue` interface.
It can find/create null value for a given type.
This is most useful to create return object for other code that might not expect null.

NullValues has default instance ready to use (`NullValues.instance`).
It also has a static method (`nullValueOf(...)` or just `of(...)`) that basically call the default instance.

# Finding null value
`NullValues` has a preset sets of strategies to find null values.
Here are the strategies in the order.

1. KnownNullValues
2. KnownNewNullValues
3. Empty array
4. AnnotatedField
5. AnnotatedMethod
5. NamedField
6. NamedMethod
7. DefaultConstructor
8. NullableInterface

## KnownNullValues
`NullValues` maintains a list of classes its know exactly what the null value is.
Most of the classes in this list are primitive or primitive wrapper types.
The list also include abstract collection and map classes where empty immutable instances are their null values.
The list also include nullable of `Runnable` which does nothing.
Notable values are:

- 0 for all numeric types
- false for boolean
- empty string for `String`
- space character for `char` and `Character`
- do-nothing for `Runnable`

## KnownNewNullValues
`NullValues` also has a list of classes that it knows for sure how to create null-value instances.
All of these classes are collections and maps.
The null-value of these classes are its new instances.

## Empty array
If the class is an array class,
	NullValues` will create an empty array of the same type. 

## AnnotatedField
If a class have a public-static-final field with compatible type,
  `NullValues` will check if such a field has an annotation with a simple name `NullValue`.
You can use the annotation that comes with package 
  or you can make your own.
As long as it is named `NullValue` and is accessible at runtime, it can be used.
This method is only recommended if the object is immutable
  as the null value will be accessible by everyone.

** Example: **
```Java
@Value
public class Person {
	@NullValue
	public static final Person instance = new Person();
	
	private String name;
}
```

## AnnotatedMethod
Similar to AnnotatedField,
  `NullValues` will check any public-static-final method with the compatible type and with no parameter to see if any is annotated with `@NullValue`.

```Java
@Value
public class Person {
	@NullValue
	public static final Person newInstance() {
		return new Person();
	}
	
	private String name;
}
```

## AnnotatedMethod
Similar to AnnotatedField,
  `NullValues` will check any public-static-final method with the compatible type and with no parameter to see if any is annotated with `@NullValue`.

```Java
@Value
public class Person {
	@NullValue
	public static final Person newInstance() {
		return new Person();
	}
	
	private String name;
}
```

## NamedField
`NullValues` will look for a public-static-final field with compatible type
  and the field is named `nullValue` or `NULL_VALUE` (whatever comes first).
  
** Example: **
```Java
@Value
public class Person {
	public static final Person nullValue = new Person();
	private String name;
}
```

## NamedMethod
`NullValues` will look for a public-static-final method with compatible type
  with no parameter and the method is named `nullValue`.
  
** Example: **
```Java
@Value
public class Person {
	public static final Person nullValue() { return new Person(); }
	private String name;
}
```

## DefaultConstructor
If the above strategies fail,
  `NullValues` will try to see if it can create a new value using default constructor.

## NullableInterface
If the class is an interface, `NullableValues` will try to create an implementation of it using Dynamic Proxy.
All the implemented method will do nothing and will returns null value of whatever the return type is.
This is done using `[NullableData](https://github.com/NawaMan/NullableJ/blob/master/docs/NullableData.md)`.

For example, given the following class.
```Java
	public interface EmailService {
		public SendEmailResponse sendEmail(Email email);
	}
```
the implemented null data will be equivalent to this ...

For example, given the following class.
```Java
	public class NullEmailService implements EmailService, Nullable<EmailService> {
		public SendEmailResponse sendEmail(Email email) {
			return NullValues.nullValueOf(SendEmailResponse.class);
		}
		public EmailService get() {
			return this;
		}
	}
```

## null
That is right, `NullValues` fails all above, it just return null.

# Conclusion
Hope this explains what `NullValues` does and how it does it.

