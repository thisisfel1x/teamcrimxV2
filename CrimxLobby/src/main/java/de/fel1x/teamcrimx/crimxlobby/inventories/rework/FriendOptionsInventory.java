package de.fel1x.teamcrimx.crimxlobby.inventories.rework;

import de.fel1x.teamcrimx.crimxapi.friends.InventoryFriend;
import de.fel1x.teamcrimx.crimxapi.friends.database.FriendPlayer;
import de.fel1x.teamcrimx.crimxapi.friends.database.IFriendPlayer;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class FriendOptionsInventory implements InventoryProvider {

    public static final SmartInventory FRIEND_OPTIONS_INVENTORY = SmartInventory.builder()
            .id("FRIEND_OPTIONS_INVENTORY")
            .provider(new FriendOptionsInventory())
            .size(3, 9)
            .title("§8● §eProfil")
            .manager(CrimxLobby.getInstance().getInventoryManager())
            .build();

    private final CrimxLobby crimxLobby = CrimxLobby.getInstance();

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillRow(0, ClickableItem.empty(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
                .setName(Component.empty()).toItemStack()));
        contents.fillRow(2, ClickableItem.empty(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
                .setName(Component.empty()).toItemStack()));

        InventoryFriend inventoryFriend = (InventoryFriend) player.getMetadata("inventoryFriend").get(0).value();
        if (inventoryFriend == null) {
            player.closeInventory();
            player.sendMessage(this.crimxLobby.getPrefix() + "§cEin Fehler ist aufgetreten");
            return;
        }

        IFriendPlayer friendPlayer = new FriendPlayer(player.getUniqueId());

        contents.set(1, 1, ClickableItem.empty(new ItemBuilder((inventoryFriend.isOnline())
                ? Material.PLAYER_HEAD : Material.SKELETON_SKULL)
                // TODO: prefix
                .setName(Component.text("§e" + inventoryFriend.getName() + " §8● "
                        + ((inventoryFriend.isOnline()) ? "§aonline" : "§coffline")))
                .setSkullOwner(inventoryFriend.getName())
                .toItemStack()));

        if (inventoryFriend.isOnline()) {
            contents.set(1, 3, ClickableItem.of(new ItemBuilder(Material.CAKE)
                    .setName(Component.text("§8● §7Zur §5Party §7einladen"))
                    .toItemStack(), event -> {
                player.closeInventory();
                // TODO: party
            }));
            contents.set(1, 5, ClickableItem.of(new ItemBuilder(Material.ENDER_PEARL)
                            .setName(Component.text("§8● §7Freund §enachspringen"))
                            .toItemStack(),
                    event -> {
                        player.closeInventory();
                        friendPlayer.jumpToFriend(inventoryFriend.getUuid(), player.hasPermission("friends.force.jump"));
                    }));
        }

        contents.set(1, 4, ClickableItem.of(new ItemBuilder(Material.IRON_CHESTPLATE)
                        .setName(Component.text("§8● §7In §bClan §7einladen"))
                        .toItemStack(),
                event -> {
                    // TODO: clan invite
                }));

        contents.set(1, 7, ClickableItem.of(new ItemBuilder(Material.BARRIER)
                .setName(Component.text("§8● §cFreund entfernen"))
                .toItemStack(), event -> {
            player.closeInventory();
            friendPlayer.removeFriend(inventoryFriend.getUuid());
        }));


    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
