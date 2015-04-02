package nu.nerd.pecon.config.valuation.player;

import nu.nerd.pecon.config.Configuration;
import nu.nerd.pecon.config.valuation.CurvedValuator;
import nu.nerd.pecon.config.valuation.Valuator;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;

/**
 * Valuates a player.
 */
public class PlayerValuator extends CurvedValuator<Player> {

    private final Valuator<EntityEquipment> armor;

    /**
     * Constructs a {@code PlayerValuator} from the given configuration
     * section. This should contain an <i>{@link ArmorValuator armor}</i> key,
     * which determines the value of the player. If this key is not present,
     * players will be valued at 0.
     *
     * @param config the configuration section
     */
    public PlayerValuator(ConfigurationSection config) {
        super(config);
        armor = new ArmorValuator(Configuration.getConfigurationSection(config, "armor", null));
    }

    @Override
    protected double baseValue(Player input) {
        return armor.valuate(input.getEquipment());
    }

}
