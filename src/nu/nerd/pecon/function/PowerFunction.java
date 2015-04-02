package nu.nerd.pecon.function;

/**
 * Applies a double-to-double power function to a number.
 */
public class PowerFunction implements NumericFunction {

    private final double power;

    /**
     * Constructs a power function from the given power.
     *
     * @param power the power
     */
    public PowerFunction(double power) {
        this.power = power;
    }

    @Override
    public double eval(double arg) {
        return Math.pow(arg, power);
    }

}
