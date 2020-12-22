package de.fel1x.teamcrimx.crimxlobby.inventories;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.objects.LobbyPlayer;
import de.fel1x.teamcrimx.crimxlobby.objects.Spawn;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class NavigatorInventory implements InventoryProvider {

    public static final SmartInventory NAVIGATOR_INVENTORY = SmartInventory.builder()
            .id("customInventory")
            .provider(new NavigatorInventory())
            .size(5, 9)
            .title("§8● §eWähle dein Ziel")
            .manager(CrimxLobby.getInstance().getInventoryManager())
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {

        contents.fillBorders(ClickableItem.empty(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, 1).setName(" ").toItemStack()));

        contents.set(3, 4, ClickableItem.of(new ItemBuilder(Material.MAGMA_CREAM).setName("§8» §e§lSpawn").toItemStack(), event -> {

            if (!(event.getWhoClicked() instanceof Player)) return;
            event.setCancelled(true);

            Player playerWhoClicked = (Player) event.getWhoClicked();
            LobbyPlayer lobbyPlayer = new LobbyPlayer(playerWhoClicked);

            player.closeInventory();
            lobbyPlayer.teleport(Spawn.SPAWN);

            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.5f, 1.25f);
            player.playEffect(player.getLocation(), Effect.ENDER_SIGNAL, null);

        }));

        contents.set(1, 3, ClickableItem.of(new ItemBuilder(Material.TNT_MINECART).setName("§8» §6§lMlgWars").toItemStack(), event -> {

            if (!(event.getWhoClicked() instanceof Player)) return;
            event.setCancelled(true);

            Player playerWhoClicked = (Player) event.getWhoClicked();
            LobbyPlayer lobbyPlayer = new LobbyPlayer(playerWhoClicked);

            player.closeInventory();
            lobbyPlayer.teleport(Spawn.MLGWARS);

            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.5f, 1.25f);
            player.playEffect(player.getLocation(), Effect.ENDER_SIGNAL, null);


        }));

        contents.set(1, 5, ClickableItem.of(new ItemBuilder(Material.LAVA_BUCKET).setName("§8» §c§lFloorIsLava").toItemStack(), event -> {

            if (!(event.getWhoClicked() instanceof Player)) return;
            event.setCancelled(true);

            Player playerWhoClicked = (Player) event.getWhoClicked();
            LobbyPlayer lobbyPlayer = new LobbyPlayer(playerWhoClicked);

            player.closeInventory();
            lobbyPlayer.teleport(Spawn.FLOOR_IS_LAVA);

            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.5f, 1.25f);
            player.playEffect(player.getLocation(), Effect.ENDER_SIGNAL, null);

        }));

        contents.set(2, 2, ClickableItem.of(new ItemBuilder(Material.BAMBOO).setName("§8» §a§lBingo").toItemStack(), event -> {

            if (!(event.getWhoClicked() instanceof Player)) return;
            event.setCancelled(true);

            Player playerWhoClicked = (Player) event.getWhoClicked();
            LobbyPlayer lobbyPlayer = new LobbyPlayer(playerWhoClicked);

            player.closeInventory();
            lobbyPlayer.teleport(Spawn.MASTERBUILDERS);

            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.5f, 1.25f);
            player.playEffect(player.getLocation(), Effect.ENDER_SIGNAL, null);

        }));

        contents.set(2, 6, ClickableItem.of(new ItemBuilder(Material.BLUE_BANNER, 1, (byte) 4).setName("§8» §9§lCaptureTheFlag").toItemStack(), event -> {

            if (!(event.getWhoClicked() instanceof Player)) return;
            event.setCancelled(true);

            Player playerWhoClicked = (Player) event.getWhoClicked();
            LobbyPlayer lobbyPlayer = new LobbyPlayer(playerWhoClicked);

            player.closeInventory();
            lobbyPlayer.teleport(Spawn.CAPTURE_THE_FLAG);
            player.playEffect(player.getLocation(), Effect.ENDER_SIGNAL, null);

            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.5f, 1.25f);

        }));

    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
