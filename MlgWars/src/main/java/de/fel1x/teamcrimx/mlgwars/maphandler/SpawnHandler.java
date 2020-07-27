package de.fel1x.teamcrimx.mlgwars.maphandler;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class SpawnHandler {

    private File configfile = new File("plugins/MlgWars/", "spawns.yml");
    private FileConfiguration config = YamlConfiguration.loadConfiguration(configfile);

    public void save() throws IOException {
        config.save(configfile);
    }

    @Deprecated
    public void saveLocation(Location location, String root, Player player, Sign sign) {

        config.set(root + ".World", location.getWorld().getName());
        config.set(root + ".X", location.getX());
        config.set(root + ".Y", location.getY());
        config.set(root + ".Z", location.getZ());
        config.set(root + ".Yaw", location.getYaw());
        config.set(root + ".Pitch", location.getPitch());
        config.set(root + ".Face", sign.getBlock().getFace(sign.getBlock()).toString());

        config.options().copyDefaults(true);

        try {
            config.save(configfile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.sendMessage("§7You have successfully set the §b§a" + root + " §7position!");


    }

    public void saveLocation(Location location, String root, Player player, BlockFace blockFace) {

        config.set(root + ".World", location.getWorld().getName());
        config.set(root + ".X", location.getX());
        config.set(root + ".Y", location.getY());
        config.set(root + ".Z", location.getZ());
        config.set(root + ".Yaw", location.getYaw());
        config.set(root + ".Pitch", location.getPitch());
        config.set(root + ".Face", blockFace.toString());

        config.options().copyDefaults(true);

        try {
            config.save(configfile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.sendMessage("§b§a" + root + " §7erfolgreich gesetzt!");


    }

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

        player.sendMessage("§b§a" + root + " §7erfolgreich gesetzt!");


    }

    public Location loadLocation(String root) {

        if (configfile.exists()) {
            try {
                config.load(configfile);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }

            if (config.getString(root + ".World") != null) {
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

    public String getWorld(String root) {

        return config.getString(root + ".World");

    }

    public BlockFace getSignFace(String root) {

        return BlockFace.valueOf(config.getString(root + ".Face"));

    }

}
