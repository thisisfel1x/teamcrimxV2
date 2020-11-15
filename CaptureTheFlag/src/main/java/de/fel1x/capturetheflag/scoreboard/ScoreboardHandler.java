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

    private final Scoreboard lobbyScoreboard;
    private final org.bukkit.scoreboard.Team redTeam;
    private final org.bukkit.scoreboard.Team blueTeam;

    public ScoreboardHandler() {

        this.lobbyScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective objective = this.lobbyScoreboard.registerNewObjective("dummy", "test", "test");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("§8» §bteamcrimx§lDE §8«");

        this.redTeam = this.lobbyScoreboard.registerNewTeam("008Rot");
        this.redTeam.setPrefix("§cRot §7| §c");
        this.redTeam.setDisplayName("§c");
        this.redTeam.setColor(ChatColor.RED);

        this.blueTeam = this.lobbyScoreboard.registerNewTeam("009Rot");
        this.blueTeam.setPrefix("§9Blau §7| §9");
        this.blueTeam.setDisplayName("§9");
        this.blueTeam.setColor(ChatColor.BLUE);

        org.bukkit.scoreboard.Team kit = this.lobbyScoreboard.registerNewTeam("kit");
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
        if (player.getScoreboard().equals(this.lobbyScoreboard)) {
            switch (team) {
                case RED:

                    if (this.blueTeam.hasEntry(player.getName())) {
                        this.blueTeam.removeEntry(player.getName());
                    }

                    this.redTeam.addEntry(player.getName());

                    player.setDisplayName(this.redTeam.getDisplayName() + player.getName());
                    break;
                case BLUE:

                    if (this.redTeam.hasEntry(player.getName())) {
                        this.redTeam.removeEntry(player.getName());
                    }

                    this.blueTeam.addEntry(player.getName());

                    player.setDisplayName(this.blueTeam.getDisplayName() + player.getName());

                    break;

            }
        }
    }

    public void handleJoin(Player player) {
        if (!player.getScoreboard().equals(this.lobbyScoreboard)) {
            player.setScoreboard(this.lobbyScoreboard);
            BukkitCloudNetCloudPermissionsPlugin.getInstance().updateNameTags(player);
        }

        Bukkit.getOnlinePlayers().forEach(current -> {
            GamePlayer gamePlayer = new GamePlayer(current);
            Team team = gamePlayer.getTeam();

            if (team == null) return;

            switch (team) {

                case RED:

                    if (this.blueTeam.hasEntry(current.getName())) {
                        this.blueTeam.removeEntry(current.getName());
                    }

                    this.redTeam.addEntry(current.getName());

                    current.setDisplayName(this.redTeam.getDisplayName() + current.getName());

                    break;

                case BLUE:

                    if (this.redTeam.hasEntry(current.getName())) {
                        this.redTeam.removeEntry(current.getName());
                    }

                    this.blueTeam.addEntry(current.getName());

                    current.setDisplayName(this.blueTeam.getDisplayName() + current.getName());

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
