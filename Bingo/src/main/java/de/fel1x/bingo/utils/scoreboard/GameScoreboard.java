package de.fel1x.bingo.utils.scoreboard;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.IPermissionGroup;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import de.dytanic.cloudnet.ext.cloudperms.bukkit.BukkitCloudNetCloudPermissionsPlugin;
import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.objects.BingoPlayer;
import de.fel1x.bingo.objects.BingoTeam;
import de.fel1x.bingo.utils.Utils;
import de.fel1x.teamcrimx.crimxapi.clanSystem.player.ClanPlayer;
import de.fel1x.teamcrimx.crimxapi.clanSystem.player.IClanPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;
import java.util.stream.Collectors;

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
            if (bingoTeam.isEmpty()) continue;

            String color = Utils.getChatColor(bingoTeam.getColor()).toString();

            Team team = this.gameScoreboard.registerNewTeam("00" + counter + bingoTeam.getName());

            team.setPrefix(String.format("%s%s" + " §8| %s", color, bingoTeam.getName(), color));
            team.setDisplayName(color);
            team.setColor(Objects.requireNonNull(Utils.getChatColor(bingoTeam.getColor())));

            this.scoreboardTeams.put(bingoTeam, team);

            Team top = this.gameScoreboard.registerNewTeam("top" + counter);
            top.addEntry("§" + counter);
            top.setPrefix(String.format("§7Team %s%s §8● §a%s§8/§c9", Utils.getChatColor(bingoTeam.getColor()),
                    bingoTeam.getName(), bingoTeam.getDoneItemsSize()));

            objective.getScore("§" + counter).setScore(counter);

            counter++;
        }

        objective.getScore("§f").setScore(counter + 1);

        Team yourItems = this.gameScoreboard.registerNewTeam("items");
        yourItems.addEntry("§e");
        yourItems.setPrefix("§7Items gefunden §8● §a0§8/§c9");

        objective.getScore("§e").setScore(counter + 2);
        objective.getScore("§k").setScore(counter + 3);

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

            IClanPlayer clanPlayer;

            if(player.getMetadata("iClanPlayer").get(0) != null) {
                clanPlayer = (IClanPlayer) player.getMetadata("iClanPlayer").get(0).value();
            } else {
                clanPlayer = new ClanPlayer(player.getUniqueId());
            }

            String clan = "";

            if(clanPlayer.hasClan()) {
                clan = " §7[§e" + clanPlayer.getCurrentClan().getClanTag() + "§7]";
            }

            team.setSuffix(clan);
            team.addEntry(player.getName());
            player.setDisplayName(team.getDisplayName() + player.getName());

        }

    }

    public void handleJoin(Player player) {
        if (!player.getScoreboard().equals(this.gameScoreboard)) {
            player.setScoreboard(this.gameScoreboard);
            BukkitCloudNetCloudPermissionsPlugin.getInstance().updateNameTags(player, player1 -> {
                IPermissionUser permissionUser = CloudNetDriver.getInstance().getPermissionManagement().getUser(player1.getUniqueId());
                IPermissionGroup permissionGroup = CloudNetDriver.getInstance().getPermissionManagement().getHighestPermissionGroup(permissionUser);

                IClanPlayer clanPlayer;

                if(player1.getMetadata("iClanPlayer").get(0) != null) {
                    clanPlayer = (IClanPlayer) player1.getMetadata("iClanPlayer").get(0).value();
                } else {
                    clanPlayer = new ClanPlayer(player1.getUniqueId());
                }

                if(clanPlayer.hasClan()) {
                    permissionGroup.setSuffix(" §7[§e" + clanPlayer.getCurrentClan().getClanTag() + "§7]");
                }

                return permissionGroup;
            });
        }

        for (Player gamePlayer : Bingo.getInstance().getData().getPlayers()) {
            BingoPlayer bingoPlayer = new BingoPlayer(gamePlayer);
            BingoTeam bingoTeam = bingoPlayer.getTeam();

            if (bingoTeam != null) {
                Team bingoScoreboardTeam = this.scoreboardTeams.get(bingoTeam);
                if (!bingoScoreboardTeam.hasEntry(gamePlayer.getName())) {
                    bingoScoreboardTeam.addEntry(gamePlayer.getName());
                }
                IClanPlayer clanPlayer;

                if(gamePlayer.getMetadata("iClanPlayer").get(0) != null) {
                    clanPlayer = (IClanPlayer) gamePlayer.getMetadata("iClanPlayer").get(0).value();
                } else {
                    clanPlayer = new ClanPlayer(gamePlayer.getUniqueId());
                }

                String clan = "";

                if(clanPlayer.hasClan()) {
                    clan = " §7[§e" + clanPlayer.getCurrentClan().getClanTag() + "§7]";
                }
                gamePlayer.setDisplayName(bingoScoreboardTeam.getDisplayName() + gamePlayer.getName()
                        + clan);
            }
        }

        this.spectatorTeam.addEntry(player.getName());

        IClanPlayer clanPlayer;

        if(player.getMetadata("iClanPlayer").get(0) != null) {
            clanPlayer = (IClanPlayer) player.getMetadata("iClanPlayer").get(0);
        } else {
            clanPlayer = new ClanPlayer(player.getUniqueId());
        }

        String clan = "";

        if(clanPlayer.hasClan()) {
            clan = " §7[§e" + clanPlayer.getCurrentClan().getClanTag() + "§7]";
        }

        player.setDisplayName(this.spectatorTeam.getDisplayName() + player.getName() + clan);

    }

    public void handleQuit(Player player) {
        this.gameScoreboard.getTeams().forEach(scoreboardTeam -> {
            if (scoreboardTeam.hasEntry(player.getName())) {
                scoreboardTeam.removeEntry(player.getName());
            }
        });
        if (this.spectatorTeam.hasEntry(player.getName())) {
            this.spectatorTeam.removeEntry(player.getName());
        }
    }

    public void updateIngameScoreboard(Player player) {
        this.i = 1;
        this.getTopTeams().forEach(bingoTeam -> {
            this.updateBoard(player,
                    String.format("§7Team %s%s §8● §a%s§8/§c9", Utils.getChatColor(bingoTeam.getColor()),
                            bingoTeam.getName(), bingoTeam.getDoneItemsSize()),
                    "top" + (this.i));
            this.i++;
        });
    }

    public void updateBoard(Player player, String value, String score) {
        if (player.getScoreboard().getTeam("top1") != null) {
            player.getScoreboard().getTeam(score).setPrefix(value);
        } else {
            setGameScoreboard(player);
            updateBoard(player, value, score);
        }
    }

    public ArrayList<BingoTeam> getTopTeams() {

        return Arrays.stream(BingoTeam.values()).filter(team -> !team.isEmpty()).sorted((o1, o2) -> {

            int i1 = o1.getDoneItemsSize();
            int i2 = o2.getDoneItemsSize();

            if (i1 > i2) {
                return 1;
            } else if (i1 < i2) {
                return -1;
            }

            return 0;

        }).collect(Collectors.toCollection(ArrayList::new));

    }
}