package nu.nerd.pecon;

import java.util.logging.Level;

import net.milkbowl.vault.economy.Economy;
import nu.nerd.pecon.config.Configuration;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The PEcon (PvP Economy) plugin.
 *
 * @author Dumbo52
 */
public class PEconPlugin extends JavaPlugin {

    private static PEconPlugin instance;

    private Configuration config;
    private EconomyManager economy;
    private CooldownList cooldowns;

    @Override
    public void onEnable() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager()
                .getRegistration(Economy.class);
        if (economyProvider == null) {
            getLogger().log(Level.SEVERE, "No economy plugin loaded; disabling.");
            this.getPluginLoader().disablePlugin(this);
            return;
        }

        instance = this;
        saveDefaultConfig();
        loadConfiguration();
        economy = new EconomyManager(economyProvider.getProvider());
        cooldowns = new CooldownList();
        getServer().getPluginManager().registerEvents(new PEconListener(), this);
    }

    @Override
    public void onDisable() {
        if (cooldowns != null) {
            cooldowns.save();
        }
        instance = null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("pecon")) {
            if (args.length == 1 && args[0].equalsIgnoreCase("reload")
                    && sender.hasPermission("pecon.reload")) {
                loadConfiguration();
                sender.sendMessage("Configuration reloaded.");
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the static plugin instance.
     *
     * @return the plugin instance
     */
    public static PEconPlugin getInstance() {
        return instance;
    }

    /**
     * Reloads the plugin's configuration.
     */
    public void loadConfiguration() {
        reloadConfig();
        config = new Configuration(getConfig());
    }

    /**
     * Gets the plugin's configuration.
     *
     * @return the configuration
     */
    public Configuration getPluginConfiguration() {
        return config;
    }

    /**
     * Gets the plugin's economy manager.
     *
     * @return the economy manager
     */
    public EconomyManager getEconomyManager() {
        return economy;
    }

    /**
     * Gets the plugin's cooldown list.
     *
     * @return the cooldown list
     */
    public CooldownList getCooldownList() {
        return cooldowns;
    }

}
