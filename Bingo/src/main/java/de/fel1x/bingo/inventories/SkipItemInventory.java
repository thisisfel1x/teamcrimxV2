package de.fel1x.bingo.inventories;

import de.fel1x.bingo.Bingo;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class SkipItemInventory implements InventoryProvider {

    public static final SmartInventory SKIP_INVENTORY = SmartInventory.builder()
            .id("customInventory")
            .provider(new SkipItemInventory())
            .type(InventoryType.DISPENSER)
            .title(Bingo.getInstance().getPrefix() + "§7Management")
            .manager(Bingo.getInstance().getInventoryManager())
            .build();
    Bingo bingo = Bingo.getInstance();

    @Override
    public void init(Player player, InventoryContents contents) {

        for (int i = 0; i < this.bingo.getItemGenerator().getPossibleItems().size(); i++) {
            int finalI = i;
            contents.add(ClickableItem.of(new ItemStack(this.bingo.getItemGenerator().getPossibleItems().get(i).getMaterial()),
                    clickEvent -> Objects.requireNonNull(clickEvent.getCurrentItem()).setType(this.bingo.getItemGenerator().generateNewItem(finalI).getMaterial())));
        }

    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
