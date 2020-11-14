package de.fel1x.bingo.inventories;

import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.utils.SkullCreator;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpectatorCompassInventory implements InventoryProvider {

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("d")
            .provider(new SpectatorCompassInventory())
            .size(3, 9)
            .title("§7Lebende Spieler")
            .closeable(true)
            .manager(Bingo.getInstance().getInventoryManager())
            .build();

    int column;
    int row;

    @Override
    public void init(Player player, InventoryContents contents) {

        this.column = 0;
        this.row = 0;
        Bingo.getInstance().getData().getPlayers().forEach(current -> {

            ItemStack skull = SkullCreator.itemFromUuid(current.getUniqueId());
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
        Bingo.getInstance().getData().getPlayers().forEach(current -> {

            ItemStack skull = SkullCreator.itemFromUuid(current.getUniqueId());
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
