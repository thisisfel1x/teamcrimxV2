package de.fel1x.teamcrimx.mlgwars.listener.entity;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

    private final MlgWars mlgWars;

    public DamageListener(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
        this.mlgWars.getPluginManager().registerEvents(this, this.mlgWars);
    }

    @EventHandler
    public void on(EntityDamageByEntityEvent event) {

        Gamestate gamestate = this.mlgWars.getGamestateHandler().getGamestate();

        if(gamestate != Gamestate.INGAME) {
            event.setCancelled(true);
            return;
        }

        if(!(event.getEntity() instanceof Player)) {
            return;
        }

        if(!(event.getDamager() instanceof Player)) {
            return;
        }

        Player damager = (Player) event.getDamager();
        Player target = (Player) event.getEntity();

        GamePlayer gameDamager = new GamePlayer(damager);
        GamePlayer gameTarget = new GamePlayer(target);

        if((gameDamager.isSpectator() && gameTarget.isPlayer()) || (gameDamager.isPlayer() && gameTarget.isSpectator())) {
            event.setCancelled(true);
            return;
        }

        this.mlgWars.getData().getLastHit().put(target, damager);

    }

    @EventHandler
    public void on(EntityDamageEvent event) {

        Gamestate gamestate = this.mlgWars.getGamestateHandler().getGamestate();

        if(gamestate != Gamestate.INGAME) {
            event.setCancelled(gamestate != Gamestate.PREGAME || (event.getCause() != EntityDamageEvent.DamageCause.VOID
                    && event.getCause() != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION));
        }

    }
}
