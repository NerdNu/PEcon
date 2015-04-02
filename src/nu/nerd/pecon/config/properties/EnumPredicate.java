package nu.nerd.pecon.config.properties;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

/**
 * Tests a constant enum property of an object.
 *
 * @param <T> the enum class
 * @param <U> the class from which the enum can be accessed
 */
public abstract class EnumPredicate<T extends Enum<T>, U> implements Predicate<U> {

    private final Map<String, Boolean> valueMap;

    /**
     * Constructs an {@code EnumPredicate} from the given configuration
     * section. The section may contain any boolean-valued keys with the same
     * names as any of the enum constants. Any enum constants without an entry
     * will produce false.
     *
     * @param config the configuration section
     */
    public EnumPredicate(ConfigurationSection config) {
        valueMap = new HashMap<>();
        for (String key : config.getKeys(false)) {
            if (config.isBoolean(key)) {
                valueMap.put(key.toUpperCase(), config.getBoolean(key));
            }
        }
    }

    @Override
    public boolean test(U input) {
        Boolean value = valueMap.get(getEnum(input).toString());
        return value != null && value;
    }

    protected abstract T getEnum(U object);

}
