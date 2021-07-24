package de.fel1x.teamcrimx.crimxlobby.scoreboard;

import de.fel1x.teamcrimx.crimxapi.clanSystem.player.ClanPlayer;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.crimxapi.utils.TimeUtils;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Objects;

public class LobbyScoreboard {

    public void setDefaultLobbyScoreboard(Player player) {
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

        Team clan = gameScoreboard.registerNewTeam("clan");
        clan.addEntry("§8");
        clan.prefix(Component.text("§8● §b%s"));

        objective.getScore("§1").setScore(12);
        objective.getScore("§fCoins:").setScore(11);
        objective.getScore("§f").setScore(10);
        objective.getScore("§2").setScore(9);
        objective.getScore("§fSpielzeit:").setScore(8);
        objective.getScore("§9").setScore(7);
        objective.getScore("§3").setScore(6);
        objective.getScore("§fClan:").setScore(5);
        objective.getScore("§8").setScore(4);
        objective.getScore("§4").setScore(3);
        objective.getScore("§fFreunde:").setScore(2);
        objective.getScore("§8● §a0§8/§70").setScore(1);
        objective.getScore("§5").setScore(0);

        if (!player.getScoreboard().equals(gameScoreboard)) {
            player.setScoreboard(gameScoreboard);
        }

        if (player.getScoreboard().equals(gameScoreboard)) {
            Objects.requireNonNull(CrimxLobby.getInstance().getCrimxAPI().getMongoDB()
                    .getObjectFromDocumentAsync(player.getUniqueId(), MongoDBCollection.USERS, "coins"))
                    .thenAccept(coinsInt -> this.updateBoard(player, String.format("§8● §e%s Coins", coinsInt),
                            "coins"));

            Objects.requireNonNull(CrimxLobby.getInstance().getCrimxAPI().getMongoDB()
                    .getObjectFromDocumentAsync(player.getUniqueId(), MongoDBCollection.USERS, "onlinetime"))
                    .thenAccept(onlineTimeInMillis -> {
                        long onlineTime;

                        try {
                            onlineTime = (long) onlineTimeInMillis;
                        } catch (Exception ignored) {
                            onlineTime = Integer.toUnsignedLong((Integer) onlineTimeInMillis);
                        }

                        String formattedTime;

                        if (onlineTime < 1000 * 60) {
                            formattedTime = "Keine Daten";
                        } else if (onlineTime > 1000 * 60 && onlineTime < 1000 * 60 * 60) {
                            long minutes = TimeUtils.splitTime(onlineTime)[2];
                            formattedTime = minutes == 1 ? minutes + " Minute" : minutes + " Minuten";
                        } else {
                            long hours = TimeUtils.splitTime(onlineTime)[1];
                            formattedTime = hours == 1 ? hours + " Stunde" : hours + " Stunden";
                        }
                        this.updateBoard(player, "§8● §6" + formattedTime, "playtime");
                    });

            new ClanPlayer(player.getUniqueId()).getCurrentClanAsync().thenAccept(iClan -> {
                if (iClan != null) {
                    this.updateBoard(player, String.format("§8● §b%s§8/§7%s §7[§b%s§7]",
                            iClan.getTotalClanMembers(), 20, // TODO: proper system here
                            iClan.getClanTag()), "clan");
                    return;
                }
                this.updateBoard(player, "§8● §bKein Clan", "clan");
            });
        }
    }

    public void updateBoard(Player player, String value, String score) {
        if (player.getScoreboard().getTeam("coins") != null) {
            Objects.requireNonNull(player.getScoreboard().getTeam(score)).prefix(Component.text(value));
        } else {
            setDefaultLobbyScoreboard(player);
            updateBoard(player, value, score);
        }
    }
}
