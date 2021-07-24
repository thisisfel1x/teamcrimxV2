package de.fel1x.bingo.commands;

import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.objects.BingoPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ItemsCommand implements CommandExecutor {

    private final Bingo bingo;

    public ItemsCommand(Bingo bingo) {
        this.bingo = bingo;

        bingo.getCommand("items").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(this.bingo.getPrefix() + "§cDu musst ein Spieler sein um das zu tun");
            return false;
        }

        Player player = (Player) sender;

        if (this.bingo.getItemGenerator() == null) {
            player.sendMessage(this.bingo.getPrefix() + "§cDie Items wurden noch nicht generiert, bitte stimme zuerst über die Schwierigkeit ab!");
            return false;
        }

        BingoPlayer bingoPlayer = new BingoPlayer(player);

        player.openInventory(this.bingo.getBingoInventory(bingoPlayer));
        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 0.5f);

        return true;
    }

}
