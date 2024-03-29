package de.fel1x.capturetheflag.utils.particles;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

import java.util.ArrayList;

public class ParticleEffects {

    public static void drawBannerCircle(Location center, double radius, int amount) {

        World world = center.getWorld();
        double increment = (2 * Math.PI) / amount;

        for (int i = 0; i < amount; i++) {

            double angle = i * increment;
            double x = center.getX() + (radius * Math.cos(angle));
            double z = center.getZ() + (radius * Math.sin(angle));

            Location particle = new Location(world, x, center.getY(), z);
            particle.getWorld().spawnParticle(Particle.SPELL_WITCH, center, 1, 0, 0, 0);
        }
    }

    public static ArrayList<Location> getCirclePoints(Location center, double radius, int amount) {

        World world = center.getWorld();
        double increment = (2 * Math.PI) / amount;

        ArrayList<Location> locations = new ArrayList<>();

        for (int i = 0; i < amount; i++) {

            double angle = i * increment;
            double x = center.getX() + (radius * Math.cos(angle));
            double z = center.getZ() + (radius * Math.sin(angle));

            Location particle = new Location(world, x, center.getY(), z);
            locations.add(particle);

        }

        return locations;

    }

}
