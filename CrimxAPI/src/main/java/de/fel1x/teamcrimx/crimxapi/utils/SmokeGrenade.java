package de.fel1x.teamcrimx.crimxapi.utils;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class SmokeGrenade extends BukkitRunnable {

    private long lifeTime; // how long the smoke grenade effect should stay
    private final World world; // World to display

    private final ArrayList<Location> locations; // Coordinates for the smoke ball

    public SmokeGrenade(JavaPlugin plugin, Location location, long lifeTime, World world) { // lifeTime here is in seconds as well
        this.lifeTime = lifeTime * 20; // cache life time and set it to ticks
        // the smoke grenade's center location
        Location location1 = location.clone().add(0, 1, 0); // cache center location and move it 1 up to show more smoke.
        // the plugin that spawned this smoke grenade
        this.world = world; // cache plugin
        runTaskTimer(plugin, 0L, 1L); // start BukkitRunnable to spawn smoke every tick

        this.locations = new ArrayList<>();

        for (double i = 0; i <= Math.PI; i += Math.PI / 35) {
            double radius = Math.sin(i);
            double y = Math.cos(i);
            for (double a = 0; a < Math.PI * 2; a += Math.PI / 25) {
                double x = Math.cos(a) * radius;
                double z = Math.sin(a) * radius;
                this.locations.add(location1.clone().add(x, y, z));
            }
        }
    }

    @Override
    public void run() {
        if (this.lifeTime == 0) { // smoke grenade should stop living at this point
            cancel(); // cancel BukkitRunnable to stop spawning smoke.
            return;
        }

        for (Location effectLocation : this.locations) {
            this.world.playEffect(effectLocation, Effect.SMOKE, 0);
        }

        this.lifeTime--; // decrease lifeTime 1 tick because 1 tick has passed.
    }
}
