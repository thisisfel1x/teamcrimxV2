package de.fel1x.teamcrimx.mlgwars.utils.entites;

import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.EntityZombie;
import net.minecraft.server.v1_16_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_16_R3.PathfinderGoalNearestAttackableTarget;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.entity.Player;

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
