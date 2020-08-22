package de.fel1x.teamcrimx.mlgwars.listener.entity;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import de.fel1x.teamcrimx.mlgwars.utils.entites.CustomZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;

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

        Player target = (Player) event.getEntity();

        if(event.getDamager() instanceof CustomZombie) {
            target.setMetadata("lastZombieHit", new FixedMetadataValue(this.mlgWars, event.getEntity().getCustomName()));
            return;
        }

        if(!(event.getDamager() instanceof Player) && !(event.getDamager() instanceof Projectile)) {
            return;
        }

        Player damager = null;

        if(event.getDamager() instanceof Player) {
            damager = (Player) event.getDamager();
        } else if(event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();
            if(projectile.getShooter() instanceof Player) {
                damager = (Player) projectile.getShooter();
            }
        }

        if(damager == null) {
            return;
        }

        GamePlayer gameDamager = new GamePlayer(damager);
        GamePlayer gameTarget = new GamePlayer(target);

        if((gameDamager.isSpectator() && gameTarget.isPlayer()) || (gameDamager.isPlayer() && gameTarget.isSpectator())) {
            event.setCancelled(true);
            return;
        }

        if(this.mlgWars.getTeamSize() > 1) {
            if(target.hasMetadata("team") && damager.hasMetadata("team")) {

                int targetTeam = target.getMetadata("team").get(0).asInt();
                int damagerTeam = damager.getMetadata("team").get(0).asInt();

                if (this.mlgWars.getData().getGameTeams().get(targetTeam).getId() == this.mlgWars.getData().getGameTeams().get(damagerTeam).getId()) {
                    event.setCancelled(true);
                    return;
                }
            }
        }

        this.mlgWars.getData().getLastHit().put(target, damager);

    }

    @EventHandler
    public void on(EntityDamageEvent event) {

        Gamestate gamestate = this.mlgWars.getGamestateHandler().getGamestate();

        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            GamePlayer gamePlayer = new GamePlayer(player);
            if(gamePlayer.isSpectator()) {
                event.setCancelled(true);
                return;
            }
        }

        if(gamestate != Gamestate.INGAME) {
            event.setCancelled(gamestate != Gamestate.PREGAME || (event.getCause() != EntityDamageEvent.DamageCause.VOID
                    && event.getCause() != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION
                    && event.getCause() != EntityDamageEvent.DamageCause.BLOCK_EXPLOSION));
        }

        if(event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION
                || event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
            event.setDamage(0.1D);
        }

        if(this.mlgWars.isLabor()) {
            if(event.getEntity() instanceof Player) {
                if(event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK
                        && event.getCause() != EntityDamageEvent.DamageCause.VOID) {
                    event.setDamage(0.1D);
                }
            }
        }

    }
}
