package de.fel1x.teamcrimx.floorislava.listener.player;

import de.fel1x.teamcrimx.floorislava.FloorIsLava;
import de.fel1x.teamcrimx.floorislava.gamehandler.Gamestate;
import de.fel1x.teamcrimx.floorislava.gameplayer.GamePlayer;
import de.fel1x.teamcrimx.floorislava.inventories.SpectatorCompassInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

public class InteractListener implements Listener {
    private final FloorIsLava floorIsLava;

    public InteractListener(FloorIsLava floorIsLava) {
        this.floorIsLava = floorIsLava;
        floorIsLava.getPluginManager().registerEvents(this, floorIsLava);
    }

    @EventHandler
    public void on(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = new GamePlayer(player);
        Gamestate gamestate = this.floorIsLava.getGamestateHandler().getGamestate();
        if (gamePlayer.isSpectator()) {
            event.setCancelled(true);
            return;
        }
        switch (gamestate) {
            case IDLE:
            case LOBBY:
            case PREGAME:
            case ENDING:
                event.setCancelled(true);
                break;
            case FARMING:
            case RISING:
                if (gamePlayer.isSpectator()) {
                    event.setCancelled(true);
                    if (event.getMaterial().equals(Material.COMPASS) && Objects.requireNonNull(event.getItem()).getItemMeta().getDisplayName()
                            .equalsIgnoreCase("§7Spieler beoabachten") && event
                            .getAction().equals(Action.RIGHT_CLICK_AIR)) {
                        SpectatorCompassInventory.INVENTORY.open(player);
                        return;
                    }
                }
                break;
        }
    }
}
