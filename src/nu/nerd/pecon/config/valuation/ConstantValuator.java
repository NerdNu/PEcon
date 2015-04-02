package nu.nerd.pecon.config.valuation;

/**
 * Valuates any input at a given constant value.
 *
 * @param <T> the type of object to valuate
 */
public class ConstantValuator<T> implements Valuator<T> {

    private final double value;

    /**
     * Constructs a {@code ConstantValuator} that always returns the given
     * value.
     *
     * @param value the value
     */
    public ConstantValuator(double value) {
        this.value = value;
    }

    @Override
    public double valuate(Object input) {
        return value;
    }

}
