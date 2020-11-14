package de.fel1x.bingo.commands;

import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.objects.BingoPlayer;
import de.fel1x.bingo.objects.stats.Stats;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class StatsCommand implements CommandExecutor {

    private final Bingo bingo;

    public StatsCommand(Bingo bingo) {
        this.bingo = bingo;
        this.bingo.getCommand("stats").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] args) {

        if (!(commandSender instanceof Player)) return false;

        Player player = (Player) commandSender;
        BingoPlayer gamePlayer = new BingoPlayer(player);

        try {
            if (args.length == 1) {
                player.sendMessage(this.bingo.getPrefix() + "§7Statistiken von anderen Spielern können momentan " +
                        "§cnoch nicht §7eingesehen werden!");
                return false;
            } else {
                if (this.bingo.getData().getCachedStats().get(player) == null) {
                    player.sendMessage(this.bingo.getPrefix() + "§cDeine Stats werden noch geladen. " +
                            "Bitte warte einen Moment.");
                    return false;
                } else {
                    Stats stats = this.bingo.getData().getCachedStats().get(player);

                    String ranking = String.format(Locale.GERMANY, "%d", stats.getPlacement());

                    int itemsPickedUp = stats.getItemsPickedUp();
                    int itemsCrafted = stats.getItemsCrafted();
                    int gamesPlayed = stats.getGamesPlayed();
                    int gamesWon = stats.getGamesWon();

                    double winrate = 0;
                    if (gamesPlayed > 0) {
                        double a = ((double) gamesWon / gamesPlayed) * 100;
                        winrate = Math.round(a);
                    }

                    String[] message = {
                            "§7<=========[ §a§lSTATS §7]=========>",
                            " §7Position im Ranking§8: §e" + ranking,
                            " §7Items aufgesammelt§8: §e" + itemsPickedUp,
                            " §7Items gecrafted§8: §e" + itemsCrafted,
                            " §7Gespielte Spiele§8: §e" + gamesPlayed,
                            " §7Gewonnene Spiele§8: §e" + gamesWon,
                            " §7Siegeswahrscheinlichkeit§8: §e" + winrate + "%",
                            "§7<=========[ §a§lSTATS §7]=========>"
                    };

                    player.sendMessage(message);
                }
            }

        } catch (Exception ignored) {
            player.sendMessage(this.bingo.getPrefix() + "§cEin Fehler ist aufgetreten!");
        }

        return true;
    }
}