package de.fel1x.teamcrimx.floorislava.listener.block;

import de.fel1x.teamcrimx.floorislava.FloorIsLava;
import de.fel1x.teamcrimx.floorislava.gamehandler.Gamestate;
import de.fel1x.teamcrimx.floorislava.gameplayer.GamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockPlaceListener implements Listener {
    private final FloorIsLava floorIsLava;

    public BlockPlaceListener(FloorIsLava floorIsLava) {
        this.floorIsLava = floorIsLava;
        floorIsLava.getPluginManager().registerEvents(this, floorIsLava);
    }

    @EventHandler
    public void on(BlockBreakEvent event) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = new GamePlayer(player);
        Gamestate gamestate = this.floorIsLava.getGamestateHandler().getGamestate();
        switch (gamestate) {
            case IDLE:
            case LOBBY:
            case PREGAME:
            case ENDING:
                event.setCancelled(true);
                break;
            case FARMING:
            case RISING:
                if (gamePlayer.isSpectator())
                    event.setCancelled(true);
                break;
        }
    }
}
