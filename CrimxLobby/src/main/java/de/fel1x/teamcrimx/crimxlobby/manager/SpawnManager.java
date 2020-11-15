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

    private final File configfile = new File("plugins/CrimxLobby/", "spawns.yml");
    private final FileConfiguration config = YamlConfiguration.loadConfiguration(this.configfile);

    public void saveLocation(Location location, String root, Player player) {

        this.config.set(root + ".World", location.getWorld().getName());
        this.config.set(root + ".X", location.getX());
        this.config.set(root + ".Y", location.getY());
        this.config.set(root + ".Z", location.getZ());
        this.config.set(root + ".Yaw", location.getYaw());
        this.config.set(root + ".Pitch", location.getPitch());


        this.config.options().copyDefaults(true);

        try {
            this.config.save(this.configfile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.sendMessage("§7Du hast erfolgreich die Position §b§a" + root + " §7gesetzt!");


    }

    public Location loadLocation(String root) {

        if (this.configfile.exists()) {

            if (this.config.get(root + ".World") != null) {

                World world = Bukkit.getWorld(this.config.getString(root + ".World"));
                double x = this.config.getDouble(root + ".X");
                double y = this.config.getDouble(root + ".Y");
                double z = this.config.getDouble(root + ".Z");
                float yaw = (float) this.config.getDouble(root + ".Yaw");
                float pitch = (float) this.config.getDouble(root + ".Pitch");

                return new Location(world, x, y, z, yaw, pitch);

            } else {

                return null;

            }


        } else {
            return null;
        }

    }

}
