package nu.nerd.pecon.function;

/**
 * Returns a constant value, regardless of the input.
 */
public class ConstantFunction implements NumericFunction {

    private double value;

    /**
     * Constructs a constant function with the given value.
     *
     * @param value the value
     */
    public ConstantFunction(double value) {
        this.value = value;
    }

    @Override
    public double eval(double arg) {
        return value;
    }

}
