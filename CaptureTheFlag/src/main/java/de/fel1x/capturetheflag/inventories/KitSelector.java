package de.fel1x.capturetheflag.inventories;

import de.fel1x.capturetheflag.CaptureTheFlag;
import de.fel1x.capturetheflag.gameplayer.GamePlayer;
import de.fel1x.capturetheflag.kit.IKit;
import de.fel1x.capturetheflag.kit.Kit;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class KitSelector implements InventoryProvider {

    public static final SmartInventory KIT_SELECTOR = SmartInventory.builder()
            .id("customInventory")
            .provider(new KitSelector())
            .size(1, 9)
            .title("§e§lWähle dein Kit")
            .manager(CaptureTheFlag.getInstance().getInventoryManager())
            .build();

    private final CaptureTheFlag captureTheFlag = CaptureTheFlag.getInstance();

    @Override
    public void init(Player player, InventoryContents contents) {

        int i = 0;

        for (Kit kit : Kit.values()) {
            if (kit == Kit.NONE) continue;

            try {
                IKit iKit = kit.getClazz().newInstance();

                contents.set(0, i, ClickableItem.of(new ItemBuilder(iKit.getKitMaterial())
                        .setName("§8● §e" + iKit.getKitName())
                        .setLore(iKit.getKitDescription()).toItemStack(), event -> {

                    if (!(event.getWhoClicked() instanceof Player)) return;

                    Player clickedPlayer = (Player) event.getWhoClicked();
                    GamePlayer gamePlayer = new GamePlayer(clickedPlayer);

                    gamePlayer.selectKit(kit);
                    player.sendMessage(this.captureTheFlag.getPrefix() + "§7Du hast das §e"
                            + iKit.getKitName() + "-Kit §7ausgewählt!");
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5, 0.5f);
                }));

                i++;
            } catch (InstantiationException | IllegalAccessException exception) {
                player.sendMessage(this.captureTheFlag.getPrefix() + "§cEs trat ein Fehler auf!");
                exception.printStackTrace();
            }
        }
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
