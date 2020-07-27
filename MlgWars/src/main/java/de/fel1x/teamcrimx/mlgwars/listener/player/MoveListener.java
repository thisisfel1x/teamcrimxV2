package de.fel1x.teamcrimx.mlgwars.listener.player;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {

    private final MlgWars mlgWars;

    public MoveListener(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
        this.mlgWars.getPluginManager().registerEvents(this, this.mlgWars);
    }

    @EventHandler
    public void on(PlayerMoveEvent event) {

        Player player = event.getPlayer();
        Location location = player.getLocation();
        GamePlayer gamePlayer = new GamePlayer(player);

        Gamestate gamestate = this.mlgWars.getGamestateHandler().getGamestate();

        if (gamestate == Gamestate.DELAY) {
            if (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getY() != event.getTo().getY() || event.getFrom().getZ() != event.getTo().getZ()) {
                player.teleport(location.setDirection(event.getTo().getDirection()));
                return;
            }
        }
    }
}
