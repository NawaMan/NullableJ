# NullableData

NullableData can create an instance of any interface that act as a null object.

The result object (called nullable data) implements both the data interface and `IAsNullable` interface.
This object holds the actual value of that interface type.
But if that value is null, this nullable data instance will act as null object.
The method {@code asNullable} from IAsNullable will return an instance of `Nullable` of the object.
If the methods has default implementation, the implementation will be called.
All other methods will do nothing and return null value of that method type
  (by calling `@code NullValues.nullValueOf(returnType)`).

The nullable data implements both the data interface and `IAsNullable` instance.
But the implement of `IAsNullable` is hidden meaning that
  the instance has to be casted to `IAsNullable` before it can be used as such.
You can also make the data interface to implement the `IAsNullable` interface.
