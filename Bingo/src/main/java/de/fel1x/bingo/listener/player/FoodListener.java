package de.fel1x.bingo.listener.player;

import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.gamehandler.Gamestate;
import de.fel1x.bingo.objects.BingoPlayer;
import de.fel1x.bingo.settings.Settings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodListener implements Listener {

    private final Bingo bingo;

    public FoodListener(Bingo bingo) {
        this.bingo = bingo;
        bingo.getPluginManager().registerEvents(this, bingo);
    }

    @EventHandler
    public void on(FoodLevelChangeEvent event) {

        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();

        BingoPlayer bingoPlayer = new BingoPlayer(player);
        Gamestate gamestate = this.bingo.getGamestateHandler().getGamestate();

        if (!gamestate.equals(Gamestate.INGAME)) {
            event.setCancelled(true);
        } else {
            if (bingoPlayer.isSpectator() || !Settings.HUNGER.isEnabled()) {
                event.setCancelled(true);
            }
        }
    }
}
