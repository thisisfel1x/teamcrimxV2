package de.fel1x.teamcrimx.floorislava.listener.player;

import de.fel1x.teamcrimx.floorislava.FloorIsLava;
import de.fel1x.teamcrimx.floorislava.gamehandler.Gamestate;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {
    private final FloorIsLava floorIsLava;

    public MoveListener(FloorIsLava floorIsLava) {
        this.floorIsLava = floorIsLava;
        this.floorIsLava.getPluginManager().registerEvents(this, this.floorIsLava);
    }

    @EventHandler
    public void on(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();
        Gamestate gamestate = this.floorIsLava.getGamestateHandler().getGamestate();
        if (gamestate == Gamestate.PREGAME && (
                event.getFrom().getX() != event.getTo().getX() || event.getFrom().getY() != event.getTo().getY() || event.getFrom().getZ() != event.getTo().getZ()))
            player.teleport(location.setDirection(event.getTo().getDirection()));
    }
}
