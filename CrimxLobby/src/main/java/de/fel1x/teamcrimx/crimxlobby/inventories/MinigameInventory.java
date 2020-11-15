package de.fel1x.teamcrimx.crimxlobby.inventories;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.objects.LobbyPlayer;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MinigameInventory implements InventoryProvider {

    public static final SmartInventory MINIGAME_INVENTORY = SmartInventory.builder()
            .id("customInventory")
            .provider(new MinigameInventory())
            .size(3, 9)
            .title("§8● §6Wähle ein Minispiel")
            .manager(CrimxLobby.getInstance().getInventoryManager())
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {

        contents.fillBorders(ClickableItem.empty(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, 1).setName(" ").toItemStack()));

        contents.set(1, 3, ClickableItem.of(new ItemBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTY4YWI4MWRlZDMxZGZkMWFkNzIyYTI4YTZmYWQwYjVlZWRmZDczNmU4NzU3YjBjMWU1Y2ZmNjU0MTc5Yjc4In19fQ==")
                .setName("§8» §aWaterMLG").toItemStack(), event -> {

            LobbyPlayer lobbyPlayer = new LobbyPlayer(player);
            lobbyPlayer.startWaterMLG();

        }));

        contents.set(1, 5, ClickableItem.of(new ItemBuilder(Material.DIAMOND_BOOTS)
                .setName("§8» §bJump and Run").toItemStack(), event -> {

            LobbyPlayer lobbyPlayer = new LobbyPlayer(player);
            if (!lobbyPlayer.isInJumpAndRun()) {
                lobbyPlayer.startJumpAndRun();
            }

        }));

    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
