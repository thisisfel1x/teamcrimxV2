package de.fel1x.teamcrimx.floorislava.listener.entity;

import de.fel1x.teamcrimx.floorislava.FloorIsLava;
import de.fel1x.teamcrimx.floorislava.gamehandler.Gamestate;
import de.fel1x.teamcrimx.floorislava.gameplayer.GamePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {
    private final FloorIsLava floorIsLava;

    public DamageListener(FloorIsLava floorIsLava) {
        this.floorIsLava = floorIsLava;
        floorIsLava.getPluginManager().registerEvents(this, floorIsLava);
    }

    @EventHandler
    public void on(EntityDamageByEntityEvent event) {
        Gamestate gamestate = this.floorIsLava.getGamestateHandler().getGamestate();

        Entity damager = event.getDamager();
        Entity victim = event.getEntity();

        switch (gamestate) {
            case IDLE: case LOBBY: case PREGAME: case ENDING:
                event.setCancelled(true);
                break;
            case FARMING:
                if(victim instanceof Player) {
                    event.setCancelled(true);
                }
                break;
            case RISING:
                if(victim instanceof Player && !this.floorIsLava.isPvpEnabled()
                        && (event.getCause() != EntityDamageEvent.DamageCause.FALL
                        || event.getCause() != EntityDamageEvent.DamageCause.LAVA)) {
                    event.setCancelled(true);
                } else if(victim instanceof Player && damager instanceof Player
                        && this.floorIsLava.isPvpEnabled()) {
                    Player victimPlayer = (Player) victim;
                    Player damagerPlayer = (Player) damager;

                    GamePlayer victimGamePlayer = new GamePlayer(victimPlayer);
                    GamePlayer damagerGamePlayer = new GamePlayer(damagerPlayer);

                    if(damagerGamePlayer.isSpectator()) {
                        event.setCancelled(true);
                        return;
                    }

                    if(victimGamePlayer.isSpectator()) {
                        event.setCancelled(true);
                    }
                }
                break;
        }

    }

    @EventHandler
    public void on(EntityDamageEvent event) {
        Gamestate gamestate = this.floorIsLava.getGamestateHandler().getGamestate();

        switch (gamestate) {
            case IDLE:
            case LOBBY:
            case PREGAME:
            case ENDING:
                event.setCancelled(true);
                break;
            case FARMING:
                if (event.getEntity() instanceof Player) {
                    event.setCancelled(true);
                }
                break;
            case RISING:
                if (event.getEntity() instanceof Player && !this.floorIsLava.isPvpEnabled()
                        && (event.getCause() != EntityDamageEvent.DamageCause.FALL
                        || event.getCause() != EntityDamageEvent.DamageCause.LAVA)) {
                    event.setCancelled(true);
                }
                break;
        }
    }
}
