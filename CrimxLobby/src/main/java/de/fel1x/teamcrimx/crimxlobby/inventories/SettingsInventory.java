package de.fel1x.teamcrimx.crimxlobby.inventories;

import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.objects.LobbyPlayer;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SettingsInventory implements InventoryProvider {

    public static final SmartInventory SETTINGS_INVENTORY = SmartInventory.builder()
            .id("customInventory")
            .provider(new SettingsInventory())
            .size(4, 9)
            .title("§8● §cEinstellungen")
            .manager(CrimxLobby.getInstance().getInventoryManager())
            .build();

    CrimxLobby crimxLobby = CrimxLobby.getInstance();

    boolean defaultSpawn;
    boolean hotbarSound;

    @Override
    public void init(Player player, InventoryContents contents) {

        defaultSpawn = this.crimxLobby.getData().getLobbyDatabasePlayer().get(player.getUniqueId()).isSpawnAtLastLocation();
        hotbarSound = this.crimxLobby.getData().getLobbyDatabasePlayer().get(player.getUniqueId()).isHotbarSoundEnabled();

        contents.fillBorders(ClickableItem.empty(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, 1).setName(" ").toItemStack()));

        contents.set(1, 3, ClickableItem.empty(
                new ItemBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjEyYTAzYTRjMTFiNGQ0NzI0NzJlN2U0NTkzZDJlMTI2YTYyNTllMzNjYzgxZjQ0ZWIwNWNmMDQyZDA3Njk2NyJ9fX0=")
                        .setName("§8● §b§lCustom Spawn")
                        .setLore("",
                                "§aAktiviert §8- §7Du spawnst beim Join", "                 §7an deiner letzten Position",
                                "§cDeaktiviert §8- §7Du spawnst immer am Spawn", "")
                        .toItemStack()));

        contents.set(1, 5, ClickableItem.empty(
                new ItemBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGNlZWI3N2Q0ZDI1NzI0YTljYWYyYzdjZGYyZDg4Mzk5YjE0MTdjNmI5ZmY1MjEzNjU5YjY1M2JlNDM3NmUzIn19fQ==")
                        .setName("§8● §e§lHotbar Sound")
                        .setLore("",
                                "§aAktiviert §8- §7Beim wechseln des Slots", "                 §7ertönt ein Sound",
                                "§cDeaktiviert §8- §7Der Sound ist deaktiviert", "")
                        .toItemStack()));

    }

    @Override
    public void update(Player player, InventoryContents contents) {

        LobbyPlayer lobbyPlayer = new LobbyPlayer(player);

        contents.set(2, 3, ClickableItem.of(new ItemBuilder((defaultSpawn) ? Material.LIME_DYE : Material.RED_DYE)
                        .setName((defaultSpawn) ? "§aAktiviert" : "§cDeaktiviert")
                        .toItemStack(),
                event -> {
                    defaultSpawn = !defaultSpawn;
                    lobbyPlayer.saveObjectInDocument("defaultSpawn", defaultSpawn, MongoDBCollection.LOBBY);
                    this.crimxLobby.getData().getLobbyDatabasePlayer().get(player.getUniqueId()).setSpawnAtLastLocation(defaultSpawn);
                }));

        contents.set(2, 5, ClickableItem.of(new ItemBuilder((hotbarSound) ? Material.LIME_DYE : Material.RED_DYE)
                        .setName((hotbarSound) ? "§aAktiviert" : "§cDeaktiviert")
                        .toItemStack(),
                event -> {
                    hotbarSound = !hotbarSound;
                    lobbyPlayer.saveObjectInDocument("hotbarSound", hotbarSound, MongoDBCollection.LOBBY);
                    this.crimxLobby.getData().getLobbyDatabasePlayer().get(player.getUniqueId()).setHotbarSoundEnabled(hotbarSound);
                }));
    }
}
