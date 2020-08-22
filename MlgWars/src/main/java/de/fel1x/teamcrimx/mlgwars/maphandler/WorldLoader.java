package de.fel1x.teamcrimx.mlgwars.maphandler;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.fel1x.teamcrimx.crimxapi.utils.Cuboid;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.enums.Size;
import de.fel1x.teamcrimx.mlgwars.enums.Spawns;
import de.fel1x.teamcrimx.mlgwars.objects.ScoreboardTeam;
import de.fel1x.teamcrimx.mlgwars.scoreboard.MlgWarsScoreboard;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.TileEntitySkull;
import org.bson.Document;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        world.setGameRuleValue("doMobSpawning", "false");
        world.setGameRuleValue("doMobLoot", "false");
        world.setGameRuleValue("doWeatherCycle", "false");
        world.setStorm(false);
        world.setThunderDuration(0);
        world.setThundering(false);
        world.setTime(1200);

        Bukkit.getConsoleSender().sendMessage(this.mlgWars.getPrefix() + "§aAusgewählte Map: " + this.mapName);

        this.forceMap(this.mapName);
        this.setTop5Wall();

    }

    public void setTop5Wall() {
        if (this.spawnHandler.loadLocation("topHead1") == null) {
            return;
        }

        List<Document> documents = this.mlgWars.getTop(5);
        for (int i = 0; i < 5; i++) {

            int current = i + 1;

            Location locHead = this.spawnHandler.loadLocation("topHead" + current);
            Location locSign = this.spawnHandler.loadLocation("topSign" + current);

            Skull skull = (Skull) locHead.getBlock().getState();

            // AVOIDING WEIRD MISPLACEMENT BUG
            for (BlockFace blockFace : this.blockFaces) {
                skull.setRotation(blockFace);
                skull.update();
            }

            if (locSign.getBlock().getState() instanceof Sign) {
                Sign sign = (Sign) locSign.getBlock().getState();

                sign.setLine(0, "§7---------------");
                sign.setLine(1, "§cNicht");
                sign.setLine(2, "§cbesetzt");
                sign.setLine(3, "§7---------------");

                sign.update();
            }

            skull.setRotation(this.spawnHandler.getSignFace("topHead" + current));
            skull.setOwner("MHF_Question");
            skull.update();

        }

        int i = 1;

        for (Document currentDocument : documents) {
            int current = i;
            i++;

            Location locHead = this.spawnHandler.loadLocation("topHead" + current);
            Location locSign = this.spawnHandler.loadLocation("topSign" + current);

            Skull skull = (Skull) locHead.getBlock().getState();

            if (locSign.getBlock().getState() instanceof Sign) {
                Sign sign = (Sign) locSign.getBlock().getState();

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
                sign.setLine(1, currentDocument.getString("name"));
                sign.setLine(2, gamesWon + " Wins");
                sign.setLine(3, "K/D: " + (kd / 100));

                sign.update();
            }

            skull.setRotation(this.spawnHandler.getSignFace("topHead" + current));
            skull.setOwner(currentDocument.getString("name"));

            TileEntitySkull skullTile = (TileEntitySkull) ((CraftWorld) skull.getWorld()).getHandle().getTileEntity(new BlockPosition(skull.getX(), skull.getY(), skull.getZ()));
            GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
            String textures = Objects.requireNonNull(this.mlgWars.getCrimxAPI().getMongoDB().getUserCollection()
                    .find(new Document("_id", currentDocument.getString("_id"))).first()).getString("skinTexture");
            gameProfile.getProperties().put("textures", new Property("textures",
                    Base64Coder.encodeString(textures)));
            skullTile.setGameProfile(gameProfile);

            skull.update();
        }

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
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }
}
