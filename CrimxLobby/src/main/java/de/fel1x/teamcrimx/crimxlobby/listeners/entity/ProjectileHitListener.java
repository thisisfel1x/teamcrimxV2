package de.fel1x.teamcrimx.crimxlobby.listeners.entity;

import de.fel1x.teamcrimx.crimxapi.utils.InstantFirework;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class ProjectileHitListener implements Listener {

    public ProjectileHitListener(CrimxLobby crimxLobby) {
        crimxLobby.getPluginManager().registerEvents(this, crimxLobby);
    }

    @EventHandler
    public void on(ProjectileHitEvent event) {

        Entity entity = event.getEntity();
        Location location = entity.getLocation().clone().add(0, 0.5, 0);

        if (entity instanceof Snowball) {
            if (entity.hasMetadata("funGun")) {
                entity.getWorld().spawnParticle(Particle.HEART, location, 4);
                entity.getWorld().spawnParticle(Particle.LAVA, location, 4);
                entity.getWorld().spawnParticle(Particle.SNOWBALL, location, 4);
                location.getWorld().playSound(location, Sound.ENTITY_CAT_PURREOW, 3f, 0.75f);
            } else if (entity.hasMetadata("firework")) {
                new InstantFirework(FireworkEffect.builder().withColor(Color.GREEN, Color.RED, Color.BLUE, Color.YELLOW,
                        Color.AQUA, Color.ORANGE, Color.PURPLE, Color.MAROON, Color.FUCHSIA)
                        .trail(true).withFade(Color.GREEN, Color.RED, Color.BLUE, Color.YELLOW,
                                Color.AQUA, Color.ORANGE, Color.PURPLE, Color.MAROON, Color.FUCHSIA)
                        .build(), location);
                new InstantFirework(FireworkEffect.builder().withColor(Color.GREEN, Color.RED, Color.BLUE, Color.YELLOW,
                        Color.AQUA, Color.ORANGE, Color.PURPLE, Color.MAROON, Color.FUCHSIA)
                        .trail(true).withFade(Color.GREEN, Color.RED, Color.BLUE, Color.YELLOW,
                                Color.AQUA, Color.ORANGE, Color.PURPLE, Color.MAROON, Color.FUCHSIA)
                        .build(), location.clone().add(0.5, 1, 0.5));
                new InstantFirework(FireworkEffect.builder().withColor(Color.GREEN, Color.RED, Color.BLUE, Color.YELLOW,
                        Color.AQUA, Color.ORANGE, Color.PURPLE, Color.MAROON, Color.FUCHSIA)
                        .trail(true).withFade(Color.GREEN, Color.RED, Color.BLUE, Color.YELLOW,
                                Color.AQUA, Color.ORANGE, Color.PURPLE, Color.MAROON, Color.FUCHSIA)
                        .build(), location.clone().add(-0.5, 1, -0.5));
                new InstantFirework(FireworkEffect.builder().withColor(Color.GREEN, Color.RED, Color.BLUE, Color.YELLOW,
                        Color.AQUA, Color.ORANGE, Color.PURPLE, Color.MAROON, Color.FUCHSIA)
                        .trail(true).withFade(Color.GREEN, Color.RED, Color.BLUE, Color.YELLOW,
                                Color.AQUA, Color.ORANGE, Color.PURPLE, Color.MAROON, Color.FUCHSIA)
                        .build(), location.clone().add(1.5, -0.5, -1.5));
            }
        }
    }
}
