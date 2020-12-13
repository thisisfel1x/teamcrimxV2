package de.fel1x.teamcrimx.crimxlobby.inventories;

import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.Cosmetic;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.CosmeticCategory;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.ICosmetic;
import de.fel1x.teamcrimx.crimxlobby.objects.LobbyPlayer;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Bukkit;
import org.bukkit.Color;
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

        contents.fillBorders(ClickableItem.empty(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, 1).setName(" ").toItemStack()));
        contents.set(1, 1, ClickableItem.empty(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, 1).setName(" ").toItemStack()));
        contents.set(2, 1, ClickableItem.empty(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, 1).setName(" ").toItemStack()));
        contents.set(3, 1, ClickableItem.empty(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, 1).setName(" ").toItemStack()));
        contents.set(1, 7, ClickableItem.empty(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, 1).setName(" ").toItemStack()));
        contents.set(2, 7, ClickableItem.empty(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, 1).setName(" ").toItemStack()));
        contents.set(3, 7, ClickableItem.empty(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, 1).setName(" ").toItemStack()));

        contents.set(1, 0, ClickableItem.of(new ItemBuilder(Material.HONEYCOMB)
                .setName("§8» §aBoots").toItemStack(), event -> this.updateInventory(player, CosmeticCategory.BOOTS, contents)));

        contents.set(2, 0, ClickableItem.of(new ItemBuilder(Material.TURTLE_EGG)
                .setName("§8» §bTrails").toItemStack(), event -> this.updateInventory(player, CosmeticCategory.TRAILS, contents)));

        contents.set(3, 0, ClickableItem.of(new ItemBuilder(Material.HEART_OF_THE_SEA)
                .setName("§8» §eSonstiges").toItemStack(), event -> this.updateInventory(player, CosmeticCategory.GADGETS, contents)));

        contents.set(4, 3, ClickableItem.empty(new ItemBuilder(Material.GOLD_NUGGET)
                .setName("§7Coins §8» §a§l" + (int) lobbyPlayer.getObjectFromMongoDocument("coins", MongoDBCollection.USERS)).toItemStack()));

        contents.set(1, 8, ClickableItem.of(new ItemBuilder(Material.PHANTOM_MEMBRANE)
                .setName("§8» §dKöpfe").toItemStack(), event -> this.updateInventory(player, CosmeticCategory.HEADS, contents)));

        contents.set(2, 8, ClickableItem.of(new ItemBuilder(Material.SADDLE)
                .setName("§8» §9Pets").toItemStack(), event -> this.updateInventory(player, CosmeticCategory.PETS, contents)));;

        contents.set(3, 8, ClickableItem.empty(new ItemBuilder(Material.OAK_SIGN)
                .setName("§8» §4Siegesanimationen §c(soon)").toItemStack()));

        contents.set(4, 5, ClickableItem.of(new ItemBuilder(Material.BARRIER).setName("§8» §cCosmetics entfernen").toItemStack(), event -> {
            CrimxLobby.getInstance().getData().getCosmetic().remove(player.getUniqueId());
            CrimxLobby.getInstance().getData().getHueMap().remove(player.getUniqueId());
            player.getInventory().setArmorContents(null);
            if(CrimxLobby.getInstance().getData().getPlayerPet().containsKey(player.getUniqueId())) {
                CrimxLobby.getInstance().getData().getPlayerPet().get(player.getUniqueId()).remove();
                CrimxLobby.getInstance().getData().getPlayerPet().remove(player.getUniqueId());
            }
            int slotToRemove = (player.hasPermission("crimxlobby.vip") ? 2 : 4);
            player.getInventory().setItem(slotToRemove, new ItemStack(Material.AIR));
            lobbyPlayer.saveObjectInDocument("selectedCosmetic", null,
                    MongoDBCollection.LOBBY);
        }));
    }

    private void updateInventory(Player player, CosmeticCategory cosmeticCategory, InventoryContents contents) {
        this.clear(contents);

        LobbyPlayer lobbyPlayer = new LobbyPlayer(player);

        int row = 1;
        int slot = 2;

        for (Cosmetic cosmetic : Cosmetic.values()) {
            if(cosmetic.getCosmeticCategory() != cosmeticCategory) continue;

            boolean bought = (boolean) lobbyPlayer.getObjectFromMongoDocument(cosmetic.name(), MongoDBCollection.LOBBY);

            try {
                ICosmetic iCosmetic = cosmetic.getCosmeticClass().newInstance();

                if (!bought) {

                    contents.set(row, slot, ClickableItem.of(new ItemBuilder(Material.GRAY_DYE, 1)
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
                                if(CrimxLobby.getInstance().getData().getPlayerPet().containsKey(player.getUniqueId())) {
                                    CrimxLobby.getInstance().getData().getPlayerPet().get(player.getUniqueId()).remove();
                                    CrimxLobby.getInstance().getData().getPlayerPet().remove(player.getUniqueId());
                                }
                                iCosmetic.startTrail(player);
                                lobbyPlayer.saveObjectInDocument("selectedCosmetic", cosmetic.name(),
                                        MongoDBCollection.LOBBY);
                            }));

                }

            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }

            slot++;

            if (slot == 6) {
                slot = 2;
                row++;
            }
        }
    }

    private void clear(InventoryContents inventoryContents) {
        for(int row = 1; row < 4; row++) {
            for(int slot = 2; slot < 7; slot++) {
                if(inventoryContents.get(row, slot).isPresent()) {
                    inventoryContents.set(row, slot, ClickableItem.empty(null));
                }
            }
        }
    }

    @Override
    public void update(Player player, InventoryContents contents) {
    }
}
