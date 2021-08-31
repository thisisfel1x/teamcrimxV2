package de.fel1x.teamcrimx.crimxlobby.inventories.rework;

import de.fel1x.teamcrimx.crimxapi.coins.CrimxCoins;
import de.fel1x.teamcrimx.crimxapi.cosmetic.CosmeticRegistry;
import de.fel1x.teamcrimx.crimxapi.cosmetic.InventoryCosmetic;
import de.fel1x.teamcrimx.crimxapi.cosmetic.database.CosmeticPlayer;
import de.fel1x.teamcrimx.crimxapi.support.CrimxSpigotAPI;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.lang.reflect.InvocationTargetException;

public class ConfirmCosmeticBuyReworkInventory implements InventoryProvider {

    public static final SmartInventory CONFIRM_COSMETIC_BUY_REWORK_INVENTORY = SmartInventory.builder()
            .id("CONFIRM_COSMETIC_BUY_REWORK_INVENTORY")
            .provider(new ConfirmCosmeticBuyReworkInventory())
            .type(InventoryType.BREWING)
            .title("§8● §cKaufbestätigung")
            .manager(CrimxLobby.getInstance().getInventoryManager())
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {
        CosmeticRegistry selectedCosmetic = (CosmeticRegistry) player.getMetadata("cosmetic").get(0).value();
        if (selectedCosmetic == null) {
            player.closeInventory();
            player.sendMessage(CrimxLobby.getInstance().getPrefix() + "§cEin Fehler ist aufgetreten");
            return;
        }

        try {
            InventoryCosmetic cosmetic = selectedCosmetic.getCosmeticClass()
                    .getDeclaredConstructor(Player.class, CrimxSpigotAPI.class).newInstance(player, CrimxSpigotAPI.getInstance());

            contents.set(0, 3, ClickableItem.empty(new ItemBuilder(cosmetic.getDisplayMaterial())
                    .setName(cosmetic.getDisplayName())
                    .setLore(cosmetic.getDescription())
                    .addLoreLine(Component.empty())
                    .addLoreLine(Component.text("§7Kosten: §e" + cosmetic.getCost() + " Coins"))
                    .toItemStack()));

            contents.set(0, 0, ClickableItem.of(new ItemBuilder(Material.GREEN_DYE)
                            .setName(Component.text("§8● §aKaufen"))
                            .toItemStack(),
                    event -> this.buyCosmetic(player, selectedCosmetic, cosmetic.getCost())));
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ignored) {
            player.closeInventory();
            player.sendMessage(CrimxLobby.getInstance().getPrefix() + "§cEin Fehler ist aufgetreten");
            return;
        }

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

    /**
     * Method for unlocking a cosmetic for the player
     *
     * @param player   the specific player
     * @param cosmetic the selected cosmetic
     * @param cost     the cosmetic price
     */
    private void buyCosmetic(Player player, CosmeticRegistry cosmetic, int cost) {
        player.closeInventory();

        CrimxCoins crimxCoins = new CrimxCoins(player.getUniqueId());
        int currentCoins = crimxCoins.getCoinsSync();

        if (currentCoins < cost) {
            player.sendTitle("§cUps...", "§7Du hast nicht genügend Coins", 10, 40, 10);
            return;
        }

        crimxCoins.removeCoinsAsync(cost).thenAccept(success -> {
            if (!success) {
                player.sendTitle("§cUps...", "§7Ein Fehler ist aufgetreten", 10, 40, 10);
                return;
            }

            new CosmeticPlayer(player.getUniqueId()).unlockCosmeticAsync(cosmetic).thenAccept(unlockSuccess -> {
                if (!unlockSuccess) {
                    player.sendTitle("§cUps...", "§7Ein Fehler beim Freischalten ist aufgetreten", 10, 40, 10);
                    return;
                }

                player.sendTitle("§aGlückwunsch!", "§7Cosmetic erfolgreich erhalten", 10, 40, 10);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.5f);
            });
        });
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
