package de.fel1x.teamcrimx.crimxlobby.inventories;

import com.google.common.base.Splitter;
import de.dytanic.cloudnet.ext.bridge.ServiceInfoSnapshotUtil;
import de.fel1x.teamcrimx.crimxapi.server.ServerType;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.objects.Spawn;
import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NavigatorInventory {

    private static final String mlgWarsLegacyDescription = "§7Kämpfe im Himmel mit außergewöhnlichen \n§eKit-Fähigkeiten §7und geladener \n§eMLG-Action §7um den Sieg";
    private static final String floorIsLavaLegacyDescription = "§7Erfinde §eTaktiken §7und equipe dich, \n§7um der steigenden §eLava §7zu entkommen \n§7und dich gegen Spieler zu behaupten \n\n§cLegacy: Dieser Modus erhält\n          §ckeine weiteren Updates";
    private static final String bingoLegacyDescription = "§7Sei mit deinem Team die Ersten, \n§7welche alle §e9 Items §7gesammelt, \n§7gecraftet oder verzaubert haben \n\n§cLegacy: Dieser Modus erhält\n          §ckeine weiteren Updates";
    private static final String captureTheFlagLegacyDescription = "§7Arbeite mit deinem Team zusammen, \n§7um die §eFlagge §7des §egegnerischen Teams \n§7zu erobern und zu sichern! \n\n§cLegacy: Dieser Modus erhält\n          §ckeine weiteren Updates";
    private static Gui navigatorInventory;
    private boolean showArrow = true;
    private int playerCount;

    public NavigatorInventory(CrimxLobby crimxLobby) {

        navigatorInventory = Gui.gui()
                .title(Component.text("● Wähle dein Ziel"))
                .rows(5)
                .create();

        for (Navigation navigation : Navigation.values()) {
            this.playerCount = this.getCurrentPlayerCount(navigation.getPlainTaskName());

            List<Component> descriptionComponents = new ArrayList<>();
            if (navigation.getLegacyDescriptionString() != null) {
                descriptionComponents.add(Component.empty());

                String legacyDescription = navigation.getLegacyDescriptionString();
                Iterable<String> pieces = Splitter.on("\n").split(legacyDescription);

                for (String piece : pieces) {
                    descriptionComponents.add(LegacyComponentSerializer.legacySection().deserialize(piece));
                }
            }

            descriptionComponents.addAll(Arrays.asList(Component.empty(), Component.text("► ", NamedTextColor.DARK_GRAY)
                            .decoration(TextDecoration.ITALIC, false)
                            .append(Component.text("Momentan "
                                    + ((this.playerCount == 1) ? "spielt" : "spielen") + " ", NamedTextColor.GRAY)
                                    .append(Component.text(this.playerCount + " Spieler", TextColor.fromHexString("#faef16")))),
                    Component.empty()));

            navigatorInventory.setItem(navigation.getRow(), navigation.getColumn(),
                    dev.triumphteam.gui.builder.item.ItemBuilder.from(navigation.getDisplayItem())
                            .name(Component.text("● ", NamedTextColor.DARK_GRAY)
                                    .decoration(TextDecoration.ITALIC, false).append(navigation.getDisplayName()))
                            .lore(descriptionComponents)
                            .pdc(persistentDataContainer -> persistentDataContainer.set(new NamespacedKey(crimxLobby, "nav"), PersistentDataType.STRING, navigation.toString()))
                            .asGuiItem(event -> {
                                if (event.getWhoClicked() instanceof Player player) {
                                    player.closeInventory();
                                    player.teleport(navigation.getToTeleport());

                                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.5f, 1.25f);
                                }
                            }));
        }

        Bukkit.getScheduler().runTaskTimer(crimxLobby, () -> {
            this.showArrow = !this.showArrow;

            navigatorInventory.getGuiItems().forEach((index, guiItem) -> {
                ItemStack guiItemStack = guiItem.getItemStack();
                ItemMeta itemMeta = guiItemStack.getItemMeta();
                NamespacedKey namespacedKey = new NamespacedKey(crimxLobby, "nav");

                if (!itemMeta.hasLore()) {
                    return;
                }

                this.playerCount = 0;
                int loreCountToUpdate = itemMeta.lore().size() - 2;

                if (itemMeta.getPersistentDataContainer().has(namespacedKey, PersistentDataType.STRING)) {
                    try {
                        Navigation navigation = Navigation.valueOf(itemMeta.getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING));
                        this.playerCount = this.getCurrentPlayerCount(navigation.getPlainTaskName());
                    } catch (Exception ignored) {
                    }
                }

                List<Component> lore = itemMeta.lore();

                lore.set(loreCountToUpdate, Component.text(((this.showArrow) ? "►" : " ") + " ", NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false)
                        .append(Component.text("Momentan "
                                + ((this.playerCount == 1) ? "spielt" : "spielen") + " ", NamedTextColor.GRAY)
                                .append(Component.text(this.playerCount + " Spieler", TextColor.fromHexString("#faef16")))));

                itemMeta.lore(lore);
                guiItemStack.setItemMeta(itemMeta);

                navigatorInventory.update();
            });

        }, 3 * 20L, 20L);

    }

    public static Gui getNavigatorInventory() {
        return navigatorInventory;
    }

    private int getCurrentPlayerCount(String... groupNames) {
        int toReturn = 0;

        for (String groupName : groupNames) {
            toReturn += ServiceInfoSnapshotUtil.getGroupOnlineCount(groupName);
        }

        return toReturn;
    }

    public enum Navigation {

        SPAWN(Component.text("Spawn", NamedTextColor.YELLOW), 5, 4,
                Spawn.SPAWN.getSpawn(), false, Material.MAGMA_CREAM, ServerType.LOBBY_SERVER,
                null, "Lobby"),
        MLG_WARS(Component.text("MlgWars", NamedTextColor.GOLD), 4, 2,
                Spawn.MLGWARS.getSpawn(), false, Material.TNT_MINECART, ServerType.GAME_SERVER,
                mlgWarsLegacyDescription, "MlgWars", "TeamMlgWars", "Labor"),
        FLOOR_IS_LAVA(Component.text("TheFloorIsLava", NamedTextColor.RED), 6, 2,
                Spawn.FLOOR_IS_LAVA.getSpawn(), false, Material.LAVA_BUCKET, ServerType.LOBBY_SERVER,
                floorIsLavaLegacyDescription, "FloorIsLava"),
        BINGO(Component.text("Bingo", NamedTextColor.GREEN), 3, 3,
                Spawn.BINGO.getSpawn(), false, Material.BAMBOO, ServerType.GAME_SERVER,
                bingoLegacyDescription, "Bingo"),
        CAPTURE_THE_FLAG(Component.text("CaptureTheFlag", NamedTextColor.BLUE), 7, 3,
                Spawn.CAPTURE_THE_FLAG.getSpawn(), false, Material.BLUE_BANNER, ServerType.GAME_SERVER,
                captureTheFlagLegacyDescription, "CTF");

        private final Component displayName;
        private final int column;
        private final int row;
        private final Location toTeleport;
        private final boolean moreOptions;
        private final Material displayItem;
        private final ServerType serverType;
        private final String legacyDescriptionString;
        private final String[] plainTaskName;

        Navigation(Component displayName, int column, int row, Location toTeleport, boolean moreOptions, Material displayItem, ServerType serverType, String legacyDescriptionString, String... plainTaskName) {
            this.displayName = displayName;
            this.column = column;
            this.row = row;
            this.toTeleport = toTeleport;
            this.moreOptions = moreOptions;
            this.displayItem = displayItem;
            this.serverType = serverType;
            this.legacyDescriptionString = legacyDescriptionString;
            this.plainTaskName = plainTaskName;
        }

        public Component getDisplayName() {
            return this.displayName;
        }

        public int getColumn() {
            return this.column;
        }

        public int getRow() {
            return this.row;
        }

        public Location getToTeleport() {
            return this.toTeleport;
        }

        public boolean hasMoreOptions() {
            return this.moreOptions;
        }

        public Material getDisplayItem() {
            return this.displayItem;
        }

        public ServerType getServerType() {
            return this.serverType;
        }

        public String[] getPlainTaskName() {
            return this.plainTaskName;
        }

        public String getLegacyDescriptionString() {
            return this.legacyDescriptionString;
        }
    }

}
