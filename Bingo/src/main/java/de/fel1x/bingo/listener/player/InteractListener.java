package de.fel1x.bingo.listener.player;

import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.gamehandler.Gamestate;
import de.fel1x.bingo.inventories.SpectatorCompassInventory;
import de.fel1x.bingo.inventories.TeamSelectorInventory;
import de.fel1x.bingo.objects.BingoPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

public class InteractListener implements Listener {

    private final Bingo bingo;

    public InteractListener(Bingo bingo) {
        this.bingo = bingo;
        bingo.getPluginManager().registerEvents(this, bingo);
    }

    @EventHandler
    public void on(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        BingoPlayer bingoPlayer = new BingoPlayer(player);
        Gamestate gamestate = this.bingo.getGamestateHandler().getGamestate();

        switch (gamestate) {

            case IDLE:
            case LOBBY:
                if (event.getMaterial().equals(Material.CHEST_MINECART)
                        && (event.getAction() == Action.RIGHT_CLICK_AIR
                        || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {

                    TeamSelectorInventory.TEAM_SELECTOR.open(player);
                    return;

                }

                event.setCancelled(true);

                break;

            case PREGAME:
            case ENDING:

                event.setCancelled(true);

            case INGAME:

                if (bingoPlayer.isSpectator()) {
                    event.setCancelled(true);

                    if (event.getMaterial().equals(Material.COMPASS)
                            && Objects.requireNonNull(event.getItem()).getItemMeta().getDisplayName()
                            .equalsIgnoreCase("§7Spieler beoabachten")
                            && (event.getAction() == Action.RIGHT_CLICK_AIR
                            || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {

                        SpectatorCompassInventory.INVENTORY.open(player);
                        return;

                    }
                }
                break;
        }

        if(gamestate == Gamestate.PREGAME || gamestate == Gamestate.INGAME) {
            if(event.hasItem()) {
                if(event.getItem() == null) return;
                if(event.getItem().equals(this.bingo.getBingoItemsQuickAccess())) {
                    player.openInventory(this.bingo.getBingoInventory(new BingoPlayer(player)));
                    event.setCancelled(true);
                }
            }
        }

    }

}
