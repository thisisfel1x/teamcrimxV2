package de.fel1x.capturetheflag.commands;

import de.fel1x.capturetheflag.CaptureTheFlag;
import de.fel1x.capturetheflag.database.Stats;
import de.fel1x.capturetheflag.gameplayer.GamePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class StatsCommand implements CommandExecutor {

    private CaptureTheFlag captureTheFlag;

    public StatsCommand(CaptureTheFlag captureTheFlag) {
        this.captureTheFlag = captureTheFlag;
        this.captureTheFlag.getCommand("stats").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] args) {

        if (!(commandSender instanceof Player)) return false;

        Player player = (Player) commandSender;
        GamePlayer gamePlayer = new GamePlayer(player);

        try {
            if (args.length == 1) {
                player.sendMessage(this.captureTheFlag.getPrefix() + "§7Statistiken von anderen Spielern können momentan " +
                        "§cnoch nicht §7eingesehen werden!");
                return false;
            } else {
                if (this.captureTheFlag.getData().getCachedStats().get(player) == null) {
                    player.sendMessage(this.captureTheFlag.getPrefix() + "§cDeine Stats werden noch geladen. " +
                            "Bitte warte einen Moment.");
                    return false;
                } else {
                    Stats stats = this.captureTheFlag.getData().getCachedStats().get(player);

                    String ranking = String.format(Locale.GERMANY, "%d", stats.getPlacement());

                    int kills = stats.getKills();
                    int deaths = stats.getDeaths();
                    int gamesPlayed = stats.getGamesPlayed();
                    int gamesWon = stats.getGamesWon();

                    double kd;
                    if (deaths > 0) {
                        double a = ((double) kills / deaths) * 100;
                        kd = Math.round(a);
                    } else {
                        kd = kills;
                    }

                    double winrate = 0;
                    if (gamesPlayed > 0) {
                        double a = ((double) gamesWon / gamesPlayed) * 100;
                        winrate = Math.round(a);
                    }

                    String[] message = {
                            "§7<=========[ §a§lSTATS §7]=========>",
                            " §7Position im Ranking§8: §e" + ranking,
                            " §7Kills§8: §e" + kills,
                            " §7Deaths§8: §e" + deaths,
                            " §7K/D§8: §e" + kd / 100,
                            " §7Gespielte Spiele§8: §e" + gamesPlayed,
                            " §7Gewonnene Spiele§8: §e" + gamesWon,
                            " §7Siegeswahrscheinlichkeit§8: §e" + winrate + "%",
                            "§7<=========[ §a§lSTATS §7]=========>"
                    };

                    player.sendMessage(message);
                }
            }

        } catch (Exception ignored) {
            player.sendMessage(this.captureTheFlag.getPrefix() + "§cEin Fehler ist aufgetreten!");
        }

        return true;
    }

}
