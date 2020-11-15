package de.fel1x.bingo.commands;

import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.gamehandler.Gamestate;
import de.fel1x.bingo.tasks.IdleTask;
import de.fel1x.bingo.tasks.LobbyTask;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand implements CommandExecutor {

    private final Bingo bingo;

    public StartCommand(Bingo bingo) {
        this.bingo = bingo;
        this.bingo.getCommand("start").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {

        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        if (!player.hasPermission("ctf.start")) return false;

        Gamestate gamestate = Bingo.getInstance().getGamestateHandler().getGamestate();

        if (gamestate != Gamestate.IDLE && gamestate != Gamestate.LOBBY) return false;

        if (this.bingo.getBingoTask() instanceof IdleTask) {
            this.bingo.getBingoTask().stop();
            this.bingo.setBingoTask(new LobbyTask());
            ((LobbyTask) this.bingo.getBingoTask()).setCountdown(10);
            this.bingo.getBingoTask().start();
            player.sendMessage(this.bingo.getPrefix() + "§aDer Countdown wurde verkürzt und gestartet");
        } else if (this.bingo.getBingoTask() instanceof LobbyTask) {
            if (((LobbyTask) this.bingo.getBingoTask()).getCountdown() > 10) {
                ((LobbyTask) this.bingo.getBingoTask()).setCountdown(10);
                player.sendMessage(this.bingo.getPrefix() + "§aDer Countdown wurde gestartet");
            } else {
                player.sendMessage(this.bingo.getPrefix()
                        + "§cDer Countdown kann nicht mehr verkürzt werden");
            }
        }

        return true;
    }
}
