package de.fel1x.bingo.listener.block;

import de.fel1x.bingo.Bingo;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class BlockTransformListener implements Listener {

    Bingo bingo;

    public BlockTransformListener(Bingo bingo) {
        this.bingo = bingo;
        bingo.getPluginManager().registerEvents(this, bingo);
    }

    @EventHandler
    public void on(EntityChangeBlockEvent event) {

        Entity entity = event.getEntity();

        if (event.getEntity() instanceof FallingBlock) {

            if (this.bingo.getFallingAnvils().contains(entity)) {
                event.getBlock().setType(Material.AIR);
                entity.remove();
                event.getBlock().getDrops().clear();
                event.setCancelled(true);
                event.getEntity().getNearbyEntities(0, 1, 0).forEach(nearbyEntities -> {
                    if (!(nearbyEntities instanceof Player)) return;

                    Player player = (Player) nearbyEntities;
                    if (!player.isDead()) {
                        player.setHealth(0D);

                        Bukkit.broadcastMessage(this.bingo.getPrefix() + player.getDisplayName() + " §7wurde von einem Amboss zerquetscht");
                    }

                });

                this.bingo.getFallingAnvils().remove(entity);

            } else if (this.bingo.getFallingGlassBlocks().contains(entity)) {

                event.setCancelled(true);

                event.getBlock().setType(Material.AIR);
                this.bingo.getFallingGlassBlocks().remove(entity);

            }

        }

    }

}
