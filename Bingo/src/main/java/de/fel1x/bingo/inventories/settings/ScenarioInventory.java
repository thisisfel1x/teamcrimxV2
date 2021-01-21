package de.fel1x.bingo.inventories.settings;

import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.scenarios.IBingoScenario;
import de.fel1x.bingo.scenarios.Scenario;
import de.fel1x.bingo.utils.ItemBuilder;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ScenarioInventory implements InventoryProvider {

    public static final SmartInventory SCENARIO_INVENTORY = SmartInventory.builder()
            .id("scenarioInventory")
            .provider(new ScenarioInventory())
            .size(5, 9)
            .title(Bingo.getInstance().getPrefix() + "§cEvents")
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

        int row = 1;
        int slot = 1;

        for (Scenario scenario : Scenario.values()) {
            try {
                IBingoScenario bingoScenario = scenario.getScenarioClazz().newInstance();

                contents.set(row, slot, ClickableItem.of(new ItemBuilder(bingoScenario.getDisplayMaterial())
                                .setName("§e" + bingoScenario.getName()
                                        + " §8● " + (scenario.isEnabled() ? "§aaktiviert" : "§cdeaktiviert"))
                                .addGlow(scenario.isEnabled())
                                .setLore(bingoScenario.getDescription())
                                .toItemStack(),
                        event -> {
                    if(event.getClick().isLeftClick()) {
                        scenario.setEnabled(!scenario.isEnabled());
                    }
                }));

            } catch (IllegalAccessException | InstantiationException e) {
                player.sendMessage(this.bingo.getPrefix() + "§cEin Fehler ist aufgetreten!");
                player.closeInventory();
            }

            slot++;
            if(slot == 8) {
                slot = 1;
                row++;
            }
        }
    }
}
