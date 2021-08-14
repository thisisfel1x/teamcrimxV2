package de.fel1x.teamcrimx.crimxlobby.inventories;

import de.fel1x.teamcrimx.crimxapi.cosmetic.CosmeticCategory;
import de.fel1x.teamcrimx.crimxapi.cosmetic.CosmeticRegistry;
import de.fel1x.teamcrimx.crimxapi.cosmetic.ICosmetic;
import de.fel1x.teamcrimx.crimxapi.cosmetic.database.CosmeticPlayer;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.crimxapi.support.CrimxSpigotAPI;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.inventories.rework.ConfirmCosmeticBuyReworkInventory;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

public class NPCShopInventory implements InventoryProvider {

    public static final SmartInventory NPC_SHOP_INVENTORY = SmartInventory.builder()
            .id("NPC_SHOP_INVENTORY")
            .provider(new NPCShopInventory())
            .size(6, 9)
            .title("§8● §6Coinshop")
            .manager(CrimxLobby.getInstance().getInventoryManager())
            .build();

    private final int[][] glassSlots2D = {
            {
                    0, 8
            },
            {
                    0, 1, 2, 3, 4, 5, 6, 7, 8
            }, {}, {}, {},
            {
                    0, 1, 2, 3, 4, 5, 6
            }
    };

    private final int[] navigationItemsSlot = {
            1, 2, 4, 6, 7
    };

    @Override
    public void init(Player player, InventoryContents contents) {
        Pagination pagination = contents.pagination();

        contents.setProperty("sorting", Sorting.ALL); // Set default sorting and selected category
        contents.setProperty("category", null);

        int row = 0;
        for (int[] ints : this.glassSlots2D) {
            for (int i : ints) {
                contents.set(row, i, ClickableItem.empty(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
                        .setName(Component.empty()).toItemStack()));
            }
            row++;
        }

        // Navigation Items
        for (int index = 0; index < this.navigationItemsSlot.length; index++) {
            CosmeticCategory cosmeticCategory = CosmeticCategory.values()[index];

            contents.set(0, this.navigationItemsSlot[index],
                    ClickableItem.of(cosmeticCategory.getItemStack(),
                            event -> {
                                this.clearInv(contents);
                                this.setInventoryContest(contents, pagination, cosmeticCategory, player);
                            }));
        }

        // TODO: discount
        contents.set(5, 0, ClickableItem.empty(new ItemBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2IxNDIxNzgyMzUyYjkzNjg1YjU5ZjE0ZTUwNjk0OWJmN2EzM2VmZGQ4MWRkNjVlZDE3ZWE2ZGI0MjJmNTNmIn19fQ==")
                .setName(Component.text("§8● §5Angebote"))
                .setLore(Component.text("§7Nächstes Angebot in §5???"))
                .toItemStack()));
        contents.set(5, 1, ClickableItem.of(new ItemBuilder(Material.PAPER)
                        .setName(Component.text("§8● §aSortierung"))
                        .setLoreByComponentList(Sorting.ALL.lore) // We can hard-code this because default ist Sorting.ALL
                        .toItemStack(),
                event -> {
                    this.updateSorting(contents);
                    if (contents.property("category") != null) {
                        this.setInventoryContest(contents, pagination, contents.property("category"), player);
                    }
                }));

        // Pagination Items
        contents.set(5, 7, ClickableItem.of(new ItemBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ2OWUwNmU1ZGFkZmQ4NGU1ZjNkMWMyMTA2M2YyNTUzYjJmYTk0NWVlMWQ0ZDcxNTJmZGM1NDI1YmMxMmE5In19fQ==")
                .setName(Component.text("§8● §7Vorherige Seite")).toItemStack(), event -> {
            if (!pagination.isFirst()) {
                NPC_SHOP_INVENTORY.open(player, pagination.previous().getPage());
            }
        }));
        contents.set(5, 8, ClickableItem.of(new ItemBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTliZjMyOTJlMTI2YTEwNWI1NGViYTcxM2FhMWIxNTJkNTQxYTFkODkzODgyOWM1NjM2NGQxNzhlZDIyYmYifX19")
                .setName(Component.text("§8● §7Nächste Seite")).toItemStack(), event -> {
            if (!pagination.isLast()) {
                NPC_SHOP_INVENTORY.open(player, pagination.next().getPage());
            }
        }));
    }

    private void updateSorting(InventoryContents contents) {
        if (!(contents.property("sorting") instanceof Sorting)) {
            return;
        }

        Sorting sorting = contents.property("sorting");

        if (sorting == Sorting.ALL) {
            sorting = Sorting.NOT_BOUGHT;
        } else {
            sorting = Sorting.ALL;
        }

        contents.setProperty("sorting", sorting);

        if (contents.get(5, 1).isEmpty()) {
            return;
        }

        ClickableItem clickableItem = contents.get(5, 1).get();
        clickableItem.getItem().lore(sorting.lore);
        contents.set(5, 1, clickableItem);

    }

    @Override
    public void update(Player player, InventoryContents contents) {
    }

    private void clearInv(InventoryContents inventoryContents) {
        int slot = 0;
        int row = 2;

        for (int i = 0; i < 27; i++) {
            if (inventoryContents.get(row, slot).isPresent()) {
                inventoryContents.set(row, slot, ClickableItem.empty(null));
            }
            slot++;
            if (slot == 9) {
                slot = 0;
                row++;
            }
        }
    }

    private void setInventoryContest(InventoryContents contents, Pagination pagination, CosmeticCategory cosmeticCategory, Player player) {
        contents.setProperty("category", cosmeticCategory);

        if (!(contents.property("sorting") instanceof Sorting)) {
            return;
        }

        Sorting sorting = contents.property("sorting");

        CrimxLobby.getInstance().getCrimxAPI().getMongoDB()
                .getDocumentAsync(player.getUniqueId(), MongoDBCollection.COSMETIC)
                .thenAccept(cosmeticDocument -> {
                    Document cosmeticCategoryRegistryDocument = (Document) ((Document) cosmeticDocument.get("registry"))
                            .get(cosmeticCategory.name());

                    if (cosmeticCategoryRegistryDocument == null) {
                        player.closeInventory();
                        player.sendMessage("§cFEHLER - Versuche es später erneut...");
                        return;
                    }

                    ClickableItem[] cosmetics = new ClickableItem[cosmeticCategoryRegistryDocument.keySet().size()];

                    int count = 0;

                    for (String key : cosmeticCategoryRegistryDocument.keySet()) {
                        CosmeticRegistry cosmeticRegistry = CosmeticRegistry.valueOf(key);

                        try {
                            ICosmetic cosmetic = cosmeticRegistry.getCosmeticClass().getDeclaredConstructor().newInstance();

                            boolean bought = cosmeticCategoryRegistryDocument.getBoolean(key);

                            // Checks if Sorting is set for not bought and if player already bought the specific cosmetic
                            if (sorting == Sorting.NOT_BOUGHT && bought) {
                                continue;
                            }

                            ItemStack itemStack = new ItemBuilder(cosmetic.getDisplayMaterial())
                                    .setName(cosmetic.getDisplayName()
                                            .append(Component.text(" §8● " + (bought ? "§agekauft" : "§cnicht gekauft")))
                                            .asComponent().decoration(TextDecoration.ITALIC, false))
                                    .setLore(cosmetic.getDescription())
                                    .addLoreLine(Component.empty())
                                    .addLoreLine(Component.text("§7Kosten: §e" + cosmetic.getCost() + " Coins"))
                                    .toItemStack();

                            cosmetics[count] = ClickableItem.of(itemStack, event -> {
                                if (bought) {
                                    player.closeInventory();
                                    if (CrimxSpigotAPI.getInstance().getCosmeticTask().getActiveCosmetics().containsKey(player.getUniqueId())) {
                                        CrimxSpigotAPI.getInstance().getCosmeticTask().getActiveCosmetics().get(player.getUniqueId()).stopCosmetic(player);
                                    }
                                    cosmetic.initializeCosmetic(player);
                                    new CosmeticPlayer(player.getUniqueId()).saveSelectedCosmeticToDatabase(cosmeticRegistry);
                                } else {
                                    player.setMetadata("cosmetic",
                                            new FixedMetadataValue(CrimxLobby.getInstance(), cosmeticRegistry));
                                    ConfirmCosmeticBuyReworkInventory.CONFIRM_COSMETIC_BUY_REWORK_INVENTORY.open(player);
                                }
                            });
                            count++;
                        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ignored) {
                        }
                    }

                    pagination.setItems(cosmetics);
                    pagination.setItemsPerPage(27);

                    pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 2, 0));
                });
    }

    private enum Sorting {
        ALL(Arrays.asList(Component.text("§a» Alle"),
                Component.text("§7Nicht gekauft"))),
        NOT_BOUGHT(Arrays.asList(Component.text("§7Alle"),
                Component.text("§a» Nicht gekauft")));

        private final List<Component> lore;

        Sorting(List<Component> lore) {
            this.lore = lore;
        }
    }
}
