package de.fel1x.teamcrimx.crimxapi.clanSystem.commands;

import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.clanSystem.clan.IClan;
import de.fel1x.teamcrimx.crimxapi.clanSystem.player.ClanPlayer;
import de.fel1x.teamcrimx.crimxapi.clanSystem.player.IClanPlayer;
import de.fel1x.teamcrimx.crimxapi.support.CrimxSpigotAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ClanCommand implements CommandExecutor, TabCompleter {

    private final CrimxAPI crimxAPI = CrimxAPI.getInstance();
    private final CrimxSpigotAPI crimxSpigotAPI;

    private final String clanPrefix = this.crimxAPI.getClanPrefix();

    public ClanCommand(CrimxSpigotAPI crimxSpigotAPI) {
        this.crimxSpigotAPI = crimxSpigotAPI;

        this.crimxSpigotAPI.getCommand("clan").setExecutor(this::onCommand);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String string, @NotNull String[] args) {

        if(!(commandSender instanceof Player)) return false;

        Player player = (Player) commandSender;
        IClanPlayer clanPlayer = new ClanPlayer(player.getUniqueId());
        IClan iClan = clanPlayer.getCurrentClan();

        if(args.length > 0) {

        } else {
            if(clanPlayer.hasClan() && iClan != null) {
                player.sendMessage(String.format("%s§7Aktueller Clan: §e%s §7(§6%s)", this.clanPrefix, iClan.getClanName(),
                        iClan.getClanTag()));
            } else {
                player.sendMessage(this.clanPrefix + "§7Du bist in keinem Clan! Erstelle einen mit §c/clan create");
            }
            return true;
        }


        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String string, @NotNull String[] args) {
        return null;
    }
}
