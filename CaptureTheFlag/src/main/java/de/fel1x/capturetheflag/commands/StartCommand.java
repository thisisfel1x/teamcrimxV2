package de.fel1x.capturetheflag.commands;

import de.fel1x.capturetheflag.CaptureTheFlag;
import de.fel1x.capturetheflag.gamestate.Gamestate;
import de.fel1x.capturetheflag.timers.IdleTimer;
import de.fel1x.capturetheflag.timers.LobbyTimer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand implements CommandExecutor {

    private final CaptureTheFlag captureTheFlag;

    public StartCommand(CaptureTheFlag captureTheFlag) {
        this.captureTheFlag = captureTheFlag;
        this.captureTheFlag.getCommand("start").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {

        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        if (!player.hasPermission("ctf.start")) return false;

        Gamestate gamestate = CaptureTheFlag.getInstance().getGamestateHandler().getGamestate();

        if (gamestate != Gamestate.IDLE && gamestate != Gamestate.LOBBY) return false;

        if(this.captureTheFlag.getiTimer() instanceof IdleTimer) {
            this.captureTheFlag.getiTimer().stop();
            this.captureTheFlag.setiTimer(new LobbyTimer());
            ((LobbyTimer) this.captureTheFlag.getiTimer()).setCountdown(10);
            this.captureTheFlag.getiTimer().start();
            player.sendMessage(this.captureTheFlag.getPrefix() + "§aDer Countdown wurde verkürzt und gestartet");
        } else if(this.captureTheFlag.getiTimer() instanceof LobbyTimer) {
            if(((LobbyTimer) this.captureTheFlag.getiTimer()).getCountdown() > 10) {
                ((LobbyTimer) this.captureTheFlag.getiTimer()).setCountdown(10);
                player.sendMessage(this.captureTheFlag.getPrefix() + "§aDer Countdown wurde gestartet");
            } else {
                player.sendMessage(this.captureTheFlag.getPrefix()
                        + "§cDer Countdown kann nicht mehr verkürzt werden");
            }
        }

        return true;
    }
}
