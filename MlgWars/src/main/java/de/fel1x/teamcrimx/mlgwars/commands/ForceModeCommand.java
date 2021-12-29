package de.fel1x.teamcrimx.mlgwars.commands;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.inventories.rework.GameTypeVoteInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ForceModeCommand implements TabCompleter, CommandExecutor {

    private final MlgWars mlgWars;
    private final List<GameTypeVoteInventory.ImplementedMode> implementedModes;

    public ForceModeCommand(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
        this.mlgWars.getCommand("forcemode").setExecutor(this::onCommand);
        this.mlgWars.getCommand("forcemode").setTabCompleter(this::onTabComplete);

        this.implementedModes = Arrays.stream(GameTypeVoteInventory.ImplementedMode.values())
                .filter(GameTypeVoteInventory.ImplementedMode::isActive).toList();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }

        Gamestate gamestate = this.mlgWars.getGamestateHandler().getGamestate();

        if (!player.hasPermission("mlgwars.forcemode")) {
            return false;
        }

        if(this.mlgWars.getTeamSize() != 1) {
            return false;
        }

        if(args.length == 0) {
            player.sendMessage(this.mlgWars.getPrefix() + "§7Aktuell aktive Gametypes: §e"
                    + this.implementedModes.stream().map(Enum::name).collect(Collectors.joining(", ")));
            return false;
        }

        if (gamestate != Gamestate.IDLE && gamestate != Gamestate.LOBBY) {
            player.sendMessage(this.mlgWars.getPrefix() + "§cDas Spiel läuft bereits");
            return false;
        }

        if (this.mlgWars.getLobbyCountdown() <= 10) {
            player.sendMessage(this.mlgWars.getPrefix() + "§7Der Modus kann nicht mehr geändert werden!");
            return false;
        }

        try {
            GameTypeVoteInventory.ImplementedMode implementedMode = GameTypeVoteInventory.ImplementedMode.valueOf(args[0]);
            this.mlgWars.setForcedMode(implementedMode);
            player.sendMessage(this.mlgWars.getPrefix() + "§7Folgender Modus wird gespielt: " + implementedMode.name());
            return true;
        } catch (Exception ignored) {}

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length == 1) {
            return this.implementedModes.stream().map(Enum::name).collect(Collectors.toList());
        }
        return null;
    }
}
