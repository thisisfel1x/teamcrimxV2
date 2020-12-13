package de.fel1x.teamcrimx.floorislava.commands;

import de.fel1x.teamcrimx.floorislava.FloorIsLava;
import de.fel1x.teamcrimx.floorislava.gamehandler.Gamestate;
import de.fel1x.teamcrimx.floorislava.tasks.LobbyTask;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand implements CommandExecutor {
    private final FloorIsLava floorIsLava;

    public StartCommand(FloorIsLava bingo) {
        this.floorIsLava = bingo;
        this.floorIsLava.getCommand("start").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {
        if (!(sender instanceof Player))
            return false;
        Player player = (Player) sender;
        if (!player.hasPermission("ctf.start"))
            return false;
        Gamestate gamestate = this.floorIsLava.getGamestateHandler().getGamestate();
        if (gamestate != Gamestate.IDLE && gamestate != Gamestate.LOBBY)
            return false;
        if (this.floorIsLava.getFloorIsLavaTask() instanceof de.fel1x.teamcrimx.floorislava.tasks.IdleTask) {
            this.floorIsLava.getFloorIsLavaTask().stop();
            this.floorIsLava.setFloorIsLavaTask(new LobbyTask());
            ((LobbyTask) this.floorIsLava.getFloorIsLavaTask()).setCountdown(10);
            this.floorIsLava.getFloorIsLavaTask().start();
            player.sendMessage(this.floorIsLava.getPrefix() + "§aDer Countdown wurde verkürzt und gestartet");
        } else if (this.floorIsLava.getFloorIsLavaTask() instanceof LobbyTask) {
            if (((LobbyTask) this.floorIsLava.getFloorIsLavaTask()).getCountdown() > 10) {
                ((LobbyTask) this.floorIsLava.getFloorIsLavaTask()).setCountdown(10);
                player.sendMessage(this.floorIsLava.getPrefix() + "§aDer Countdown wurde gestartet");
            } else {
                player.sendMessage(this.floorIsLava.getPrefix() + "§cDer Countdown kann nicht mehr verkürzt werden");
            }
        }
        return true;
    }
}
