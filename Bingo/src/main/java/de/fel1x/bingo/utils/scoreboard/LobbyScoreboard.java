package de.fel1x.bingo.utils.scoreboard;

import de.dytanic.cloudnet.ext.cloudperms.bukkit.BukkitCloudNetCloudPermissionsPlugin;
import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.objects.BingoPlayer;
import de.fel1x.bingo.objects.BingoTeam;
import de.fel1x.bingo.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LobbyScoreboard {

    private final Scoreboard lobbyScoreboard;

    private final Map<BingoTeam, Team> scoreboardTeams;

    public LobbyScoreboard() {

        this.scoreboardTeams = new HashMap<>();

        this.lobbyScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective objective = this.lobbyScoreboard.registerNewObjective("dummy", "test", "§8» §bteamcrimx§lDE §8«");
        objective.setDisplayName("§8» §bteamcrimx§lDE §8«");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Team biome = this.lobbyScoreboard.registerNewTeam("biome");
        biome.addEntry("§9");
        biome.setPrefix("§8● §6%s");

        Team players = this.lobbyScoreboard.registerNewTeam("players");
        players.addEntry("§8");
        players.setPrefix("§8● §a%s");

        objective.getScore("§b").setScore(6);
        objective.getScore("§fBiome:").setScore(5);
        objective.getScore("§9").setScore(4);
        objective.getScore("§1").setScore(3);
        objective.getScore("§fSpieler:").setScore(2);
        objective.getScore("§8").setScore(1);
        objective.getScore("§a").setScore(0);

        int counter = 0;

        for (BingoTeam bingoTeam : BingoTeam.values()) {
            String color = Utils.getChatColor(bingoTeam.getColor()).toString();

            Team team = this.lobbyScoreboard.registerNewTeam("00" + counter + bingoTeam.getName());

            team.setPrefix(String.format("%s%s" + " §8| %s", color, bingoTeam.getName(), color));
            team.setDisplayName(color);
            team.setColor(Objects.requireNonNull(Utils.getChatColor(bingoTeam.getColor())));

            this.scoreboardTeams.put(bingoTeam, team);
            counter++;
        }
    }

    public void setLobbyScoreboard(Player player) {
        BingoPlayer bingoPlayer = new BingoPlayer(player);
        BingoTeam bingoTeam = bingoPlayer.getTeam();

        if (!player.getScoreboard().equals(this.lobbyScoreboard)) {
            player.setScoreboard(this.lobbyScoreboard);
        }

        if (player.getScoreboard().equals(this.lobbyScoreboard) && bingoTeam != null) {
            Team team = this.scoreboardTeams.get(bingoTeam);

            this.lobbyScoreboard.getTeams().forEach(scoreboardTeam -> {
                if (scoreboardTeam.hasEntry(player.getName())) {
                    scoreboardTeam.removeEntry(player.getName());
                }

            });

            team.addEntry(player.getName());
            player.setDisplayName(team.getDisplayName() + player.getName());

        }

    }

    public void handleJoin(Player player) {

        if (!player.getScoreboard().equals(this.lobbyScoreboard)) {
            player.setScoreboard(this.lobbyScoreboard);
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
    }

    public void handleQuit(Player player) {

        this.lobbyScoreboard.getTeams().forEach(scoreboardTeam -> {

            if (scoreboardTeam.hasEntry(player.getName())) {
                scoreboardTeam.removeEntry(player.getName());
            }

        });

    }

    public void updateBoard(Player player, String value, String score, String lastColorcode) {
        if (player.getScoreboard().getTeam("biome") != null) {
            if (value.length() <= 32) {
                Objects.requireNonNull(player.getScoreboard().getTeam(score)).setPrefix(value);
                return;
            }

            value = value.substring(64);

            Objects.requireNonNull(player.getScoreboard().getTeam(score)).setPrefix(value.substring(0, 32));
            Objects.requireNonNull(player.getScoreboard().getTeam(score)).setSuffix(lastColorcode + value.substring(32));
        }
        this.setLobbyScoreboard(player);
    }

}
