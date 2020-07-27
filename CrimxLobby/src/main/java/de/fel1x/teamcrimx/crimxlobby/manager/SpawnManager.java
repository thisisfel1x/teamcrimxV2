package de.fel1x.teamcrimx.crimxlobby.manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class SpawnManager {

    private File configfile = new File("plugins/CrimxLobby/", "spawns.yml");
    private FileConfiguration config = YamlConfiguration.loadConfiguration(configfile);

    public void saveLocation(Location location, String root, Player player) {

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

        player.sendMessage("§7Du hast erfolgreich die Position §b§a" + root + " §7gesetzt!");


    }

    public Location loadLocation(String root) {

        if (configfile.exists()) {

            if (config.get(root + ".World") != null) {

                World world = Bukkit.getWorld(config.getString(root + ".World"));
                double x = config.getDouble(root + ".X");
                double y = config.getDouble(root + ".Y");
                double z = config.getDouble(root + ".Z");
                float yaw = (float) config.getDouble(root + ".Yaw");
                float pitch = (float) config.getDouble(root + ".Pitch");

                return new Location(world, x, y, z, yaw, pitch);

            } else {

                return null;

            }


        } else {
            return null;
        }

    }

}
