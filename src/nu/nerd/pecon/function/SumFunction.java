package nu.nerd.pecon.function;

import java.util.List;

/**
 * A double-to-double function that sums a list of functions, each evaluated at
 * the same input.
 */
public class SumFunction implements NumericFunction {

    private final List<NumericFunction> functions;

    /**
     * Constructs a sum function from the given list of functions.
     *
     * @param functions the list of functions
     */
    public SumFunction(List<NumericFunction> functions) {
        this.functions = functions;
    }

    @Override
    public double eval(double arg) {
        double result = 0.0;
        for (NumericFunction func : functions) {
            result += func.eval(arg);
        }
        return result;
    }

}
