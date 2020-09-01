package de.fel1x.capturetheflag.listener.entity;

import de.fel1x.capturetheflag.CaptureTheFlag;
import de.fel1x.capturetheflag.gameplayer.GamePlayer;
import de.fel1x.capturetheflag.gamestate.Gamestate;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

    @EventHandler
    public void on(EntityDamageByEntityEvent event) {

        Gamestate gamestate = CaptureTheFlag.getInstance().getGamestateHandler().getGamestate();

        if (!gamestate.equals(Gamestate.INGAME)) {
            event.setCancelled(true);
            return;
        }

        if (event.getDamager() instanceof Arrow) {
            if (event.getEntity() instanceof Player) {
                Arrow arrow = (Arrow) event.getDamager();

                if (arrow.getShooter() instanceof Player) {

                    Player damager = (Player) arrow.getShooter();
                    Player victim = (Player) event.getEntity();

                    GamePlayer damageGamePlayer = new GamePlayer(damager);
                    GamePlayer victimGamePlayer = new GamePlayer(victim);

                    if (!damageGamePlayer.isPlayer()) {
                        event.setCancelled(true);
                        return;
                    }

                    if (!victimGamePlayer.isPlayer()) {
                        event.setCancelled(true);
                        return;
                    }

                    if (damageGamePlayer.getTeam().equals(victimGamePlayer.getTeam())) {
                        event.setCancelled(true);
                        return;
                    }

                    CaptureTheFlag.getInstance().getData().getLastHit().put(victim, damager);

                }
            }
        }

        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;

        Player damager = (Player) event.getDamager();
        Player victim = (Player) event.getEntity();

        GamePlayer damageGamePlayer = new GamePlayer(damager);
        GamePlayer victimGamePlayer = new GamePlayer(victim);

        if (!damageGamePlayer.isPlayer()) {
            event.setCancelled(true);
            return;
        }

        if (!victimGamePlayer.isPlayer()) {
            event.setCancelled(true);
            return;
        }

        if (damageGamePlayer.getTeam().equals(victimGamePlayer.getTeam())) {
            event.setCancelled(true);
            return;
        }

        CaptureTheFlag.getInstance().getData().getLastHit().put(victim, damager);

    }

    @EventHandler
    public void on(EntityDamageEvent event) {

        Gamestate gamestate = CaptureTheFlag.getInstance().getGamestateHandler().getGamestate();

        if (!gamestate.equals(Gamestate.INGAME)) {
            event.setCancelled(true);
        }

    }


}
