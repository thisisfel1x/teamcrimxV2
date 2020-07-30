package de.fel1x.teamcrimx.mlgwars.maphandler;

import de.fel1x.teamcrimx.crimxapi.utils.Cuboid;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.enums.Size;
import de.fel1x.teamcrimx.mlgwars.enums.Spawns;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import java.io.File;
import java.util.Random;

public class WorldLoader {

    private final MlgWars mlgWars = MlgWars.getInstance();
    private final MapHandler mapHandler = new MapHandler();
    private String mapName;

    public WorldLoader() {

        SpawnHandler spawnHandler = new SpawnHandler();

        Location lobby = spawnHandler.loadLocation("lobby");

        if(lobby == null) {
            this.sendErrorMessage("lobby");
            return;
        } else {
            Bukkit.createWorld(new WorldCreator(spawnHandler.getWorld("lobby")));
            lobby = spawnHandler.loadLocation("lobby");
            Spawns.LOBBY.setLocation(lobby);
        }

        Spawns.LOBBY.getLocation().getWorld().getEntities().forEach(Entity::remove);

        File[] files = new File("plugins/MlgWars/maps").listFiles();
        Random rand = new Random();

        if (files.length == 0) {
            Bukkit.getConsoleSender().sendMessage(this.mlgWars.getPrefix() + "§cKeine Map gefunden! Bitte erstellen!");
            this.mlgWars.setNoMap(true);
            return;
        }

        File file = files[rand.nextInt(files.length)];
        mapName = FilenameUtils.removeExtension(file.getName());

        Bukkit.getConsoleSender().sendMessage(this.mlgWars.getPrefix() + "§aAusgewählte Map: " + mapName);

        this.forceMap(mapName);

    }

    public void forceMap(String mapName) {

        Location spectator = this.mapHandler.loadLocation(mapName, "spectator");
        if(spectator == null) {
            this.sendErrorMessage("spectator");
            this.mlgWars.setNoMap(true);
            return;
        } else {
            Bukkit.createWorld(new WorldCreator(this.mapHandler.getWorld(mapName, "spectator")));
            spectator = this.mapHandler.loadLocation(mapName, "spectator");
            Spawns.SPECTATOR.setLocation(spectator);
        }

        Spawns.SPECTATOR.getLocation().getWorld().getEntities().forEach(entity -> {
            if(!(entity instanceof ArmorStand)) {
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

        int totalPlayerSpawns = size.getSize();

        for(int i = 0; i < totalPlayerSpawns; i++) {
            Location location = this.mapHandler.loadLocation(mapName, String.valueOf(i + 1));
            if(location == null) {
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

        if(loc1 == null || loc2 == null || middle1 == null || middle2 == null) {
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

        Bukkit.getConsoleSender().sendMessage(this.mlgWars.getPrefix() + "§aDie Map " + mapName +
                " wurde erfolgreich geladen");

        this.mlgWars.setMapName(mapName);

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
