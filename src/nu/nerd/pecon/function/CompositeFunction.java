package nu.nerd.pecon.function;

import java.util.List;

/**
 * Applies a list of functions to a number, applying the result of one to the
 * next.
 */
public class CompositeFunction implements NumericFunction {

    private final List<NumericFunction> functions;

    /**
     * Constructs a composite function from the given list of functions. The
     * functions are applied in the order in which they were given, so the
     * first element of <i>functions</i> will be passed the argument to the
     * composite function, while the result of this function will be passed to
     * the second element, etc.
     *
     * @param functions the list of functions
     */
    public CompositeFunction(List<NumericFunction> functions) {
        this.functions = functions;
    }

    @Override
    public double eval(double arg) {
        for (NumericFunction function : functions) {
            arg = function.eval(arg);
        }
        return arg;
    }

}
