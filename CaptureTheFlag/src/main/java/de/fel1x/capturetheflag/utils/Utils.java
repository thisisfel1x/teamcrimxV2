package de.fel1x.capturetheflag.utils;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Random;

public class Utils {

    public static void spawnCircle(Location center, double radius, int amount) {

        World world = center.getWorld();
        double increment = (2 * Math.PI) / amount;
        for (int i = 0; i < amount; i++) {

            double angle = i * increment;
            double x = center.getX() + (radius * Math.cos(angle));
            double z = center.getZ() + (radius * Math.sin(angle));

            Location current = new Location(world, x, center.getY(), z);

            Firework firework = (Firework) current.getWorld().spawnEntity(current, EntityType.FIREWORK);

            FireworkMeta fireworkMeta = firework.getFireworkMeta();

            Random r = new Random();

            fireworkMeta.setPower(2);
            fireworkMeta.addEffect(FireworkEffect.builder().withColor(Color.fromBGR(r.nextInt(255), r.nextInt(255), r.nextInt(255))).flicker(true).build());
            firework.setFireworkMeta(fireworkMeta);

        }
    }

}
