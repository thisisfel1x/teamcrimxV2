package de.fel1x.bingo.listener.player;

import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.events.BingoItemUnlockEvent;
import de.fel1x.bingo.gamehandler.Gamestate;
import de.fel1x.bingo.objects.BingoPlayer;
import de.fel1x.bingo.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.Objects;

public class InventoryClickListener implements Listener {

    private final Bingo bingo;

    public InventoryClickListener(Bingo bingo) {
        this.bingo = bingo;
        bingo.getPluginManager().registerEvents(this, bingo);
    }

    @EventHandler
    public void on(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        BingoPlayer bingoPlayer = new BingoPlayer(player);

        Gamestate gamestate = this.bingo.getGamestateHandler().getGamestate();

        if (!(gamestate.equals(Gamestate.INGAME))) {
            event.setCancelled(true);
        } else {
            if (event.getView().getTitle().toLowerCase().contains("items")) {
                event.setCancelled(true);
                return;
            }

            if (!bingoPlayer.isPlayer()) {
                event.setCancelled(true);
                return;
            }

            if (bingoPlayer.getTeam() == null) {
                return;
            }

            if (event.getSlotType().equals(InventoryType.SlotType.RESULT) ||
                    ((event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY) || event.getAction().equals(InventoryAction.PICKUP_ALL))
                            && !event.getView().getBottomInventory().equals(event.getClickedInventory()))) {

                this.bingo.getItemGenerator().getPossibleItems().values().forEach(bingoItem -> {
                    if (bingoItem.getMaterial().equals(Objects.requireNonNull(event.getCurrentItem()).getType())) {

                        int unlock = this.bingo.getItemGenerator().getPossibleItems().inverse().get(bingoItem);
                        Boolean[] array = bingoPlayer.getTeam().getDoneItems();

                        if (!array[unlock]) {
                            array[unlock] = true;

                            bingoPlayer.getTeam().setDoneItems(array);
                            bingoPlayer.getTeam().increaseByOne();
                            Bukkit.broadcastMessage(this.bingo.getPrefix() + "§a" + player.getDisplayName() + " §8("
                                    + Utils.getChatColor(bingoPlayer.getTeam().getColor())
                                    + "Team " + bingoPlayer.getTeam().getName() + "§8)"
                                    + " §7hat das Item §b§l"
                                    + event.getCurrentItem().getType().name().replace('_', ' ') + " §7gefunden! §8(§a"
                                    + bingoPlayer.getTeam().getDoneItemsSize() + "§7/§c9§8)");

                            this.bingo.getData().getCachedStats().get(player).increaseItemsCrafted();

                            Bukkit.getServer().getPluginManager().callEvent(new BingoItemUnlockEvent(player, bingoPlayer.getTeam(), bingoItem));
                        }

                    }
                });

            }
        }
    }
}
