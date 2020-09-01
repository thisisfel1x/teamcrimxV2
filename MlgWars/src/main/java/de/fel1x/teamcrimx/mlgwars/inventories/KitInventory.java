package de.fel1x.teamcrimx.mlgwars.inventories;

import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.kit.IKit;
import de.fel1x.teamcrimx.mlgwars.kit.Kit;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import de.fel1x.teamcrimx.mlgwars.scoreboard.MlgWarsScoreboard;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class KitInventory implements InventoryProvider {

    public static final SmartInventory KIT_OVERVIEW_INVENTORY = SmartInventory.builder()
            .id("customInventory")
            .provider(new KitInventory())
            .size(3, 9)
            .title("§8● §e§lKits")
            .manager(MlgWars.getInstance().getInventoryManager())
            .build();
    private final MlgWarsScoreboard mlgWarsScoreboard = new MlgWarsScoreboard();

    @Override
    public void init(Player player, InventoryContents contents) {

        GamePlayer gamePlayer = new GamePlayer(player, true);

        int column = 0;
        int row = 0;

        for (Kit kit : Kit.values()) {
            try {
                IKit iKit = kit.getClazz().newInstance();

                boolean bought = (boolean) gamePlayer.getObjectFromMongoDocument(kit.name(), MongoDBCollection.MLGWARS);
                String boughtString = (bought ? "§aGekauft" : "§cNicht gekauft");

                contents.set(row, column, ClickableItem.of(new ItemBuilder(iKit.getKitMaterial())
                                .setName("§8● §a" + iKit.getKitName() + " §8» " + boughtString)
                                .setLore(iKit.getKitDescription()).toItemStack(),
                        event -> {

                            if (bought) {
                                gamePlayer.setSelectedKit(kit);
                                this.mlgWarsScoreboard.updateBoard(player, "§8● §6" + iKit.getKitName(), "kit", "§6");

                                player.playSound(player.getLocation(), Sound.ORB_PICKUP, 5, 0.5f);
                                player.sendMessage(MlgWars.getInstance().getPrefix() + "§7Du nutzt nun das §e[" + iKit.getKitName() + "] §7Kit!");
                                player.closeInventory();
                            } else {
                                player.setMetadata("toBuy", new FixedMetadataValue(MlgWars.getInstance(), kit));
                                BuyInventory.INVENTORY.open(player);
                            }

                        }));

                column++;

                if (column == 9) {
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
