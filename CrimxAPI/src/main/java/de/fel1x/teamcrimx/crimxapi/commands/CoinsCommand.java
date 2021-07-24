package de.fel1x.teamcrimx.crimxapi.commands;

import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.coins.CrimxCoins;
import de.fel1x.teamcrimx.crimxapi.support.CrimxSpigotAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoinsCommand implements CommandExecutor {

    private final CrimxAPI crimxAPI = CrimxAPI.getInstance();
    private CrimxCoins crimxCoins;

    public CoinsCommand(CrimxSpigotAPI crimxSpigotAPI) {
        crimxSpigotAPI.getCommand("coins").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] args) {
        if (!(commandSender instanceof Player)) {
            return false;
        }

        Player player = (Player) commandSender;

        boolean hasPermission = player.hasPermission("crimxapi.coins");

        if (!hasPermission || args.length == 0) {
            new CrimxCoins(player.getUniqueId()).getCoinsAsync().thenAccept(coins ->
                    player.sendMessage(this.crimxAPI.getPrefix() + "§7Du besitzt momentan stolze §e" + coins + " Coins"));
        } else {
            if (args.length == 1) {
                player.sendMessage(this.crimxAPI.getPrefix() + "§cNutzung: /coins <set|add|remove> <playername> <coins>");
            } else if (args.length == 3) {
                String operation = args[0];
                Player targetPlayer = Bukkit.getPlayer(args[1]);
                int coinsOperation;

                try {
                    coinsOperation = Integer.parseInt(args[2]);
                } catch (NumberFormatException ignored) {
                    player.sendMessage(this.crimxAPI + "§cBitte gebe eine Zahl an!");
                    return false;
                }

                if (targetPlayer == null || !targetPlayer.isOnline()) {
                    player.sendMessage(this.crimxAPI.getPrefix() + "§cDieser Spieler ist nicht online");
                    return false;
                }

                this.crimxCoins = new CrimxCoins(targetPlayer.getUniqueId());

                switch (operation) {
                    case "get":
                        this.crimxCoins.getCoinsAsync().thenAccept(coins ->
                                player.sendMessage(this.crimxAPI.getPrefix() + "§7Der Spieler §a" + targetPlayer.displayName()
                                        + " §7besitzt §e" + coins + " Coins"));
                        break;

                    case "add":
                        this.crimxCoins.addCoinsAsync(coinsOperation).thenAccept(success -> {
                            if (success) {
                                this.crimxCoins.getCoinsAsync().thenAccept(coins ->
                                        player.sendMessage(this.crimxAPI.getPrefix() + "§e" + coinsOperation + " Coins gutgeschrieben! " +
                                                "§7Der Spieler §a" + targetPlayer.getDisplayName() + " §7besitzt §e" + coins + " Coins"));
                            } else {
                                player.sendMessage(this.crimxAPI.getPrefix() + "§cEs trat ein Fehler auf");
                            }
                        });
                        break;

                    case "remove":
                        this.crimxCoins.removeCoinsAsync(coinsOperation).thenAccept(success -> {
                            if (success) {
                                this.crimxCoins.getCoinsAsync().thenAccept(coins ->
                                        player.sendMessage(this.crimxAPI.getPrefix() + "§e" + coinsOperation + " Coins entfernt! " +
                                                "§7Der Spieler §a" + targetPlayer.getDisplayName() + " §7besitzt §e" + coins + " Coins"));
                            } else {
                                player.sendMessage(this.crimxAPI.getPrefix() + "§cEs trat ein Fehler auf");
                            }
                        });
                        break;

                    case "set":
                        this.crimxCoins.setCoinsAsync(coinsOperation).thenAccept(success -> {
                            if (success) {
                                this.crimxCoins.getCoinsAsync().thenAccept(coins ->
                                        player.sendMessage(this.crimxAPI.getPrefix() + "§e" + coinsOperation + " Coins gesetzt! " +
                                                "§7Der Spieler §a" + targetPlayer.getDisplayName() + " §7besitzt §e" + coins + " Coins"));
                            } else {
                                player.sendMessage(this.crimxAPI.getPrefix() + "§cEs trat ein Fehler auf");
                            }
                        });
                        break;

                }

            }
        }
        return true;
    }
}
