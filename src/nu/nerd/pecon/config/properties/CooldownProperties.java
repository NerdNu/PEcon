package nu.nerd.pecon.config.properties;

import org.bukkit.configuration.ConfigurationSection;

import nu.nerd.pecon.config.Configuration;
import nu.nerd.pecon.function.Functions;
import nu.nerd.pecon.function.NumericFunction;

/**
 * The plugin's cooldown properties.
 */
public class CooldownProperties {

    private final int time;
    private final NumericFunction function;

    /**
     * Constructs a {@code CooldownProperties} from the given configuration
     * section. This should contain an integer-valued <i>time</i> entry and a
     * <i>{@link NumericFunction cooldown-function}</i> entry.
     *
     * @param config the configuration section
     */
    public CooldownProperties(ConfigurationSection config) {
        time = Configuration.getInt(config, "time", 30);
        if (config.isConfigurationSection("cooldown-function")) {
            function = Functions.fromConfig(config.getConfigurationSection("cooldown-function"));
        } else {
            function = Functions.ZERO;
        }
    }

    /**
     * Gets the cooldown time.
     *
     * @return the cooldown time
     */
    public int getTime() {
        return time;
    }

    /**
     * Gets the cooldown function.
     *
     * @return the cooldown function
     */
    public NumericFunction getFunction() {
        return function;
    }

}
