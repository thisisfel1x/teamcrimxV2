package de.fel1x.bingo.utils.world;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.IPermissionGroup;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import de.fel1x.bingo.Bingo;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
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

public class StatsArmorstand {

    private final Bingo bingo;
    private final Location[] signs;
    int count;

    public StatsArmorstand(Bingo bingo) {
        this.bingo = bingo;

        this.signs = new Location[]{
                new Location(this.bingo.getSpawnLocation().getWorld(), -9, 6, 11),
                new Location(this.bingo.getSpawnLocation().getWorld(), -9, 5, 13),
                new Location(this.bingo.getSpawnLocation().getWorld(), -11, 5, 11)
        };
        this.setTop3Wall();
    }

    public void setTop3Wall() {
        if (this.signs == null) {
            return;
        }

        Location titleSpawn = new Location(Bukkit.getWorld("Wartelobby"), -9, 6, 11);
        ArmorStand title = (ArmorStand) titleSpawn.getWorld().spawnEntity(titleSpawn.toCenterLocation(), EntityType.ARMOR_STAND);
        title.setCustomName("§a§lBingo");
        title.setInvisible(true);
        title.setGravity(false);
        title.setCustomNameVisible(true);

        ArmorStand subTitle = (ArmorStand) titleSpawn.getWorld().spawnEntity(titleSpawn.toCenterLocation()
                .clone().subtract(0, 0.25, 0), EntityType.ARMOR_STAND);
        subTitle.setCustomName("§aTop 3");
        subTitle.setInvisible(true);
        subTitle.setGravity(false);
        subTitle.setCustomNameVisible(true);

        List<Document> documents = this.bingo.getTop(3);
        for (int i = 0; i < 3; i++) {

            Location locSign = this.signs[i];

            if (locSign == null) continue;

            if (locSign.getBlock().getState() instanceof Sign) {
                Sign sign = (Sign) locSign.getBlock().getState();

                sign.setLine(0, "§7---------------");
                sign.setLine(1, "§cNicht");
                sign.setLine(2, "§cbesetzt");
                sign.setLine(3, "§7---------------");

                sign.update();
            }
        }

        int i = 1;

        Location near = this.signs[0];

        ArmorStand armorStandTop1 = null, armorStandTop2 = null, armorStandTop3 = null;

        for (ArmorStand armorStand : near.getNearbyEntitiesByType(ArmorStand.class, 10)) {
            if (armorStand.getItem(EquipmentSlot.HAND).getType() == Material.TNT) {
                armorStandTop1 = armorStand;
            } else if (armorStand.getItem(EquipmentSlot.HAND).getType() == Material.GRASS_BLOCK) {
                armorStandTop2 = armorStand;
            } else if (armorStand.getItem(EquipmentSlot.HAND).getType() == Material.IRON_SWORD) {
                armorStandTop3 = armorStand;
            }
        }

        this.runMainTask(armorStandTop1.getLocation(), armorStandTop2.getLocation(), armorStandTop3.getLocation());

        for (Document currentDocument : documents) {
            int current = i;

            Location locSign = this.signs[current - 1];

            if (locSign == null) continue;

            if (locSign.getBlock().getState() instanceof Sign) {
                Sign sign = (Sign) locSign.getBlock().getState();

                String name = currentDocument.getString("name");

                int itemsPickedUp = currentDocument.getInteger("itemsPickedUp");
                int itemsCrafted = currentDocument.getInteger("itemsCrafted");
                int gamesWon = currentDocument.getInteger("gamesWon");

                int totalItemsCollected = itemsCrafted + itemsPickedUp;

                sign.setLine(0, "#" + current);
                sign.setLine(1, name);
                sign.setLine(2, gamesWon + " Wins");
                sign.setLine(3, "Items: " + totalItemsCollected);

                sign.update();

                ItemStack skullOwner = new ItemBuilder(Material.PLAYER_HEAD)
                        .setSkullOwner(name).toItemStack();

                ArmorStand toSet = current == 1 ? armorStandTop1 : current == 2 ? armorStandTop2 : armorStandTop3;
                toSet.getItem(EquipmentSlot.HEAD).setItemMeta(skullOwner.getItemMeta());

                IPermissionUser iPermissionUser = CloudNetDriver.getInstance().getPermissionManagement()
                        .getUser(UUID.fromString(currentDocument.getString("_id")));

                if (iPermissionUser == null) return;

                IPermissionGroup permissionGroup = CloudNetDriver.getInstance().getPermissionManagement()
                        .getHighestPermissionGroup(iPermissionUser);

                permissionGroup.getDisplay();

                toSet.setCustomName("§7#" + current + " §8● §r" +
                        ChatColor.translateAlternateColorCodes('&', permissionGroup.getDisplay()) + name);
                toSet.setCustomNameVisible(true);

                i++;

            }
        }
    }

    private void runMainTask(Location location, Location location1, Location location2) {
        ArrayList<Location> armorStand1 = this.getCirclePoints(location, 0.7, 20);
        ArrayList<Location> armorStand2 = this.getCirclePoints(location1, 0.7, 20);
        ArrayList<Location> armorStand3 = this.getCirclePoints(location2, 0.7, 20);

        this.count = 0;

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this.bingo, () -> {
            armorStand1.get(this.count).getWorld().spawnParticle(Particle.REDSTONE, armorStand1.get(this.count), 2,
                    new Particle.DustOptions(Color.fromRGB(255, 165, 0), 1));
            armorStand2.get(this.count).getWorld().spawnParticle(Particle.REDSTONE, armorStand2.get(this.count), 2,
                    new Particle.DustOptions(Color.fromRGB(211, 211, 211), 1));
            armorStand3.get(this.count).getWorld().spawnParticle(Particle.REDSTONE, armorStand3.get(this.count), 2,
                    new Particle.DustOptions(Color.fromRGB(205, 127, 50), 1));

            this.count++;

            if (this.count > 19) {
                this.count = 0;
            }
        }, 0L, 0L);
    }

    private ArrayList<Location> getCirclePoints(Location center, double radius, int amount) {
        World world = center.getWorld();
        double increment = (2 * Math.PI) / amount;

        ArrayList<Location> locations = new ArrayList<>();
        for (int i = 0; i < amount; i++) {

            double angle = i * increment;
            double x = center.getX() + (radius * Math.cos(angle));
            double z = center.getZ() + (radius * Math.sin(angle));

            Location particle = new Location(world, x, center.getY(), z);
            locations.add(particle);

        }
        return locations;
    }

}
