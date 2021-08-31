package de.fel1x.teamcrimx.crimxlobby.minigames.connectfour;

import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.minigames.connectfour.objects.Game;
import de.fel1x.teamcrimx.crimxlobby.minigames.connectfour.objects.Invitation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ConnectFourGameManager implements CommandExecutor {

    private final CrimxLobby crimxLobby;

    private final List<Invitation> invitations;
    private final List<Game> activeConnectFourGames;

    private final Component prefix = Component.text("VierGewinnt ", TextColor.fromHexString("#00de16"))
            .decoration(TextDecoration.ITALIC, false).append(Component.text("● ", NamedTextColor.DARK_GRAY));

    public ConnectFourGameManager(CrimxLobby crimxLobby) {
        this.crimxLobby = crimxLobby;

        this.invitations = new ArrayList<>();
        this.activeConnectFourGames = new ArrayList<>();

        this.crimxLobby.getCommand("connectfour").setExecutor(this);
    }

    public boolean addGame(Game game) {
        return this.activeConnectFourGames.add(game);
    }

    public void addInvitation(Invitation invitation) {
        Player whoInvited = invitation.getWhoInvited();
        for (Invitation inv : this.invitations) {
            if (inv.getWhoInvited().getUniqueId().equals(whoInvited.getUniqueId())) {
                this.invitations.remove(inv);
                return;
            }
        }
        this.invitations.add(invitation);
    }

    public boolean hasInvitedRecently(Player player) {
        for (Invitation invitation : this.invitations) {
            if (invitation.getWhoInvited().getUniqueId().equals(player.getUniqueId())) {
                return System.currentTimeMillis() - invitation.getInviteTime() < 10000L;
            }
        }
        return false;
    }

    public boolean isPlayerInvitedByPlayer(Player player1, Player player2) {
        for (Invitation invitation : this.invitations) {
            if (invitation.getWhoInvited().getUniqueId().equals(player1.getUniqueId())) {
                if (!invitation.getTarget().getUniqueId().equals(player2.getUniqueId())) {
                    return false;
                }
                return System.currentTimeMillis() - invitation.getInviteTime() < 10000L;
            }
        }
        return false;
    }

    public Game getGame(Player player) {
        boolean found = false;
        int i = 0;
        Game game = null;
        while (!found && i < this.activeConnectFourGames.size()) {
            if (this.activeConnectFourGames.get(i).isPlaying(player)) {
                found = true;
                game = this.activeConnectFourGames.get(i);
            }
            i++;
        }
        return game;
    }

    public boolean isPlaying(Player player) {
        for (Game game : this.activeConnectFourGames) {
            if (game.isPlaying(player)) {
                return true;
            }
        }
        return false;
    }

    private void removeInvitation(Player player) {
        for (Invitation invitation : this.invitations) {
            if(invitation.getWhoInvited().getUniqueId().equals(player.getUniqueId())
                    || invitation.getTarget().getUniqueId().equals(player.getUniqueId())) {
                this.invitations.remove(invitation);
                break;
            }
        }
    }

    public List<Invitation> getInvitations() {
        return this.invitations;
    }

    public List<Game> getActiveConnectFourGames() {
        return this.activeConnectFourGames;
    }

    public Component getPrefix() {
        return this.prefix;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            return false;
        }

        if(args.length == 0) {
            return false;
        }

        if(args.length == 1) {
            try {
                UUID whoInvitedUUID = UUID.fromString(args[0].split("=")[1]);
                Player whoInvited = Bukkit.getPlayer(whoInvitedUUID);

                if(whoInvited == null) {
                    player.sendMessage(this.prefix
                            .append(Component.text("Der Spieler ist nicht mehr online", NamedTextColor.GRAY)));
                    return false;
                }

                if(this.isPlaying(whoInvited)) {
                    player.sendMessage(this.prefix
                            .append(whoInvited.displayName())
                            .append(Component.text(" spielt bereits eine Runde", NamedTextColor.GRAY)));
                    return false;
                }

                if(!this.isPlayerInvitedByPlayer(whoInvited, player)) {
                    player.sendMessage(this.prefix
                            .append(Component.text("Die Einladung ist abgelaufen", NamedTextColor.GRAY)));
                    return false;
                }
                new Game(this.crimxLobby, player, whoInvited).startGame();

            } catch (Exception ignored) {
                player.sendMessage(this.prefix
                        .append(Component.text("Ein Fehler ist aufgetreten", NamedTextColor.RED)));
            }

            this.crimxLobby.getConnectFourGameManager().removeInvitation(player);

        }

        return false;
    }
}
