package de.fel1x.teamcrimx.mlgwars.utils.entites;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class CustomZombie extends EntityZombie {

    private final Player player;

    public CustomZombie(Location location, Player player) {
        super(EntityTypes.ZOMBIE, ((CraftWorld) location.getWorld()).getHandle());

        this.player = player;
        this.setPosition(location.getX(), location.getY(), location.getZ());
    }

    @Override
    protected void initPathfinder() {
        super.initPathfinder();

        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityZombie.class, 8.0F));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityZombie.class, true));

    }

    @Override
    public CraftEntity getBukkitEntity() {
        return super.getBukkitEntity();
    }
}
