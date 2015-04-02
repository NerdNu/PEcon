package nu.nerd.pecon.function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Applies a double-to-double polynomial function to a number.
 */
public class PolynomialFunction implements NumericFunction {

    private final List<Double> coefficients;

    /**
     * Constructs a polynomial function with the given coefficients. The
     * coefficients should be in order of increasing degree (i.e. the first
     * element is the constant coefficient, etc.).
     *
     * @param coefficients the list of coefficients
     */
    public PolynomialFunction(List<Double> coefficients) {
        this.coefficients = new ArrayList<>(coefficients);
    }

    /**
     * Constructs a polynomial function with the given coefficients. The
     * coefficients should be in order of increasing degree (i.e. the first
     * element is the constant coefficient, etc.).
     *
     * @param coefficients the coefficients
     */
    public PolynomialFunction(Double... coefficients) {
        this.coefficients = Arrays.asList(coefficients);
    }

    @Override
    public double eval(double arg) {
        double base = 1.0;
        double result = 0.0;
        for (double coefficient : coefficients) {
            result += coefficient * base;
            base *= arg;
        }
        return result;
    }

}
