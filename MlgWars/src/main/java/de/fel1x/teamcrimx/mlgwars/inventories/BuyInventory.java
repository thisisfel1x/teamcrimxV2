package de.fel1x.teamcrimx.mlgwars.inventories;

import de.fel1x.teamcrimx.crimxapi.coins.CoinsAPI;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.kit.IKit;
import de.fel1x.teamcrimx.mlgwars.kit.Kit;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

public class BuyInventory implements InventoryProvider {

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("d")
            .provider(new BuyInventory())
            .type(InventoryType.BREWING)
            .title("§8● §aKit kaufen?")
            .closeable(true)
            .manager(MlgWars.getInstance().getInventoryManager())
            .build();
    private final MlgWars mlgWars = MlgWars.getInstance();

    @Override
    public void init(Player player, InventoryContents contents) {

        GamePlayer gamePlayer = new GamePlayer(player);

        if (player.hasMetadata("toBuy")) {
            Kit kit = (Kit) player.getMetadata("toBuy").get(0).value();

            try {
                IKit iKit = kit.getClazz().newInstance();
                contents.set(0, 3, ClickableItem.empty(new ItemBuilder(iKit.getKitMaterial())
                        .setName("§8● §a" + iKit.getKitName())
                        .setLore(iKit.getKitDescription()).toItemStack()));

                contents.set(0, 0, ClickableItem.of(new ItemBuilder(Material.RED_DYE)
                        .setName("§8● §cAbbrechen")
                        .toItemStack(), event -> {

                    KitInventory.KIT_OVERVIEW_INVENTORY.open(player);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 3f, 1.75f);

                }));

                CoinsAPI coinsAPI = new CoinsAPI(player.getUniqueId());
                int coins = coinsAPI.getCoins();

                contents.set(0, 2, ClickableItem.of(new ItemBuilder(Material.GREEN_DYE)
                        .setName("§8● §aKaufen")
                        .setColor(10)
                        .toItemStack(), event -> gamePlayer.unlockKit(kit)));

                contents.set(0, 1, ClickableItem.empty(new ItemBuilder(Material.GOLD_NUGGET)
                        .setName("§7Du besitzt §e" + coins + " Coins")
                        .toItemStack()));

            } catch (InstantiationException | IllegalAccessException ignored) {
                player.sendMessage(this.mlgWars.getPrefix() + "§cEin Fehler ist aufgetreten!");
                player.closeInventory();
            }


        } else {
            player.sendMessage(this.mlgWars.getPrefix() + "§cEin Fehler ist aufgetreten!");
            player.closeInventory();
        }

    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
