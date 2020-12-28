package de.fel1x.bingo.listener.player;

import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.gamehandler.Gamestate;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {

    private final Bingo bingo;

    public MoveListener(Bingo bingo) {
        this.bingo = bingo;
        this.bingo.getPluginManager().registerEvents(this, this.bingo);
    }

    @EventHandler
    public void on(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();
        Gamestate gamestate = this.bingo.getGamestateHandler().getGamestate();
        if (gamestate == Gamestate.PREGAME
                && (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getZ() != event.getTo().getZ())) {
            player.teleport(location.setDirection(event.getTo().getDirection()));
        }
    }

}
