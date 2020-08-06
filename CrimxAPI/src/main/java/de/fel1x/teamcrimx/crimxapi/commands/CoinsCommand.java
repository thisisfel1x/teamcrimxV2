package de.fel1x.teamcrimx.crimxapi.commands;

import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.coins.CoinsAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoinsCommand implements CommandExecutor {

    CrimxAPI crimxAPI;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] args) {

        crimxAPI = CrimxAPI.getInstance();

        if(!(commandSender instanceof Player)) {
            return false;
        }

        Player player = (Player) commandSender;
        CoinsAPI coinsAPI;

        boolean hasPermission = player.hasPermission("crimxapi.coins");

        if(!hasPermission) {
            coinsAPI = new CoinsAPI(player.getUniqueId());
            player.sendMessage(crimxAPI.getPrefix() + "§7Du besitzt momentan stolze §e" + coinsAPI.getCoins() + " Coins");
        } else {
            if(args.length == 0) {
                coinsAPI = new CoinsAPI(player.getUniqueId());
                player.sendMessage(crimxAPI.getPrefix() + "§7Du besitzt momentan stolze §e" + coinsAPI.getCoins() + " Coins");
            } else if(args.length == 1) {
                player.sendMessage(crimxAPI.getPrefix() + "§cUsage: /coins <set|add|remove> <playername>");
            } else if(args.length == 3) {

                String operation = args[0];
                Player targetPlayer = Bukkit.getPlayer(args[1]);
                int coinsOperation;

                try {
                    coinsOperation = Integer.parseInt(args[2]);
                } catch (NumberFormatException ignored) {
                    player.sendMessage(this.crimxAPI + "§cBitte gebe eine Zahl an!");
                    return false;
                }

                if(targetPlayer == null || !targetPlayer.isOnline()) {
                    player.sendMessage(crimxAPI.getPrefix() + "§cDieser Spieler ist nicht online");
                    return false;
                }

                coinsAPI = new CoinsAPI(targetPlayer.getUniqueId());

                switch (operation) {
                    case "get":
                        player.sendMessage(this.crimxAPI.getPrefix() + "§7Der Spieler §a" + targetPlayer.getDisplayName() + " §7besitzt §e" + coinsAPI.getCoins() + " Coins");
                        break;

                    case "add":
                        coinsAPI.addCoins(coinsOperation);
                        coinsAPI = new CoinsAPI(targetPlayer.getUniqueId());
                        player.sendMessage(this.crimxAPI.getPrefix() + "§e" + coinsOperation + " Coins gutgeschrieben! " +
                                "§7Der Spieler §a" + targetPlayer.getDisplayName() + " §7besitzt §e" + coinsAPI.getCoins() + " Coins");
                        break;

                    case "remove":
                        coinsAPI.removeCoins(coinsOperation);
                        coinsAPI = new CoinsAPI(targetPlayer.getUniqueId());
                        player.sendMessage(this.crimxAPI.getPrefix() + "§e" + coinsOperation + " Coins entfernt! " +
                                "§7Der Spieler §a" + targetPlayer.getDisplayName() + " §7besitzt §e" + coinsAPI.getCoins() + " Coins");
                        break;

                    case "set":
                        coinsAPI.setCoins(coinsOperation);
                        coinsAPI = new CoinsAPI(targetPlayer.getUniqueId());
                        player.sendMessage(this.crimxAPI.getPrefix() + "§e" + coinsOperation + " Coins gesetzt! " +
                                "§7Der Spieler §a" + targetPlayer.getDisplayName() + " §7besitzt §e" + coinsAPI.getCoins() + " Coins");
                        break;

                }

            }
        }

        return true;
    }
}
