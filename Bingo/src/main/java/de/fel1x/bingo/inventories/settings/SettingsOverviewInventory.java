package de.fel1x.bingo.inventories.settings;

import de.fel1x.bingo.Bingo;
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

        if(state % 2 != 0) {
            return;
        }

        boolean eventsEnabled = this.bingo.getData().areEventsEnabled();
        boolean daylightCycle = this.bingo.getData().doDaylightCycle();
        boolean doMobSpawn = this.bingo.getData().doMobSpawn();
        boolean randomizerEnabled = this.bingo.getData().isRandomizer();
        boolean advancedRandomizerEnabled = this.bingo.getData().isAdvancedRandomizer();

        contents.set(1, 1, ClickableItem.of(new ItemBuilder((eventsEnabled) ? Material.ENDER_EYE : Material.ENDER_PEARL)
                .setName("§eEvents §8● " + (eventsEnabled ? "§aaktiviert" : "§cdeaktiviert"))
                .setLore("" , "§eLinksklick§7, um Events zu §aaktivieren§7/§cdeaktivieren", "§eRechtsklick§7, um einzelne Events anzupassen", "")
                .toItemStack(), event -> {
            ClickType clickType = event.getClick();
            if(clickType.isLeftClick()) {
                this.bingo.getData().setEventsEnabled(!eventsEnabled);
            } else if(clickType.isRightClick()) {
                ScenarioInventory.SCENARIO_INVENTORY.open(player);
            }
        }));

        contents.set(1, 2, ClickableItem.of(new ItemBuilder(Material.LANTERN)
                .setName("§eTakeszyklus §8● " + (daylightCycle ? "§aaktiviert" : "§cdeaktiviert"))
                .addGlow(daylightCycle)
                .setLore("" , "§eLinksklick§7, um Tageszyklen zu §aaktivieren§7/§cdeaktivieren", "§eRechtsklick§7, um eine bestimmte Zeit auszuwählen", "")
                .toItemStack(), event -> {
            ClickType clickType = event.getClick();
            if(clickType.isLeftClick()) {
                this.bingo.getData().setDoDaylightCycle(!daylightCycle);
            } else if(clickType.isRightClick()) {
                player.closeInventory();
                player.sendMessage(this.bingo.getPrefix() + "§cDieses Feature ist noch nicht verfügbar!");
            }
        }));

        contents.set(1, 3, ClickableItem.of(new ItemBuilder((doMobSpawn) ? Material.ZOMBIE_HEAD : Material.BARRIER)
                .setName("§eMobspawning §8● " + (doMobSpawn ? "§aaktiviert" : "§cdeaktiviert"))
                .addGlow(doMobSpawn)
                .setLore("" , "§7Linksklick, um Mobspawning zu §aaktivieren§7/§cdeaktivieren", "")
                .toItemStack(), event -> {
            ClickType clickType = event.getClick();
            if(clickType.isLeftClick()) {
                this.bingo.getData().setDoMobSpawn(!doMobSpawn);
            }
        }));

        contents.set(1, 4, ClickableItem.of(new ItemBuilder(Material.DIAMOND_PICKAXE)
                .setName("§eRandomizer §8● " + (randomizerEnabled ? "§aaktiviert" : "§cdeaktiviert"))
                .addGlow(randomizerEnabled)
                .setLore("" , "§eLinksklick§7, um den Randomizer zu §aaktivieren§7/§cdeaktivieren", "")
                .toItemStack(), event -> {
            ClickType clickType = event.getClick();
            if(clickType.isLeftClick()) {
                this.bingo.getData().setRandomizer(!randomizerEnabled);
            }
        }));

        contents.set(1, 5, ClickableItem.of(new ItemBuilder(Material.CRAFTING_TABLE)
                .setName("§eAdvanced Randomizer §8● " + (advancedRandomizerEnabled ? "§aaktiviert" : "§cdeaktiviert"))
                .addGlow(advancedRandomizerEnabled)
                .setLore("" , "§eLinksklick§7, um den Craft-Randomizer zu §aaktivieren§7/§cdeaktivieren", "")
                .toItemStack(), event -> {
            ClickType clickType = event.getClick();
            if(clickType.isLeftClick()) {
                this.bingo.getData().setAdvancedRandomizer(!advancedRandomizerEnabled);
            }
        }));
    }
}
