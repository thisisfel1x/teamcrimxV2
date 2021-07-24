package de.fel1x.teamcrimx.crimxapi.friends.commands;

import de.dytanic.cloudnet.ext.bridge.player.ICloudOfflinePlayer;
import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.friends.database.FriendPlayer;
import de.fel1x.teamcrimx.crimxapi.friends.database.IFriendPlayer;
import de.fel1x.teamcrimx.crimxapi.support.CrimxSpigotAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class FriendCommand implements CommandExecutor, TabCompleter {

    private final CrimxAPI crimxAPI = CrimxAPI.getInstance();
    private final String friendPrefix = this.crimxAPI.getFriendPrefix();
    private final String guideString = this.friendPrefix
            + "§7Nutze §c/friend add | remove | list | denyRequest | acceptRequest | <Name> oder /friend denyAllRequests";

    public FriendCommand(CrimxSpigotAPI crimxSpigotAPI) {
        Objects.requireNonNull(crimxSpigotAPI.getCommand("friend")).setExecutor(this);
        Objects.requireNonNull(crimxSpigotAPI.getCommand("friend")).setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;
        IFriendPlayer friendPlayer = new FriendPlayer(player.getUniqueId());

        if (args.length > 0) {
            if (args.length == 1) {
                switch (args[0].toLowerCase()) {
                    case "add":
                    case "remove":
                    case "denyRequest":
                    case "acceptRequest":
                        player.sendMessage(this.guideString);
                        break;
                    case "list":
                        friendPlayer.listFriends(1);
                        break;
                    case "denyallrequests":
                        friendPlayer.denyAllRequests().thenAccept(success -> {
                            if (success) {
                                player.sendMessage(this.friendPrefix + "§7Alle offenen Freundschaftsanfragen wurden erfolgreich gelöscht");
                            } else {
                                player.sendMessage(this.friendPrefix + "§cEin Fehler ist aufgetreten");
                            }
                        });
                        break;
                    default:
                        String internalCommand = args[0];
                        if (!internalCommand.startsWith("?")) {
                            return false;
                        }
                        UUID targetUUID = UUID.fromString(internalCommand.split("=")[1]);
                        switch (internalCommand.substring(1).split("=")[0].toLowerCase()) {
                            case "add":
                                if (friendPlayer.areAlreadyFriends(targetUUID)) {
                                    player.sendMessage(this.friendPrefix + "§cDu bist bereits mit diesem Spieler befreundet!");
                                    return false;
                                }
                                friendPlayer.addFriend(targetUUID);
                                break;
                            case "deny":
                                if (!friendPlayer.hasOpenFriendRequest(targetUUID)) {
                                    player.sendMessage(this.friendPrefix + "§cDu hast keine offene " +
                                            "Freundschaftsanfrage von diesem Spieler!");
                                    return false;
                                }
                                friendPlayer.denyFriendRequest(targetUUID);
                                break;
                        }

                }
            } else if (args.length == 2) {
                UUID targetPlayerUUID = this.getUUIDFromName(args[1]);
                if (targetPlayerUUID == null) {
                    player.sendMessage(this.friendPrefix + "§cDieser Spieler war noch nie auf dem Netzwerk. " +
                            "Bitte achte auf Groß- und Kleinschreibung");
                    return false;
                }
                if (targetPlayerUUID.equals(player.getUniqueId())) {
                    player.sendMessage(this.friendPrefix + "§cDu kannst nicht selbst mit dir interagieren!");
                    return false;
                }
                switch (args[0].toLowerCase()) {
                    case "add":
                        if (friendPlayer.areAlreadyFriends(targetPlayerUUID)) {
                            player.sendMessage(this.friendPrefix + "§cDu bist bereits mit diesem Spieler befreundet");
                            return false;
                        }
                        if (friendPlayer.hasOpenFriendRequest(targetPlayerUUID)) {
                            player.sendMessage(this.friendPrefix + "§7Dieser Spieler hat dir bereits eine " +
                                    "Freundschaftsanfrage geschickt");
                            return false;
                        }
                        friendPlayer.sendFriendRequest(targetPlayerUUID).thenAccept(success -> {
                            if (!success) {
                                player.sendMessage(this.friendPrefix + "§cEin interner Fehler ist aufgetreten");
                            } else {
                                // TODO: rank prefix
                                player.sendMessage(this.friendPrefix + "§7Eine Freundschaftsanfrage wurde an §e"
                                        + args[1] + " §7versandt");
                            }
                        });
                        break;
                    case "remove":
                        friendPlayer.removeFriend(targetPlayerUUID).thenAccept(success -> {
                            if (!success) {
                                player.sendMessage(this.friendPrefix + "§cEin interner Fehler ist aufgetreten");
                            }
                        });
                        break;
                    case "list":
                        try {
                            int page = Integer.parseInt(args[1]);
                            player.sendMessage(this.friendPrefix + "§cDieses Feature ist noch nicht verfügbar");
                            friendPlayer.listFriends(page);
                        } catch (NumberFormatException ignored) {
                            player.sendMessage(this.friendPrefix + "§cBitte gebe eine Zahl als Seite an");
                        }
                        break;
                    case "acceptrequest":
                        friendPlayer.acceptFriendRequest(targetPlayerUUID).thenAccept(success -> {
                            if (!success) {
                                player.sendMessage(this.friendPrefix + "§cEin interner Fehler ist aufgetreten!");
                            } else {
                                // TODO: rank prefix
                                player.sendMessage(this.friendPrefix + "§7Du bist nun mit §e"
                                        + args[1] + " §7befreundet aufgelöst!");
                            }
                        });
                        break;
                    case "denyrequest":
                        friendPlayer.denyFriendRequest(targetPlayerUUID).thenAccept(success -> {
                            if (!success) {
                                player.sendMessage(this.friendPrefix + "§cEin interner Fehler ist aufgetreten!");
                            } else {
                                // TODO: rank prefix
                                player.sendMessage(this.friendPrefix + "§7Du hast die Freundschaftsanfrage von §e"
                                        + args[1] + " §7abgelehnt!");
                            }
                        });
                        break;
                }
            }
        } else {
            player.sendMessage(this.guideString);
            return false;
        }

        return true;
    }

    // TODO: look iFriendPlayer
    private UUID getUUIDFromName(String playerName) {
        ICloudOfflinePlayer cloudOfflinePlayer = this.crimxAPI.getPlayerManager().getFirstOfflinePlayer(playerName);
        if (cloudOfflinePlayer == null) {
            return null;
        }
        return cloudOfflinePlayer.getUniqueId();
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return Collections.singletonList("Lukas ist schwul und Sven stinkt");
    }
}
