package nu.nerd.pecon.config.valuation.player;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import nu.nerd.pecon.config.valuation.CurvedValuator;
import nu.nerd.pecon.config.valuation.Valuator;
import nu.nerd.pecon.function.ConstantFunction;
import nu.nerd.pecon.function.Functions;
import nu.nerd.pecon.function.NumericFunction;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;

/**
 * Valuates a piece of armor based on its material.
 */
public class ArmorEnchantmentValuator extends CurvedValuator<Map<Enchantment, Integer>> {

    private final Map<Enchantment, LevelData> valueMap;
    private final double defaultValue;

    /**
     * Constructs an {@code ArmorEnchantmentValuator} from the given
     * configuration section. This should contain keys with lower-case Bukkit
     * enchantment names. Each of these entries can contain any number of keys
     * with integer names and double values, each of which indicates the value
     * of an enchantment at that level. If an enchantment value is not
     * explicitly defined at a certain level, then a given function with key
     * <i>level-function</i> may be evaluated at that level to produce the
     * value of the enchantment. If neither a level value nor a level function
     * are defined, the default enchantment value is used.
     * <p>
     * The configuration section may contain a <i>default</i> key with double
     * value, onto which all enchantment values are added. If this key is not
     * present, the value defaults to 0.
     *
     * @param config the configuration section
     * @see org.bukkit.enchantments.Enchantment
     */
    public ArmorEnchantmentValuator(ConfigurationSection config) {
        super(config);
        valueMap = new HashMap<>();
        defaultValue = config.getDouble("default", 0.0);
        if (config != null) {
            for (String key : config.getKeys(false)) {
                Enchantment enchant = Enchantment.getByName(key.toUpperCase());
                if (enchant != null) {
                    valueMap.put(enchant, new LevelData(config.getConfigurationSection(key),
                            defaultValue));
                }
            }
        }
    }

    @Override
    protected double baseValue(Map<Enchantment, Integer> input) {
        double sum = defaultValue;
        for (Entry<Enchantment, Integer> enchant : input.entrySet()) {
            if (valueMap.containsKey(enchant.getKey())) {
                sum += valueMap.get(enchant.getKey()).valuate(enchant.getValue());
            }
        }
        return sum;
    }

    private static class LevelData implements Valuator<Integer> {

        private Map<Integer, Double> levels;
        private NumericFunction levelFunction;

        public LevelData(ConfigurationSection config, double defaultValue) {
            levels = new HashMap<>();
            for (String key : config.getKeys(false)) {
                try {
                    int level = Integer.parseInt(key);
                    double value = config.getDouble(key);
                    levels.put(level, value);
                } catch (NumberFormatException e) {
                    // Expected, carry on.
                }
            }
            if (config.isConfigurationSection("level-function")) {
                levelFunction = Functions.fromConfig(
                        config.getConfigurationSection("level-function"));
            } else {
                levelFunction = new ConstantFunction(defaultValue);
            }
        }

        @Override
        public double valuate(Integer input) {
            if (levels.containsKey(input)) {
                return levels.get(input);
            } else {
                return levelFunction.eval(input);
            }
        }

    }

}
