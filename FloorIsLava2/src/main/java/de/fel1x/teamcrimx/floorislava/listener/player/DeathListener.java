package de.fel1x.teamcrimx.floorislava.listener.player;

import de.fel1x.teamcrimx.floorislava.FloorIsLava;
import de.fel1x.teamcrimx.floorislava.gamehandler.Gamestate;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {
    private final FloorIsLava floorIsLava;

    public DeathListener(FloorIsLava floorIsLava) {
        this.floorIsLava = floorIsLava;
        floorIsLava.getPluginManager().registerEvents(this, floorIsLava);
    }

    @EventHandler
    public void on(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Gamestate gamestate = this.floorIsLava.getGamestateHandler().getGamestate();
        event.getDrops().clear();
        if (gamestate != Gamestate.RISING) {
            event.setDeathMessage(null);
        } else {
            event.setDeathMessage(String.format("%s§7Der Spieler §a%s §7ist gestorben", this.floorIsLava
                    .getPrefix(), player.getDisplayName()));
        }
    }
}
