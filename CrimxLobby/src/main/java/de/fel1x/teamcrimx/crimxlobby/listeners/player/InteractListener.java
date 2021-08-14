package de.fel1x.teamcrimx.crimxlobby.listeners.player;

import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.inventories.NavigatorInventory;
import de.fel1x.teamcrimx.crimxlobby.inventories.rework.CosmeticReworkInventory;
import de.fel1x.teamcrimx.crimxlobby.inventories.rework.LobbySwitcherInventory;
import de.fel1x.teamcrimx.crimxlobby.inventories.rework.ProfileInventory;
import de.fel1x.teamcrimx.crimxlobby.objects.LobbyPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

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

        if (event.getAction() == Action.PHYSICAL) {
            if (event.getClickedBlock() != null
                    && event.getClickedBlock().getType() == Material.FARMLAND) {
                event.setCancelled(true);
            }
        }

        if (item == null) {
            return;
        }

        if (event.hasItem() && item.getType() == Material.FISHING_ROD) {
            return;
        }

        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);

            if (event.hasItem()) {
                switch (item.getType()) {
                    case MUSIC_DISC_CAT -> NavigatorInventory.getNavigatorInventory().open(player);
                    case RED_DYE, LIME_DYE, PURPLE_DYE -> lobbyPlayer.updatePlayerHiderState();
                    case CHEST_MINECART -> CosmeticReworkInventory.COSMETICS_REWORK_INVENTORY.open(player);
                    case PLAYER_HEAD -> ProfileInventory.PROFILE_REWORK_INVENTORY.open(player);
                    case MOJANG_BANNER_PATTERN -> LobbySwitcherInventory.getLobbySwitcherGui().open(player);
                    case NAME_TAG -> this.updateNickItem(player);
                }
            }
        }
    }

    @EventHandler
    public void on(PlayerArmorStandManipulateEvent event) {

        Player player = event.getPlayer();
        LobbyPlayer lobbyPlayer = new LobbyPlayer(player);

        if (lobbyPlayer.isInBuild()) {
            return;
        }

        event.setCancelled(true);

    }

    private void updateNickItem(Player player) {
        if (!player.hasPermission("crimxlobby.vip")) {
            return;
        }

        Objects.requireNonNull(this.crimxLobby.getCrimxAPI().getMongoDB()
                .getObjectFromDocumentAsyncOrDefault(player.getUniqueId(), MongoDBCollection.USERS, "nick", false)).
                thenAccept(nickActivated -> {

                    boolean inverted = !(Boolean) nickActivated;

                    ItemStack itemStack = player.getInventory().getItem(4);

                    if (itemStack == null || itemStack.getItemMeta() == null) {
                        return;
                    }

                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.displayName(Component.text("● ", NamedTextColor.DARK_GRAY)
                            .decoration(TextDecoration.ITALIC, false)
                            .append(Component.text("Nick ", NamedTextColor.DARK_PURPLE))
                            .append(Component.text("» ", NamedTextColor.GRAY))
                            .append(Component.text((inverted ? "aktiviert" : "deaktiviert"),
                                    (inverted ? NamedTextColor.GREEN : NamedTextColor.RED))));

                    itemStack.setItemMeta(itemMeta);

                    this.crimxLobby.getCrimxAPI().getMongoDB()
                            .insertObjectInDocumentAsync(player.getUniqueId(), MongoDBCollection.USERS, "nick", inverted);
                });
    }
}
