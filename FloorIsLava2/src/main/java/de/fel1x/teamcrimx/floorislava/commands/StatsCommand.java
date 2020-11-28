package de.fel1x.teamcrimx.floorislava.commands;

import de.fel1x.teamcrimx.floorislava.FloorIsLava;
import de.fel1x.teamcrimx.floorislava.database.Stats;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class StatsCommand implements CommandExecutor {
    private final FloorIsLava floorIsLava;

    public StatsCommand(FloorIsLava floorIsLava) {
        this.floorIsLava = floorIsLava;
        this.floorIsLava.getCommand("stats").setExecutor(this);
    }

    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] args) {
        if (!(commandSender instanceof Player))
            return false;
        Player player = (Player) commandSender;
        try {
            double kd;
            if (args.length == 1) {
                player.sendMessage(this.floorIsLava.getPrefix() + "§7Statistiken von anderen Spielern können momentan §cnoch nicht §7eingesehen werden!");
                return false;
            }
            if (this.floorIsLava.getData().getCachedStats().get(player) == null) {
                player.sendMessage(this.floorIsLava.getPrefix() + "§cDeine Stats werden noch geladen. Bitte warte einen Moment.");
                return false;
            }
            Stats stats = this.floorIsLava.getData().getCachedStats().get(player);
            String ranking = String.format(Locale.GERMANY, "%d", stats.getPlacement());
            int kills = stats.getKills();
            int deaths = stats.getDeaths();
            int gamesPlayed = stats.getGamesPlayed();
            int gamesWon = stats.getGamesWon();
            if (deaths > 0) {
                double a = kills / deaths * 100.0D;
                kd = Math.round(a);
            } else {
                kd = kills;
            }
            double winrate = 0.0D;
            if (gamesPlayed > 0) {
                double a = gamesWon / gamesPlayed * 100.0D;
                winrate = Math.round(a);
            }
            String[] message = {"§7<=========[ §a§lSTATS §7]=========>", " §7Position im Ranking§8: §e" + ranking, " §7Kills§8: §e" + kills, " §7Deaths§8: §e" + deaths, " §7K/D§8: §e" + (kd / 100.0D), " §7Gespielte Spiele§8: §e" + gamesPlayed, " §7Gewonnene Spiele§8: §e" + gamesWon, " §7Siegeswahrscheinlichkeit§8: §e" + winrate + "%", "§7<=========[ §a§lSTATS §7]=========>"};
            player.sendMessage(message);
        } catch (Exception ignored) {
            player.sendMessage(this.floorIsLava.getPrefix() + "§cEin Fehler ist aufgetreten!");
        }
        return true;
    }
}
