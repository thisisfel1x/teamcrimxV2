package de.fel1x.teamcrimx.crimxapi.utils.math;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;

public class MathUtils {

    public static ArrayList<Location> getSphereLocations(Location center, double radius, int amount, int height) {

        ArrayList<Location> sphereLocations = new ArrayList<>();

        World world = center.getWorld();

        for(int sphereHeight = 0; sphereHeight < height; sphereHeight++) {
            double increment = (2 * Math.PI) / amount;

            for (int i = 1; i <= amount; i++) {

                double angle = (i * increment) / i;
                double x = center.getX() + (radius * Math.cos(angle));
                double z = center.getZ() + (radius * Math.sin(angle));

                sphereLocations.add(new Location(world, x, center.getY() + sphereHeight, z));

            }
        }

        return sphereLocations;

    }

}
