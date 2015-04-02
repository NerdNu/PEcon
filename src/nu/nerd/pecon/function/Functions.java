package nu.nerd.pecon.function;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import nu.nerd.pecon.PEconPlugin;
import nu.nerd.pecon.config.BadConfigurationException;

import org.bukkit.configuration.ConfigurationSection;

/**
 * Provides {@link NumericFunction} utilities.
 */
public final class Functions {

    /**
     * Constant function that always evaluates to 0.
     */
    public static final NumericFunction ZERO = new ConstantFunction(0.0);
    /**
     * Constant function that always evaluates to 1.
     */
    public static final NumericFunction ONE = new ConstantFunction(1.0);
    /**
     * Function that evaluates to its argument.
     */
    public static final NumericFunction IDENTITY = new PolynomialFunction(0.0, 1.0);
    /**
     * Function that evaluates to its argument's multiplicative inverse.
     */
    public static final NumericFunction INVERSE = new PowerFunction(-1.0);

    /**
     * Constructs a new function from the given configuration section. The
     * section should have a <i>type</i> key and a <i>value</i> key. The
     * following values are valid for each function type:
     * <table>
     *   <thead>
     *     <tr><th>Key</th><th>Value</th></tr>
     *   </thead>
     *   <tbody>
     *     <tr><td>composite</td><td>function list</td></tr>
     *     <tr><td>constant</td><td>double</td></tr>
     *     <tr><td>exponential</td><td>double</td></tr>
     *     <tr><td>identity</td><td>none</td></tr>
     *     <tr><td>inverse</td><td>none</td></tr>
     *     <tr><td>logarithmic</td><td>double</td></tr>
     *     <tr><td>normal</td><td>double list</td></tr>
     *     <tr><td>polynomial</td><td>double list</td></tr>
     *     <tr><td>power</td><td>double</td></tr>
     *     <tr><td>product</td><td>function list</td></tr>
     *     <tr><td>sum</td><td>function list</td></tr>
     *   </tbody>
     * </table>
     *
     * @param config the configuration section
     * @return the function
     */
    public static NumericFunction fromConfig(ConfigurationSection config) {
        try {
            return fromMap(config.getValues(false));
        } catch (BadConfigurationException e) {
            PEconPlugin.getInstance().getLogger().warning(config.getCurrentPath() + ": "
                    + e.getMessage() + " Check config.yml.");
            return ZERO;
        }
    }

    /**
     * Constructs a new function from the given configuration section. The
     * section should have a <i>type</i> key and a <i>value</i> key.
     *
     * @param config the configuration section
     * @return the function
     * @throws BadConfigurationException the function is malformed
     * @see Functions#fromConfig(ConfigurationSection)
     */
    @SuppressWarnings("unchecked")
    public static NumericFunction fromMap(Map<String, Object> map) {
        if (!(map.get("type") instanceof String)) {
            return null;
        }

        Object args = map.get("args");
        try {
            switch (((String) map.get("type")).toLowerCase()) {
            case "composite":
                return new CompositeFunction(toFunctionList((List<Map<String, Object>>) args));
            case "constant":
                return new PolynomialFunction((double) args);
            case "exponential":
                return new ExponentialFunction((double) args);
            case "identity":
                return IDENTITY;
            case "inverse":
                return INVERSE;
            case "logarithmic":
                return new LogarithmicFunction((double) args);
            case "normal":
                return new NormalFunction((List<Double>) args);
            case "polynomial":
                return new PolynomialFunction((List<Double>) args);
            case "power":
                return new PowerFunction((double) args);
            case "product":
                return new ProductFunction(toFunctionList((List<Map<String, Object>>) args));
            case "sum":
                return new SumFunction(toFunctionList((List<Map<String, Object>>) args));
            default:
                throw new BadConfigurationException("Unknown function type " + map.get("type")
                        + ".");
            }
        } catch (ClassCastException e) {
            throw new BadConfigurationException("Bad arguments for function type "
                    + map.get("type") + ".");
        }
    }

    private static List<NumericFunction> toFunctionList(List<Map<String, Object>> maps) {
        List<NumericFunction> functions = new LinkedList<>();
        for (Map<String, Object> function : maps) {
            functions.add(fromMap(function));
        }
        return functions;
    }

}
