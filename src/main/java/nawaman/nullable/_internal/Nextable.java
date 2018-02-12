package nawaman.nullable._internal;

import java.util.Iterator;

/**
 * This iterator can return current value without progressing the cursor.
 * IMPORTANT: This iterator will call {@code next()} in the constructor
 *   so it first element is always there in the current.
 * If you use this iterator as normal iterator ... you will miss the first element... so DON'T
 **/
@SuppressWarnings("javadoc")
public class Nextable<T> implements Iterator<T> {
    private Iterator<T> iterator;
    private T current = null;
    
    public Nextable(Iterator<T> iterator) {
        this.iterator = iterator;
        this.next();
    }
    
    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }
    @Override
    public T next() {
        return (current = iterator.next());
    }
    
    public T current() {
        return current;
    }
}