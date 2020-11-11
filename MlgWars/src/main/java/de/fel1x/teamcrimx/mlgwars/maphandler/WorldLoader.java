package de.fel1x.teamcrimx.mlgwars.maphandler;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.IPermissionGroup;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import de.fel1x.teamcrimx.crimxapi.utils.Cuboid;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.enums.Size;
import de.fel1x.teamcrimx.mlgwars.enums.Spawns;
import de.fel1x.teamcrimx.mlgwars.objects.ScoreboardTeam;
import de.fel1x.teamcrimx.mlgwars.scoreboard.MlgWarsScoreboard;
import org.bson.Document;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WorldLoader {

    private final MlgWars mlgWars = MlgWars.getInstance();
    private final MapHandler mapHandler = new MapHandler();
    private final SpawnHandler spawnHandler = new SpawnHandler();
    private final MlgWarsScoreboard mlgWarsScoreboard = new MlgWarsScoreboard();

    private final BlockFace[] blockFaces = {
            BlockFace.NORTH,
            BlockFace.WEST,
            BlockFace.SOUTH,
            BlockFace.EAST
    };

    private String mapName;

    public WorldLoader(String mapName) {

        if (mapName == null) {
            this.mlgWars.setNoMap(true);
            return;
        }

        this.mapName = mapName;

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
        world.setDifficulty(Difficulty.EASY);
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        world.setGameRule(GameRule.DO_MOB_LOOT, false);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setStorm(false);
        world.setThunderDuration(0);
        world.setThundering(false);
        world.setTime(1200);

        Bukkit.getConsoleSender().sendMessage(this.mlgWars.getPrefix() + "§aAusgewählte Map: " + this.mapName);

        this.forceMap(this.mapName);
        this.setTop3Wall();

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

        ArmorStand armorStandTop1 = null, armorStandTop2 = null, armorStandTop3 = null;

        for (ArmorStand armorStand : near.getNearbyEntitiesByType(ArmorStand.class, 10)) {
            if(armorStand.getItem(EquipmentSlot.HAND).getType() == Material.TNT) {
                armorStandTop1 = armorStand;
            } else if(armorStand.getItem(EquipmentSlot.HAND).getType() == Material.GRASS_BLOCK) {
                armorStandTop3 = armorStand;
            } else if(armorStand.getItem(EquipmentSlot.HAND).getType() == Material.IRON_SWORD) {
                armorStandTop2 = armorStand;
            }
        }

        this.runMainTask(armorStandTop1.getLocation(), armorStandTop2.getLocation(), armorStandTop3.getLocation());

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
        ArrayList<Location> armorStand1 = this.getCirclePoints(location, 0.7, 20);
        ArrayList<Location> armorStand2 = this.getCirclePoints(location1, 0.7, 20);
        ArrayList<Location> armorStand3 = this.getCirclePoints(location2, 0.7, 20);

        this.count = 0;

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this.mlgWars, () -> {
            armorStand1.get(this.count).getWorld().spawnParticle(Particle.REDSTONE,  armorStand1.get(this.count), 2,
                    new Particle.DustOptions(Color.fromRGB(255,165,0), 1));
            armorStand3.get(this.count).getWorld().spawnParticle(Particle.REDSTONE,  armorStand3.get(this.count), 2,
                    new Particle.DustOptions(Color.fromRGB(211,211,211), 1));
            armorStand2.get(this.count).getWorld().spawnParticle(Particle.REDSTONE,  armorStand2.get(this.count), 2,
                    new Particle.DustOptions(Color.fromRGB(205, 127, 50), 1));

            this.count++;

            if(this.count > 19) {
                this.count = 0;
            }
        }, 0L, 0L);
    }

    public void forceMap(String mapName) {

        Location spectator = this.mapHandler.loadLocation(mapName, "spectator");
        if (spectator == null) {
            this.sendErrorMessage("spectator");
            this.mlgWars.setNoMap(true);
            return;
        } else {
            Bukkit.createWorld(new WorldCreator(this.mapHandler.getWorld(mapName, "spectator")));
            spectator = this.mapHandler.loadLocation(mapName, "spectator");
            Spawns.SPECTATOR.setLocation(spectator);
        }

        Spawns.SPECTATOR.getLocation().getWorld().getEntities().forEach(entity -> {
            if (!(entity instanceof ArmorStand)) {
                entity.remove();
            }
        });

        Size size;

        try {
            size = this.mapHandler.getSize(mapName);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (this.mlgWars.getTeamSize() != -1) {
            if (this.mlgWars.getTeamSize() != size.getTeamSize()) {
                Bukkit.broadcastMessage(this.mlgWars.getPrefix() + "§cKann nicht auf eine andere Teamgröße wechseln!");
                return;
            }
        }

        int totalPlayerSpawns = size.getMaxTeams();

        this.mlgWars.getData().getPlayerSpawns().clear();
        this.mlgWars.getData().getGameTeams().clear();

        if (totalPlayerSpawns > Bukkit.getServer().getMaxPlayers()) {
            if (totalPlayerSpawns < Bukkit.getOnlinePlayers().size()) {
                int count = 1;
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (count > totalPlayerSpawns) {
                        player.kickPlayer(this.mlgWars.getPrefix() + "§7Nach einem Forcemap sind nicht genügen Spawns übrig. " +
                                "Du wurdest gekickt");
                    }
                    count++;
                }
            }
        }

        for (int i = 0; i < totalPlayerSpawns; i++) {
            Location location = this.mapHandler.loadLocation(mapName, String.valueOf(i + 1));
            if (location == null) {
                this.sendErrorMessage("spawn " + (i + 1));
                this.mlgWars.setNoMap(true);
                return;
            } else {
                this.mlgWars.getData().getPlayerSpawns().add(location);
            }
        }

        Location loc1 = this.mapHandler.loadLocation(mapName, "loc1");
        Location loc2 = this.mapHandler.loadLocation(mapName, "loc2");
        Location middle1 = this.mapHandler.loadLocation(mapName, "middle1");
        Location middle2 = this.mapHandler.loadLocation(mapName, "middle2");

        if (loc1 == null || loc2 == null || middle1 == null || middle2 == null) {
            this.sendErrorMessage("map_region/middle_region");
            this.mlgWars.setNoMap(true);
            return;
        }

        Spawns.LOC_1.setLocation(loc1);
        Spawns.LOC_2.setLocation(loc2);

        this.mlgWars.getData().setMapRegion(new Cuboid(Spawns.LOC_1.getLocation(), Spawns.LOC_2.getLocation()));

        Spawns.MIDDLE_1.setLocation(middle1);
        Spawns.MIDDLE_2.setLocation(middle2);

        this.mlgWars.getData().setMiddleRegion(new Cuboid(Spawns.MIDDLE_1.getLocation(), Spawns.MIDDLE_2.getLocation()));

        World world = spectator.getWorld();
        world.setSpawnLocation((int) spectator.getX(), (int) spectator.getY(), (int) spectator.getZ());
        world.setDifficulty(Difficulty.EASY);
        world.setGameRuleValue("doMobSpawning", "false");
        world.setGameRuleValue("doMobLoot", "false");
        world.setGameRuleValue("doWeatherCycle", "false");
        world.setStorm(false);
        world.setThunderDuration(0);
        world.setThundering(false);
        world.setTime(1200);

        this.mlgWars.setTeamSize(size.getTeamSize());

        if (this.mlgWars.getTeamSize() > 1) {
            int teamSize = this.mlgWars.getTeamSize();

            for (int i = 0; i < totalPlayerSpawns; i++) {
                this.mlgWars.getData().getGameTeams().put(i, new ScoreboardTeam(i, (i + 1), teamSize, new ArrayList<>(), new ArrayList<>()));
            }
        }

        Bukkit.getConsoleSender().sendMessage(this.mlgWars.getPrefix() + "§aDie Map " + mapName +
                " wurde erfolgreich geladen");

        this.setMapName(mapName);

        this.mlgWars.getData().getPlayers().forEach(player ->
                this.mlgWarsScoreboard.updateBoard(player, "§8● §e" + this.getMapName(), "map", "§e"));

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
