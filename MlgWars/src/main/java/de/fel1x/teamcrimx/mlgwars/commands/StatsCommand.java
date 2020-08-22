package de.fel1x.teamcrimx.mlgwars.commands;

import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class StatsCommand implements CommandExecutor {

    private MlgWars mlgWars;

    public StatsCommand(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
        this.mlgWars.getCommand("stats").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] args) {

        if (!(commandSender instanceof Player)) return false;

        Player player = (Player) commandSender;
        GamePlayer gamePlayer;

        try {
            if (args.length == 1) {
                player.sendMessage(this.mlgWars.getPrefix() + "§7Statistiken von anderen Spielern können momentan " +
                        "§cnoch nicht §7eingesehen werden!");
                return false;
            } else {
                gamePlayer = new GamePlayer(player, true);
                Bukkit.getScheduler().runTaskAsynchronously(this.mlgWars, () -> {
                    String ranking = String.format(Locale.GERMANY, "%d", gamePlayer.getRankingPosition());
                    ;

                    int kills = (int) gamePlayer.getObjectFromMongoDocument("kills", MongoDBCollection.MLGWARS);
                    int deaths = (int) gamePlayer.getObjectFromMongoDocument("deaths", MongoDBCollection.MLGWARS);
                    int gamesPlayed = (int) gamePlayer.getObjectFromMongoDocument("gamesPlayed", MongoDBCollection.MLGWARS);
                    int gamesWon = (int) gamePlayer.getObjectFromMongoDocument("gamesWon", MongoDBCollection.MLGWARS);

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
                });
            }

        } catch (Exception ignored) {
            player.sendMessage(this.mlgWars.getPrefix() + "§cEin Fehler ist aufgetreten!");
        }

        return true;
    }
}
