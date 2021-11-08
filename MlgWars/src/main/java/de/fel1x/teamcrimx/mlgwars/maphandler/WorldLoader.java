package de.fel1x.teamcrimx.mlgwars.maphandler;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.github.juliarn.npc.NPC;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.IPermissionGroup;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.crimxapi.utils.npc.NPCCreator;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.enums.Spawns;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bson.Document;
import org.bukkit.*;
import org.bukkit.block.Sign;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class WorldLoader {

    private final MlgWars mlgWars;
    private final MapHandler mapHandler = new MapHandler();
    private final SpawnHandler spawnHandler = new SpawnHandler();

    private final String signature = "N5t81nKeRPDgVPjGTbSPJ2tBX73NpoPTiFkvBE+Q3IQ8o4MQzmA1VdukfrdUcAv3JseGzLfeQbo9OQ2gHSNQ8Cx3NUuvdDsWsFrexoeDXFe5bAta8xHipscBC30Ltx3q+QQt1BqWLT0lLxTJAHmQhFQJX3Pr/cxftehkQkRmhPdY4DJIC/v/SMx35OtVvY3CGnwxeBZSkuIsEl4S3ZnjiZoxCYQ1iT/ChFgrt+PzVAYUZeyt6HgQvjR1J3yZGm2rf647blR3h4MxB11azpRSf7VJ7FvtjD8aSeZ8ZqCVfEEWkq1TEBBFwWRmoJYIx7/xFX74NxP++Dha7Viqb6lyAlwYhpk03I8jUoJOQnPt/0fL8nadbLRvnigks7GHNwJlalwxGOX25aHTAAjmBLeB7DJ9qkeV5YoykFsK8ZV3DAY8GaIGf/2uQx+/yIKEN+ibBuJAah3YlYNU5YCj+Gttf6iq44QkHt195Hqg++OQek4jSEBBBlz6DnuvYT/sC3FcNtAedFbWcyU14Ax4NeuasA1my2i8ebeR4yBxI8/3yic4ysCVqw/ScFc1PM8b2JoKhNBXuulYA/sWQeBlvRrPicJcA+irjt1IxkXoWziGrs2PlBRsdv7tndHfG6X5gKbHHzd3hwTh6VCLq5JFD0a8L6EAQubJBj7T47iKJnI5yzg=";
    private final String value = "eyJ0aW1lc3RhbXAiOjE1NzA1Mjg0MjcxODYsInByb2ZpbGVJZCI6ImVkNTNkZDgxNGY5ZDRhM2NiNGViNjUxZGNiYTc3ZTY2IiwicHJvZmlsZU5hbWUiOiJGb3J5eExPTCIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGE4MmVjMWU3ODg4OTE5YzkwZDUxNThjMTZhOWZmMTMxZWFkNDQ5ZDdkMGNiOTcxYjhkYmU0ZmUxMTY5MGM3MCJ9fX0=";

    int count;
    private String mapName;

    public WorldLoader(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
    }

    public void loadLobby() {
        SpawnHandler spawnHandler = new SpawnHandler();

        Location lobby = spawnHandler.loadLocation("lobby");

        if (lobby == null) {
            this.sendErrorMessage("lobby");
            return;
        } else {
            Bukkit.createWorld(new WorldCreator(spawnHandler.getWorld("lobby")));
            lobby = spawnHandler.loadLocation("lobby");
            Spawns.LOBBY.setLocation(lobby);
        }

        lobby.getWorld().getEntities().forEach(Entity::remove);
        World world = lobby.getWorld();
        world.setSpawnLocation((int) lobby.getX(), (int) lobby.getY(), (int) lobby.getZ());
        world.setDifficulty(Difficulty.NORMAL);
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        world.setGameRule(GameRule.DO_MOB_LOOT, false);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setStorm(false);
        world.setThunderDuration(0);
        world.setThundering(false);
        world.setTime(1200);

        this.setTop3();
    }

    public void setTop3() {
        List<Document> documents = this.mlgWars.getTop(3);

        for (int i = 0; i < 3; i++) {
            Location location = this.spawnHandler.loadLocation("TOP" + (i + 1));
            if (location == null) {
                continue;
            }

            try {
                Document currentDocument = documents.get(i);
                this.setMob(location, currentDocument.getString("name"),
                        UUID.fromString(currentDocument.getString("_id")), i + 1);

            } catch (Exception ignored) {
                this.setMob(location, null, null, i + 1);
            }
        }
    }

    private void setMob(Location location, @Nullable String name, @Nullable UUID uuid, int placement) {
        NPCCreator npcCreator = new NPCCreator(location)
                .shouldImitatePlayer(false)
                .shouldLookAtPlayer(true);

        String displayName = "§cNicht besetzt";

        if(uuid != null && name != null) {
            PlayerProfile profile = Bukkit.createProfile(uuid);
            profile.complete();

            ProfileProperty profileProperty = profile.getProperties().stream()
                    .filter(profileProperty1 -> profileProperty1.getName().equalsIgnoreCase("textures"))
                    .collect(Collectors.toList()).get(0);

            npcCreator = npcCreator.createProfile(profileProperty.getValue(), profileProperty.getSignature());
            IPermissionUser iPermissionUser = CloudNetDriver.getInstance().getPermissionManagement()
                    .getUser(uuid);

            if (iPermissionUser == null) return;

            IPermissionGroup permissionGroup = CloudNetDriver.getInstance().getPermissionManagement()
                    .getHighestPermissionGroup(iPermissionUser);

            displayName = ChatColor.translateAlternateColorCodes('&', permissionGroup.getDisplay()) + name;
        } else {
            npcCreator = npcCreator.createProfile(this.value, this.signature);
        }

        npcCreator = npcCreator.addHeaders(new String[]{ "§7#" + placement + " §8● §r" + displayName });
        npcCreator.spawn();

    }

    public void setTop3Wall() {
        if (this.spawnHandler.loadLocation("topHead1") == null) {
            return;
        }

        Location titleSpawn = new Location(Bukkit.getWorld("Wartelobby"), -9, 6, 11);
        ArmorStand title = (ArmorStand) titleSpawn.getWorld().spawnEntity(titleSpawn.toCenterLocation(), EntityType.ARMOR_STAND);
        title.setCustomName("§e§lMlgWars");
        title.setInvisible(true);
        title.setGravity(false);
        title.setCustomNameVisible(true);

        ArmorStand subTitle = (ArmorStand) titleSpawn.getWorld().spawnEntity(titleSpawn.toCenterLocation()
                .clone().subtract(0, 0.25, 0), EntityType.ARMOR_STAND);
        subTitle.setCustomName("§eTop 3");
        subTitle.setInvisible(true);
        subTitle.setGravity(false);
        subTitle.setCustomNameVisible(true);

        List<Document> documents = this.mlgWars.getTop(3);
        for (int i = 0; i < 3; i++) {

            int current = i + 1;

            Location locSign = this.spawnHandler.loadLocation("topSign" + current);

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

        Location near = this.spawnHandler.loadLocation("topSign1");

        /*ArmorStand armorStandTop1 = null, armorStandTop2 = null, armorStandTop3 = null;

        for (ArmorStand armorStand : near.getNearbyEntitiesByType(ArmorStand.class, 10)) {
            if (armorStand.getItem(EquipmentSlot.HAND).getType() == Material.TNT) {
                armorStandTop1 = armorStand;
            } else if (armorStand.getItem(EquipmentSlot.HAND).getType() == Material.GRASS_BLOCK) {
                armorStandTop2 = armorStand;
            } else if (armorStand.getItem(EquipmentSlot.HAND).getType() == Material.IRON_SWORD) {
                armorStandTop3 = armorStand;
            }
        }*/

        //this.runMainTask(armorStandTop1.getLocation(), armorStandTop2.getLocation(), armorStandTop3.getLocation());

        for (Document currentDocument : documents) {
            int current = i;
            i++;

            Location locSign = this.spawnHandler.loadLocation("topSign" + current);

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

                /*ArmorStand toSet = (current == 1) ? armorStandTop1 : ((current == 2) ? armorStandTop2 : armorStandTop3);
                toSet.getItem(EquipmentSlot.HEAD).setItemMeta(skullOwner.getItemMeta());

                IPermissionUser iPermissionUser = CloudNetDriver.getInstance().getPermissionManagement()
                        .getUser(UUID.fromString(currentDocument.getString("_id")));

                if (iPermissionUser == null) return;

                IPermissionGroup permissionGroup = CloudNetDriver.getInstance().getPermissionManagement()
                        .getHighestPermissionGroup(iPermissionUser);

                permissionGroup.getDisplay();

                toSet.setCustomName("§7#" + current + " §8● §r" +
                        ChatColor.translateAlternateColorCodes('&', permissionGroup.getDisplay()) + name);
                toSet.setCustomNameVisible(true); */

            }
        }
    }

    private void runMainTask(Location location, Location location1, Location location2) {
        ArrayList<Location> armorStand1 = this.getCirclePoints(location, 0.7, 20);
        ArrayList<Location> armorStand2 = this.getCirclePoints(location1, 0.7, 20);
        ArrayList<Location> armorStand3 = this.getCirclePoints(location2, 0.7, 20);

        this.count = 0;

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this.mlgWars, () -> {
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

    private void sendErrorMessage(String error) {

        Bukkit.getConsoleSender().sendMessage(this.mlgWars.getPrefix() + String.format("§cDer Spawn '%s' existiert nicht!", error));

    }

    public String getMapName() {
        return this.mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
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
