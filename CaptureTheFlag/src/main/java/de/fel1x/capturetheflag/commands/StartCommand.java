package de.fel1x.capturetheflag.commands;

import de.fel1x.capturetheflag.CaptureTheFlag;
import de.fel1x.capturetheflag.gamestate.Gamestate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {

        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        if (!player.hasPermission("ctf.start")) return false;

        Gamestate gamestate = CaptureTheFlag.getInstance().getGamestateHandler().getGamestate();

        if (!gamestate.equals(Gamestate.LOBBY)) return false;

        if (!CaptureTheFlag.getInstance().getLobbyTimer().isRunning()) {
            CaptureTheFlag.getInstance().getLobbyTimer().setCountdown(10);
            CaptureTheFlag.getInstance().getLobbyTimer().start();
            return true;
        }

        if (CaptureTheFlag.getInstance().getLobbyTimer().getCountdown() <= 10) return false;

        CaptureTheFlag.getInstance().getLobbyTimer().setCountdown(10);

        return true;
    }
}
