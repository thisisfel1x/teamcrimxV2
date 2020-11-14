package de.fel1x.bingo.commands;

import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.gamehandler.Gamestate;
import de.fel1x.bingo.inventories.SkipItemInventory;
import de.fel1x.bingo.objects.BingoPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SkipItemCommand implements CommandExecutor {

    private final Bingo bingo;

    public SkipItemCommand(Bingo bingo) {
        this.bingo = bingo;

        bingo.getCommand("skipitem").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(this.bingo.getPrefix() + "§cDu musst ein Spieler sein um das zu tun");
            return false;
        }

        Player player = (Player) sender;
        BingoPlayer bingoPlayer = new BingoPlayer(player);

        if (!this.bingo.getGamestateHandler().getGamestate().equals(Gamestate.IDLE)
                && !this.bingo.getGamestateHandler().getGamestate().equals(Gamestate.LOBBY)) {
            player.sendMessage(this.bingo.getPrefix() + "§7Die Runde läuft noch nicht oder ist schon vorbei!");
            return false;
        }

        if (!player.isOp() || !player.hasPermission("bingo.skipitem")) return false;

        SkipItemInventory.SKIP_INVENTORY.open(player);
        return true;

    }

}
