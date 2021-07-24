package de.fel1x.bingo.inventories.settings;

import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.settings.Settings;
import de.fel1x.bingo.utils.ItemBuilder;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class SettingsOverviewInventory implements InventoryProvider {

    public static final SmartInventory SETTINGS_OVERVIEW_INVENTORY = SmartInventory.builder()
            .id("settingsOverviewInventory")
            .provider(new SettingsOverviewInventory())
            .size(5, 9)
            .title(Bingo.getInstance().getPrefix() + "§cEinstellungen")
            .manager(Bingo.getInstance().getInventoryManager())
            .build();
    private final Bingo bingo = Bingo.getInstance();

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(" ").toItemStack()));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        int state = contents.property("state", 0);
        contents.setProperty("state", state + 1);

        if (state % 2 != 0) {
            return;
        }

        int row = 1;
        int slot = 1;

        for (Settings setting : Settings.values()) {
            contents.set(row, slot, ClickableItem.of(new ItemBuilder(setting.getDisplayMaterial())
                    .setName("§e" + setting.getName()
                            + " §8● " + (setting.isEnabled() ? "§aaktiviert" : "§cdeaktiviert"))
                    .setLore("", "§eLinksklick§7, um §e" + setting.getName() + " §7zu §aaktivieren§7/§cdeaktivieren",
                            setting.hasConfiguration() ? "§eRechtsklick§7, um einzelne Events anzupassen\n " : "")
                    .addGlow(setting.isEnabled())
                    .toItemStack(), event -> {
                ClickType clickType = event.getClick();
                if (clickType.isLeftClick()) {
                    setting.setEnabled(!setting.isEnabled());
                } else if (clickType.isRightClick() && setting.hasConfiguration() && setting.getInventoryClazz() != null) {
                    ((SmartInventory) setting.getInventoryClazz()).open(player);
                }
            }));

            slot++;
            if (slot == 8) {
                slot = 1;
                row++;
            }
        }
    }
}
