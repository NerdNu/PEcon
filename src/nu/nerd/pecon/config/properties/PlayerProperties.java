package nu.nerd.pecon.config.properties;

import nu.nerd.pecon.PEconPlugin;
import nu.nerd.pecon.config.Configuration;
import org.bukkit.configuration.ConfigurationSection;

/**
 * The plugin's player properties.
 */
public class PlayerProperties {

    private final double sourceRatio;
    private final boolean deferToServer;
    private final CooldownProperties cooldown;

    /**
     * Constructs a {@code PlayerProperties} from the given configuration
     * section. This should contain a double-valued <i>player-source</i> entry,
     * a boolean-valued <i>defer-to-server</i> entry, and a
     * <i>{@link CooldownProperties cooldown}</i> entry.
     *
     * @param config the configuration section
     */
    public PlayerProperties(ConfigurationSection config) {
        double sr = Configuration.getDouble(config, "source-ratio", 0.0);
        if (sr < 0.0) {
            PEconPlugin.getInstance().getLogger().warning(config.getCurrentPath()
                    + ".source-ratio must be >= 0.");
            sr = 0.0;
        } else if (sr > 1.0) {
            PEconPlugin.getInstance().getLogger().warning(config.getCurrentPath()
                    + ".source-ratio must be <= 1.");
            sr = 0.0;
        }
        sourceRatio = sr;
        deferToServer = Configuration.getBoolean(config, "defer-to-server", false);
        cooldown = new CooldownProperties(Configuration.getConfigurationSection(config, "cooldown",
                null));
    }

    /**
     * Gets the ratio of reward money that should be taken from a player when
     * they are killed.
     *
     * @return the ratio of money taken from the player
     */
    public double getSourceRatio() {
        return sourceRatio;
    }

    /**
     * Gets whether the player's killer should receive a reward from the server
     * instead if the killed player goes bankrupt.
     *
     * @return whether the server should provide missing rewards
     */
    public boolean getDeferToServer() {
        return deferToServer;
    }

    /**
     * Gets the cooldown properties.
     *
     * @return the cooldown properties
     */
    public CooldownProperties getCooldown() {
        return cooldown;
    }

}
