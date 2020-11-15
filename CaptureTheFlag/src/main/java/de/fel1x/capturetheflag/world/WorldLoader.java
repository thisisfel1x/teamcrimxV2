package de.fel1x.capturetheflag.world;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.IPermissionGroup;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.fel1x.capturetheflag.CaptureTheFlag;
import de.fel1x.capturetheflag.filehandler.SpawnHandler;
import de.fel1x.capturetheflag.utils.particles.ParticleEffects;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.crimxapi.utils.ParticleUtils;
import org.bson.Document;
import org.bukkit.*;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WorldLoader {

    private final IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry()
            .getFirstService(IPlayerManager.class);

    public WorldLoader() {

        String lobbyWorldName = SpawnHandler.getWorld("lobby");
        String gameWorldName = SpawnHandler.getWorld("spectator");

        World lobbyWorld = Bukkit.createWorld(new WorldCreator(lobbyWorldName));
        World gameWorld = Bukkit.createWorld(new WorldCreator(gameWorldName));

        try {

            lobbyWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            lobbyWorld.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            lobbyWorld.setThundering(false);
            lobbyWorld.setStorm(false);
            lobbyWorld.setTime(8000);

            gameWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            gameWorld.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            gameWorld.setThundering(false);
            gameWorld.setStorm(false);
            gameWorld.setTime(8000);

        } catch (Exception ignored) {
        }

        this.setTop3Wall();

    }

    private void setTop3Wall() {
        if (SpawnHandler.loadLocation("topHead1") == null) {
            return;
        }

        Location titleSpawn = new Location(Bukkit.getWorld("Wartelobby"), -9, 6, 11);
        ArmorStand title = (ArmorStand) titleSpawn.getWorld().spawnEntity(titleSpawn.toCenterLocation(), EntityType.ARMOR_STAND);
        title.setCustomName("§9§lCaptureTheFlag");
        title.setInvisible(true);
        title.setGravity(false);
        title.setCustomNameVisible(true);

        ArmorStand subTitle = (ArmorStand) titleSpawn.getWorld().spawnEntity(titleSpawn.toCenterLocation()
                .clone().subtract(0, 0.25, 0), EntityType.ARMOR_STAND);
        subTitle.setCustomName("§9Top 3");
        subTitle.setInvisible(true);
        subTitle.setGravity(false);
        subTitle.setCustomNameVisible(true);

        List<Document> documents = CaptureTheFlag.getInstance().getTop(3);
        for (int i = 0; i < 3; i++) {

            int current = i + 1;

            Location locSign = SpawnHandler.loadLocation("topSign" + current);

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

        Location near = SpawnHandler.loadLocation("topSign1");

        ArmorStand armorStandTop1 = null, armorStandTop2 = null, armorStandTop3 = null;

        for (ArmorStand armorStand : near.getNearbyEntitiesByType(ArmorStand.class, 10)) {
            if(armorStand.getItem(EquipmentSlot.HAND).getType() == Material.TNT) {
                armorStandTop1 = armorStand;
            } else if(armorStand.getItem(EquipmentSlot.HAND).getType() == Material.GRASS_BLOCK) {
                armorStandTop2 = armorStand;
            } else if(armorStand.getItem(EquipmentSlot.HAND).getType() == Material.IRON_SWORD) {
                armorStandTop3 = armorStand;
            }
        }

        this.runMainTask(armorStandTop1.getLocation(), armorStandTop2.getLocation(), armorStandTop3.getLocation());

        for (Document currentDocument : documents) {
            int current = i;
            i++;

            Location locSign = SpawnHandler.loadLocation("topSign" + current);

            if (locSign == null) continue;

            if (locSign.getBlock().getState() instanceof Sign) {
                Sign sign = (Sign) locSign.getBlock().getState();

                String name = currentDocument.getString("name");

                int kills = currentDocument.getInteger("kills");
                int deaths = currentDocument.getInteger("deaths");
                int gamesWon = currentDocument.getInteger("gamesWon");

                double kd;
                if (deaths > 0) {
                    double a = ((double) kills / deaths) * 100;
                    kd = Math.round(a);
                } else {
                    kd = kills;
                }

                sign.setLine(0, "#" + current);
                sign.setLine(1, name);
                sign.setLine(2, gamesWon + " Wins");
                sign.setLine(3, "K/D: " + (kd / 100));

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

            }
        }
    }

    int count;

    private void runMainTask(Location location, Location location1, Location location2) {
        ArrayList<Location> armorStand1 = ParticleEffects.getCirclePoints(location, 0.7, 20);
        ArrayList<Location> armorStand2 = ParticleEffects.getCirclePoints(location1, 0.7, 20);
        ArrayList<Location> armorStand3 = ParticleEffects.getCirclePoints(location2, 0.7, 20);

        this.count = 0;

        Bukkit.getScheduler().scheduleSyncRepeatingTask(CaptureTheFlag.getInstance(), () -> {
            armorStand1.get(this.count).getWorld().spawnParticle(Particle.REDSTONE,  armorStand1.get(this.count), 2,
                    new Particle.DustOptions(Color.fromRGB(255,165,0), 1));
            armorStand2.get(this.count).getWorld().spawnParticle(Particle.REDSTONE,  armorStand2.get(this.count), 2,
                    new Particle.DustOptions(Color.fromRGB(211,211,211), 1));
            armorStand3.get(this.count).getWorld().spawnParticle(Particle.REDSTONE,  armorStand3.get(this.count), 2,
                    new Particle.DustOptions(Color.fromRGB(205, 127, 50), 1));

            this.count++;

            if(this.count > 19) {
                this.count = 0;
            }
        }, 0L, 0L);
    }
}
