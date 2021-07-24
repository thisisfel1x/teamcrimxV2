package de.fel1x.teamcrimx.crimxlobby.inventories.rework;

import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.crimxapi.friends.FriendSettings;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.inventories.utils.InventoryUtils;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SettingsReworkInventory implements InventoryProvider {

    public static final SmartInventory SETTINGS_REWORK_INVENTORY = SmartInventory.builder()
            .id("SETTINGS_REWORK_INVENTORY")
            .provider(new SettingsReworkInventory())
            .size(6, 9)
            .title("§8● §eEinstellungen")
            .manager(CrimxLobby.getInstance().getInventoryManager())
            .build();

    private boolean defaultSpawn;

    @Override
    public void init(Player player, InventoryContents contents) {
        InventoryUtils.setNavigationItems(contents, player, false);

        CrimxLobby.getInstance().getCrimxAPI().getMongoDB().getNestedDocumentAsync(player.getUniqueId(),
                MongoDBCollection.FRIENDS, "settings")
                .thenAccept(document -> contents.setProperty("document", document));

        this.defaultSpawn = CrimxLobby.getInstance().getData().getLobbyDatabasePlayer().get(player.getUniqueId()).isSpawnAtLastLocation();
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        if(contents.property("document") == null) {
            return;
        }

        Document document = contents.property("document");

        if(document == null) {
            return;
        }

        int column = 1;

        for (FriendSettings setting : FriendSettings.values()) {
            boolean bool = document.getBoolean(setting.name());
            contents.set(3, column, ClickableItem.of(new ItemBuilder(setting.getDisplayMaterial())
                    .setName(Component.empty().append(Component.text(setting.getName(), NamedTextColor.YELLOW)).append(Component.space())
                            .append(Component.text("●", NamedTextColor.GRAY).append(Component.space())
                                    .append(Component.text(bool ? "aktiviert" : "deaktiviert",
                                            bool ? NamedTextColor.GREEN : NamedTextColor.RED))).asComponent().decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                    .setLore(Component.empty().append(Component.newline()).append(Component.text(setting.getDescription(), NamedTextColor.GRAY)
                            .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
                    .addGlow(bool)
                    .toItemStack(), inventoryClickEvent -> {
                document.put(setting.name(), !bool);
                CrimxLobby.getInstance().getCrimxAPI().getMongoDB()
                        .updateValueInNestedDocument(player.getUniqueId(),
                                MongoDBCollection.FRIENDS, "settings", setting.name(), !bool);
            }));
            column++;
        }

        contents.set(3, 6, ClickableItem.of(
                new ItemBuilder(Material.MAGMA_CREAM)
                        .setName(Component.text("●", NamedTextColor.DARK_GRAY).append(Component.space())
                                .append(Component.text("Spawnpoint", NamedTextColor.YELLOW)).append(Component.space())
                                .append(Component.text("●", NamedTextColor.GRAY)).append(Component.space())
                                .append(Component.text(this.defaultSpawn ? "aktiviert" : "deaktiviert",
                                        this.defaultSpawn ? NamedTextColor.GREEN : NamedTextColor.RED)
                                        .asComponent().decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
                        .setLore("",
                                "§aAktiviert §8- §7Du spawnst beim Join", "                 §7an deiner letzten Position",
                                "§cDeaktiviert §8- §7Du spawnst immer am Spawn", "")
                        .addGlow(this.defaultSpawn)
                        .toItemStack(), inventoryClickEvent -> {
                    this.defaultSpawn = !this.defaultSpawn;
                    CrimxLobby.getInstance().getData().getLobbyDatabasePlayer().get(player.getUniqueId()).setSpawnAtLastLocation(this.defaultSpawn);
                }));
    }
}
