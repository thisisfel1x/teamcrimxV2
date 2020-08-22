package de.fel1x.teamcrimx.crimxapi.utils;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class SmokeGrenade extends BukkitRunnable {

    private long lifeTime; // how long the smoke grenade effect should stay
    private Location location; // the smoke grenade's center location
    private JavaPlugin plugin; // the plugin that spawned this smoke grenade
    private World world; // World to display

    private ArrayList<Location> locations; // Coordinates for the smoke ball

    public SmokeGrenade(JavaPlugin plugin, Location location, long lifeTime, World world) { // lifeTime here is in seconds as well
        this.lifeTime = lifeTime * 20; // cache life time and set it to ticks
        this.location = location.clone().add(0, 1, 0); // cache center location and move it 1 up to show more smoke.
        this.plugin = plugin; // cache plugin
        this.world = world; // cache plugin
        runTaskTimer(plugin, 0L, 1L); // start BukkitRunnable to spawn smoke every tick

        this.locations = new ArrayList<>();

        for (double i = 0; i <= Math.PI; i += Math.PI / 35) {
            double radius = Math.sin(i);
            double y = Math.cos(i);
            for (double a = 0; a < Math.PI * 2; a += Math.PI / 25) {
                double x = Math.cos(a) * radius;
                double z = Math.sin(a) * radius;
                this.locations.add(this.location.clone().add(x, y, z));
            }
        }
    }

    @Override
    public void run() {
        if (lifeTime == 0) { // smoke grenade should stop living at this point
            cancel(); // cancel BukkitRunnable to stop spawning smoke.
            return;
        }

        for (Location effectLocation : locations) {
            world.playEffect(effectLocation, Effect.LARGE_SMOKE, 0);
        }

        lifeTime--; // decrease lifeTime 1 tick because 1 tick has passed.
    }
}
