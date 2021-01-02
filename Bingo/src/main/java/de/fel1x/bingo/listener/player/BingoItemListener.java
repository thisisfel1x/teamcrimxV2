package de.fel1x.bingo.listener.player;

import com.destroystokyo.paper.Title;
import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.events.BingoItemUnlockEvent;
import de.fel1x.bingo.objects.BingoPlayer;
import de.fel1x.bingo.objects.BingoTeam;
import de.fel1x.bingo.tasks.EndingTask;
import de.fel1x.bingo.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Arrays;

public class BingoItemListener implements Listener {

    private final Bingo bingo;

    public BingoItemListener(Bingo bingo) {
        this.bingo = bingo;
        bingo.getPluginManager().registerEvents(this, bingo);
    }

    @EventHandler
    public void on(BingoItemUnlockEvent event) {

        BingoTeam bingoTeam = event.getTeam();

        for (Player teamPlayer : bingoTeam.getTeamPlayers()) {
            this.bingo.getGameScoreboard().updateBoard(teamPlayer,
                    String.format("§7Items gefunden §8● §a%s§8/§c9", bingoTeam.getDoneItemsSize()),
                    "items");
        }

        boolean allDone = Arrays.stream(bingoTeam.getDoneItems()).allMatch(val -> val);

        for (Player player : Bukkit.getOnlinePlayers()) {
            this.bingo.getGameScoreboard().updateIngameScoreboard(player);
        }

        if (allDone) {

            this.bingo.startTimerByClass(EndingTask.class);

            Bukkit.broadcastMessage(this.bingo.getPrefix() + Utils.getChatColor(bingoTeam.getColor()) + "Team "
                    + bingoTeam.getName()
                    + " §7hat alle Items gefunden und somit die Runde gewonnen!");

            for (Player teamPlayer : bingoTeam.getTeamPlayers()) {
                this.bingo.getData().getCachedStats().get(teamPlayer).increaseWinsByOne();
            }

            Bukkit.getOnlinePlayers().forEach(player -> {
                BingoPlayer bingoPlayer = new BingoPlayer(player);

                player.sendTitle(Title.builder()
                        .title(Utils.getChatColor(bingoTeam.getColor()) + "§lTeam " + bingoTeam.getName())
                        .subtitle("§7hat das Spiel gewonnen")
                        .fadeIn(10)
                        .stay(40)
                        .fadeOut(10)
                        .build());

                if (bingoPlayer.isPlayer()) {
                    bingoPlayer.saveStats();
                }

                player.teleportAsync(this.bingo.getSpawnLocation());

                player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 2, 0.5f);

                player.setFoodLevel(20);
                player.setHealth(20);

                player.getInventory().clear();
                player.getInventory().setArmorContents(null);

                player.setHealth(20);
                player.setFoodLevel(20);
            });
        }
    }
}
