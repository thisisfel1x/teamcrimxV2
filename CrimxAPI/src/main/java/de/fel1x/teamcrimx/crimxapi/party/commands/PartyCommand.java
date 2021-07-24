package de.fel1x.teamcrimx.crimxapi.party.commands;

import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.support.CrimxSpigotAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class PartyCommand implements CommandExecutor, TabCompleter {

    private final CrimxAPI crimxAPI;

    public PartyCommand(CrimxSpigotAPI crimxSpigotAPI) {
        this.crimxAPI = CrimxAPI.getInstance();

        Objects.requireNonNull(crimxSpigotAPI.getCommand("party")).setExecutor(this);
        Objects.requireNonNull(crimxSpigotAPI.getCommand("party")).setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }
}
