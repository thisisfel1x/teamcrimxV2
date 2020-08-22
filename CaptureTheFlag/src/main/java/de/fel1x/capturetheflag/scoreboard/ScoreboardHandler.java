package de.fel1x.capturetheflag.scoreboard;

import de.dytanic.cloudnet.ext.cloudperms.bukkit.BukkitCloudNetCloudPermissionsPlugin;
import de.fel1x.capturetheflag.gameplayer.GamePlayer;
import de.fel1x.capturetheflag.team.Teams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ScoreboardHandler {

    private Scoreboard gameScoreboard;
    private Objective objective;
    private Team redTeam;
    private Team blueTeam;

    public ScoreboardHandler() {

        gameScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        objective = gameScoreboard.registerNewObjective("dummy", "test");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("§8» §bteamcrimx§lDE §8«");

        redTeam = gameScoreboard.registerNewTeam("008Rot");
        redTeam.setPrefix("§cRot §7| §c");
        redTeam.setDisplayName("§c");

        blueTeam = gameScoreboard.registerNewTeam("009Rot");
        blueTeam.setPrefix("§9Blau §7| §9");
        blueTeam.setDisplayName("§9");

    }

    public void setGameScoreboard(Player player, Teams team) {

        GamePlayer gamePlayer = new GamePlayer(player);

        //player.setDisplayName("§f" + player.getName());

        if (player.getScoreboard().equals(gameScoreboard)) {

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

        if (!player.getScoreboard().equals(gameScoreboard)) {
            player.setScoreboard(gameScoreboard);
            BukkitCloudNetCloudPermissionsPlugin.getInstance().updateNameTags(player);
        }

        Bukkit.getOnlinePlayers().forEach(current -> {

            GamePlayer gamePlayer = new GamePlayer(current);
            Teams team = gamePlayer.getTeam();

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

    public void handleQuit(Player player) {

        if (blueTeam.hasEntry(player.getName())) {
            blueTeam.removeEntry(player.getName());
        }

        if (redTeam.hasEntry(player.getName())) {
            redTeam.removeEntry(player.getName());
        }

    }

}
