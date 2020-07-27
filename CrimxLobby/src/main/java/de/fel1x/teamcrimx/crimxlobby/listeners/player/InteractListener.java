package de.fel1x.teamcrimx.crimxlobby.listeners.player;

import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.inventories.CosmeticInventory;
import de.fel1x.teamcrimx.crimxlobby.inventories.MinigameInventory;
import de.fel1x.teamcrimx.crimxlobby.inventories.NavigatorInventory;
import de.fel1x.teamcrimx.crimxlobby.inventories.SettingsInventory;
import de.fel1x.teamcrimx.crimxlobby.objects.LobbyPlayer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InteractListener implements Listener {

    private CrimxLobby crimxLobby;

    public InteractListener(CrimxLobby crimxLobby) {
        this.crimxLobby = crimxLobby;
        this.crimxLobby.getPluginManager().registerEvents(this, this.crimxLobby);
    }

    @EventHandler
    public void on(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        LobbyPlayer lobbyPlayer = new LobbyPlayer(player);

        Action action = event.getAction();
        ItemStack item = event.getItem();

        if (lobbyPlayer.isInBuild()) {
            return;
        }

        if (event.hasItem() && item.getType() == Material.FISHING_ROD) {
            return;
        }

        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {

            if(lobbyPlayer.isInWaterMLG() && event.getMaterial().equals(Material.WATER_BUCKET)) {
                return;
            }

            event.setCancelled(true);

            if (event.hasItem()) {
                switch (item.getType()) {
                    case GREEN_RECORD:
                        NavigatorInventory.NAVIGATOR_INVENTORY.open(player);
                        break;

                    case INK_SACK:
                        if(lobbyPlayer.isInJumpAndRun()) {
                            lobbyPlayer.endJumpAndRun();
                        } else if(lobbyPlayer.isInWaterMLG()) {
                            lobbyPlayer.endWaterMLG();
                        } else {
                            lobbyPlayer.updatePlayerHiderState();
                        }
                        break;

                    case STORAGE_MINECART:
                        CosmeticInventory.COSMETICS_INVENTORY.open(player);
                        break;

                    case DIODE:
                        SettingsInventory.SETTINGS_INVENTORY.open(player);
                        break;

                    case NETHER_STAR:
                        MinigameInventory.MINIGAME_INVENTORY.open(player);
                        break;

                }

            }
        }
    }
}
