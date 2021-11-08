package de.fel1x.teamcrimx.mlgwars.inventories;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.kit.rework.InventoryKitManager;
import de.fel1x.teamcrimx.mlgwars.kit.rework.KitRegistry;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

public class BuyInventory implements InventoryProvider {

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("BUY INVENTORY")
            .provider(new BuyInventory())
            .type(InventoryType.BREWING)
            .title("§8● Kit kaufen?")
            .closeable(true)
            .manager(MlgWars.getInstance().getInventoryManager())
            .build();
    private final MlgWars mlgWars = MlgWars.getInstance();

    @Override
    public void init(Player player, InventoryContents contents) {
        KitRegistry kitRegistry = (KitRegistry) player.getMetadata("toBuy").get(0).value();
        if (kitRegistry == null) {
            return;
        }

        GamePlayer gamePlayer = this.mlgWars.getData().getGamePlayers().get(player.getUniqueId());
        InventoryKitManager.InventoryKit inventoryKit = this.mlgWars.getInventoryKitManager().getAvailableKits().get(kitRegistry);

        contents.set(0, 3, ClickableItem.empty(new ItemBuilder(inventoryKit.getKitMaterial())
                .setName(inventoryKit.getKitName())
                .setLore(inventoryKit.getKitDescription())
                .addLoreLine(Component.empty())
                .addLoreLine(Component.text("§7Kosten: §e" + inventoryKit.getKitCost() + " Coins"))
                .toItemStack()));

        contents.set(0, 0, ClickableItem.of(new ItemBuilder(Material.GREEN_DYE)
                        .setName(Component.text("§8● §aKaufen"))
                        .toItemStack(),
                event -> gamePlayer.unlockKit(kitRegistry)));


        contents.set(0, 2, ClickableItem.of(new ItemBuilder(Material.RED_DYE)
                        .setName(Component.text("§8● §cAbbrechen"))
                        .toItemStack(),
                event -> player.closeInventory()));

        contents.set(0, 4,
                ClickableItem.empty(new ItemBuilder(Material.MAP)
                        .setName(Component.text("§8● §aStatistiken"))
                        .setLore(Component.text("§7Ingesamt §a0x §7gekauft"),
                                Component.text("§7Insgesamt §60 Coins §7ausgegeben"))
                        .toItemStack()));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
    }
}
