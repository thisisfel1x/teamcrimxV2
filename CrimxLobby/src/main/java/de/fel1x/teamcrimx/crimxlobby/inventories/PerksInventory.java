package de.fel1x.teamcrimx.crimxlobby.inventories;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.apache.commons.lang.WordUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PerksInventory implements InventoryProvider {

    public static final SmartInventory PERKS_INVENTORY = SmartInventory.builder()
            .id("customInventory")
            .provider(new PerksInventory())
            .size(3, 9)
            .title("§8● §aPerks §7- §6FloorIsLava")
            .manager(CrimxLobby.getInstance().getInventoryManager())
            .build();

    private final CrimxLobby crimxLobby = CrimxLobby.getInstance();

    private final Material[] glassTypes = {

            Material.WHITE_STAINED_GLASS,
            Material.ORANGE_STAINED_GLASS,
            Material.MAGENTA_STAINED_GLASS,
            Material.LIGHT_BLUE_STAINED_GLASS,
            Material.YELLOW_STAINED_GLASS,
            Material.LIME_STAINED_GLASS,
            Material.PINK_STAINED_GLASS,
            Material.GRAY_STAINED_GLASS,
            Material.LIGHT_GRAY_STAINED_GLASS,
            Material.PURPLE_STAINED_GLASS,
            Material.BLUE_STAINED_GLASS,
            Material.BROWN_STAINED_GLASS,
            Material.GREEN_STAINED_GLASS,
            Material.RED_STAINED_GLASS

    };

    @Override
    public void init(Player player, InventoryContents contents) {
        int row = 0;
        int slot = 0;
        for (Material glassType : this.glassTypes) {
            contents.set(row, slot, ClickableItem.of(new ItemBuilder(glassType)
                            .setName(WordUtils.capitalizeFully(glassType.name().replace('_', ' ')))
                            .toItemStack(),
                    inventoryClickEvent -> {
                player.closeInventory();
                player.sendMessage(CrimxLobby.getInstance().getPrefix() + "§7Du nutzt nun §a"
                        + WordUtils.capitalizeFully(glassType.name().replace('_', ' ')) + " §7als Startblock");
                Bukkit.getScheduler().runTaskAsynchronously(this.crimxLobby, () -> {
                    Document playerDocument = this.crimxLobby.getCrimxAPI().getMongoDB().getFloorIsLavaCollection().
                            find(new Document("_id", player.getUniqueId().toString())).first();
                    if(playerDocument != null) {
                    Document toUpdate = new Document("blockType", glassType.name());
                    Bson updateOperation = new Document("$set", toUpdate);
                    this.crimxLobby.getCrimxAPI().getMongoDB().getFloorIsLavaCollection().updateOne(playerDocument, updateOperation);
                    }
                });
            }));
            slot++;
            if(slot > 8) {
                slot = 0;
                row++;
            }
        }
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
