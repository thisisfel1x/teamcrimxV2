package de.fel1x.bingo.listener.player;

import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.events.BingoItemUnlockEvent;
import de.fel1x.bingo.gamehandler.Gamestate;
import de.fel1x.bingo.objects.BingoPlayer;
import de.fel1x.bingo.utils.Utils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;

import java.util.Objects;

public class InventoryMoveListener implements Listener {

    private final Bingo bingo;

    public InventoryMoveListener(Bingo bingo) {
        this.bingo = bingo;
        bingo.getPluginManager().registerEvents(this, bingo);
    }

    @Deprecated
    @EventHandler
    public void on(InventoryDragEvent event) {

        Gamestate gamestate = this.bingo.getGamestateHandler().getGamestate();

        if (!gamestate.equals(Gamestate.INGAME)) {
            event.setCancelled(true);
        } else {

            if (!(event.getWhoClicked() instanceof Player)) {
                return;
            }

            Player player = (Player) event.getWhoClicked();
            BingoPlayer bingoPlayer = new BingoPlayer(player);

            if (!bingoPlayer.isPlayer()) {
                event.setCancelled(true);
                return;
            }

            if (bingoPlayer.getTeam() == null) {
                return;
            }

            if(event.getCursor() == null) return;
            if(event.getCursor().equals(this.bingo.getBingoItemsQuickAccess())) {
                event.setCancelled(true);
                return;
            }

            if(event.getOldCursor().equals(this.bingo.getBingoItemsQuickAccess())) {
                event.setCancelled(true);
                return;
            }

            this.bingo.getItemGenerator().getPossibleItems().values().forEach(bingoItem -> {
                if (bingoItem.getMaterial().equals(Objects.requireNonNull(event.getOldCursor()).getType())) {

                    int unlock = this.bingo.getItemGenerator().getPossibleItems().inverse().get(bingoItem);
                    Boolean[] array = bingoPlayer.getTeam().getDoneItems();

                    if (!array[unlock]) {
                        array[unlock] = true;

                        bingoPlayer.getTeam().setDoneItems(array);
                        bingoPlayer.getTeam().increaseByOne();

                        Bukkit.broadcastMessage(this.bingo.getPrefix() + "§a" + player.getDisplayName() + " §8("
                                + Utils.getChatColor(bingoPlayer.getTeam().getColor())
                                + "Team " + bingoPlayer.getTeam().getName() + "§8)"
                                + " §7hast das Item §b§l"
                                + WordUtils.capitalizeFully(event.getOldCursor().getType().name().replace('_', ' '))
                                + " §7gefunden! §8(§a"
                                + bingoPlayer.getTeam().getDoneItemsSize() + "§7/§c9§8)");

                        this.bingo.getData().getCachedStats().get(player).increaseItemsCrafted();

                        Bukkit.getServer().getPluginManager().callEvent(new BingoItemUnlockEvent(player, bingoPlayer.getTeam(), bingoItem));
                    }

                }
            });
        }
    }
}
