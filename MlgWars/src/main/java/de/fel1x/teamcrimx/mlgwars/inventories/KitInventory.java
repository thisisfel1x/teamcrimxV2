package de.fel1x.teamcrimx.mlgwars.inventories;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.kit.IKit;
import de.fel1x.teamcrimx.mlgwars.kit.Kit;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.entity.Player;

public class KitInventory implements InventoryProvider {

    public static final SmartInventory KIT_OVERVIEW_INVENTORY = SmartInventory.builder()
            .id("customInventory")
            .provider(new KitInventory())
            .size(5, 9)
            .title("§a§lKits")
            .manager(MlgWars.getInstance().getInventoryManager())
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {

        GamePlayer gamePlayer = new GamePlayer(player);

        int column = 0;
        int row = 0;

        for (Kit kit : Kit.values()) {

            try {
                IKit iKit = kit.getClazz().newInstance();

                contents.set(row, column, ClickableItem.of(new ItemBuilder(iKit.getKitMaterial())
                        .setName("§8● §a" + iKit.getKitName())
                        .setLore(iKit.getKitDescription()).toItemStack(),
                        event -> {
                    gamePlayer.setSelectedKit(kit);
                    player.closeInventory();
                        }));

                column++;

                if(column == 8) {
                    column = 0;
                    row++;
                }

            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
