package nu.nerd.pecon.config.valuation;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

/**
 * Valuates an enum constant property of an object.
 *
 * @param <T> the enum class
 * @param <U> the class from which the enum can be accessed
 */
public abstract class EnumValuator<T extends Enum<T>, U> implements Valuator<U> {

    private final Map<String, Double> valueMap;

    /**
     * Constructs an {@code EnumValuator} from the given configuration section.
     * The section may contain any double-valued keys with the same names as
     * any of the enum constants. Any enum constants without an entry will be
     * valuated at 0.
     *
     * @param config the configuration section
     */
    public EnumValuator(ConfigurationSection config) {
        valueMap = new HashMap<>();
        for (String key : config.getKeys(false)) {
            if (config.isDouble(key)) {
                valueMap.put(key.toUpperCase(), config.getDouble(key));
            }
        }
    }

    @Override
    public double valuate(U input) {
        Double value = valueMap.get(getEnum(input).toString());
        return value == null ? 0 : value;
    }

    protected abstract T getEnum(U object);

}
