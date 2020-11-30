package de.fel1x.teamcrimx.floorislava.gameevents.lootdrop;

import de.fel1x.teamcrimx.crimxapi.utils.InstantFirework;
import de.fel1x.teamcrimx.floorislava.FloorIsLava;
import de.fel1x.teamcrimx.floorislava.utils.ArmorstandStatsLoader;
import org.bukkit.*;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class LootDrop {

    private Location origin;
    private LootDropTier lootDropTier = LootDropTier.TIER_1;
    private double speed = 1.0;
    private Material blockType;
    private int chickenAmount = 20;

    public LootDrop(@NotNull Location origin, Material material) {
        this.origin = origin;
        this.blockType = material;
    }

    public LootDrop lootDropTier(@NotNull LootDropTier lootDropTier) {
        this.lootDropTier = lootDropTier;
        return this;
    }

    public LootDrop setLootDropSpeed(double speed) {
        this.speed = speed;
        return this;
    }

    public LootDrop setChickenAmount(int chickenAmount) {
        this.chickenAmount = chickenAmount;
        return this;
    }

    public LootDrop build() {
        this.startDrop(this.blockType);
        return this;
    }

    private void startDrop(Material material) {
        World world = this.origin.getWorld();

        FallingBlock fallingChest = world.spawnFallingBlock(this.origin, Bukkit.createBlockData(material));
        ArrayList<Chicken> chickens = new ArrayList<>();

        int amount = 21;
        double height = 0;

        for(double radius = 3; radius >= 0; radius-=0.75) {
            double finalHeight = height;
            ArmorstandStatsLoader.getCirclePoints(this.origin.clone().add(0, 2, 0), radius, amount).forEach(location -> {
                Chicken chicken = (Chicken) world.spawnEntity(location.clone().add(0, finalHeight, 0), EntityType.CHICKEN);
                chicken.setLeashHolder(fallingChest);
                chicken.setSilent(true);
                chickens.add(chicken);
            });
            Bukkit.broadcastMessage(String.valueOf(finalHeight));
            amount-= 5;
            height+= 0.5;
        }

        Bukkit.getScheduler().runTaskTimer(FloorIsLava.getInstance(), () -> {
            Vector toSet = new Vector(0, -0.05, 0);
            fallingChest.setVelocity(toSet);
            chickens.forEach(chicken -> chicken.setVelocity(new Vector(0, -0.09, 0)));
            if(fallingChest.isOnGround()) {
                fallingChest.remove();
                chickens.forEach(Chicken::remove);
            }
        }, 0L, 0L);

    }


}
