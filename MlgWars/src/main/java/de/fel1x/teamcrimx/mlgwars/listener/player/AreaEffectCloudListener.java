package de.fel1x.teamcrimx.mlgwars.listener.player;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;

public class AreaEffectCloudListener implements Listener {

    private final MlgWars mlgWars;

    public AreaEffectCloudListener(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
        this.mlgWars.getPluginManager().registerEvents(this, mlgWars);
    }

    @EventHandler
    public void on(AreaEffectCloudApplyEvent event) {
        for (LivingEntity entity : event.getAffectedEntities()) {
            Player player = (Player) entity;

            player.setVelocity(entity.getVelocity().setY(4f));
            Bukkit.getScheduler().runTaskLater(this.mlgWars, () -> player.setGliding(true), 20L);

            player.playSound(player.getLocation(), Sound.ITEM_ELYTRA_FLYING, 1F, 1F);
        }
    }

}
