package de.fel1x.teamcrimx.mlgwars.inventories;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.enums.Size;
import de.fel1x.teamcrimx.mlgwars.maphandler.MapHandler;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class ForcemapInventory implements InventoryProvider {

    public static final SmartInventory FORCEMAP_INVENTORY = SmartInventory.builder()
            .id("customInventory")
            .provider(new ForcemapInventory())
            .size(5, 9)
            .title("§8● §cForcemap")
            .manager(MlgWars.getInstance().getInventoryManager())
            .build();

    private final MlgWars mlgWars = MlgWars.getInstance();
    private final MapHandler mapHandler = new MapHandler();

    @Override
    public void init(Player player, InventoryContents contents) {

        File dir = new File("plugins/MlgWars/maps");
        File[] directoryListing = dir.listFiles();

        if (directoryListing == null) {
            contents.set(3, 4, ClickableItem.empty(new ItemBuilder(Material.BARRIER).setName("§8● §cKeine Maps vorhanden!").toItemStack()));
            return;
        }

        int column = 0;
        int row = 0;

        for (File file : directoryListing) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);

            String mapName = config.getString("name");
            Size size = this.mapHandler.getSize(mapName);

            contents.set(row, column, ClickableItem.of(new ItemBuilder(Material.PAPER).setName("§8● §7" + mapName).setLore("", "§7Größe §8●  §b" + size.getName(), "").toItemStack(), event -> {
                player.closeInventory();
                if(mapName.equalsIgnoreCase(this.mlgWars.getMapName())) {
                    player.sendMessage(this.mlgWars.getPrefix() + "§cDiese Map ist bereits ausgewählt");
                } else {
                    player.sendMessage(this.mlgWars.getPrefix() + "§7Versuche §a'" + mapName + "' §7zu laden!");
                    this.mlgWars.getWorldLoader().forceMap(mapName);
                    player.sendMessage(this.mlgWars.getPrefix() + "§aDie Map '" + mapName + "' wurde erfolgreich geladen!");
                }
            }));

            column++;

            if(column == 8) {
                column = 0;
                row++;
            }
        }

    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
