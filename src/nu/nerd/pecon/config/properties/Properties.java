package nu.nerd.pecon.config.properties;

import nu.nerd.pecon.config.Configuration;

import org.bukkit.configuration.ConfigurationSection;

/**
 * The plugin's properties.
 */
public class Properties {

    private final int damageTimeout;
    private final boolean renewDamage;
    private final boolean logRewards;
    private final boolean notifyRewards;
    private final String moneyFormat;
    private final PlayerProperties playerProperties;
    private final MobProperties mobProperties;

    /**
     * Constructs a {@code Properties} from the given configuration section.
     * This should contain an integer-valued <i>damage-timeout</i> entry, a
     * boolean-valued <i>renew-damage</i> entry, a boolean-valued
     * <i>log-rewards</i> entry, a boolean-valued <i>notify-rewards</i> entry,
     * a string-valued <i>money-format</i> entry, a
     * <i>{@link PlayerProperties players}</i> entry, and a
     * <i>{@link MobProperties mobs}</i> entry.
     *
     * @param config the configuration section
     */
    public Properties(ConfigurationSection config) {
        damageTimeout = Configuration.getInt(config, "damage-timeout", 30);
        renewDamage = Configuration.getBoolean(config, "renew-damage", true);
        logRewards = Configuration.getBoolean(config, "log-rewards", true);
        notifyRewards = Configuration.getBoolean(config, "notify-rewards", true);
        moneyFormat = Configuration.getString(config, "money-format", "%f");
        playerProperties = new PlayerProperties(Configuration.getConfigurationSection(config,
                "players", null));
        mobProperties = new MobProperties(Configuration.getConfigurationSection(config, "mobs",
                null));
    }

    /**
     * Gets the damage timeout.
     *
     * @return the damage timeout
     */
    public int getDamageTimeout() {
        return damageTimeout;
    }

    /**
     * Gets whether the damage timeout should be renewed after each subsequent
     * damage to the entity.
     *
     * @return whether damage should be renewed
     */
    public boolean getRenewDamage() {
        return renewDamage;
    }

    /**
     * Gets whether rewards given to players should be logged to console.
     *
     * @return whether rewards should be logged
     */
    public boolean getLogRewards() {
        return logRewards;
    }

    /**
     * Gets whether players should be notified when they are rewarded.
     *
     * @return whether players should be notified of rewards
     */
    public boolean getNotifyRewards() {
        return notifyRewards;
    }

    public String formatMoney(double amount) {
        return String.format(moneyFormat, amount);
    }

    /**
     * Gets the player properties.
     *
     * @return the player properties
     */
    public PlayerProperties getPlayerProperties() {
        return playerProperties;
    }

    /**
     * Gets the mob properties.
     *
     * @return the mob properties
     */
    public MobProperties getMobProperties() {
        return mobProperties;
    }

}
