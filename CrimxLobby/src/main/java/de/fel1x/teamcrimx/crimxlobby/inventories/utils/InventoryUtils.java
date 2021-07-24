package de.fel1x.teamcrimx.crimxlobby.inventories.utils;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.crimxlobby.inventories.rework.ProfileInventory;
import de.fel1x.teamcrimx.crimxlobby.inventories.rework.SettingsReworkInventory;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class InventoryUtils {

    public static void setNavigationItems(InventoryContents contents, Player player, boolean pagination) {
        int[][] glassSlots2D = {
                {
                        0, 8
                }, {
                    0, 1, 2, 3, 4, 5, 6, 7, 8
                }, {}, {}, {}, (pagination) ? new int[]{
                0, 1, 2, 3, 4, 5, 6
        } : new int[]{
                0, 1, 2, 3, 4, 5, 6, 7, 8 }
        };

        int row = 0;
        for (int[] ints : glassSlots2D) {
            for (int i : ints) {
                contents.set(row, i, ClickableItem.empty(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
                        .setName(Component.empty()).toItemStack()));
            }
            row++;
        }

        // Navigation Items
        contents.set(0, 2, ClickableItem.of(new ItemBuilder(Material.PLAYER_HEAD)
                .setName(Component.text("§8● §aFreunde")).setSkullOwner(player.getName())
                .toItemStack(), inventoryClickEvent -> ProfileInventory.PROFILE_REWORK_INVENTORY.open(player)));
        contents.set(0, 3, ClickableItem.empty(new ItemBuilder(Material.CAKE)
                .setName(Component.text("§8● §5Party"))
                .toItemStack()));
        contents.set(0, 5, ClickableItem.empty(new ItemBuilder(Material.DIAMOND_CHESTPLATE)
                .setName(Component.text("§8● §bClan"))
                .toItemStack()));
        contents.set(0, 6, ClickableItem.of(new ItemBuilder(Material.REPEATER)
                .setName(Component.text("§8● §cEinstellungen"))
                .toItemStack(), inventoryClickEvent -> SettingsReworkInventory.SETTINGS_REWORK_INVENTORY.open(player)));
    }

}
