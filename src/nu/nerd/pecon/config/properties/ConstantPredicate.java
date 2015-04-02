package nu.nerd.pecon.config.properties;

/**
 * Predicate that always returns a given value.
 *
 * @param <T> the type of object to test
 */
public class ConstantPredicate<T> implements Predicate<T> {

    private boolean value;

    /**
     * Constructs a {@code ConstantPredicate} that always returns the given
     * value.
     *
     * @param value the value
     */
    public ConstantPredicate(boolean value) {
        this.value = value;
    }

    @Override
    public boolean test(T object) {
        return value;
    }

}
