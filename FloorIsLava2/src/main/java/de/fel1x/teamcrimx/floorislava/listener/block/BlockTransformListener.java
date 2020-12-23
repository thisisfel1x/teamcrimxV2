package de.fel1x.teamcrimx.floorislava.listener.block;

import de.fel1x.teamcrimx.floorislava.FloorIsLava;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class BlockTransformListener implements Listener {
    final FloorIsLava floorIsLava;

    public BlockTransformListener(FloorIsLava floorIsLava) {
        this.floorIsLava = floorIsLava;
        floorIsLava.getPluginManager().registerEvents(this, floorIsLava);
    }

    @EventHandler
    public void on(EntityChangeBlockEvent event) {
        /* Entity entity = event.getEntity();
        if (event.getEntity() instanceof org.bukkit.entity.FallingBlock &&
                this.floorIsLava.getFallingAnvils().contains(entity)) {
            event.getBlock().setType(Material.AIR);
            event.setCancelled(true);
            event.getEntity().getNearbyEntities(2.0D, 1.0D, 2.0D).forEach(nearbyEntities -> {
                if (nearbyEntities instanceof Player) {
                    Player player = (Player) nearbyEntities;
                    if (!player.isDead()) {
                        player.setHealth(player.getHealth() - 1.0D);
                        Bukkit.broadcastMessage(this.floorIsLava.getPrefix() + player.getDisplayName() + " §7wurde von einem Amboss zerquetscht");
                    }
                } else if(nearbyEntities instanceof FallingBlock) {
                    if(entity.hasMetadata("custom")) {
                        entity.remove();
                    }
                }
            });
            this.floorIsLava.getFallingAnvils().remove(entity);
        } */
    }
}
