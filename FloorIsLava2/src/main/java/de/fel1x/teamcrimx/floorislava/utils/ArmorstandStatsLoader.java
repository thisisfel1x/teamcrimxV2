package de.fel1x.teamcrimx.floorislava.utils;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.IPermissionGroup;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.floorislava.FloorIsLava;
import org.bson.Document;
import org.bukkit.*;
import org.bukkit.block.Sign;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ArmorstandStatsLoader {
    private final FloorIsLava floorIsLava;
    private final Location[] signs;

    int count;

    public ArmorstandStatsLoader(FloorIsLava floorIsLava) {
        this.floorIsLava = floorIsLava;
        this.signs = new Location[]{
                new Location(this.floorIsLava.getSpawnLocation().getWorld(), -9.0D, 6.0D, 11.0D),
                new Location(this.floorIsLava.getSpawnLocation().getWorld(), -9.0D, 5.0D, 13.0D),
                new Location(this.floorIsLava.getSpawnLocation().getWorld(), -11.0D, 5.0D, 11.0D)
        };
        setTop3Wall();
    }

    public static ArrayList<Location> getCirclePoints(Location center, double radius, int amount) {
        World world = center.getWorld();
        double increment = 6.283185307179586D / amount;
        ArrayList<Location> locations = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            double angle = i * increment;
            double x = center.getX() + radius * Math.cos(angle);
            double z = center.getZ() + radius * Math.sin(angle);
            Location particle = new Location(world, x, center.getY(), z);
            locations.add(particle);
        }
        return locations;
    }

    public void setTop3Wall() {
        if (this.signs == null)
            return;
        Location titleSpawn = new Location(Bukkit.getWorld("Wartelobby"), -9.0D, 6.0D, 11.0D);
        ArmorStand title = (ArmorStand) titleSpawn.getWorld().spawnEntity(titleSpawn.toCenterLocation(), EntityType.ARMOR_STAND);
        title.setCustomName("§6§lFloorIsLava");
        title.setInvisible(true);
        title.setGravity(false);
        title.setCustomNameVisible(true);
        ArmorStand subTitle = (ArmorStand) titleSpawn.getWorld().spawnEntity(titleSpawn.toCenterLocation()
                .clone().subtract(0.0D, 0.25D, 0.0D), EntityType.ARMOR_STAND);
        subTitle.setCustomName("§aTop 3");
        subTitle.setInvisible(true);
        subTitle.setGravity(false);
        subTitle.setCustomNameVisible(true);
        List<Document> documents = this.floorIsLava.getTop(3);
        int i;
        for (i = 0; i < 3; i++) {
            Location locSign = this.signs[i];
            if (locSign != null)
                if (locSign.getBlock().getState() instanceof Sign) {
                    Sign sign = (Sign) locSign.getBlock().getState();
                    sign.setLine(0, "§7---------------");
                    sign.setLine(1, "§cNicht");
                    sign.setLine(2, "§cbesetzt");
                    sign.setLine(3, "§7---------------");
                    sign.update();
                }
        }
        i = 1;
        Location near = this.signs[0];
        ArmorStand armorStandTop1 = null, armorStandTop2 = null, armorStandTop3 = null;
        for (ArmorStand armorStand : near.getNearbyEntitiesByType(ArmorStand.class, 10.0D)) {
            if (armorStand.getItem(EquipmentSlot.HAND).getType() == Material.TNT) {
                armorStandTop1 = armorStand;
                continue;
            }
            if (armorStand.getItem(EquipmentSlot.HAND).getType() == Material.GRASS_BLOCK) {
                armorStandTop2 = armorStand;
                continue;
            }
            if (armorStand.getItem(EquipmentSlot.HAND).getType() == Material.IRON_SWORD)
                armorStandTop3 = armorStand;
        }
        runMainTask(armorStandTop1.getLocation(), armorStandTop2.getLocation(), armorStandTop3.getLocation());
        for (Document currentDocument : documents) {
            int current = i;
            Location locSign = this.signs[current - 1];
            if (locSign == null)
                continue;
            if (locSign.getBlock().getState() instanceof Sign) {
                double kd;
                Sign sign = (Sign) locSign.getBlock().getState();
                String name = currentDocument.getString("name");
                int kills = currentDocument.getInteger("kills");
                int deaths = currentDocument.getInteger("deaths");
                int gamesWon = currentDocument.getInteger("gamesWon");
                if (deaths > 0) {
                    double a = kills / deaths * 100.0D;
                    kd = Math.round(a);
                } else {
                    kd = kills;
                }
                sign.setLine(0, "#" + current);
                sign.setLine(1, name);
                sign.setLine(2, gamesWon + " Wins");
                sign.setLine(3, "K/D: " + (kd / 100.0D));
                sign.update();
                ItemStack skullOwner = (new ItemBuilder(Material.PLAYER_HEAD)).setSkullOwner(name).toItemStack();
                ArmorStand toSet = (current == 1) ? armorStandTop1 : ((current == 2) ? armorStandTop2 : armorStandTop3);
                toSet.getItem(EquipmentSlot.HEAD).setItemMeta(skullOwner.getItemMeta());
                IPermissionUser iPermissionUser = CloudNetDriver.getInstance().getPermissionManagement().getUser(UUID.fromString(currentDocument.getString("_id")));
                if (iPermissionUser == null)
                    return;
                IPermissionGroup permissionGroup = CloudNetDriver.getInstance().getPermissionManagement().getHighestPermissionGroup(iPermissionUser);
                permissionGroup.getDisplay();
                toSet.setCustomName("§7#" + current + " §8● §r" +
                        ChatColor.translateAlternateColorCodes('&', permissionGroup.getDisplay()) + name);
                toSet.setCustomNameVisible(true);
                i++;
            }
        }
    }

    private void runMainTask(Location location, Location location1, Location location2) {
        ArrayList<Location> armorStand1 = getCirclePoints(location, 0.7D, 20);
        ArrayList<Location> armorStand2 = getCirclePoints(location1, 0.7D, 20);
        ArrayList<Location> armorStand3 = getCirclePoints(location2, 0.7D, 20);
        this.count = 0;

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this.floorIsLava, () -> {
            armorStand1.get(this.count).getWorld().spawnParticle(Particle.REDSTONE, armorStand1.get(this.count), 2, new Particle.DustOptions(Color.fromRGB(255, 165, 0), 1.0F));
            armorStand2.get(this.count).getWorld().spawnParticle(Particle.REDSTONE, armorStand2.get(this.count), 2, new Particle.DustOptions(Color.fromRGB(211, 211, 211), 1.0F));
            armorStand3.get(this.count).getWorld().spawnParticle(Particle.REDSTONE, armorStand3.get(this.count), 2, new Particle.DustOptions(Color.fromRGB(205, 127, 50), 1.0F));
            this.count++;
            if (this.count > 19)
                this.count = 0;
        }, 0L, 0L);
    }
}
