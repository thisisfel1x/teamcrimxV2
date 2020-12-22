package de.fel1x.bingo.utils.scoreboard;

import de.dytanic.cloudnet.ext.cloudperms.bukkit.BukkitCloudNetCloudPermissionsPlugin;
import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.objects.BingoPlayer;
import de.fel1x.bingo.objects.BingoTeam;
import de.fel1x.bingo.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GameScoreboard {

    private final Scoreboard gameScoreboard;

    private final Map<BingoTeam, Team> scoreboardTeams;
    private final Team spectatorTeam;

    private int i;

    public GameScoreboard() {
        this.scoreboardTeams = new HashMap<>();

        this.gameScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective objective = this.gameScoreboard.registerNewObjective("dummy", "test", "§8» §bteamcrimx§lDE §8«");
        objective.setDisplayName("§8» §bteamcrimx§lDE §8«");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        objective.getScore("§a").setScore(0);

        int counter = 1;

        for (BingoTeam bingoTeam : BingoTeam.values()) {
            if(bingoTeam.isEmpty()) continue;

            String color = Utils.getChatColor(bingoTeam.getColor()).toString();

            Team team = this.gameScoreboard.registerNewTeam("00" + counter + bingoTeam.getName());

            team.setPrefix(String.format("%s%s" + " §8| %s", color, bingoTeam.getName(), color));
            team.setDisplayName(color);
            team.setColor(Objects.requireNonNull(Utils.getChatColor(bingoTeam.getColor())));

            this.scoreboardTeams.put(bingoTeam, team);

            objective.getScore(String.format("§7Team %s%s §8» §a%s§8/§c9", Utils.getChatColor(bingoTeam.getColor()),
                    bingoTeam.getName(), bingoTeam.getDoneItemsSize()))
                    .setScore(counter);

            Team top = this.gameScoreboard.registerNewTeam("top" + counter);
            top.addEntry("§" + counter);

            counter++;
        }

        objective.getScore("§f").setScore(counter + 1);

        this.spectatorTeam = this.gameScoreboard.registerNewTeam("00999Spectator");
        this.spectatorTeam.setPrefix("§7§oSpectator §r§8| §7");
        this.spectatorTeam.setDisplayName("§7");
        this.spectatorTeam.setColor(ChatColor.GRAY);

    }

    public void setGameScoreboard(Player player) {
        BingoPlayer bingoPlayer = new BingoPlayer(player);
        BingoTeam bingoTeam = bingoPlayer.getTeam();

        if (!player.getScoreboard().equals(this.gameScoreboard)) {
            player.setScoreboard(this.gameScoreboard);
        }

        if (player.getScoreboard().equals(this.gameScoreboard) && bingoTeam != null) {
            Team team = this.scoreboardTeams.get(bingoTeam);
            team.addEntry(player.getName());
            player.setDisplayName(team.getDisplayName() + player.getName());

        }

    }

    public void handleJoin(Player player) {
        if (!player.getScoreboard().equals(this.gameScoreboard)) {
            player.setScoreboard(this.gameScoreboard);
            BukkitCloudNetCloudPermissionsPlugin.getInstance().updateNameTags(player);
        }

        for (Player gamePlayer : Bingo.getInstance().getData().getPlayers()) {
            BingoPlayer bingoPlayer = new BingoPlayer(gamePlayer);
            BingoTeam bingoTeam = bingoPlayer.getTeam();

            if(bingoTeam != null) {
                Team bingoScoreboardTeam = this.scoreboardTeams.get(bingoTeam);
                if (!bingoScoreboardTeam.hasEntry(gamePlayer.getName())) {
                    bingoScoreboardTeam.addEntry(gamePlayer.getName());
                }
                gamePlayer.setDisplayName(bingoScoreboardTeam.getDisplayName() + gamePlayer.getName());
            }
        }

        this.spectatorTeam.addEntry(player.getName());
        player.setDisplayName(this.spectatorTeam.getDisplayName() + player.getName());

    }

    public void handleQuit(Player player) {
        this.gameScoreboard.getTeams().forEach(scoreboardTeam -> {
            if (scoreboardTeam.hasEntry(player.getName())) {
                scoreboardTeam.removeEntry(player.getName());
            }
        });
        if(this.spectatorTeam.hasEntry(player.getName())) {
            this.spectatorTeam.removeEntry(player.getName());
        }
    }

    public void updateIngameScoreboard(ArrayList<BingoTeam> sortedTeams, Player player) {
        this.i = 1;
        sortedTeams.forEach(bingoTeam -> {
            this.updateBoard(player,
                    String.format("§7Team %s%s §8» §a%s§8/§c9", Utils.getChatColor(bingoTeam.getColor()),
                            bingoTeam.getName(), bingoTeam.getDoneItemsSize()),
                    "top" + (this.i));
            this.i++;
        });
    }

    public void updateBoard(Player player, String value, String score) {
        if (player.getScoreboard().getTeam("top1") != null) {
            Objects.requireNonNull(player.getScoreboard().getTeam(score)).setPrefix(value);
        } else {
            setGameScoreboard(player);
            updateBoard(player, value, score);
        }
    }
}