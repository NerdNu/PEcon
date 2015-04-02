package nu.nerd.pecon.function;

import java.util.List;
import java.util.Random;

/**
 * Randomly selects a value according to a normal distribution.
 */
public class NormalFunction implements NumericFunction {

    private final Random rand;
    private final double mean;
    private final double sd;

    /**
     * Constructs a new normal distribution function from the given list of
     * arguments. The first element of the list is interpreted to be the
     * distribution's standard deviation, while the second element is the mean.
     * Any further arguments are ignored. If either the first or second
     * elements of the argument list do not exist, then 0 and 1 are used for
     * the mean and standard deviation, respectively.
     *
     * @param args the argument list
     */
    public NormalFunction(List<Double> args) {
        rand = new Random();

        if (args.size() > 0) {
            sd = args.get(0);
        } else {
            sd = 1.0;
        }

        if (args.size() > 1) {
            mean = args.get(1);
        } else {
            mean = 1.0;
        }
    }

    @Override
    public double eval(double arg) {
        return rand.nextGaussian() * sd + mean;
    }

}
