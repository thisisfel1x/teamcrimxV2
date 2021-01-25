package de.fel1x.bingo.listener.block;

import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.gamehandler.Gamestate;
import de.fel1x.bingo.objects.BingoPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockPlaceListener implements Listener {

    private final Bingo bingo;

    public BlockPlaceListener(Bingo bingo) {
        this.bingo = bingo;
        bingo.getPluginManager().registerEvents(this, bingo);
    }

    @EventHandler
    public void on(BlockBreakEvent event) {

        Player player = event.getPlayer();

        BingoPlayer bingoPlayer = new BingoPlayer(player);
        Gamestate gamestate = this.bingo.getGamestateHandler().getGamestate();

        switch (gamestate) {

            case IDLE:
            case LOBBY:
            case PREGAME:
            case ENDING:

                event.setCancelled(true);

                break;


            case INGAME:

                if (bingoPlayer.isSpectator()) {
                    event.setCancelled(true);
                }

                break;


        }


    }

}
