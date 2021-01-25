package de.fel1x.bingo.listener.player;

import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.gamehandler.Gamestate;
import de.fel1x.bingo.objects.BingoPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DropListener implements Listener {

    private final Bingo bingo;

    public DropListener(Bingo bingo) {
        this.bingo = bingo;
        bingo.getPluginManager().registerEvents(this, bingo);
    }

    @EventHandler
    public void on(PlayerDropItemEvent event) {

        Player player = event.getPlayer();
        BingoPlayer bingoPlayer = new BingoPlayer(player);

        Gamestate gamestate = this.bingo.getGamestateHandler().getGamestate();

        if(event.getItemDrop().getItemStack().equals(this.bingo.getBingoItemsQuickAccess())) {
            event.setCancelled(true);
            return;
        }

        if (gamestate.equals(Gamestate.INGAME)) {
            if (!bingoPlayer.isPlayer()) {
                event.setCancelled(true);
            }
        } else {
            event.setCancelled(true);
        }
    }

}
