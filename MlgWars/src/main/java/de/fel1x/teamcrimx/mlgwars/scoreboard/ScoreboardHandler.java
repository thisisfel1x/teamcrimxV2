package de.fel1x.teamcrimx.mlgwars.scoreboard;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.kit.rework.InventoryKitManager;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.Objects;

public class ScoreboardHandler {

    private final MlgWars mlgWars;
    private final LegacyComponentSerializer legacyComponentSerializer;

    public ScoreboardHandler(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
        this.legacyComponentSerializer = LegacyComponentSerializer.builder().hexColors().build();
    }

    public void setLobbyScoreboard(Player player) {
        Scoreboard scoreboard = this.mlgWars.getServer().getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("dummy", "test",
                this.mlgWars.miniMessage().parse("<dark_gray>» <aqua>teamcrimx<bold>DE</bold> <dark_gray>«"));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Team mapTeam = scoreboard.registerNewTeam("map");
        mapTeam.addEntry("§a");
        mapTeam.prefix(this.mlgWars.miniMessage().parse("<dark_gray>● <yellow>N/A"));

        Team kitTeam = scoreboard.registerNewTeam("kit");
        kitTeam.addEntry("§b");
        kitTeam.prefix(this.mlgWars.miniMessage().parse("<dark_gray>● <gold>N/A"));

        Team modeTeam = scoreboard.registerNewTeam("mode");
        modeTeam.addEntry("§c");
        modeTeam.prefix(this.mlgWars.miniMessage().parse("<dark_gray>● <#5035f4>N/A"));

        objective.getScore("§1").setScore(9);
        objective.getScore("§fMap:").setScore(8);
        objective.getScore("§a").setScore(7);
        objective.getScore("§2").setScore(6);
        objective.getScore("§fKit:").setScore(5);
        objective.getScore("§b").setScore(4);
        objective.getScore("§3").setScore(3);
        objective.getScore("§fModus:").setScore(2);
        objective.getScore("§c").setScore(1);
        objective.getScore("§4").setScore(0);

        if (!player.getScoreboard().equals(scoreboard)) {
            player.setScoreboard(scoreboard);
        }

        GamePlayer gamePlayer = this.mlgWars.getData().getGamePlayers().get(player.getUniqueId());
        InventoryKitManager.InventoryKit selectedKit = gamePlayer.getSelectedInventoryKit();

        this.updateBoard(player, "§e" + this.mlgWars.getSelectedMap().getMapName(), "map");
        this.updateBoard(player, selectedKit == null ? Component.text("nichts ausgewählt",
                TextColor.fromHexString("#fb1155"))
                : selectedKit.getScoreboardName(), "kit");
        this.updateBoardMiniMessage(player, "<#5035f4>" + this.mlgWars.getGameType().getGameTypeName(), "mode");
    }

    public void setIngameScoreboard(Player player) {
        Scoreboard scoreboard = this.mlgWars.getServer().getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("dummy", "test",
                this.mlgWars.miniMessage().parse("<dark_gray>» <aqua>teamcrimx<bold>DE</bold> <dark_gray>«"));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Team mapTeam = scoreboard.registerNewTeam("map");
        mapTeam.addEntry("§a");
        mapTeam.prefix(this.mlgWars.miniMessage().parse("<dark_gray>● <yellow>N/A"));

        Team killsTeam = scoreboard.registerNewTeam("kills");
        killsTeam.addEntry("§b");
        killsTeam.prefix(this.mlgWars.miniMessage().parse("<dark_gray>● <aqua>N/A"));

        Team playersTeam = scoreboard.registerNewTeam("players");
        playersTeam.addEntry("§c");
        playersTeam.prefix(this.mlgWars.miniMessage().parse("<dark_gray>● <red>N/A"));

        Team kitTeam = scoreboard.registerNewTeam("kit");
        kitTeam.addEntry("§d");
        kitTeam.prefix(this.mlgWars.miniMessage().parse("<dark_gray>● <gold>N/A"));

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

        if (!player.getScoreboard().equals(scoreboard)) {
            player.setScoreboard(scoreboard);
        }

        GamePlayer gamePlayer = this.mlgWars.getData().getGamePlayers().get(player.getUniqueId());

        this.updateBoard(player, "§e" + this.mlgWars.getSelectedMap().getMapName(), "map");
        this.updateBoard(player, "§c" + this.mlgWars.getData().getPlayers().size(), "players");
        this.updateBoard(player, "§b" + player.getMetadata("kills").get(0).asInt(), "kills");
        this.updateBoard(player, gamePlayer.getSelectedInventoryKit().getScoreboardName(), "kit");
    }

    public void updateBoard(Player player, String value, String score) {
        if (player.getScoreboard().getTeam("map") != null) {
            player.getScoreboard().getTeam(score).prefix(this.mlgWars.miniMessage().parse("<dark_gray>● ")
                    .append(this.legacyComponentSerializer.deserialize(value)));
        } else {
            this.setLobbyScoreboard(player);
            updateBoard(player, value, score);
        }
    }

    public void updateBoardMiniMessage(Player player, String value, String score) {
        if (player.getScoreboard().getTeam("map") != null) {
            player.getScoreboard().getTeam(score).prefix(this.mlgWars.miniMessage().parse("<dark_gray>● ")
                    .append(this.mlgWars.miniMessage().parse(value)));
        } else {
            this.setLobbyScoreboard(player);
            updateBoard(player, value, score);
        }
    }

    public void updateBoard(Player player, Component value, String score) {
        if (player.getScoreboard().getTeam("map") != null) {
            Objects.requireNonNull(player.getScoreboard().getTeam(score)).prefix(value);
        } else {
            this.setLobbyScoreboard(player);
            this.updateBoard(player, value, score);
        }
    }
}
