package de.fel1x.bingo.listener.player;

import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.gamehandler.Gamestate;
import de.fel1x.bingo.inventories.SpectatorCompassInventory;
import de.fel1x.bingo.inventories.TeamSelectorInventory;
import de.fel1x.bingo.inventories.settings.SettingsOverviewInventory;
import de.fel1x.bingo.inventories.voting.VotingInventory;
import de.fel1x.bingo.objects.BingoPlayer;
import de.fel1x.bingo.tasks.LobbyTask;
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
                event.setCancelled(true);

                if ((event.getAction() == Action.RIGHT_CLICK_AIR
                        || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
                    if (event.getMaterial() == Material.CHEST_MINECART) {
                        TeamSelectorInventory.TEAM_SELECTOR.open(player);
                    } else if (event.getMaterial() == Material.PAPER) {
                        if (this.bingo.getBingoTask() instanceof LobbyTask) {
                            LobbyTask lobbyTask = (LobbyTask) this.bingo.getBingoTask();
                            if (lobbyTask.getCountdown() <= 15) {
                                player.sendMessage(this.bingo.getPrefix() + "§cDu kannst nicht mehr abstimmen!");
                                return;
                            }
                        }
                        VotingInventory.VOTING_INVENTORY.open(player);
                    } else if (event.getMaterial() == Material.REPEATER) {
                        SettingsOverviewInventory.SETTINGS_OVERVIEW_INVENTORY.open(player);
                    }
                }

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

        if (gamestate == Gamestate.PREGAME || gamestate == Gamestate.INGAME) {
            if (event.hasItem()) {
                if (event.getItem() == null) return;
                if (event.getItem().equals(this.bingo.getBingoItemsQuickAccess())) {
                    player.openInventory(this.bingo.getBingoInventory(new BingoPlayer(player)));
                    event.setCancelled(true);
                }
            }
        }

    }

}
