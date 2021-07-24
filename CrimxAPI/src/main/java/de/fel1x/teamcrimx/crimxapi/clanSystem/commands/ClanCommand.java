package de.fel1x.teamcrimx.crimxapi.clanSystem.commands;

import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.clanSystem.clan.Clan;
import de.fel1x.teamcrimx.crimxapi.clanSystem.clan.IClan;
import de.fel1x.teamcrimx.crimxapi.clanSystem.events.ClanUpdateEvent;
import de.fel1x.teamcrimx.crimxapi.clanSystem.player.ClanPlayer;
import de.fel1x.teamcrimx.crimxapi.clanSystem.player.IClanPlayer;
import de.fel1x.teamcrimx.crimxapi.support.CrimxSpigotAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ClanCommand implements CommandExecutor, TabCompleter {

    private final CrimxAPI crimxAPI = CrimxAPI.getInstance();

    private final String clanPrefix = this.crimxAPI.getClanPrefix();

    public ClanCommand(CrimxSpigotAPI crimxSpigotAPI) {

        crimxSpigotAPI.getCommand("clan").setExecutor(this::onCommand);
        crimxSpigotAPI.getCommand("clan").setTabCompleter(this::onTabComplete);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String string, @NotNull String[] args) {

        if (!(commandSender instanceof Player)) return false;

        Player player = (Player) commandSender;
        IClanPlayer clanPlayer = new ClanPlayer(player.getUniqueId());
        IClan iClan = clanPlayer.getCurrentClan();

        if (args.length > 0) {
            if (args.length == 1) {
                if (args[0].startsWith("?join=")) {
                    String clanId = args[0].split("=")[1];
                    UUID clanUUID = UUID.fromString(clanId);
                    IClan toJoin = new Clan(clanUUID);
                    toJoin.addPlayerToClan(clanPlayer);
                    player.sendMessage(String.format("%s§7Du bist erfolgreich dem Clan §e%s §8(§6%s§8) §7beigetreten",
                            this.clanPrefix, toJoin.getClanName(),
                            toJoin.getClanTag()));
                    return true;
                }

                switch (args[0].toLowerCase()) {
                    case "info":
                        if (clanPlayer.hasClan() && iClan != null) {
                            player.sendMessage(String.format("%s§7Aktueller Clan: §e%s §8(§6%s§8)", this.clanPrefix, iClan.getClanName(),
                                    iClan.getClanTag()));
                            player.sendMessage(String.format("%s§7Mitglieder: §a%s§8/§c20 §8● §7Owner: §5§o%s", this.clanPrefix,
                                    iClan.getTotalClanMembers(), iClan.getFormattedUserName(iClan.getClanOwner())));
                            player.sendMessage(this.clanPrefix + "§7Nutze §b/clan members§7, um dir alle Clanmitglieder auflisten zu lassen");
                        } else {
                            player.sendMessage(this.clanPrefix + "§7Du bist in keinem Clan! Erstelle einen mit §c/clan create");
                        }
                        break;
                    case "create":
                        player.sendMessage(this.clanPrefix + "§7Du kannst deinen Clan in wenigen Schritten erstellen. " +
                                "Nutze dafür bitte §c/clan create <Name> <Tag> §o(max 5. Zeichen)§r§c <Material> §o(Beliebiges Minecraft Material)");
                        break;
                    case "members":
                        clanPlayer.sendMembersList();
                        break;
                    case "search":
                        player.sendMessage(this.clanPrefix + "§7Suche einen Clan mit einem Tag §o§8(§b/clan search <Tag>§8)§r§7 oder suche nach dem Clan eines Spielers §o§8(§b/clan search <Playername>§8)");
                        break;
                    case "invite":
                        player.sendMessage(this.clanPrefix + "§7Laden einen Spieler in deinen Clan ein §o§8(§b/clan invite <Playername>§8)");
                        break;
                    case "kick":
                        player.sendMessage(this.clanPrefix + "§7Kicke einen Spieler aus deinem Clan §o§8(§b/clan kick <Playername>§8)");
                        break;
                    case "quit":
                        if (clanPlayer.hasClan()) {
                            iClan.removePlayerFromClan(clanPlayer);
                        } else {
                            player.sendMessage(this.clanPrefix + "§cDu bist in keinem Clan");
                        }
                        break;
                    case "deleteallrequests":
                        if (clanPlayer.deleteAllRequests()) {
                            player.sendMessage(this.clanPrefix + "§7Alle Anfragen wurden erfolgreich §cgelöscht!");
                        } else {
                            player.sendMessage(this.clanPrefix + "§7Du hast keine offenen Anfragen!");
                        }
                        break;
                }
            } else if (args.length == 2) {
                switch (args[0].toLowerCase()) {
                    case "invite":
                        String playerName = args[1];
                        if (iClan.invitePlayer(playerName)) {
                            player.sendMessage(this.clanPrefix + "§7Dem Spieler §e" + playerName + " §7wurde eine Anfrage gesendet");
                        } else {
                            player.sendMessage(this.clanPrefix + "§cDieser Spieler ist bereits in einem Clan");
                        }
                        break;
                    case "kick":
                        String playerNameToKick = args[1];
                        if (iClan.removePlayerFromClanByName(playerNameToKick)) {
                            player.sendMessage(this.clanPrefix + "§7Du hast den Spieler §e" + playerNameToKick + " §7erfolgreich aus deinem Clan entfernt!");
                        } else {
                            player.sendMessage(this.clanPrefix + "§cDieser Spieler ist nicht in deinem Clan");
                        }
                        break;
                }

            } else if (args.length == 4) {
                if (args[0].equalsIgnoreCase("create")) {
                    if (clanPlayer.hasClan()) {
                        player.sendMessage(this.clanPrefix + "§cDu bist bereits in einem Clan!");
                        return false;
                    }

                    String clanName = args[1];
                    String clanTag = args[2];

                    String clanMaterialNotParsed = args[3];
                    Material clanMaterialParsed;

                    if (clanTag.length() > 5) {
                        player.sendMessage(this.clanPrefix + "§cDer Clantag ist zu lang. Er darf maximal aus 5 Zeichen bestehen!");
                        return false;
                    }

                    try {
                        clanMaterialParsed = Material.valueOf(clanMaterialNotParsed);
                    } catch (Exception ignored) {
                        player.sendMessage(this.clanPrefix + "§cDas Clanmaterial wurde nicht gefunden!");
                        return false;
                    }

                    IClan clan = new Clan(UUID.randomUUID(), false);
                    if (clan.createClan(clanName, clanTag, clanMaterialParsed, clanPlayer)) {
                        clanPlayer.sendMessage("§7Dein Clan wurde erstellt!", true, player.getUniqueId());
                    } else {
                        player.sendMessage(this.clanPrefix + "§cEin Fehler ist aufgetreten!");
                        return false;
                    }
                }
            }
        } else {
            player.sendMessage(this.clanPrefix + "§7Nutze §c/clan create | info | members | search | invite | kick | quit | deleteAllRequests");
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String string, @NotNull String[] args) {
        if (args.length > 0) {
            switch (args.length) {
                case 2:
                    if (args[0].equalsIgnoreCase("search") || args[0].equalsIgnoreCase("invite")) {
                        return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
                    } else {
                        return null;
                    }
                case 4:
                    if (args[0].equalsIgnoreCase("create")) {
                        return Arrays.stream(Material.values()).map(Enum::name).collect(Collectors.toList());
                    } else {
                        return null;
                    }
            }
        }
        return Arrays.asList("create", "info", "members", "invite", "kick", "deleteAllRequests", "quit");
    }
}
