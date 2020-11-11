package de.fel1x.teamcrimx.mlgwars.inventories;

import de.fel1x.teamcrimx.crimxapi.utils.SkullCreator;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpectatorInventory implements InventoryProvider {

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("d")
            .provider(new SpectatorInventory())
            .size(3, 9)
            .title("§8● §aLebende Spieler")
            .closeable(true)
            .manager(MlgWars.getInstance().getInventoryManager())
            .build();

    int column;
    int row;

    @Override
    public void init(Player player, InventoryContents contents) {

        this.column = 0;
        this.row = 0;
        MlgWars.getInstance().getData().getPlayers().forEach(current -> {

            ItemStack skull = SkullCreator.itemFromName(current.getName());
            ItemMeta itemMeta = skull.getItemMeta();
            itemMeta.setDisplayName(current.getName());
            skull.setItemMeta(itemMeta);
            contents.set(this.row, this.column, ClickableItem.of(skull, event -> {

                if (!(event.getWhoClicked() instanceof Player)) return;

                Player clicker = (Player) event.getWhoClicked();
                Player toTp = Bukkit.getPlayer(event.getCurrentItem().getItemMeta().getDisplayName());

                try {
                    clicker.teleport(toTp);
                } catch (Exception ex) {
                    clicker.sendMessage("§cEin Fehler ist aufgetreten!");
                }

                clicker.closeInventory();

            }));

            this.column++;

            if (this.column == 9) {
                this.column = 0;
                this.row += 1;
            }
        });
    }

    @Override
    public void update(Player player, InventoryContents contents) {

        this.column = 0;
        this.row = 0;
        MlgWars.getInstance().getData().getPlayers().forEach(current -> {

            ItemStack skull = SkullCreator.itemFromName(current.getName());
            contents.set(this.row, this.column, ClickableItem.of(skull, event -> {

                if (!(event.getWhoClicked() instanceof Player)) return;

                Player clicker = (Player) event.getWhoClicked();
                Player toTp = Bukkit.getPlayer(event.getCurrentItem().getItemMeta().getDisplayName());

                try {
                    clicker.teleport(toTp);
                    clicker.closeInventory();
                } catch (Exception ex) {
                    clicker.sendMessage("§cEin Fehler ist aufgetreten!");
                }

            }));

            this.column++;

            if (this.column == 9) {
                this.column = 0;
                this.row += 1;
            }
        });
    }

}
