package nu.nerd.pecon;

import java.util.logging.Level;

import net.milkbowl.vault.economy.Economy;
import nu.nerd.pecon.config.Configuration;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.mewin.WGCustomFlags.WGCustomFlagsPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;

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
    private WorldGuardPlugin worldguard;
    private StateFlag rewardsFlag;

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

        PluginManager pm = getServer().getPluginManager();
        worldguard = (WorldGuardPlugin) pm.getPlugin("WorldGuard");
        WGCustomFlagsPlugin flags = (WGCustomFlagsPlugin) pm.getPlugin("WGCustomFlags");
        if (flags != null) {
            rewardsFlag = new StateFlag("pecon-rewards", true);
            flags.addCustomFlag(rewardsFlag);
        }

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
        if (command.getName().equalsIgnoreCase("pecon") && sender.hasPermission("pecon.admin")) {
            if (args.length == 1 && args[0].equalsIgnoreCase("reload")
                    && sender.hasPermission("pecon.admin")) {
                loadConfiguration();
                sender.sendMessage("Configuration reloaded.");
                return true;
            }
            if (args.length == 1 && args[0].equalsIgnoreCase("reset-cooldown")
                    && sender.hasPermission("pecon.admin")) {
                cooldowns.clear();
                return true;
            }
            sender.sendMessage("/pecon reload - reloads the plugin configuration");
            sender.sendMessage("/pecon reset-cooldown - resets all players' PvP cooldowns");
            return true;
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

    /**
     * Gets whether a reward can be given for a death in the given location.
     *
     * @param loc the location
     * @return whether rewards are allowed
     */
    public boolean allowsRewards(Location loc) {
        if (worldguard != null && rewardsFlag != null) {
            ApplicableRegionSet regions = worldguard.getRegionContainer().get(loc.getWorld()).getApplicableRegions(loc);
            return regions.testState(null, rewardsFlag);
        }
        return true;
    }

}
