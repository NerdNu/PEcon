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
 * Valuates a piece of armor based on its material.
 */
public class ArmorMaterialValuator extends CurvedValuator<Material> {

    private final Map<ArmorMaterial, Double> valueMap;

    /**
     * Constructs an {@code ArmorMaterialValuator} from the given configuration
     * section. This should contain only the double-value keys <i>leather</i>,
     * <i>chainmail</i>, <i>gold</i>, <i>iron</i>, and <i>diamond</i>, each of
     * which supplies a double-type valuation for that armor material.
     *
     * @param config the configuration section
     */
    public ArmorMaterialValuator(ConfigurationSection config) {
        super(config);
        valueMap = new HashMap<>();
        if (config != null) {
            for (String key : config.getKeys(false)) {
                ArmorMaterial material = ArmorMaterial.valueOf(key.toUpperCase());
                if (material != null) {
                    valueMap.put(material, Configuration.getDouble(config, key, 0.0));
                }
            }
        }
    }

    @Override
    protected double baseValue(Material input) {
        for (Entry<ArmorMaterial, Double> piece : valueMap.entrySet()) {
            if (piece.getKey().isMaterial(input)) {
                return piece.getValue();
            }
        }
        return 0.0;
    }

    private static enum ArmorMaterial {
        LEATHER(Material.LEATHER_BOOTS, Material.LEATHER_LEGGINGS, Material.LEATHER_CHESTPLATE,
                Material.LEATHER_HELMET),
        CHAINMAIL(Material.CHAINMAIL_BOOTS, Material.CHAINMAIL_LEGGINGS,
                Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_HELMET),
        GOLD(Material.GOLD_BOOTS, Material.GOLD_LEGGINGS, Material.GOLD_CHESTPLATE,
                Material.GOLD_HELMET),
        IRON(Material.IRON_BOOTS, Material.IRON_LEGGINGS, Material.IRON_CHESTPLATE,
                Material.IRON_HELMET),
        DIAMOND(Material.DIAMOND_BOOTS, Material.DIAMOND_LEGGINGS, Material.DIAMOND_CHESTPLATE,
                Material.DIAMOND_HELMET);

        private final List<Material> materials;

        private ArmorMaterial(Material... materials) {
            this.materials = Arrays.asList(materials);
        }

        public boolean isMaterial(Material mat) {
            return materials.contains(mat);
        }
    }

}
