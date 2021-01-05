package de.fel1x.teamcrimx.crimxapi.clanSystem.commands;

import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.clanSystem.clan.IClan;
import de.fel1x.teamcrimx.crimxapi.clanSystem.player.ClanPlayer;
import de.fel1x.teamcrimx.crimxapi.clanSystem.player.IClanPlayer;
import de.fel1x.teamcrimx.crimxapi.support.CrimxSpigotAPI;
import org.bukkit.Bukkit;
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

public class ClanCommand implements CommandExecutor, TabCompleter {

    private final CrimxAPI crimxAPI = CrimxAPI.getInstance();
    private final CrimxSpigotAPI crimxSpigotAPI;

    private final String clanPrefix = this.crimxAPI.getClanPrefix();

    public ClanCommand(CrimxSpigotAPI crimxSpigotAPI) {
        this.crimxSpigotAPI = crimxSpigotAPI;

        this.crimxSpigotAPI.getCommand("clan").setExecutor(this::onCommand);
        this.crimxSpigotAPI.getCommand("clan").setTabCompleter(this::onTabComplete);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String string, @NotNull String[] args) {

        if(!(commandSender instanceof Player)) return false;

        Player player = (Player) commandSender;
        IClanPlayer clanPlayer = new ClanPlayer(player.getUniqueId());
        IClan iClan = clanPlayer.getCurrentClan();

        if(args.length > 0) {
            if(args.length == 1) {
                switch (args[0].toLowerCase()) {
                    case "info":
                        if (clanPlayer.hasClan() && iClan != null) {
                            player.sendMessage(String.format("%s§7Aktueller Clan: §e%s §7(§6%s)", this.clanPrefix, iClan.getClanName(),
                                    iClan.getClanTag()));
                        } else {
                            player.sendMessage(this.clanPrefix + "§7Du bist in keinem Clan! Erstelle einen mit §c/clan create");
                        }
                        break;
                    case "create":
                        player.sendMessage(this.clanPrefix + "§7Du kannst deinen Clan in wenigen Schritten erstellen. " +
                                "Nutze dafür bitte §c/clan create <Name> <Tag> §o(max 5. Zeichen)§r§c <Material> §o(Beliebiges Minecraft Material)");
                        break;
                    case "search":
                        player.sendMessage(this.clanPrefix + "§7Suche einen Clan mit einem Tag §o§8(§b/clan search <Tag>§8)§r§7 oder suche nach dem Clan eines Spielers §o§8(§b/clan search <Playername>§8)");
                        break;
                }
            }
        } else {
            player.sendMessage(this.clanPrefix + "§7Nutze §c/clan create | info | search | invite | kick");
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String string, @NotNull String[] args) {
        if(args.length > 0) {
            switch (args.length) {
                case 2:
                    switch (args[0]) {
                        case "search":
                    }
            }
        }
        return Arrays.asList("create", "info", "search", "invite", "kick");
    }
}
