package de.fel1x.teamcrimx.crimxlobby.inventories.rework;

import de.fel1x.teamcrimx.crimxapi.friends.InventoryFriend;
import de.fel1x.teamcrimx.crimxapi.friends.database.FriendPlayer;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.inventories.utils.InventoryUtils;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class ProfileInventory implements InventoryProvider {

    public static final SmartInventory PROFILE_REWORK_INVENTORY = SmartInventory.builder()
            .id("PROFILE_REWORK_INVENTORY")
            .provider(new ProfileInventory())
            .size(6, 9)
            .title("§8● §eProfil")
            .manager(CrimxLobby.getInstance().getInventoryManager())
            .build();

    private final int[][] glassSlots2D = {
            {
                    0, 8
            }, {
            0, 1, 2, 3, 4, 5, 6, 7, 8
    }, {}, {}, {}, {
            0, 1, 2, 3, 4, 5, 6
    }
    };

    @Override
    public void init(Player player, InventoryContents contents) {
        Pagination pagination = contents.pagination();

        InventoryUtils.setNavigationItems(contents, player, true);

        new FriendPlayer(player.getUniqueId()).getFriends().thenAccept(inventoryFriends -> {
            ClickableItem[] clickableItem = new ClickableItem[inventoryFriends.size()];
            int counter = 0;
            for (InventoryFriend inventoryFriend : inventoryFriends) {
                Component[] lore;
                if (inventoryFriend.isOnline()) {
                    lore = new Component[]{
                            Component.text("§7Verbundener Server §8● §a" + inventoryFriend.getConnectedServer()),
                            Component.text(""),
                            Component.text("§eKlicke §7für weitere Optionen")
                    };
                } else {
                    lore = new Component[]{
                            Component.text(""),
                            Component.text("§eKlicke §7für weitere Optionen")
                    };
                }

                clickableItem[counter] = ClickableItem.of(new ItemBuilder((inventoryFriend.isOnline())
                                ? Material.PLAYER_HEAD : Material.SKELETON_SKULL)
                                // TODO: prefix
                                .setName(Component.text("§e" + inventoryFriend.getName() + " §8● "
                                        + ((inventoryFriend.isOnline()) ? "§aonline" : "§coffline")))
                                .setLore(lore)
                                .setSkullOwner(inventoryFriend.getName())
                                .toItemStack(),
                        event -> {
                            player.setMetadata("inventoryFriend",
                                    new FixedMetadataValue(CrimxLobby.getInstance(), inventoryFriend));
                            FriendOptionsInventory.FRIEND_OPTIONS_INVENTORY.open(player);
                        });

                counter++;
            }

            pagination.setItems(clickableItem);
            pagination.setItemsPerPage(27);

            pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 2, 0));
        });


        // Pagination Items
        contents.set(5, 7, ClickableItem.of(new ItemBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ2OWUwNmU1ZGFkZmQ4NGU1ZjNkMWMyMTA2M2YyNTUzYjJmYTk0NWVlMWQ0ZDcxNTJmZGM1NDI1YmMxMmE5In19fQ==")
                .setName(Component.text("§8● §7Vorherige Seite")).toItemStack(), event -> {
            if (!pagination.isFirst()) {
                PROFILE_REWORK_INVENTORY.open(player, pagination.previous().getPage());
            }
        }));
        contents.set(5, 8, ClickableItem.of(new ItemBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTliZjMyOTJlMTI2YTEwNWI1NGViYTcxM2FhMWIxNTJkNTQxYTFkODkzODgyOWM1NjM2NGQxNzhlZDIyYmYifX19")
                .setName(Component.text("§8● §7Nächste Seite")).toItemStack(), event -> {
            if (!pagination.isLast()) {
                PROFILE_REWORK_INVENTORY.open(player, pagination.next().getPage());
            }
        }));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
    }
}
