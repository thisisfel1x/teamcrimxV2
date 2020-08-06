package de.fel1x.teamcrimx.crimxlobby.inventories;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.Cosmetic;
import de.fel1x.teamcrimx.crimxlobby.objects.LobbyPlayer;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class BuyInventory implements InventoryProvider {

    public static final SmartInventory BUY_INVENTORY = SmartInventory.builder()
            .id("customInventory")
            .provider(new BuyInventory())
            .size(5, 9)
            .title("§8● §aWirklich kaufen?")
            .manager(CrimxLobby.getInstance().getInventoryManager())
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {

        Cosmetic cosmetic = Cosmetic.valueOf(player.getMetadata("toBuy").get(0).asString());

        contents.fillBorders(ClickableItem.empty(new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (byte) 7).setName(" ").toItemStack()));
        contents.set(1, 4, ClickableItem.empty(new ItemBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmFkYzA0OGE3Y2U3OGY3ZGFkNzJhMDdkYTI3ZDg1YzA5MTY4ODFlNTUyMmVlZWQxZTNkYWYyMTdhMzhjMWEifX19")
                .setName("§eWirklich kaufen?").toItemStack()));

        contents.set(2, 3, ClickableItem.of(new ItemBuilder(Material.BARRIER).setName("§8● §cAbbrechen").toItemStack(), event1 -> BUY_INVENTORY.open(player)));

        contents.set(2, 5, ClickableItem.of(new ItemBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTMwZjQ1MzdkMjE0ZDM4NjY2ZTYzMDRlOWM4NTFjZDZmN2U0MWEwZWI3YzI1MDQ5YzlkMjJjOGM1ZjY1NDVkZiJ9fX0=")
                .setName("§8● §aKaufen").toItemStack(), event1 -> {

            LobbyPlayer lobbyPlayer = new LobbyPlayer(player);

            lobbyPlayer.unlockCosmetic(cosmetic);

            CosmeticInventory.COSMETICS_INVENTORY.open(player);

        }));

    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
