package de.fel1x.teamcrimx.mlgwars.listener.entity;

import de.fel1x.teamcrimx.crimxapi.utils.Actionbar;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import de.fel1x.teamcrimx.mlgwars.objects.ScoreboardTeam;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

public class EntityTargetListener implements Listener {

    private MlgWars mlgWars;

    public EntityTargetListener(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
        this.mlgWars.getPluginManager().registerEvents(this, this.mlgWars);
    }

    @EventHandler
    public void on(EntityTargetEvent event) {
        Entity target = event.getTarget();
        Entity entity = event.getEntity();

        if(target instanceof Player) {
            Player player = (Player) target;
            GamePlayer gamePlayer = new GamePlayer(player);

            if(gamePlayer.isSpectator()) {
                event.setCancelled(true);
                return;
            }

            if(entity.getCustomName() != null) {
                if(entity.getCustomName().equalsIgnoreCase(player.getDisplayName())) {
                    event.setTarget(null);
                    return;
                }
            }

            if(this.mlgWars.getTeamSize() > 1) {
                if(entity.getCustomName() != null) {
                    Player player1 = Bukkit.getPlayer(entity.getCustomName());
                    if(player.hasMetadata("team")) {
                        ScoreboardTeam scoreboardTeam = this.mlgWars.getData().getGameTeams()
                                .get(player1.getMetadata("team").get(0).asInt());
                        for (Player teamPlayer : scoreboardTeam.getTeamPlayers()) {
                            if(event.getTarget().equals(teamPlayer)) {
                                event.setTarget(null);
                                return;
                            }
                        }
                    }
                }
            }

        } else if (target instanceof Zombie) {
            Zombie zombie = (Zombie) target;
            if(entity.getCustomName() != null && zombie.getCustomName() != null) {
                if(entity.getCustomName().equalsIgnoreCase(zombie.getCustomName())) {
                    event.setTarget(null);
                }
            }
        }
    }
}
