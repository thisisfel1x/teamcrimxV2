package de.fel1x.bingo.listener.entity;

import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.gamehandler.Gamestate;
import de.fel1x.bingo.objects.BingoPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

    private final Bingo bingo;

    public DamageListener(Bingo bingo) {
        this.bingo = bingo;
        bingo.getPluginManager().registerEvents(this, bingo);
    }

    @EventHandler
    public void on(EntityDamageByEntityEvent event) {

        Gamestate gamestate = this.bingo.getGamestateHandler().getGamestate();

        if (gamestate.equals(Gamestate.INGAME)) {

            if (!(event.getDamager() instanceof Player)) return;

            Player damager = (Player) event.getDamager();
            BingoPlayer bingoDamager = new BingoPlayer(damager);

            if (bingoDamager.isSpectator()) {
                event.setCancelled(true);
                return;
            }

            if (!(event.getEntity() instanceof Player)) return;

        }
        event.setCancelled(true);

    }

    @EventHandler
    public void on(EntityDamageEvent event) {

        Gamestate gamestate = this.bingo.getGamestateHandler().getGamestate();

        if (!gamestate.equals(Gamestate.INGAME)) {
            event.setCancelled(true);
        } else {
            if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                event.setCancelled(true);
            }
        }

    }

}
