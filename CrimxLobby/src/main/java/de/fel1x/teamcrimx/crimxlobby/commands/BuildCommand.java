package de.fel1x.teamcrimx.crimxlobby.commands;

import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.objects.LobbyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuildCommand implements CommandExecutor {

    private final CrimxLobby crimxLobby;

    public BuildCommand(CrimxLobby crimxLobby) {
        this.crimxLobby = crimxLobby;
        this.crimxLobby.getCommand("build").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] args) {

        if (!(commandSender instanceof Player)) return false;

        Player player = (Player) commandSender;
        LobbyPlayer lobbyPlayer = new LobbyPlayer(player);

        if (!player.hasPermission("crimxlobby.build")) return false;

        if (args.length == 0) {
            if ((lobbyPlayer.isInBuild())) {
                lobbyPlayer.removeFromBuild();
            } else {
                lobbyPlayer.activateBuild();
            }
        } else if (args.length == 1) {
            try {
                Player targetPlayer = Bukkit.getPlayer(args[0]);
                lobbyPlayer.grantOrRemoveBuildModeToPlayer(targetPlayer);

            } catch (Exception ignored) {
                player.sendMessage(this.crimxLobby.getPrefix() + "§cSpieler nicht gefunden");
            }
        }


        return true;
    }
}
