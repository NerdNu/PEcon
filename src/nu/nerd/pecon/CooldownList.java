package nu.nerd.pecon;

import java.io.File;
import java.io.IOException;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import nu.nerd.pecon.config.properties.CooldownProperties;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * A list of cooldown entries.
 */
public class CooldownList {

    // This should always be sorted by time.
    private Deque<CooldownEntry> cooldowns;

    /**
     * Constructs a new cooldown list from cooldown.yml, if it exists.
     */
    public CooldownList() {
        cooldowns = new LinkedList<>();
        File cFile = new File(PEconPlugin.getInstance().getDataFolder(), "cooldown.yml");
        if (cFile.exists()) {
            FileConfiguration config = new YamlConfiguration();
            try {
                config.load(cFile);
                if (config.isList("cooldowns")) {
                    List<Map<?, ?>> ser = config.getMapList("cooldowns");
                    for (Map<?, ?> entry : ser) {
                        cooldowns.add(CooldownEntry.fromMap(entry));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Saves the cooldown list to cooldown.yml.
     */
    public void save() {
        FileConfiguration config = new YamlConfiguration();
        List<Map<String, Object>> ser = new LinkedList<>();
        for (CooldownEntry entry : cooldowns) {
            ser.add(entry.toMap());
        }
        config.set("cooldowns", ser);

        try {
            config.save(new File(PEconPlugin.getInstance().getDataFolder(), "cooldown.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes all cooldown entries.
     */
    public void clear() {
        cooldowns.clear();
    }

    /**
     * Resets the cooldown between the given killer and victim and returns a
     * (possibly) cooldown-modified version of the given reward amount.
     *
     * @param killer the killer
     * @param victim the victim
     * @param amount the amount of money
     * @return the modified amount of money
     */
    public double renewKill(OfflinePlayer killer, OfflinePlayer victim, double amount) {
        CooldownProperties properties = PEconPlugin.getInstance().getPluginConfiguration()
                .getProperties().getPlayerProperties().getCooldown();
        String killerId = killer.getUniqueId().toString();
        String victimId = victim.getUniqueId().toString();
        long time = System.currentTimeMillis();
        long cooldown = properties.getTime() * 1000;

        boolean expired = false;
        while (!cooldowns.isEmpty() && time - cooldowns.peek().getTime() > cooldown) {
            CooldownEntry entry = cooldowns.remove();
            if (entry.getKiller().equals(killerId) && entry.getVictim().equals(victimId)) {
                expired = true;
            }
        }
        if (expired) {
            cooldowns.add(new CooldownEntry(killerId, victimId, time));
            return amount;
        }

        for (Iterator<CooldownEntry> it = cooldowns.descendingIterator(); it.hasNext();) {
            CooldownEntry entry = it.next();
            if (entry.getKiller().equals(killer.getUniqueId().toString())
                    && entry.getVictim().equals(victim.getUniqueId().toString())) {
                it.remove();
                cooldowns.add(new CooldownEntry(killerId, victimId, time));
                return amount * properties.getFunction().eval((double) (time - entry.getTime())
                        / (double) cooldown);
            }
        }

        cooldowns.add(new CooldownEntry(killerId, victimId, time));
        return amount;
    }

    /**
     * An entry wrapping a PvP victim, their killer, and the time of death.
     */
    public static class CooldownEntry {

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "CooldownEntry [killer=" + killer + ", victim=" + victim + ", time=" + time + "]";
        }

        private final String killer;
        private final String victim;
        private final long time;

        /**
         * Constructs a cooldown entry with the given killer, victim, and time
         * of death.
         *
         * @param killer the killer
         * @param victim the victim
         * @param time the time of death
         */
        public CooldownEntry(String killer, String victim, long time) {
            this.killer = killer;
            this.victim = victim;
            this.time = time;
        }

        /**
         * Gets the killer's UUID.
         *
         * @return the killer
         */
        public String getKiller() {
            return killer;
        }

        /**
         * Gets the victim's UUID.
         *
         * @return the victim
         */
        public String getVictim() {
            return victim;
        }

        /**
         * Gets the time of death.
         *
         * @return the death
         */
        public long getTime() {
            return time;
        }

        /**
         * Serializes the entry to a key-value map.
         *
         * @return the map
         */
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("killer",  killer);
            map.put("victim", victim);
            map.put("time", time);
            return map;
        }

        /**
         * Deserializes an entry from a key-value map.
         *
         * @param map the map
         * @return a cooldown entry
         */
        public static CooldownEntry fromMap(Map<?, ?> map) {
            if (map.containsKey("killer") && map.containsKey("victim")
                    && map.containsKey("time")) {
                return new CooldownEntry((String) map.get("killer"), (String) map.get("victim"),
                        (Long) map.get("time"));
            }
            return null;
        }

    }

}
