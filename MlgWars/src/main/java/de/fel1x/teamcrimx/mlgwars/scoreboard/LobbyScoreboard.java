package de.fel1x.teamcrimx.mlgwars.scoreboard;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.kit.Kit;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Objects;

public class LobbyScoreboard {
    
    public void setLobbyScoreboard(Player player) {
        Scoreboard lobbyScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = lobbyScoreboard.registerNewObjective("dummy", "test");

        GamePlayer gamePlayer = new GamePlayer(player);
        Kit selectedKit = gamePlayer.getSelectedKit();
        
        objective.setDisplayName("§8» §bteamcrimx§lDE §8«");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Team map = lobbyScoreboard.registerNewTeam("map");
        map.addEntry("§f");
        map.setPrefix("§8● §e%s");

        Team kit = lobbyScoreboard.registerNewTeam("kit");
        kit.addEntry("§9");
        kit.setPrefix("§8● §6%s");

        objective.getScore("§b").setScore(6);
        objective.getScore("§fMap:").setScore(5);
        objective.getScore("§f").setScore(4);
        objective.getScore("§1").setScore(3);
        objective.getScore("§fKit:").setScore(2);
        objective.getScore("§9").setScore(1);
        objective.getScore("§a").setScore(0);

        if (!player.getScoreboard().equals(lobbyScoreboard)) {
            player.setScoreboard(lobbyScoreboard);
        }

        this.updateBoard(player, "§8● §e" + MlgWars.getInstance().getWorldLoader().getMapName(), "map", "§e");
        try {
            this.updateBoard(player, "§8● §6" + gamePlayer.getSelectedKit().getClazz().newInstance().getKitName(), "kit", "§6");
        } catch (InstantiationException | IllegalAccessException ignored) {
        }

    }

    public void setEndingScoreboard(Player player, String winner, String farbcode) {
        Scoreboard endingScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = endingScoreboard.registerNewObjective("dummy", "test");

        objective.setDisplayName("§8» §bteamcrimx§lDE §8«");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        int gameKills;
        if(player.hasMetadata("kills")) {
            gameKills = player.getMetadata("kills").get(0).asInt();
        } else {
            gameKills = 0;
        }

        Team winnerTeam = endingScoreboard.registerNewTeam("map");
        winnerTeam.addEntry("§9");
        winnerTeam.setPrefix("§8● §6%s");

        objective.getScore("§b").setScore(6);
        objective.getScore("§fGewinner:").setScore(5);
        objective.getScore("§9").setScore(4);
        objective.getScore("§1").setScore(3);
        objective.getScore("§fKills:").setScore(2);
        objective.getScore("§8● §c" + gameKills).setScore(1);
        objective.getScore("§a").setScore(0);

        if (!player.getScoreboard().equals(endingScoreboard)) {
            player.setScoreboard(endingScoreboard);
        }

        this.updateBoard(player, "§8● " + winner + farbcode, "map", farbcode);
    }

    public void setIngameScoreboard(Player player) {
        Scoreboard ingameScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = ingameScoreboard.registerNewObjective("dummy", "test");

        GamePlayer gamePlayer = new GamePlayer(player);
        Kit selectedKit = gamePlayer.getSelectedKit();

        objective.setDisplayName("§8» §bteamcrimx§lDE §8«");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Team map = ingameScoreboard.registerNewTeam("map");
        map.addEntry("§a");
        map.setPrefix("§8● §e%s");

        Team kills = ingameScoreboard.registerNewTeam("kills");
        kills.addEntry("§b");
        kills.setPrefix("§8● §b%s");

        Team players = ingameScoreboard.registerNewTeam("players");
        players.addEntry("§c");
        players.setPrefix("§8● §c%s");

        Team kit = ingameScoreboard.registerNewTeam("kit");
        kit.addEntry("§d");
        kit.setPrefix("§8● §6%s");

        objective.getScore("§1").setScore(12);
        objective.getScore("§fMap:").setScore(11);
        objective.getScore("§a").setScore(10);
        objective.getScore("§2").setScore(9);
        objective.getScore("§fKills:").setScore(8);
        objective.getScore("§b").setScore(7);
        objective.getScore("§3").setScore(6);
        objective.getScore("§fSpieler:").setScore(5);
        objective.getScore("§c").setScore(4);
        objective.getScore("§4").setScore(3);
        objective.getScore("§fKit:").setScore(2);
        objective.getScore("§d").setScore(1);
        objective.getScore("§5").setScore(0);

        if (!player.getScoreboard().equals(ingameScoreboard)) {
            player.setScoreboard(ingameScoreboard);
        }

        this.updateBoard(player, "§8● §e" + MlgWars.getInstance().getWorldLoader().getMapName(), "map", "§e");
        this.updateBoard(player, "§8● §c" + MlgWars.getInstance().getData().getPlayers().size(), "players", "§c");
        this.updateBoard(player, "§8● §b" + player.getMetadata("kills").get(0).asInt(), "kills", "§b");
        try {
            this.updateBoard(player, "§8● §6" + gamePlayer.getSelectedKit().getClazz().newInstance().getKitName(), "kit", "§6");
        } catch (InstantiationException | IllegalAccessException ignored) {
        }

    }

    public void updateBoard(Player player, String value, String score, String lastColorcode) {

        if (player.getScoreboard().getTeam("map") != null) {
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
            this.setLobbyScoreboard(player);
            updateBoard(player, value, score, lastColorcode);
        }

    }
    
}
