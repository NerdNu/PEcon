package nu.nerd.pecon.config;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;

import nu.nerd.pecon.PEconPlugin;
import nu.nerd.pecon.config.properties.Properties;
import nu.nerd.pecon.config.valuation.ConstantValuator;
import nu.nerd.pecon.config.valuation.EntityValuator;
import nu.nerd.pecon.config.valuation.Valuator;
import nu.nerd.pecon.config.valuation.mob.MobValuator;

/**
 * The plugin's configuration data.
 */
public class Configuration {

    private final Properties properties;
    private final Valuator<LivingEntity> values;
    private final Valuator<LivingEntity> chances;

    /**
     * Constructs a {@code Configuration} from the given configuration section.
     * This should contain <i>{@link Properties properties}</i>,
     * <i>{@link EntityValuator players}</i>, and
     * <i>{@link EntityValuator mobs}</i> entries.
     *
     * @param config the configuration section
     */
    public Configuration(ConfigurationSection config) {
        properties = new Properties(config.getConfigurationSection("properties"));

        if (config.isConfigurationSection("values")) {
            values = new EntityValuator(config.getConfigurationSection("values"));
        } else {
            values = new ConstantValuator<LivingEntity>(0.0);
        }
        if (config.isConfigurationSection("chances.mobs")) {
            chances = new MobValuator(config.getConfigurationSection("chances.mobs"));
        } else {
            chances = new ConstantValuator<LivingEntity>(0.0);
        }
    }

    /**
     * Gets the plugin's properties.
     *
     * @return the plugin's properties
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * Gets the drop values.
     *
     * @return the drop values
     */
    public Valuator<LivingEntity> getValues() {
        return values;
    }

    /**
     * Gets the drop chances.
     *
     * @return the drop chances
     */
    public Valuator<LivingEntity> getChances() {
        return chances;
    }

    /**
     * Produces a warning in console that the given key in the given
     * configuration context could not be parsed.
     *
     * @param context the configuration context
     * @param key the key
     */
    public static void reportProperty(ConfigurationSection context, String key) {
        PEconPlugin.getInstance().getLogger().warning(context.getCurrentPath() + ": " + key
                + " is either missing or malformed. Check config.yml.");
    }

    /**
     * Gets a boolean value from the given configuration section.
     *
     * @param config the configuration section
     * @param key the key
     * @param defaultValue the default value
     * @return the boolean value with the given key
     */
    public static boolean getBoolean(ConfigurationSection config, String key,
            boolean defaultValue) {
        if (config == null) {
            return defaultValue;
        }
        if (config.isBoolean(key)) {
            return config.getBoolean(key);
        }
        reportProperty(config, key);
        return defaultValue;
    }

    /**
     * Gets a configuration section value from the given configuration section.
     *
     * @param config the configuration section
     * @param key the key
     * @param defaultValue the default value
     * @return the configuration section value with the given key
     */
    public static ConfigurationSection getConfigurationSection(ConfigurationSection config,
            String key, ConfigurationSection defaultValue) {
        if (config == null) {
            return defaultValue;
        }
        if (config.isConfigurationSection(key)) {
            return config.getConfigurationSection(key);
        }
        reportProperty(config, key);
        return defaultValue;
    }

    /**
     * Gets a double value from the given configuration section.
     *
     * @param config the configuration section
     * @param key the key
     * @param defaultValue the default value
     * @return the double value with the given key
     */
    public static double getDouble(ConfigurationSection config, String key, double defaultValue) {
        if (config == null) {
            return defaultValue;
        }
        if (config.isDouble(key)) {
            return config.getDouble(key);
        }
        reportProperty(config, key);
        return defaultValue;
    }

    /**
     * Gets an int value from the given configuration section.
     *
     * @param config the configuration section
     * @param key the key
     * @param defaultValue the default value
     * @return the int value with the given key
     */
    public static int getInt(ConfigurationSection config, String key, int defaultValue) {
        if (config == null) {
            return defaultValue;
        }
        if (config.isInt(key)) {
            return config.getInt(key);
        }
        reportProperty(config, key);
        return defaultValue;
    }

    /**
     * Gets a list value from the given configuration section.
     *
     * @param config the configuration section
     * @param key the key
     * @param defaultValue the default value
     * @param <E> the type of element the list should contain
     * @return the list value with the given key
     */
    @SuppressWarnings("unchecked")
    public static <E> List<E> getList(ConfigurationSection config, String key,
            List<E> defaultValue) {
        if (config == null) {
            return defaultValue;
        }
        if (config.isList(key)) {
            try {
                return (List<E>) config.getList(key);
            } catch (ClassCastException e) {
                // Expected; continue and report the error.
            }
        }
        reportProperty(config, key);
        return defaultValue;
    }

    /**
     * Gets a long value from the given configuration section.
     *
     * @param config the configuration section
     * @param key the key
     * @param defaultValue the default value
     * @return the long value with the given key
     */
    public static long getLong(ConfigurationSection config, String key, long defaultValue) {
        if (config == null) {
            return defaultValue;
        }
        if (config.isLong(key)) {
            return config.getLong(key);
        }
        reportProperty(config, key);
        return defaultValue;
    }

    /**
     * Gets a string value from the given configuration section.
     *
     * @param config the configuration section
     * @param key the key
     * @param defaultValue the default value
     * @return the string value with the given key
     */
    public static String getString(ConfigurationSection config, String key, String defaultValue) {
        if (config == null) {
            return defaultValue;
        }
        if (config.isString(key)) {
            return config.getString(key);
        }
        reportProperty(config, key);
        return defaultValue;
    }

}
