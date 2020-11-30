package de.fel1x.teamcrimx.floorislava.utils.scoreboard;

import de.fel1x.teamcrimx.floorislava.FloorIsLava;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Objects;

public class GameScoreboard {

    public void setGameScoreboard(Player player) {

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("dummy", "test", "test");
        objective.setDisplayName("§8» §bteamcrimx§lDE §8«");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Team countdown = scoreboard.registerNewTeam("countdown");
        countdown.addEntry("§e");
        countdown.setPrefix(String.format("§8%s §eWarte...", FloorIsLava.DOT));

        Team payAttention = scoreboard.registerNewTeam("attention");
        payAttention.addEntry("§d");
        payAttention.setPrefix(String.format("§a%s §8%s §7Du bist §asicher", FloorIsLava.SAVE, FloorIsLava.DOT));

        objective.getScore("§2").setScore(5);
        objective.getScore("§fNächster Rise:").setScore(4);
        objective.getScore("§e").setScore(3);
        objective.getScore("§1").setScore(2);
        objective.getScore("§d").setScore(1);
        objective.getScore("§0").setScore(0);

        if (!player.getScoreboard().equals(scoreboard)) {
            player.setScoreboard(scoreboard);
        }

    }

    public void updateBoard(Player player, String value, String score, String lastColorcode) {
        if (player.getScoreboard().getTeam("attention") != null) {
            if (value.length() <= 32) {
                Objects.requireNonNull(player.getScoreboard().getTeam(score)).setPrefix(value);
                return;
            }

            if (value.length() > 64) {
                value = value.substring(64);
            }

            Objects.requireNonNull(player.getScoreboard().getTeam(score)).setPrefix(value.substring(0, 32));
            Objects.requireNonNull(player.getScoreboard().getTeam(score)).setSuffix(lastColorcode + value.substring(32));
        } else {
            this.setGameScoreboard(player);
            this.updateBoard(player, value, score, lastColorcode);
        }

    }

}
