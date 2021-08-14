package de.fel1x.teamcrimx.crimxlobby.inventories.rework;

import de.fel1x.teamcrimx.crimxapi.cosmetic.CosmeticCategory;
import de.fel1x.teamcrimx.crimxapi.cosmetic.CosmeticRegistry;
import de.fel1x.teamcrimx.crimxapi.cosmetic.ICosmetic;
import de.fel1x.teamcrimx.crimxapi.cosmetic.database.CosmeticPlayer;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.crimxapi.support.CrimxSpigotAPI;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicInteger;

public class CosmeticReworkInventory implements InventoryProvider {

    public static final SmartInventory COSMETICS_REWORK_INVENTORY = SmartInventory.builder()
            .id("COSMETIC_REWORK_INVENTORY")
            .provider(new CosmeticReworkInventory())
            .size(6, 9)
            .title("§8● §dDeine Cosmetics")
            .manager(CrimxLobby.getInstance().getInventoryManager())
            .build();

    private final String oakWoodX = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWE2Nzg3YmEzMjU2NGU3YzJmM2EwY2U2NDQ5OGVjYmIyM2I4OTg0NWU1YTY2YjVjZWM3NzM2ZjcyOWVkMzcifX19";

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
        final CosmeticPlayer cosmeticPlayer = new CosmeticPlayer(player.getUniqueId());

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
                                this.setInventoryContents(contents, pagination, cosmeticCategory, player);
                            }));
        }

        // Remove all Cosmetics Item
        contents.set(5, 0, ClickableItem.of(new ItemBuilder(Material.BARRIER)
                .setName(Component.text("§8● §cEntferne alle Cosmetics"))
                .toItemStack(), event -> {
            player.closeInventory();
            cosmeticPlayer.stopAllActiveCosmeticsAsync(player).thenAccept(success -> {
                if (!success) {
                    player.sendMessage(CrimxLobby.getInstance().getPrefix()
                            + "§cEin Fehler ist aufgetreten. Versuche es später erneut");
                }
            });
        }));

        // Active Cosmetics
        AtomicInteger column = new AtomicInteger(2);
        cosmeticPlayer.getSelectedCosmeticsAsync().thenAccept(cosmeticRegistries -> {
            for (CosmeticRegistry cosmeticRegistry : cosmeticRegistries) {
                if (cosmeticRegistry == null) {
                    continue;
                }
                contents.set(5, column.get(), ClickableItem.of(new ItemBuilder(this.oakWoodX)
                        .setName(Component.text("● ", NamedTextColor.DARK_GRAY)
                                .append(Component.text(cosmeticRegistry.getCosmeticCategory().getPlainName(),
                                        NamedTextColor.GRAY).append(Component.text(" -> ", NamedTextColor.DARK_GRAY)
                                        .append(Component.text("aktiv", NamedTextColor.GREEN)))))
                        .setLore(Component.empty(), Component.text("Klicke zum ausziehen", NamedTextColor.RED),
                                Component.empty())
                        .toItemStack(), event -> {
                    cosmeticPlayer.stopCosmeticByType(cosmeticRegistry.getCosmeticCategory(), player);
                    player.closeInventory();
                }));
                column.getAndIncrement();
            }
        });

        // Pagination Items
        contents.set(5, 7, ClickableItem.of(new ItemBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ2OWUwNmU1ZGFkZmQ4NGU1ZjNkMWMyMTA2M2YyNTUzYjJmYTk0NWVlMWQ0ZDcxNTJmZGM1NDI1YmMxMmE5In19fQ==")
                .setName(Component.text("§8● §7Vorherige Seite")).toItemStack(), event -> {
            if (!pagination.isFirst()) {
                COSMETICS_REWORK_INVENTORY.open(player, pagination.previous().getPage());
            }
        }));
        contents.set(5, 8, ClickableItem.of(new ItemBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTliZjMyOTJlMTI2YTEwNWI1NGViYTcxM2FhMWIxNTJkNTQxYTFkODkzODgyOWM1NjM2NGQxNzhlZDIyYmYifX19")
                .setName(Component.text("§8● §7Nächste Seite")).toItemStack(), event -> {
            if (!pagination.isLast()) {
                COSMETICS_REWORK_INVENTORY.open(player, pagination.next().getPage());
            }
        }));

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

    private void setInventoryContents(InventoryContents contents, Pagination pagination, CosmeticCategory cosmeticCategory, Player player) {
        CrimxLobby.getInstance().getCrimxAPI().getMongoDB()
                .getDocumentAsync(player.getUniqueId(), MongoDBCollection.COSMETIC)
                .thenAccept(cosmeticDocument -> {
                    Document cosmeticCategoryRegistryDocument = (Document) ((Document) cosmeticDocument.get("registry"))
                            .get(cosmeticCategory.name());

                    if (cosmeticCategoryRegistryDocument == null) {
                        player.closeInventory();
                        player.sendMessage(CrimxLobby.getInstance().getPrefix()
                                + "§cEin Fehler ist aufgetreten. Versuche es später erneut...");
                        return;
                    }

                    ClickableItem[] cosmetics = new ClickableItem[cosmeticCategoryRegistryDocument.keySet().size()];

                    int count = 0;

                    for (String key : cosmeticCategoryRegistryDocument.keySet()) {
                        CosmeticRegistry cosmeticRegistry = CosmeticRegistry.valueOf(key);

                        try {
                            ICosmetic cosmetic = cosmeticRegistry.getCosmeticClass().getDeclaredConstructor().newInstance();

                            boolean bought = cosmeticCategoryRegistryDocument.getBoolean(key);

                            // Checks if player hasn't bought the specific cosmetic
                            if (!bought) {
                                continue;
                            }

                            ItemStack itemStack = new ItemBuilder(cosmetic.getDisplayMaterial())
                                    .setName(cosmetic.getDisplayName())
                                    .setLore(cosmetic.getDescription())
                                    .addLoreLine(Component.empty())
                                    .toItemStack();

                            cosmetics[count] = ClickableItem.of(itemStack, event -> {
                                player.closeInventory();
                                if (CrimxSpigotAPI.getInstance().getCosmeticTask().getActiveCosmetics().containsKey(player.getUniqueId())) {
                                    CrimxSpigotAPI.getInstance().getCosmeticTask().getActiveCosmetics().get(player.getUniqueId()).stopCosmetic(player);
                                }
                                cosmetic.initializeCosmetic(player);
                                new CosmeticPlayer(player.getUniqueId()).saveSelectedCosmeticToDatabase(cosmeticRegistry);
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
}
