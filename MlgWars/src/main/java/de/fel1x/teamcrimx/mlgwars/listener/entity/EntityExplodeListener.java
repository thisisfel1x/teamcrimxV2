package de.fel1x.teamcrimx.mlgwars.listener.entity;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class EntityExplodeListener implements Listener {

    private final MlgWars mlgWars;

    private final Collection<ItemStack> drops = new ArrayList<>();

    public EntityExplodeListener(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
        this.mlgWars.getPluginManager().registerEvents(this, this.mlgWars);

        Random random = new Random();
        this.drops.add(new ItemBuilder(Material.TNT, 3 + random.nextInt(5))
                .setName("§cInstant TNT")
                .toItemStack());
        this.drops.add(new ItemBuilder(Material.TNT, random.nextInt(3) + 1)
                .setName("§cVelocity TNT").addGlow()
                .toItemStack());
        this.drops.add(new ItemBuilder(Material.TNT, 1)
                .setName("§cInstant TNT Boost").addGlow()
                .toItemStack());
    }

    @EventHandler
    public void on(EntityExplodeEvent event) {

        Entity entity = event.getEntity();

        if (this.mlgWars.isLabor()) {
            if (entity instanceof TNTPrimed) {
                if (entity.hasMetadata("velocity")) {
                    for (Entity nearbyEntity : entity.getNearbyEntities(3, 3, 3)) {
                        if (nearbyEntity instanceof Player) {
                            Player player = (Player) nearbyEntity;
                            player.setVelocity(player.getLocation().getDirection().multiply(5D).setY(1.5D));
                        } else {
                            nearbyEntity.setVelocity(nearbyEntity.getVelocity().multiply(5D).setY(1.5D));
                        }
                    }
                }
            }

            for (Block block : event.blockList()) {
                block.getDrops().clear();
                block.getDrops().addAll(this.drops);
            }
        }
    }
}
