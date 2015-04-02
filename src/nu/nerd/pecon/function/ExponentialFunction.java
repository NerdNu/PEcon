package nu.nerd.pecon.function;

/**
 * Applies a double-to-double exponential function to a number.
 */
public class ExponentialFunction implements NumericFunction {

    private final double base;

    /**
     * Constructs an exponential function with the given base.
     *
     * @param base the base
     */
    public ExponentialFunction(double base) {
        this.base = base;
    }

    @Override
    public double eval(double arg) {
        return Math.pow(base, arg);
    }

}
