package nu.nerd.pecon;

import java.util.HashMap;
import java.util.Map;

import nu.nerd.pecon.config.Configuration;
import nu.nerd.pecon.config.properties.Properties;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * The plugin's event listener.
 */
public class PEconListener implements Listener {

    @SuppressWarnings("unchecked")
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player damager = (Player) event.getDamager();
            Entity entity = event.getEntity();
            Properties properties = PEconPlugin.getInstance().getPluginConfiguration()
                    .getProperties();
            int timeout = properties.getDamageTimeout() * 20;
            long time = event.getEntity().getWorld().getFullTime();
            Map<String, DamageEntry> damagers;

            if (entity.hasMetadata("pecon.damagers")) {
                damagers = (Map<String, DamageEntry>) entity.getMetadata("pecon.damagers").get(0)
                        .value();
            } else {
                damagers = new HashMap<>();
            }

            // Remove expired damage entries
            if (entity.hasMetadata("pecon.lastdamage") && timeout >= 0) {
                if (time - entity.getMetadata("pecon.lastdamage").get(0).asLong() > timeout) {
                    damagers.clear();
                } else if (!properties.getRenewDamage()) {
                    for (String name : damagers.keySet()) {
                        if (time - damagers.get(name).getTime() > timeout) {
                            damagers.remove(name);
                        }
                    }
                }
            }

            if (damagers.containsKey(damager.getName())) {
                DamageEntry entry = damagers.get(damager.getName());
                if (countDamage(entity, entry.getTime(), time)) {
                    entry.setDamage(entry.getDamage() + event.getFinalDamage());
                } else {
                    entry.setDamage(event.getFinalDamage());
                }
                entry.setTime(time);
            } else {
                DamageEntry entry = new DamageEntry(damager, event.getFinalDamage(), time);
                damagers.put(damager.getName(), entry);
            }

            entity.setMetadata("pecon.damagers", new FixedMetadataValue(PEconPlugin.getInstance(),
                    damagers));
            entity.setMetadata("pecon.lastdamage",
                    new FixedMetadataValue(PEconPlugin.getInstance(), time));
        }
    }

    @SuppressWarnings("unchecked")
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Configuration config = PEconPlugin.getInstance().getPluginConfiguration();

        if (!entity.hasMetadata("pecon.lastdamage")) {
            return;
        }
        if (entity.getWorld().getFullTime()
                - entity.getMetadata("pecon.lastdamage").get(0).asLong()
                > PEconPlugin.getInstance().getPluginConfiguration().getProperties()
                .getDamageTimeout() * 20) {
            return;
        }
        if (!PEconPlugin.getInstance().allowsRewards(entity.getLocation())) {
            return;
        }
        if (!config.getProperties().getMobProperties().shouldReward(entity)) {
            return;
        }

        if (entity instanceof Player || Math.random() <= config.getChances().valuate(entity)) {
            double amount = config.getValues().valuate(entity);
            if (entity.hasMetadata("pecon.damagers")) {
                PEconPlugin.getInstance().getEconomyManager().rewardPlayers(
                        ((Map<String, DamageEntry>) entity.getMetadata("pecon.damagers").get(0)
                                .value()).values(), amount, entity);
                entity.removeMetadata("pecon.damagers", PEconPlugin.getInstance());
            }
            entity.removeMetadata("pecon.lastdamage", PEconPlugin.getInstance());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (PEconPlugin.getInstance().getPluginConfiguration().getProperties().getMobProperties()
                .getIgnoreSpawnerMobs()) {
            event.getEntity().setMetadata("pecon.fromspawner",
                    new FixedMetadataValue(PEconPlugin.getInstance(),
                            event.getSpawnReason() == SpawnReason.SPAWNER));
        }
    }

    private boolean countDamage(Entity entity, long damageTime, long compareTime) {
        Properties properties = PEconPlugin.getInstance().getPluginConfiguration().getProperties();
        int timeout = properties.getDamageTimeout() * 20;

        if (timeout < 0) {
            return true;
        }

        long lastDamage;
        if (properties.getRenewDamage()) {
            if (entity.hasMetadata("pecon.lastdamage")) {
                lastDamage = entity.getMetadata("pecon.lastdamage").get(0).asLong();
            } else {
                return false;
            }
        } else {
            lastDamage = damageTime;
        }

        return compareTime - lastDamage <= timeout;
    }

}
