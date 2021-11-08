package de.fel1x.teamcrimx.mlgwars.inventories.rework;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.inventories.BuyInventory;
import de.fel1x.teamcrimx.mlgwars.kit.rework.InventoryKitManager;
import de.fel1x.teamcrimx.mlgwars.kit.rework.KitRegistry;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class KitOverviewInventory {

    private final MlgWars mlgWars;

    public KitOverviewInventory(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
    }

    public void openInventory(Player player) {
        GamePlayer gamePlayer = this.mlgWars.getData().getGamePlayers().get(player.getUniqueId());

        Gui gui = Gui.gui()
                .title(Component.text("● Wähle dein Kit"))
                .rows(4)
                .create();

        for (KitRegistry kitRegistry : KitRegistry.values()) {
            boolean bought = gamePlayer.getBoughtKits().get(kitRegistry);
            InventoryKitManager.InventoryKit inventoryKit = this.mlgWars.getInventoryKitManager()
                    .getAvailableKits().get(kitRegistry);

            gui.addItem(ItemBuilder.from(inventoryKit.getKitMaterial())
                    .name(inventoryKit.getKitName().append(Component.text(" » ", NamedTextColor.DARK_GRAY)
                            .append(bought ? Component.text("gekauft", NamedTextColor.GREEN)
                                    : Component.text("nicht gekauft", NamedTextColor.RED))))
                    .lore(inventoryKit.getKitDescription())
                    .asGuiItem(event -> {
                        if(bought) {
                            gamePlayer.setSelectedKit(kitRegistry);

                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5, 0.5f);
                            player.sendMessage(this.mlgWars.prefix()
                                    .append(Component.text("Du nutzt nun das [", NamedTextColor.GRAY)
                                            .append(inventoryKit.getKitName().color(NamedTextColor.YELLOW))
                                            .append(Component.text("] Kit", NamedTextColor.GRAY))));
                            player.closeInventory();
                        } else {
                            player.setMetadata("toBuy", new FixedMetadataValue(this.mlgWars, kitRegistry));
                            BuyInventory.INVENTORY.open(player);
                        }
                    }));
        }
        gui.open(player);
    }
}
