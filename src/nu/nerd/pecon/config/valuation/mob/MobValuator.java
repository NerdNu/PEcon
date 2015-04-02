package nu.nerd.pecon.config.valuation.mob;

import java.util.HashMap;
import java.util.Map;

import nu.nerd.pecon.PEconPlugin;
import nu.nerd.pecon.config.valuation.ConstantValuator;
import nu.nerd.pecon.config.valuation.CurvedValuator;
import nu.nerd.pecon.config.valuation.EnumValuator;
import nu.nerd.pecon.config.valuation.Valuator;
import nu.nerd.pecon.function.Functions;
import nu.nerd.pecon.function.NumericFunction;

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
import org.bukkit.entity.Slime;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Zombie;

/**
 * Valuates a mob.
 */
public class MobValuator extends CurvedValuator<LivingEntity> {

    private final Map<EntityType, Valuator<LivingEntity>> valueMap;
    private final double defaultValue;

    /**
     * Constructs a {@code MobValuator} from the given configuration section.
     * Each key should have the same name as one of
     * {@link org.bukkit.entity.EntityType EntityType}'s enum constants.
     *
     * @param config the configuration section
     */
    public MobValuator(ConfigurationSection config) {
        super(config);
        valueMap = new HashMap<>();
        if (config != null) {
            for (String entity : config.getKeys(false)) {
                try {
                    EntityType type = EntityType.valueOf(entity.toUpperCase());
                    if (config.isDouble(entity)) {
                        valueMap.put(type, new ConstantValuator<LivingEntity>(
                                config.getDouble(entity)));
                    } else if (config.isConfigurationSection(entity)) {
                        valueMap.put(type, fromConfig(type,
                                config.getConfigurationSection(entity)));
                    } else {
                        PEconPlugin.getInstance().getLogger().warning(config.getCurrentPath() + "."
                                + entity + " does not contain a valid value specification. Check"
                                + " config.yml.");
                    }
                } catch (IllegalArgumentException exception) {
                    PEconPlugin.getInstance().getLogger().warning(config.getCurrentPath() + ": "
                            + entity + " is not a valid mob type. Check config.yml.");
                }
            }
        }
        defaultValue = config.getDouble("default", 0.0);
    }

    @Override
    protected double baseValue(LivingEntity input) {
        if (valueMap.containsKey(input.getType())) {
            return valueMap.get(input.getType()).valuate(input);
        } else {
            return defaultValue;
        }
    }

    private static Valuator<LivingEntity> fromConfig(EntityType type,
            final ConfigurationSection config) {
        switch (type) {
        case CREEPER:
            return new Valuator<LivingEntity>() {
                double powered = config.getDouble("powered");
                double unpowered = config.getDouble("default");
                @Override
                public double valuate(LivingEntity input) {
                    return ((Creeper) input).isPowered() ? powered : unpowered;
                }
            };
        case GUARDIAN:
            return new Valuator<LivingEntity>() {
                double elder = config.getDouble("elder");
                double normal = config.getDouble("default");
                @Override
                public double valuate(LivingEntity input) {
                    return ((Guardian) input).isElder() ? elder : normal;
                }
            };
        case HORSE:
            return new EnumValuator<Horse.Variant, LivingEntity>(config) {
                @Override
                protected Horse.Variant getEnum(LivingEntity object) {
                    return ((Horse) object).getVariant();
                }
            };
        case OCELOT:
            return new EnumValuator<Ocelot.Type, LivingEntity>(config) {
                @Override
                protected Ocelot.Type getEnum(LivingEntity object) {
                    return ((Ocelot) object).getCatType();
                }
            };
        case RABBIT:
            return new EnumValuator<Rabbit.Type, LivingEntity>(config) {
                @Override
                protected Rabbit.Type getEnum(LivingEntity object) {
                    return ((Rabbit) object).getRabbitType();
                }
            };
        case SHEEP:
            return new EnumValuator<DyeColor, LivingEntity>(config) {
                @Override
                protected DyeColor getEnum(LivingEntity object) {
                    return ((Sheep) object).getColor();
                }
            };
        case SKELETON:
            return new EnumValuator<Skeleton.SkeletonType, LivingEntity>(config) {
                @Override
                protected Skeleton.SkeletonType getEnum(LivingEntity object) {
                    return ((Skeleton) object).getSkeletonType();
                }
            };
        case MAGMA_CUBE:
        case SLIME:
            if (config.isConfigurationSection("size-function")) {
                return new Valuator<LivingEntity>() {
                    NumericFunction sizeFunction = Functions.fromConfig(
                            config.getConfigurationSection("size-function"));
                    @Override
                    public double valuate(LivingEntity input) {
                        return sizeFunction.eval(((Slime) input).getSize());
                    }
                };
            }
            break;
        case VILLAGER:
            return new EnumValuator<Villager.Profession, LivingEntity>(config) {
                @Override
                protected Villager.Profession getEnum(LivingEntity object) {
                    return ((Villager) object).getProfession();
                }
            };
        case ZOMBIE:
            return new Valuator<LivingEntity>() {
                double baby = config.getDouble("baby");
                double villager = config.getDouble("villager");
                double normal = config.getDouble("default");
                @Override
                public double valuate(LivingEntity input) {
                    return ((Zombie) input).isBaby() ? baby :
                        ((Zombie) input).isVillager() ? villager : normal;
                }
            };
        default:
            break;
        }
        PEconPlugin.getInstance().getLogger().warning(config.getCurrentPath() + "." + type
                + " does not contain a valid value specification. Check config.yml.");
        return new ConstantValuator<LivingEntity>(0.0);
    }

}
