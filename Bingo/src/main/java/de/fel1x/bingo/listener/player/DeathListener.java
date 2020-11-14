package de.fel1x.bingo.listener.player;

import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.gamehandler.Gamestate;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    private final Bingo bingo;

    public DeathListener(Bingo bingo) {
        this.bingo = bingo;
        bingo.getPluginManager().registerEvents(this, bingo);
    }

    @EventHandler
    public void on(PlayerDeathEvent event) {

        Player player = event.getEntity();

        Gamestate gamestate = this.bingo.getGamestateHandler().getGamestate();

        event.getDrops().clear();

        if (!gamestate.equals(Gamestate.INGAME)) {
            event.setDeathMessage(null);
        } else {
            event.setKeepInventory(true);
            event.setKeepLevel(true);
            event.setDeathMessage(String.format("%s§7Der Spieler §a%s §7ist gestorben",
                    this.bingo.getPrefix(), player.getDisplayName()));
        }

    }

}
