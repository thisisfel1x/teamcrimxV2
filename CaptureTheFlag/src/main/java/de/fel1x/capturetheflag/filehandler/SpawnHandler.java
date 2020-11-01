package de.fel1x.capturetheflag.filehandler;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class SpawnHandler {

    private static File configfile = new File("plugins/CaptureTheFlag/", "spawns.yml");
    private static FileConfiguration config = YamlConfiguration.loadConfiguration(configfile);

    public static void saveLocation(Location location, String root, Player p) {

        config.set(root + ".World", location.getWorld().getName());
        config.set(root + ".X", location.getX());
        config.set(root + ".Y", location.getY());
        config.set(root + ".Z", location.getZ());
        config.set(root + ".Yaw", location.getYaw());
        config.set(root + ".Pitch", location.getPitch());


        config.options().copyDefaults(true);

        try {
            config.save(configfile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        p.sendMessage("§7Du hast erfolgreich die Position §b§a" + root + " §7gesetzt!");


    }

    public static Location loadLocation(String root) {

        if (configfile.exists()) {

            if (config.get(root + ".World") != null) {

                World world = Bukkit.getWorld(config.getString(root + ".World"));
                double x = config.getDouble(root + ".X");
                double y = config.getDouble(root + ".Y");
                double z = config.getDouble(root + ".Z");
                float yaw = (float) config.getDouble(root + ".Yaw");
                float pitch = (float) config.getDouble(root + ".Pitch");

                try {
                    return new Location(world, x, y, z, yaw, pitch);
                } catch (Exception ignored) {
                    return null;
                }

            } else {

                return null;

            }


        } else {
            return null;
        }

    }

    public static Location loadBannerLocation(String root) {

        if (configfile.exists()) {

            if (config.get(root + ".World") != null) {

                World world = Bukkit.getWorld(config.getString(root + ".World"));
                double x = config.getDouble(root + ".X");
                double y = config.getDouble(root + ".Y");
                double z = config.getDouble(root + ".Z");

                try {
                    return new Location(world, x, y, z);
                } catch (Exception ignored) {
                    return null;
                }

            } else {

                return null;

            }


        } else {
            return null;
        }

    }

    public static String getWorld(String root) {

        return config.getString(root + ".World");

    }

}
