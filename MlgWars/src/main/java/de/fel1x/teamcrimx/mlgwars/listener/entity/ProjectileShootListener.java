package de.fel1x.teamcrimx.mlgwars.listener.entity;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class ProjectileShootListener implements Listener {

    private final MlgWars mlgWars;

    public ProjectileShootListener(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
        this.mlgWars.getPluginManager().registerEvents(this, this.mlgWars);
    }

    @EventHandler
    public void on(ProjectileLaunchEvent event) {

        if (!(event.getEntity().getShooter() instanceof Player)) {
            return;
        }

        if (this.mlgWars.isLabor()) {
            if (event.getEntity() instanceof Arrow) {
                Player player = (Player) event.getEntity().getShooter();
                player.getInventory().getItemInMainHand();
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                if (itemStack.getType() == Material.BOW) {
                    if (itemStack.getItemMeta().hasDisplayName()) {
                        if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("Explosionsbogen")) {
                            event.getEntity().setMetadata("explode", new FixedMetadataValue(this.mlgWars, true));
                        } else if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("TNT-Bogen")) {
                            Bukkit.getScheduler().runTaskLater(this.mlgWars, () -> {
                                TNTPrimed tntPrimed = (TNTPrimed) player.getWorld().spawnEntity(event.getEntity().getLocation().clone().add(0, 1, 0), EntityType.PRIMED_TNT);
                                tntPrimed.setFuseTicks(10);
                                tntPrimed.setIsIncendiary(true);
                                tntPrimed.setTicksLived(5);
                                tntPrimed.setVelocity(event.getEntity().getVelocity());
                                event.getEntity().addPassenger(tntPrimed);
                            }, 3L);
                        }
                    }
                }
            } else if (event.getEntity() instanceof Snowball) {
                Player player = (Player) event.getEntity().getShooter();
                player.getInventory().getItemInMainHand();
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                if (itemStack.getType() == Material.SNOWBALL) {
                    if (itemStack.getItemMeta().hasDisplayName()) {
                        if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("§cWerfbares TNT")) {
                            TNTPrimed tntPrimed = (TNTPrimed) player.getWorld().spawnEntity(event.getEntity().getLocation().clone().add(0, 2, 0), EntityType.PRIMED_TNT);
                            tntPrimed.setFuseTicks(20);
                            tntPrimed.setIsIncendiary(true);
                            tntPrimed.setTicksLived(5);
                            tntPrimed.setVelocity(event.getEntity().getVelocity());
                        }
                    }
                }
            }
        }

    }

}
