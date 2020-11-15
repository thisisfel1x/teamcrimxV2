package de.fel1x.teamcrimx.crimxlobby.scoreboard;

import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.crimxlobby.objects.LobbyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Objects;

public class LobbyScoreboard {

    public LobbyScoreboard() {

    }

    public void setGameScoreboard(Player player) {

        Scoreboard gameScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective objective = gameScoreboard.registerNewObjective("dummy", "test", "test");
        objective.setDisplayName("§8» §bteamcrimx§lDE §8«");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Team coins = gameScoreboard.registerNewTeam("coins");
        coins.addEntry("§f");
        coins.setPrefix("§8● §e%s Coins");

        Team playtime = gameScoreboard.registerNewTeam("playtime");
        playtime.addEntry("§9");
        playtime.setPrefix("§8● §6%s");

        objective.getScore("§b").setScore(6);
        objective.getScore("§fDeine Coins:").setScore(5);
        objective.getScore("§f").setScore(4);
        objective.getScore("§1").setScore(3);
        objective.getScore("§fSpielzeit:").setScore(2);
        objective.getScore("§9").setScore(1);
        objective.getScore("§a").setScore(0);

        if (!player.getScoreboard().equals(gameScoreboard)) {
            player.setScoreboard(gameScoreboard);
        }

        LobbyPlayer lobbyPlayer = new LobbyPlayer(player);

        if (player.getScoreboard().equals(gameScoreboard)) {

            int coinsInt = (int) lobbyPlayer.getObjectFromMongoDocument("coins", MongoDBCollection.USERS);
            this.updateBoard(player, String.format("§8● §e%s Coins", coinsInt), "coins", "§e");
            this.updateBoard(player, "§8● §6" + lobbyPlayer.getOnlineTimeForScoreboard(), "playtime", "§6");

        }

    }

    public void updateBoard(Player player, String value, String score, String lastColorcode) {

        if (player.getScoreboard().getTeam("coins") != null) {
            if (value.length() <= 16) {
                Objects.requireNonNull(player.getScoreboard().getTeam(score)).setPrefix(value);
                return;
            }

            if (value.length() > 32) {
                value = value.substring(32);
            }

            Objects.requireNonNull(player.getScoreboard().getTeam(score)).setPrefix(value.substring(0, 16));
            Objects.requireNonNull(player.getScoreboard().getTeam(score)).setSuffix(lastColorcode + value.substring(16));
        } else {
            setGameScoreboard(player);
            updateBoard(player, value, score, lastColorcode);
        }

    }

}
