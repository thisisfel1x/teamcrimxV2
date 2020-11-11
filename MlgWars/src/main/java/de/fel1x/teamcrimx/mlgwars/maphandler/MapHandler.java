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

        this.configfile = new File("plugins/MlgWars/maps", name + ".yml");
        this.config = YamlConfiguration.loadConfiguration(this.configfile);

        this.config.set("name", name);
        this.config.set("size", size.getName());

        try {
            this.save();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void save() throws IOException {
        this.config.save(this.configfile);
    }

    public void saveLocation(Location location, String mapName, String root, Player player) {

        this.configfile = new File("plugins/MlgWars/maps", mapName + ".yml");
        this.config = YamlConfiguration.loadConfiguration(this.configfile);

        this.config.set(root + ".World", location.getWorld().getName());
        this.config.set(root + ".X", location.getX());
        this.config.set(root + ".Y", location.getY());
        this.config.set(root + ".Z", location.getZ());
        this.config.set(root + ".Yaw", location.getYaw());
        this.config.set(root + ".Pitch", location.getPitch());
        this.config.set(root, location);


        this.config.options().copyDefaults(true);

        try {
            this.config.save(this.configfile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.sendMessage("§7You have successfully set the §b§a" + root + " §7position!");

    }

    public Location loadLocation(String mapName, String id) {

        this.configfile = new File("plugins/MlgWars/maps", mapName + ".yml");
        this.config = YamlConfiguration.loadConfiguration(this.configfile);

        this.config.options().copyDefaults(true);

        if (this.configfile.exists()) {

            try {
                this.config.load(this.configfile);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }

            if (this.config.get(id) != null) {
                World world = Bukkit.getWorld(Objects.requireNonNull(this.config.getString(id + ".World")));
                double x = this.config.getDouble(id + ".X");
                double y = this.config.getDouble(id + ".Y");
                double z = this.config.getDouble(id + ".Z");
                float yaw = (float) this.config.getDouble(id + ".Yaw");
                float pitch = (float) this.config.getDouble(id + ".Pitch");

                return new Location(world, x, y, z, yaw, pitch);

            } else {
                return null;
            }

        } else {
            return null;
        }

    }

    public Size getSize(String mapName) {

        this.configfile = new File("plugins/MlgWars/maps", mapName + ".yml");
        this.config = YamlConfiguration.loadConfiguration(this.configfile);

        if (this.configfile.exists()) {

            for (Size size : Size.values()) {
                if (size.getName().equals(this.config.get("size"))) {
                    return size;
                }
            }
            return null;

        } else {
            return null;
        }
    }

    public String getWorld(String mapName, String root) {

        this.configfile = new File("plugins/MlgWars/maps", mapName + ".yml");
        return this.config.getString(root + ".World");

    }

    public boolean exists(String mapName) {

        this.configfile = new File("plugins/MlgWars/maps", mapName + ".yml");
        return (this.configfile.exists());

    }

}

