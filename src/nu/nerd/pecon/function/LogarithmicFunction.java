package nu.nerd.pecon.function;

/**
 * Applies a double-to-double logarithmic function to a number.
 */
public class LogarithmicFunction implements NumericFunction {

    private final double logBase;

    /**
     * Constructs a logarithmic function with the given base.
     *
     * @param base the base
     */
    public LogarithmicFunction(double base) {
        logBase = Math.log(base);
    }

    @Override
    public double eval(double arg) {
        return Math.log(arg) / logBase;
    }

}
