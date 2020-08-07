package de.fel1x.teamcrimx.crimxlobby.inventories;

import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.Cosmetic;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.ICosmetic;
import de.fel1x.teamcrimx.crimxlobby.objects.LobbyPlayer;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class CosmeticInventory implements InventoryProvider {

    public static final SmartInventory COSMETICS_INVENTORY = SmartInventory.builder()
            .id("customInventory")
            .provider(new CosmeticInventory())
            .size(5, 9)
            .title("§8● §dDein Inventar")
            .manager(CrimxLobby.getInstance().getInventoryManager())
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {

        LobbyPlayer lobbyPlayer = new LobbyPlayer(player, true);

        contents.fillBorders(ClickableItem.empty(new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (byte) 7).setName(" ").toItemStack()));

        contents.set(4, 3, ClickableItem.empty(new ItemBuilder(Material.GOLD_NUGGET)
                .setName("§7Coins §8» §a§l" + (int) lobbyPlayer.getObjectFromMongoDocument("coins", MongoDBCollection.USERS)).toItemStack()));

        contents.set(4, 5, ClickableItem.of(new ItemBuilder(Material.BARRIER).setName("§8» §cCosmetics entfernen").toItemStack(), event -> {
            CrimxLobby.getInstance().getData().getCosmetic().remove(player.getUniqueId());
            CrimxLobby.getInstance().getData().getHueMap().remove(player.getUniqueId());
            player.getInventory().setArmorContents(null);
            lobbyPlayer.saveObjectInDocument("selectedCosmetic", null,
                    MongoDBCollection.LOBBY);
        }));

        int row = 1;
        int slot = 1;

        for (Cosmetic cosmetic : Cosmetic.values()) {

            boolean bought = (boolean) lobbyPlayer.getObjectFromMongoDocument(cosmetic.name(), MongoDBCollection.LOBBY);

                try {
                    ICosmetic iCosmetic = cosmetic.getCosmeticClass().newInstance();

                    if(!bought) {

                        contents.set(row, slot, ClickableItem.of(new ItemBuilder(Material.INK_SACK, 1, (byte) 8)
                                        .setName("§8● " + iCosmetic.getCosmeticName() + " §8» §e" + iCosmetic.getCosmeticCost() + " Coins")
                                        .setLore("§7§kaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                                                "§7§kaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa").toItemStack(),
                                event -> {

                                player.setMetadata("toBuy", new FixedMetadataValue(CrimxLobby.getInstance(), cosmetic.name()));
                                BuyInventory.BUY_INVENTORY.open(player);

                                }));

                    } else {

                        ItemBuilder itemBuilder = new ItemBuilder(iCosmetic.getCosmeticMaterial())
                                .setName("§8● " + iCosmetic.getCosmeticName())
                                .setLore(iCosmetic.getCosmeticDescription());

                        if (iCosmetic.getCosmeticMaterial() == Material.LEATHER_BOOTS && iCosmetic.getLeatherShoeColor() != null) {
                            itemBuilder.setLeatherArmorColor(iCosmetic.getLeatherShoeColor());
                        }

                        contents.set(row, slot, ClickableItem.of(itemBuilder.toItemStack(),
                                event -> {
                                    CrimxLobby.getInstance().getData().getHueMap().remove(player.getUniqueId());
                                    player.getInventory().setArmorContents(null);
                                    int slotToRemove = (player.hasPermission("crimxlobby.vip") ? 2 : 4);
                                    player.getInventory().setItem(slotToRemove, new ItemStack(Material.AIR));
                                    player.closeInventory();
                                    iCosmetic.startTrail(player);
                                    lobbyPlayer.saveObjectInDocument("selectedCosmetic", cosmetic.name(),
                                            MongoDBCollection.LOBBY);
                                }));

                    }

                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }

            slot++;

            if(slot == 8) {
                slot = 1;
                row++;
            }

        }

    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }


}
