package nu.nerd.pecon.config.valuation.player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nu.nerd.pecon.config.Configuration;
import nu.nerd.pecon.config.valuation.CurvedValuator;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Valuates a piece of armor based on its type.
 */
public class ArmorTypeValuator extends CurvedValuator<Material> {

    private final Map<ArmorType, Double> valueMap;

    /**
     * Constructs an {@code ArmorTypeValuator} from the given configuration
     * section. This should contain only the double-value keys <i>boots</i>,
     * <i>leggings</i>, <i>chestplate</i>, and <i>helmet</i>, each of which
     * supplies a double-type valuation for that armor type.
     *
     * @param config the configuration section
     */
    public ArmorTypeValuator(ConfigurationSection config) {
        super(config);
        valueMap = new HashMap<>();
        if (config != null) {
            for (String key : config.getKeys(false)) {
                ArmorType type = ArmorType.valueOf(key.toUpperCase());
                if (type != null) {
                    valueMap.put(type, Configuration.getDouble(config, key, 0.0));
                }
            }
        }
    }

    @Override
    protected double baseValue(Material input) {
        for (Entry<ArmorType, Double> piece : valueMap.entrySet()) {
            if (piece.getKey().isType(input)) {
                return piece.getValue();
            }
        }
        return 0.0;
    }

    private static enum ArmorType {
        BOOTS(Material.LEATHER_BOOTS, Material.CHAINMAIL_BOOTS, Material.GOLD_BOOTS,
                Material.IRON_BOOTS, Material.DIAMOND_BOOTS),
        LEGGINGS(Material.LEATHER_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.GOLD_LEGGINGS,
                Material.IRON_LEGGINGS, Material.DIAMOND_LEGGINGS),
        CHESTPLATE(Material.LEATHER_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE,
                Material.GOLD_CHESTPLATE, Material.IRON_CHESTPLATE, Material.DIAMOND_CHESTPLATE),
        HELMET(Material.LEATHER_HELMET, Material.CHAINMAIL_HELMET, Material.GOLD_HELMET,
                Material.IRON_HELMET, Material.DIAMOND_HELMET);

        private final List<Material> materials;

        private ArmorType(Material... materials) {
            this.materials = Arrays.asList(materials);
        }

        public boolean isType(Material mat) {
            return materials.contains(mat);
        }
    }

}
