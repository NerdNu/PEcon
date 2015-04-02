package nu.nerd.pecon.config.properties;

// TODO: Can this and its subclasses be merged with Valuator into
//       Valuator<T, U> with return type U?

/**
 * Produces true or false when passed an object.
 *
 * @param <T> the type of object to test
 */
public interface Predicate<T> {

    /**
     * Returns a boolean value based on the given object.
     *
     * @param object the object
     * @return the result
     */
    public boolean test(T object);

}
