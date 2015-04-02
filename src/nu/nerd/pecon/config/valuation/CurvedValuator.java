package nu.nerd.pecon.config.valuation;

import org.bukkit.configuration.ConfigurationSection;

import nu.nerd.pecon.function.Functions;
import nu.nerd.pecon.function.NumericFunction;

/**
 * Applies a curve to a numeric valuation of an object. The curve function to
 * apply to the value should be in a <i>curve</i> section of the configuration
 * and should conform to {@link
 * nu.nerd.pecon.function.Functions#fromConfig(ConfigurationSection) this}
 * format. If this section does not exist, no curve function is applied.
 *
 * @param <T> the type of object to valuate
 */
public abstract class CurvedValuator<T> implements Valuator<T> {

    private final NumericFunction curve;

    protected CurvedValuator(ConfigurationSection config) {
        if (config != null && config.isConfigurationSection("curve")) {
            curve = Functions.fromConfig(config.getConfigurationSection("curve"));
        } else {
            curve = null;
        }
    }

    @Override
    public final double valuate(T input) {
        if (curve == null) {
            return baseValue(input);
        } else {
            return curve.eval(baseValue(input));
        }
    }

    protected abstract double baseValue(T input);

}
