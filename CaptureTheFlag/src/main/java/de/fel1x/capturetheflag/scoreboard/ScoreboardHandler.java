package de.fel1x.capturetheflag.scoreboard;

import de.dytanic.cloudnet.ext.cloudperms.bukkit.BukkitCloudNetCloudPermissionsPlugin;
import de.fel1x.capturetheflag.gameplayer.GamePlayer;
import de.fel1x.capturetheflag.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Objects;

public class ScoreboardHandler {

    private Scoreboard lobbyScoreboard;
    private Objective objective;
    private org.bukkit.scoreboard.Team redTeam;
    private org.bukkit.scoreboard.Team blueTeam;

    public ScoreboardHandler() {

        lobbyScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        objective = lobbyScoreboard.registerNewObjective("dummy", "test", "test");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("§8» §bteamcrimx§lDE §8«");

        redTeam = lobbyScoreboard.registerNewTeam("008Rot");
        redTeam.setPrefix("§cRot §7| §c");
        redTeam.setDisplayName("§c");
        redTeam.setColor(ChatColor.RED);

        blueTeam = lobbyScoreboard.registerNewTeam("009Rot");
        blueTeam.setPrefix("§9Blau §7| §9");
        blueTeam.setDisplayName("§9");
        blueTeam.setColor(ChatColor.BLUE);

        org.bukkit.scoreboard.Team kit = lobbyScoreboard.registerNewTeam("kit");
        kit.addEntry("§9");
        kit.setPrefix("§8● §6%s");

        objective.getScore("§b").setScore(6);
        objective.getScore("§fMap:").setScore(5);
        objective.getScore("§8● §eCastle").setScore(4);
        objective.getScore("§1").setScore(3);
        objective.getScore("§fKit:").setScore(2);
        objective.getScore("§9").setScore(1);
        objective.getScore("§a").setScore(0);

    }

    public void setGameScoreboard(Player player, Team team) {
        if (player.getScoreboard().equals(lobbyScoreboard)) {
            switch (team) {
                case RED:

                    if (blueTeam.hasEntry(player.getName())) {
                        blueTeam.removeEntry(player.getName());
                    }

                    redTeam.addEntry(player.getName());

                    player.setDisplayName(redTeam.getDisplayName() + player.getName());
                    break;
                case BLUE:

                    if (redTeam.hasEntry(player.getName())) {
                        redTeam.removeEntry(player.getName());
                    }

                    blueTeam.addEntry(player.getName());

                    player.setDisplayName(blueTeam.getDisplayName() + player.getName());

                    break;

            }
        }
    }

    public void handleJoin(Player player) {
        if (!player.getScoreboard().equals(lobbyScoreboard)) {
            player.setScoreboard(lobbyScoreboard);
            BukkitCloudNetCloudPermissionsPlugin.getInstance().updateNameTags(player);
        }

        Bukkit.getOnlinePlayers().forEach(current -> {
            GamePlayer gamePlayer = new GamePlayer(current);
            Team team = gamePlayer.getTeam();

            if (team == null) return;

            switch (team) {

                case RED:

                    if (blueTeam.hasEntry(current.getName())) {
                        blueTeam.removeEntry(current.getName());
                    }

                    redTeam.addEntry(current.getName());

                    current.setDisplayName(redTeam.getDisplayName() + current.getName());

                    break;

                case BLUE:

                    if (redTeam.hasEntry(current.getName())) {
                        redTeam.removeEntry(current.getName());
                    }

                    blueTeam.addEntry(current.getName());

                    current.setDisplayName(blueTeam.getDisplayName() + current.getName());

                    break;

            }
        });
    }

    public void updateBoard(Player player, String value, String score, String lastColorcode) {
        if (player.getScoreboard().getTeam("kit") != null) {
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
            //updateBoard(player, value, score, lastColorcode);
        }

    }

}
