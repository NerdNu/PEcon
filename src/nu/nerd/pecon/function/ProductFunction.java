package nu.nerd.pecon.function;

import java.util.List;

/**
 * A double-to-double function that multiplies a list of functions, each
 * evaluated at the same input.
 */
public class ProductFunction implements NumericFunction {

    private final List<NumericFunction> functions;

    /**
     * Constructs a product function from the given list of functions.
     *
     * @param functions the list of functions
     */
    public ProductFunction(List<NumericFunction> functions) {
        this.functions = functions;
    }

    @Override
    public double eval(double arg) {
        double result = 1.0;
        for (NumericFunction func : functions) {
            result *= func.eval(arg);
        }
        return result;
    }

}
