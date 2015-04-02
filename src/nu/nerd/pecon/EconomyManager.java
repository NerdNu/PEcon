package nu.nerd.pecon;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import net.milkbowl.vault.economy.Economy;
import nu.nerd.pecon.config.properties.Properties;

/**
 * Manages economy-related functions.
 */
public class EconomyManager {

    private Economy econ;

    /**
     * Constructs a new economy manager with the given {@link Economy}.
     *
     * @param econ the economy
     */
    public EconomyManager(Economy econ) {
        this.econ = econ;
    }

    /**
     * Rewards the given player with the given amount of money.
     *
     * @param player the player
     * @param amount the amount of money
     * @param victim the entity that was killed
     */
    public void rewardPlayer(OfflinePlayer player, double amount, LivingEntity victim) {
        if (amount <= 0.0) {
            return;
        }

        econ.depositPlayer(player, amount);

        Properties properties = PEconPlugin.getInstance().getPluginConfiguration().getProperties();
        if (properties.getLogRewards()) {
            PEconPlugin.getInstance().getLogger().info(player.getName() + " gained "
                    + properties.formatMoney(amount) + " from killing " + victim.getName() + ".");
        }
        if (properties.getNotifyRewards() && player.isOnline()) {
            ((Player) player).sendMessage(ChatColor.LIGHT_PURPLE + "You gained "
                    + properties.formatMoney(amount) + " from killing " + victim.getName() + ".");
        }
    }

    /**
     * Rewards the given players with the given amount of money, taken
     * taken partially from the given entity.
     *
     * @param player the players
     * @param amount the amount of money
     * @param source the entity that was killed.
     */
    public void rewardPlayers(Collection<DamageEntry> players, double amount,
            LivingEntity source) {
        if (amount <= 0.0) {
            return;
        }
        if (players.isEmpty()) {
            return;
        }

        Properties properties = PEconPlugin.getInstance().getPluginConfiguration().getProperties();
        CooldownList cooldown = PEconPlugin.getInstance().getCooldownList();

        double totalDamage = 0.0;
        for (DamageEntry entry : players) {
            totalDamage += entry.getDamage();
        }

        double given = 0.0;
        Map<OfflinePlayer, Double> distribution = new HashMap<>();
        if (source instanceof Player) {
            for (DamageEntry entry : players) {
                double modAmount = cooldown.renewKill(entry.getPlayer(), (OfflinePlayer) source,
                        entry.getDamage() * amount / totalDamage);
                given += modAmount;
                distribution.put(entry.getPlayer(), modAmount);
            }
        } else {
            given = amount;
            for (DamageEntry entry : players) {
                distribution.put(entry.getPlayer(), entry.getDamage() * amount / totalDamage);
            }
        }

        double ratio = 1.0;
        if (source instanceof OfflinePlayer) {
            double taken;
            OfflinePlayer sourcePlayer = (OfflinePlayer) source;
            double playerAmount = given * properties.getPlayerProperties().getSourceRatio();
            if (econ.has(sourcePlayer, playerAmount)) {
                taken = playerAmount;
            } else if (properties.getPlayerProperties().getDeferToServer()) {
                taken = econ.getBalance(sourcePlayer);
            } else {
                taken = econ.getBalance(sourcePlayer);
                ratio = taken / given;
            }
            if (taken > 0.0) {
                econ.withdrawPlayer(sourcePlayer, taken);
                if (properties.getLogRewards()) {
                    PEconPlugin.getInstance().getLogger().info(sourcePlayer + " died and lost "
                            + properties.formatMoney(taken) + ".");
                }
                if (properties.getNotifyRewards()) {
                    if (sourcePlayer.isOnline()) {
                        ((Player) sourcePlayer).sendMessage(ChatColor.LIGHT_PURPLE
                                + "Oh no! You lost " + properties.formatMoney(taken) + ".");
                    }
                }
            }
        }

        if (given > 0.0) {
            for (Entry<OfflinePlayer, Double> entry : distribution.entrySet()) {
                rewardPlayer(entry.getKey(), entry.getValue() * ratio, source);
            }
        }
    }

}
