package de.fel1x.teamcrimx.crimxlobby.listeners.player;

import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.ICosmetic;
import de.fel1x.teamcrimx.crimxlobby.inventories.CosmeticInventory;
import de.fel1x.teamcrimx.crimxlobby.inventories.NavigatorInventory;
import de.fel1x.teamcrimx.crimxlobby.inventories.SettingsInventory;
import de.fel1x.teamcrimx.crimxlobby.objects.LobbyPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class InteractListener implements Listener {

    private final CrimxLobby crimxLobby;

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

            if (lobbyPlayer.isInWaterMLG() && event.getMaterial().equals(Material.WATER_BUCKET)) {
                return;
            }

            ICosmetic iCosmetic = lobbyPlayer.getSelectedCosmetic();

            event.setCancelled(true);

            if (event.hasItem()) {
                switch (item.getType()) {
                    case MUSIC_DISC_CAT:
                        NavigatorInventory.NAVIGATOR_INVENTORY.open(player);
                        break;

                    case RED_DYE:
                        if (lobbyPlayer.isInJumpAndRun()) {
                            lobbyPlayer.endJumpAndRun();
                        } else if (lobbyPlayer.isInWaterMLG()) {
                            lobbyPlayer.endWaterMLG();
                        } else {
                            lobbyPlayer.updatePlayerHiderState();
                        }
                        break;


                    case LIME_DYE:
                    case PURPLE_DYE:
                        lobbyPlayer.updatePlayerHiderState();
                        break;


                    case CHEST_MINECART:
                        CosmeticInventory.COSMETICS_INVENTORY.open(player);
                        break;

                    case REPEATER:
                        SettingsInventory.SETTINGS_INVENTORY.open(player);
                        break;

                    case NETHER_STAR:
                        //MinigameInventory.MINIGAME_INVENTORY.open(player);
                        player.sendMessage(this.crimxLobby.getPrefix() + "§7Dieses Feature wird noch auf die 1.16 geupdatet!");
                        break;

                    case BLAZE_ROD:
                        if (iCosmetic.getCosmeticMaterial() == null) {
                            return;
                        }

                        if (player.hasMetadata("gadgetDelay")) {
                            long delay = player.getMetadata("gadgetDelay").get(0).asLong();

                            if (delay > System.currentTimeMillis()) {
                                player.sendMessage(this.crimxLobby.getPrefix() + "§7Bitte warte einen Moment");
                                return;
                            }
                        }

                        player.setMetadata("gadgetDelay", new FixedMetadataValue(this.crimxLobby,
                                System.currentTimeMillis() + (1000 * 4)));

                        if (iCosmetic.getCosmeticMaterial() == Material.BLAZE_ROD) {
                            Snowball snowball = player.launchProjectile(Snowball.class);
                            snowball.setMetadata("funGun", new FixedMetadataValue(this.crimxLobby, true));
                        }
                        break;

                    case STICK:
                        if (iCosmetic.getCosmeticMaterial() == null) {
                            return;
                        }

                        if (player.hasMetadata("gadgetDelay")) {
                            long delay = player.getMetadata("gadgetDelay").get(0).asLong();

                            if (delay > System.currentTimeMillis()) {
                                player.sendMessage(this.crimxLobby.getPrefix() + "§7Bitte warte einen Moment");
                                return;
                            }
                        }

                        player.setMetadata("gadgetDelay", new FixedMetadataValue(this.crimxLobby,
                                System.currentTimeMillis() + (1000 * 5)));

                        if (iCosmetic.getCosmeticMaterial() == Material.FIREWORK_ROCKET) {
                            Snowball snowball = player.launchProjectile(Snowball.class);
                            snowball.setMetadata("firework", new FixedMetadataValue(this.crimxLobby, true));
                        }
                        break;

                }
            }
        }
    }
}
