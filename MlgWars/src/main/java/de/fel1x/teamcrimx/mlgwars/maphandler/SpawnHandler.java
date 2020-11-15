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

    private final File configfile = new File("plugins/MlgWars/", "spawns.yml");
    private final FileConfiguration config = YamlConfiguration.loadConfiguration(this.configfile);

    public void save() throws IOException {
        this.config.save(this.configfile);
    }

    @Deprecated
    public void saveLocation(Location location, String root, Player player, Sign sign) {

        this.config.set(root + ".World", location.getWorld().getName());
        this.config.set(root + ".X", location.getX());
        this.config.set(root + ".Y", location.getY());
        this.config.set(root + ".Z", location.getZ());
        this.config.set(root + ".Yaw", location.getYaw());
        this.config.set(root + ".Pitch", location.getPitch());
        this.config.set(root + ".Face", sign.getBlock().getFace(sign.getBlock()).toString());

        this.config.options().copyDefaults(true);

        try {
            this.config.save(this.configfile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.sendMessage("§7You have successfully set the §b§a" + root + " §7position!");


    }

    public void saveLocation(Location location, String root, Player player, BlockFace blockFace) {

        this.config.set(root + ".World", location.getWorld().getName());
        this.config.set(root + ".X", location.getX());
        this.config.set(root + ".Y", location.getY());
        this.config.set(root + ".Z", location.getZ());
        this.config.set(root + ".Yaw", location.getYaw());
        this.config.set(root + ".Pitch", location.getPitch());
        this.config.set(root + ".Face", blockFace.toString());

        this.config.options().copyDefaults(true);

        try {
            this.config.save(this.configfile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.sendMessage("§b§a" + root + " §7erfolgreich gesetzt!");


    }

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

        player.sendMessage("§b§a" + root + " §7erfolgreich gesetzt!");


    }

    public Location loadLocation(String root) {

        if (this.configfile.exists()) {
            try {
                this.config.load(this.configfile);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }

            if (this.config.getString(root + ".World") != null) {
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

    public String getWorld(String root) {

        return this.config.getString(root + ".World");

    }

    public BlockFace getSignFace(String root) {

        return BlockFace.valueOf(this.config.getString(root + ".Face"));

    }

}
