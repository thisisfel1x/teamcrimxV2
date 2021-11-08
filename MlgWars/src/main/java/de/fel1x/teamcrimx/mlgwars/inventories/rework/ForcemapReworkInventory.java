package de.fel1x.teamcrimx.mlgwars.inventories.rework;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.objects.Map;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

// TODO: pagination
public class ForcemapReworkInventory {

    private final MlgWars mlgWars;

    public ForcemapReworkInventory(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
    }

    public void openForcemapInventory(Player player) {
        List<Map> filteredMaps = this.mlgWars.getAvailableMaps().stream()
                .filter(map -> map.getSize().getTeamSize() == this.mlgWars.getTeamSize())
                .collect(Collectors.toList());

        Gui gui = Gui.gui()
                .title(Component.text("● Forcemap"))
                .rows(4)
                .create();

        for (Map filteredMap : filteredMaps) {
            String mapName = filteredMap.getMapName();
            GuiItem toAdd = ItemBuilder.from(Material.PAPER)
                    .name(Component.text("● ", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false)
                            .append(Component.text(mapName, NamedTextColor.GRAY)))
                    .lore(Component.empty(),
                            Component.text("Größe", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                                    .append(Component.text(" ● ", NamedTextColor.DARK_GRAY))
                                    .append(Component.text(filteredMap.getSize().getName(), NamedTextColor.AQUA)),
                            Component.text("Erbauer", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                                    .append(Component.text(" ● ", NamedTextColor.DARK_GRAY))
                                    .append(Component.text(filteredMap.getMapBuilder(), NamedTextColor.AQUA)),
                            Component.empty())
                    .asGuiItem(event -> {
                        player.closeInventory();
                        if(mapName.equalsIgnoreCase(this.mlgWars.getSelectedMap().getMapName())) {
                            player.sendMessage(this.mlgWars.getPrefix() + "§cDiese Map ist bereits ausgewählt");
                        } else {
                            if (this.mlgWars.getLobbyCountdown() <= 10) {
                                player.sendMessage(this.mlgWars.getPrefix() + "§7Du kannst die Map nicht mehr ändern");
                            } else {
                                player.sendMessage(this.mlgWars.getPrefix() + "§7Versuche §a'" + mapName + "' §7zu laden!");
                                this.mlgWars.setSelectedMap(filteredMap);
                                this.mlgWars.getGameType().loadMap(mapName); // New GameType Impl
                                player.sendMessage(this.mlgWars.getPrefix() + "§aDie Map '" + mapName + "' wurde erfolgreich geladen!");
                            }
                        }
                    });
            gui.addItem(toAdd);
        }
        gui.open(player);
    }
}
