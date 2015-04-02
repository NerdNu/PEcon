package nu.nerd.pecon.config.valuation.player;

import java.util.Map;

import nu.nerd.pecon.config.Configuration;
import nu.nerd.pecon.config.valuation.CurvedValuator;
import nu.nerd.pecon.config.valuation.Valuator;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

/**
 * Valuates a player's set of armor.
 */
public class ArmorValuator extends CurvedValuator<EntityEquipment> {

    private final Valuator<Material> typeValuator;
    private final Valuator<Material> materialValuator;
    private final Valuator<Map<Enchantment, Integer>> enchantmentValuator;
    private final Valuator<ItemStack> damageValuator;

    /**
     * Constructs an {@code ArmorValuator} from the given configuration
     * section. This may contain the <i>{@link ArmorTypeValuator type}</i>,
     * <i>{@link ArmorMaterialValuator material}</i>,
     * <i>{@link ArmorEnchantmentValuator enchantment}</i>, and
     * <i>{@link ArmorDamageValuator damage}</i> keys and evaluates to
     * {@code type * material * enchantment + material * damage}. Any of these
     * values which are not present will default to 1.
     *
     * @param config the configuration section
     */
    public ArmorValuator(ConfigurationSection config) {
        super(config);
        typeValuator = new ArmorTypeValuator(Configuration.getConfigurationSection(config, "type",
                null));
        materialValuator = new ArmorMaterialValuator(Configuration.getConfigurationSection(config,
                "material", null));
        enchantmentValuator = new ArmorEnchantmentValuator(Configuration.getConfigurationSection(
                config, "enchantment", null));
        damageValuator = new ArmorDamageValuator(Configuration.getConfigurationSection(config,
                "damage", null));
    }

    @Override
    protected double baseValue(EntityEquipment input) {
        return valuateItem(input.getBoots()) + valuateItem(input.getLeggings())
                + valuateItem(input.getChestplate()) + valuateItem(input.getHelmet());
    }

    private double valuateItem(ItemStack item) {
        if (item == null) {
            return 0.0;
        }

        double type = typeValuator.valuate(item.getType());
        double material = materialValuator.valuate(item.getType());
        double enchantment = enchantmentValuator.valuate(item.getEnchantments());
        double damage = damageValuator.valuate(item);

        return type * material * enchantment + damage * material;
    }

}
