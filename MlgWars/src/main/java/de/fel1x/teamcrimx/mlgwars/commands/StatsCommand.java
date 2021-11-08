package de.fel1x.teamcrimx.mlgwars.commands;

import de.dytanic.cloudnet.ext.bridge.player.ICloudOfflinePlayer;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import de.fel1x.teamcrimx.mlgwars.objects.Stats;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class StatsCommand implements CommandExecutor {

    private final MlgWars mlgWars;

    public StatsCommand(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
        this.mlgWars.getCommand("stats").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] args) {
        if (!(commandSender instanceof Player player)) return false;

        try {
            Stats stats;
            if (args.length == 1) {
               stats = this.getPlayerStats(args[0]);
            } else {
                stats = this.mlgWars.getData().getGamePlayers().get(player.getUniqueId()).getStats();
            }

            if(stats == null) {
                player.sendMessage(this.mlgWars.getPrefix() + "§7Dieser Spieler war noch nie online!");
                return false;
            }

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

            String name = args.length == 1 ? args[0] : player.getName();

            String[] message = {
                    "§7§m--------§r §eStatistiken §7von §e" + name + " §7§m--------",
                    " §7Position im Ranking§8: §e" + ranking,
                    " §7Kills§8: §e" + kills,
                    " §7Deaths§8: §e" + deaths,
                    " §7K/D§8: §e" + kd / 100,
                    " §7Gespielte Spiele§8: §e" + gamesPlayed,
                    " §7Gewonnene Spiele§8: §e" + gamesWon,
                    " §7Siegeswahrscheinlichkeit§8: §e" + winrate + "%",
                    "§7§m--------§r §eStatistiken §7von §e" + name + " §7§m--------"
            };
            player.sendMessage(message);

        } catch (Exception ignored) {
            player.sendMessage(this.mlgWars.getPrefix() + "§cEin Fehler ist aufgetreten!");
        }

        return true;
    }

    private @Nullable Stats getPlayerStats(String query) {
        ICloudOfflinePlayer offlinePlayer = this.mlgWars.getCrimxAPI().getPlayerManager().getFirstOfflinePlayer(query);
        if(offlinePlayer == null) {
            return null;
        }

        int kills = (int) this.mlgWars.getCrimxAPI().getMongoDB().getObjectFromDocumentSync(offlinePlayer.getUniqueId(),
                MongoDBCollection.MLGWARS, "kills");
        int deaths = (int) this.mlgWars.getCrimxAPI().getMongoDB().getObjectFromDocumentSync(offlinePlayer.getUniqueId(),
                MongoDBCollection.MLGWARS, "deaths");
        int gamesPlayed = (int) this.mlgWars.getCrimxAPI().getMongoDB().getObjectFromDocumentSync(offlinePlayer.getUniqueId(),
                MongoDBCollection.MLGWARS, "gamesPlayed");
        int gamesWon = (int) this.mlgWars.getCrimxAPI().getMongoDB().getObjectFromDocumentSync(offlinePlayer.getUniqueId(),
                MongoDBCollection.MLGWARS, "gamesWon");

        int placement = -1;
        try {
            placement = this.mlgWars.getRankingPosition(offlinePlayer.getUniqueId()).get();
        } catch (InterruptedException | ExecutionException ignored) {}

        return new Stats(kills, deaths, gamesPlayed, gamesWon, placement);
    }

}
