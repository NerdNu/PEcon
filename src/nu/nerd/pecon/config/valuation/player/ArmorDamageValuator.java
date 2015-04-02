package nu.nerd.pecon.config.valuation.player;

import nu.nerd.pecon.config.valuation.CurvedValuator;
import nu.nerd.pecon.function.Functions;
import nu.nerd.pecon.function.NumericFunction;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

/**
 * Valuates a piece of armor based on its damage.
 */
public class ArmorDamageValuator extends CurvedValuator<ItemStack> {

    private final NumericFunction damageFunction;

    /**
     * Constructs an {@code ArmorDamageValuator} from the given configuration
     * section. This should contain the <i>damage-function</i> key, which
     * specifies a function to which the armor's damage value should be passed.
     * The damage value is passed as a ratio of damage to maximum damage the
     * piece of armor can withstand, so a new piece of armor will cause 0 to be
     * passed to the damage function, while an almost broken piece of armor
     * will pass it a number close to 1. If no function is provided, 0 is
     * always returned.
     *
     * @param config the configuration section
     */
    public ArmorDamageValuator(ConfigurationSection config) {
        super(config);
        if (config != null && config.isConfigurationSection("damage-function")) {
            damageFunction = Functions.fromConfig(
                    config.getConfigurationSection("damage-function"));
        } else {
            damageFunction = Functions.ZERO;
        }
    }

    @Override
    protected double baseValue(ItemStack input) {
        double damage = ((double) input.getDurability())
                / ((double) input.getType().getMaxDurability());
        return damageFunction.eval(damage);
    }

}
