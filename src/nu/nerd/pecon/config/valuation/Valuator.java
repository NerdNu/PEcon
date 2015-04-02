package nu.nerd.pecon.config.valuation;

/**
 * Provides a valuation of an object.
 *
 * @param <T> the type of object to valuate
 */
public interface Valuator<T> {

    /**
     * Returns the valuation of the given object.
     *
     * @param input the object to valuate
     * @return the valuation of the object
     */
    public double valuate(T input);

}
