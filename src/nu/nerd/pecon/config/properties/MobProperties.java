package nu.nerd.pecon.config.properties;

import java.util.HashMap;
import java.util.Map;

import nu.nerd.pecon.PEconPlugin;
import nu.nerd.pecon.config.Configuration;

import org.bukkit.DyeColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Zombie;

/**
 * The plugin's player properties.
 */
public class MobProperties {

    private final boolean ignoreSpawnerMobs;
    private final Map<EntityType, Predicate<LivingEntity>> spawnerTypes;

    /**
     * Constructs a {@code MobProperties} from the given configuration section.
     * This should contain a boolean-valued <i>ignore-spawner-mobs</i> entry
     * and a <i>spawner-types</i> entry containing a map from entity type
     * names to booleans.
     * <p>
     * If <i>ignore-spawner-mobs</i> is {@code true}, then killing mobs that
     * originated from spawners will not
     *
     * @param config
     */
    public MobProperties(ConfigurationSection config) {
        ignoreSpawnerMobs = Configuration.getBoolean(config, "ignore-spawner-mobs", true);
        spawnerTypes = new HashMap<>();
        if (ignoreSpawnerMobs && config.isConfigurationSection("spawner-types")) {
            ConfigurationSection typeSection = config.getConfigurationSection("spawner-types");
            for (String entity : typeSection.getKeys(false)) {
                try {
                    EntityType type = EntityType.valueOf(entity.toUpperCase());
                    if (typeSection.isBoolean(entity)) {
                        spawnerTypes.put(type, new ConstantPredicate<LivingEntity>(
                                typeSection.getBoolean(entity)));
                    } else if (typeSection.isConfigurationSection(entity)) {
                        spawnerTypes.put(type, getPredicate(type,
                                typeSection.getConfigurationSection(entity)));
                    } else {
                        PEconPlugin.getInstance().getLogger().warning(typeSection.getCurrentPath()
                                + "." + entity + " does not contain a valid value specification."
                                + " Check config.yml.");
                    }
                } catch (IllegalArgumentException exception) {
                    PEconPlugin.getInstance().getLogger().warning(typeSection.getCurrentPath()
                            + ": " + entity + " is not a valid mob type. Check config.yml.");
                }
            }
        }
    }

    /**
     * Gets whether mobs spawned from spawners should not give rewards to their
     * killers.
     *
     * @return whether spawner mobs should not give rewards
     */
    public boolean getIgnoreSpawnerMobs() {
        return ignoreSpawnerMobs;
    }

    private Predicate<LivingEntity> getPredicate(EntityType type,
            final ConfigurationSection config) {
        switch (type) {
        case CREEPER:
            return new Predicate<LivingEntity>() {
                boolean powered = config.getBoolean("powered");
                boolean unpowered = config.getBoolean("default");
                @Override
                public boolean test(LivingEntity input) {
                    return ((Creeper) input).isPowered() ? powered : unpowered;
                }
            };
        case GUARDIAN:
            return new Predicate<LivingEntity>() {
                boolean elder = config.getBoolean("elder");
                boolean normal = config.getBoolean("default");
                @Override
                public boolean test(LivingEntity input) {
                    return ((Guardian) input).isElder() ? elder : normal;
                }
            };
        case HORSE:
            return new EnumPredicate<Horse.Variant, LivingEntity>(config) {
                @Override
                protected Horse.Variant getEnum(LivingEntity object) {
                    return ((Horse) object).getVariant();
                }
            };
        case OCELOT:
            return new EnumPredicate<Ocelot.Type, LivingEntity>(config) {
                @Override
                protected Ocelot.Type getEnum(LivingEntity object) {
                    return ((Ocelot) object).getCatType();
                }
            };
        case RABBIT:
            return new EnumPredicate<Rabbit.Type, LivingEntity>(config) {
                @Override
                protected Rabbit.Type getEnum(LivingEntity object) {
                    return ((Rabbit) object).getRabbitType();
                }
            };
        case SHEEP:
            return new EnumPredicate<DyeColor, LivingEntity>(config) {
                @Override
                protected DyeColor getEnum(LivingEntity object) {
                    return ((Sheep) object).getColor();
                }
            };
        case SKELETON:
            return new EnumPredicate<Skeleton.SkeletonType, LivingEntity>(config) {
                @Override
                protected Skeleton.SkeletonType getEnum(LivingEntity object) {
                    return ((Skeleton) object).getSkeletonType();
                }
            };
        case VILLAGER:
            return new EnumPredicate<Villager.Profession, LivingEntity>(config) {
                @Override
                protected Villager.Profession getEnum(LivingEntity object) {
                    return ((Villager) object).getProfession();
                }
            };
        case ZOMBIE:
            return new Predicate<LivingEntity>() {
                boolean baby = config.getBoolean("baby");
                boolean villager = config.getBoolean("villager");
                boolean normal = config.getBoolean("default");
                @Override
                public boolean test(LivingEntity input) {
                    return ((Zombie) input).isBaby() ? baby :
                        ((Zombie) input).isVillager() ? villager : normal;
                }
            };
        default:
            break;
        }
        PEconPlugin.getInstance().getLogger().warning(config.getCurrentPath() + "." + type
                + " does not contain a valid value specification. Check config.yml.");
        return new ConstantPredicate<LivingEntity>(false);
    }

    /**
     * Determines whether this mob should have a chance of rewarding its
     * killer. If <i>ignore-spawner-mobs</i> is false, this returns true.
     * Otherwise, this should return true if and only if the mob is not a spawner
     * type, or it has a "pecon.fromspawner" metadata tag with a value of
     * {@code false}.
     *
     * @param mob the mob to check
     * @return whether the mob should reward its killer
     */
    public boolean shouldReward(LivingEntity mob) {
        if (ignoreSpawnerMobs) {
            return (mob.hasMetadata("pecon.fromspawner")
                            && !mob.getMetadata("pecon.fromspawner").get(0).asBoolean())
                    || !(spawnerTypes.containsKey(mob.getType())
                            && spawnerTypes.get(mob.getType()).test(mob));
        }
        return true;
    }

}
