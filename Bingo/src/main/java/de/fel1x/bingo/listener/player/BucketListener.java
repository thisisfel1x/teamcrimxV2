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
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

public class BucketListener implements Listener {

    private final Bingo bingo;

    public BucketListener(Bingo bingo) {
        this.bingo = bingo;
        bingo.getPluginManager().registerEvents(this, bingo);
    }

    @EventHandler
    public void on(PlayerBucketFillEvent event) {

        Player player = event.getPlayer();
        BingoPlayer bingoPlayer = new BingoPlayer(player);

        Gamestate gamestate = this.bingo.getGamestateHandler().getGamestate();

        if (gamestate.equals(Gamestate.INGAME)) {
            if (!bingoPlayer.isPlayer()) {
                event.setCancelled(true);
                return;
            }

            if (bingoPlayer.getTeam() == null) {
                return;
            }

            this.bingo.getItemGenerator().getPossibleItems().values().forEach(bingoItem -> {
                if (bingoItem.getMaterial().equals(event.getBucket())) {
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
                                + WordUtils.capitalizeFully(event.getBucket().name().replace('_', ' '))
                                + " §7gefunden! §8(§a"
                                + bingoPlayer.getTeam().getDoneItemsSize() + "§7/§c9§8)");

                        Bukkit.getServer().getPluginManager().callEvent(new BingoItemUnlockEvent(player, bingoPlayer.getTeam(), bingoItem));
                    }
                }
            });
        } else {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void on(PlayerBucketEmptyEvent event) {

        Player player = event.getPlayer();
        BingoPlayer bingoPlayer = new BingoPlayer(player);

        Gamestate gamestate = this.bingo.getGamestateHandler().getGamestate();

        if (gamestate.equals(Gamestate.INGAME)) {
            if (!bingoPlayer.isPlayer()) {
                event.setCancelled(true);
            }

        } else {
            event.setCancelled(true);
        }
    }
}
