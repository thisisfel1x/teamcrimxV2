package de.fel1x.teamcrimx.mlgwars.maphandler;

import de.fel1x.teamcrimx.mlgwars.enums.Size;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class MapHandler {

    private File configfile;
    private FileConfiguration config;

    public void createMap(String name, Size size) {

        configfile = new File("plugins/MlgWars/maps", name + ".yml");
        config = YamlConfiguration.loadConfiguration(configfile);

        config.set("name", name);
        config.set("size", size.getName());

        try {
            this.save();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void save() throws IOException {
        config.save(configfile);
    }

    public void saveLocation(Location location, String mapName, String root, Player player) {

        configfile = new File("plugins/MlgWars/maps", mapName + ".yml");
        config = YamlConfiguration.loadConfiguration(configfile);

        config.set(root + ".World", location.getWorld().getName());
        config.set(root + ".X", location.getX());
        config.set(root + ".Y", location.getY());
        config.set(root + ".Z", location.getZ());
        config.set(root + ".Yaw", location.getYaw());
        config.set(root + ".Pitch", location.getPitch());
        config.set(root, location);


        config.options().copyDefaults(true);

        try {
            config.save(configfile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.sendMessage("§7You have successfully set the §b§a" + root + " §7position!");


    }

    public Location loadLocation(String mapName, String id) {

        configfile = new File("plugins/MlgWars/maps", mapName + ".yml");
        config = YamlConfiguration.loadConfiguration(configfile);

        config.options().copyDefaults(true);

        if (configfile.exists()) {

            try {
                config.load(configfile);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }

            if (config.get(id) != null) {
                World world = Bukkit.getWorld(Objects.requireNonNull(config.getString(id + ".World")));
                double x = config.getDouble(id + ".X");
                double y = config.getDouble(id + ".Y");
                double z = config.getDouble(id + ".Z");
                float yaw = (float) config.getDouble(id + ".Yaw");
                float pitch = (float) config.getDouble(id + ".Pitch");

                return new Location(world, x, y, z, yaw, pitch);

            } else {
                return null;
            }

        } else {
            return null;
        }

    }

    public Size getSize(String mapName) {

        configfile = new File("plugins/MlgWars/maps", mapName + ".yml");
        config = YamlConfiguration.loadConfiguration(configfile);

        if (configfile.exists()) {

            for (Size size : Size.values()) {
                if (size.getName().equals(config.get("size"))) {
                    return size;
                }
            }
            return null;

        } else {
            return null;
        }
    }

    public String getWorld(String mapName, String root) {

        configfile = new File("plugins/MlgWars/maps", mapName + ".yml");
        return config.getString(root + ".World");

    }

    public boolean exists(String mapName) {

        configfile = new File("plugins/MlgWars/maps", mapName + ".yml");
        return (configfile.exists());

    }

}

