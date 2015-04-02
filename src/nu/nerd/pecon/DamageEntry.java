package nu.nerd.pecon;

import org.bukkit.OfflinePlayer;

/**
 * An entry containing the player who damaged an entity, the total amount of
 * damage that was done to an entity by the player, and the world time at which
 * the player last damaged the entity.
 */
public class DamageEntry {

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "DamageEntry [player=" + player.getName() + ", damage=" + damage + ", time=" + time + "]";
    }

    private OfflinePlayer player;
    private double damage;
    private long time;

    /**
     * Constructs a damage entry with the given damage and time.
     *
     * @param damage the damage
     * @param time the time
     */
    public DamageEntry(OfflinePlayer player, double damage, long time) {
        this.player = player;
        this.damage = damage;
        this.time = time;
    }

    /**
     * @return the player
     */
    public OfflinePlayer getPlayer() {
        return player;
    }

    /**
     * @param player the player to set
     */
    public void setPlayer(OfflinePlayer player) {
        this.player = player;
    }

    /**
     * @return the damage
     */
    public double getDamage() {
        return damage;
    }

    /**
     * @param damage the damage to set
     */
    public void setDamage(double damage) {
        this.damage = damage;
    }

    /**
     * @return the time
     */
    public long getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(long time) {
        this.time = time;
    }

}
