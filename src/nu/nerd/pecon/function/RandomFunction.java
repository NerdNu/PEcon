package nu.nerd.pecon.function;

import java.util.List;
import java.util.Random;

import nu.nerd.pecon.config.BadConfigurationException;

/**
 * Randomly selects a value according to a bounded uniform distribution.
 */
public class RandomFunction implements NumericFunction {

    private final Random rand;
    private final double min;
    private final double range;

    /**
     * Constructs a new random uniform distribution function from the given
     * list of arguments. The first element of the list is interpreted to be
     * the distribution's lower bound, while the second element is the upper
     * bound. Any further arguments are ignored.
     *
     * @param args the argument list
     * @throws BadConfigurationException the argument list is too small
     */
    public RandomFunction(List<Double> args) {
        if (args.size() < 2) {
            throw new BadConfigurationException("Not enough arguments for function type random.");
        }
        rand = new Random();
        min = args.get(0);
        range = args.get(1) - min;
    }

    @Override
    public double eval(double arg) {
        return rand.nextDouble() * range + min;
    }

}
