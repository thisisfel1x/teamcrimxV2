package de.fel1x.teamcrimx.mlgwars.listener.entity;

import de.fel1x.teamcrimx.crimxapi.utils.Cuboid;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.utils.entites.ZombieEquipment;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class ProjectileHitListener implements Listener {

    private final MlgWars mlgWars;
    private final ZombieEquipment zombieEquipment = new ZombieEquipment();

    private final Random random = new Random();

    public ProjectileHitListener(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
        this.mlgWars.getPluginManager().registerEvents(this, this.mlgWars);
    }

    @EventHandler
    public void on(ProjectileHitEvent event) {

        if (event.getEntity() instanceof Egg) {
            Egg egg = (Egg) event.getEntity();

            if (egg.hasMetadata("webTrap")) {
                Location location = egg.getLocation();
                if (location.getY() < 0) return;
                Cuboid cuboid = new Cuboid(location.clone().add(1, 3, 1),
                        location.clone().subtract(1, 1, 1));

                cuboid.getBlocks().forEach(block -> {
                    if (block.getType() != Material.AIR) return;
                    boolean shouldPlace = ThreadLocalRandom.current().nextBoolean();
                    if (shouldPlace) {
                        block.setType(Material.COBWEB);
                    }
                });
            }

        } else if (event.getEntity() instanceof Fireball) {
            Fireball fireball = (Fireball) event.getEntity();

            if (fireball.hasMetadata("moli")) {

                Location a = fireball.getLocation().clone().add(3, 3, 3);
                Location b = fireball.getLocation().clone().subtract(3, 3, 3);
                Cuboid cuboid = new Cuboid(a, b);
                for (Block block : cuboid.getBlocks()) {
                    if (block.getType() != Material.AIR) continue;
                    boolean replace = ThreadLocalRandom.current().nextBoolean();
                    if (replace) {
                        block.setType(Material.FIRE);
                    }
                }
            }
        } else if (event.getEntity() instanceof Snowball) {
            Snowball snowball = (Snowball) event.getEntity();

            if (snowball.hasMetadata("nuts")) {
                snowball.getPassengers().forEach(Entity::remove);

                snowball.getWorld().createExplosion(snowball, 2F, true, false);

                for (Player player : snowball.getWorld().getNearbyPlayers(snowball.getLocation(), 5, 5)
                        .stream().filter(player -> !player.equals(snowball.getShooter())).collect(Collectors.toList())) {

                    Vector vector = player.getVelocity();
                    vector.multiply(2).setY(5).multiply(2);
                    player.setVelocity(vector);
                }
            }
        }

    }

}
