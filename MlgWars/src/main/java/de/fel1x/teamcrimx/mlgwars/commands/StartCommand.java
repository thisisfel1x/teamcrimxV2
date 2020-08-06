package de.fel1x.teamcrimx.mlgwars.commands;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.timer.IdleTimer;
import de.fel1x.teamcrimx.mlgwars.timer.LobbyTimer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand implements CommandExecutor {

    private final MlgWars mlgWars;

    public StartCommand(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
        this.mlgWars.getCommand("start").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] args) {

        if(!(commandSender instanceof Player)) {
            return false;
        }

        Player player = (Player) commandSender;
        Gamestate gamestate = this.mlgWars.getGamestateHandler().getGamestate();

        if(!player.hasPermission("mlgwars.start")) {
            return false;
        }

        LobbyTimer lobbyTimer = new LobbyTimer();

        if(gamestate != Gamestate.IDLE && gamestate != Gamestate.LOBBY) {
            player.sendMessage(this.mlgWars.getPrefix() + "§cDas Spiel läuft bereits");
            return false;
        }

        if(this.mlgWars.getLobbyCountdown() <= 10) {
            player.sendMessage(this.mlgWars.getPrefix() + "§7Der Countdown kann nicht mehr verkürzt werden!");
            return false;
        }

        if(gamestate == Gamestate.IDLE) {
            Bukkit.getScheduler().cancelTasks(this.mlgWars);
            lobbyTimer.start(10);
            player.sendMessage(this.mlgWars.getPrefix() + "§7Der Countdown wurde verkürzt und das Spiel wird gestartet");
            return true;
        } else {
            Bukkit.getScheduler().cancelTasks(this.mlgWars);
            lobbyTimer.start(10);
            player.sendMessage(this.mlgWars.getPrefix() + "§7Der Countdown wurde verkürzt");
            return true;
        }
    }
}
