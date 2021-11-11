package de.fel1x.teamcrimx.mlgwars.commands;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ItemMultiplierCommand implements CommandExecutor {

    private final MlgWars mlgWars;

    public ItemMultiplierCommand(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
        this.mlgWars.getCommand("multiplier").setExecutor(this::onCommand);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            return false;
        }

        Gamestate gamestate = this.mlgWars.getGamestateHandler().getGamestate();

        if (!player.hasPermission("mlgwars.multiplier")) {
            return false;
        }

        if(args.length == 0) {
            player.sendMessage(this.mlgWars.getPrefix() + "Bitte gebe eine Zahl zwischen 1 und 20 an");
            return false;
        }

        if (gamestate != Gamestate.IDLE && gamestate != Gamestate.LOBBY) {
            player.sendMessage(this.mlgWars.getPrefix() + "§cDas Spiel läuft bereits");
            return false;
        }

        if (this.mlgWars.getLobbyCountdown() <= 10) {
            player.sendMessage(this.mlgWars.getPrefix() + "§7Der Multiplier kann nicht mehr gesetzt werden!");
            return false;
        }

        int multiplier = 1;

        try {
            multiplier = Integer.valueOf(args[0]);
        } catch (Exception ignored) {}

        if(multiplier < 1 || multiplier > 20) {
            multiplier = 1;
        }

        player.sendMessage(this.mlgWars.getPrefix() + "§7Der Multiplier wurde auf " + multiplier + " gesetzt");
        this.mlgWars.getGameType().setItemMultiplier(multiplier);

        return true;
    }
}
