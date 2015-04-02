package nu.nerd.pecon.function;

/**
 * Applies a double-to-double function to a number.
 */
public interface NumericFunction {

    /**
     * Evaluates the function at the given argument.
     *
     * @param arg the argument to the function
     * @return the result of the function
     */
    public double eval(double arg);

}
