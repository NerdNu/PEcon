package nu.nerd.pecon.config.valuation;

import nu.nerd.pecon.config.valuation.mob.MobValuator;
import nu.nerd.pecon.config.valuation.player.PlayerValuator;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

/**
 * Valuates a living entity.
 */
public class EntityValuator extends CurvedValuator<LivingEntity> {

    private final Valuator<Player> playerValuator;
    private final Valuator<LivingEntity> mobValuator;

    /**
     * Constructs an {@code EntityValuator} from the given configuration
     * section. This should contain a <i>{@link PlayerValuator players}</i>
     * entry and a <i>{@link MobValuator mobs}</i> entry.
     *
     * @param config the configuration section
     */
    public EntityValuator(ConfigurationSection config) {
        super(config);
        if (config.isConfigurationSection("players")) {
            playerValuator = new PlayerValuator(config.getConfigurationSection("players"));
        } else {
            playerValuator = new ConstantValuator<>(0.0);
        }
        if (config.isConfigurationSection("mobs")) {
            mobValuator = new MobValuator(config.getConfigurationSection("mobs"));
        } else {
            mobValuator = new ConstantValuator<>(0.0);
        }
    }

    @Override
    protected double baseValue(LivingEntity input) {
        if (input instanceof Player) {
            return playerValuator.valuate((Player) input);
        } else {
            return mobValuator.valuate(input);
        }
    }

}
