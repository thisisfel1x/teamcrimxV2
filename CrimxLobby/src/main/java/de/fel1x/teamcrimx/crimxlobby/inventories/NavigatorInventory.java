package de.fel1x.teamcrimx.crimxlobby.inventories;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.objects.LobbyPlayer;
import de.fel1x.teamcrimx.crimxlobby.objects.Spawn;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class NavigatorInventory implements InventoryProvider {

    // TODO: rework this shit

    public static final SmartInventory NAVIGATOR_INVENTORY = SmartInventory.builder()
            .id("NAVIGATOR_INVENTORY")
            .provider(new NavigatorInventory())
            .size(5, 9)
            .title("§8● §eWähle dein Ziel")
            .manager(CrimxLobby.getInstance().getInventoryManager())
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {

        contents.fillBorders(ClickableItem.empty(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, 1)
                .setName(Component.empty()).toItemStack()));

        for (Navigation navigation : Navigation.values()) {

            contents.set(navigation.getRow(), navigation.getColumn(),
                    ClickableItem.of(new ItemBuilder(navigation.getDisplayItem())
                                    .setName(Component.text("●", NamedTextColor.DARK_GRAY).append(Component.space())
                                            .append(navigation.getDisplayName())
                                            .asComponent().decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                                    .setLore(navigation.getLore().decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                                    .toItemStack(),
                            event -> {
                                if (!(event.getWhoClicked() instanceof Player)) return;
                                event.setCancelled(true);

                                player.closeInventory();
                                player.teleport(navigation.getToTeleport());
                                player.playEffect(player.getLocation(), Effect.ENDER_SIGNAL, null);

                                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.5f, 1.25f);
                            }));
        }
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    // TODO: update
    public enum Navigation {

        SPAWN(Component.text("Spawn", NamedTextColor.YELLOW), Component.text("spawn"), 3, 2,
                Spawn.SPAWN.getPlayerSpawn(), false, Material.MAGMA_CREAM),
        MLG_WARS(Component.text("MlgWars", NamedTextColor.GOLD), Component.text("mlgwarts"), 1, 3,
                Spawn.MLGWARS.getPlayerSpawn(), false, Material.TNT_MINECART),
        FLOOR_IS_LAVA(Component.text("TheFloorIsLava", NamedTextColor.RED), Component.text("floorislava"), 1, 5,
                Spawn.FLOOR_IS_LAVA.getPlayerSpawn(), false, Material.LAVA_BUCKET),
        BINGO(Component.text("Bingo", NamedTextColor.GREEN), Component.text("bingo"), 2, 2,
                Spawn.SPAWN.getPlayerSpawn(), false, Material.BAMBOO),
        CAPTURE_THE_FLAG(Component.text("CaptureTheFlag", NamedTextColor.BLUE), Component.text("ctf"), 2, 6,
                Spawn.CAPTURE_THE_FLAG.getPlayerSpawn(), false, Material.BLUE_BANNER);


        private Component displayName;
        private Component lore;
        private int column;
        private int row;
        private Location toTeleport;
        private boolean moreOptions;
        private Material displayItem;

        Navigation(Component displayName, Component lore, int column, int row, Location toTeleport, boolean moreOptions, Material displayItem) {
            this.displayName = displayName;
            this.lore = lore;
            this.column = column;
            this.row = row;
            this.toTeleport = toTeleport;
            this.moreOptions = moreOptions;
            this.displayItem = displayItem;
        }

        public Component getDisplayName() {
            return this.displayName;
        }

        public void setDisplayName(Component displayName) {
            this.displayName = displayName;
        }

        public Component getLore() {
            return this.lore;
        }

        public void setLore(Component lore) {
            this.lore = lore;
        }

        public int getColumn() {
            return this.column;
        }

        public void setColumn(int column) {
            this.column = column;
        }

        public int getRow() {
            return this.row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public Location getToTeleport() {
            return this.toTeleport;
        }

        public void setToTeleport(Location toTeleport) {
            this.toTeleport = toTeleport;
        }

        public boolean isMoreOptions() {
            return this.moreOptions;
        }

        public void setMoreOptions(boolean moreOptions) {
            this.moreOptions = moreOptions;
        }

        public Material getDisplayItem() {
            return this.displayItem;
        }

        public void setDisplayItem(Material displayItem) {
            this.displayItem = displayItem;
        }
    }

}
