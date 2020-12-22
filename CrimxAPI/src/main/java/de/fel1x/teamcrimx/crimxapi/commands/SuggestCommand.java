package de.fel1x.teamcrimx.crimxapi.commands;

import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.support.CrimxSpigotAPI;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SuggestCommand implements CommandExecutor {

    private final CrimxSpigotAPI crimxSpigotAPI;
    private final CrimxAPI crimxAPI = CrimxAPI.getInstance();

    public SuggestCommand(CrimxSpigotAPI crimxSpigotAPI) {
        this.crimxSpigotAPI = crimxSpigotAPI;
        this.crimxSpigotAPI.getCommand("suggest").setExecutor(this::onCommand);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if(!(commandSender instanceof Player)) {
            return false;
        }

        Player player = (Player) commandSender;
        long currentTime = System.currentTimeMillis();

        long lastSuggestion = player.hasMetadata("lastSuggestion") ? player.getMetadata("lastSuggestion").get(0).asLong() : 0;

        if(lastSuggestion > currentTime) {
            player.sendMessage(this.crimxAPI.getPrefix() + "§cBitte warte noch einen Moment bevor du auf diesem Server den nächsten Vorschlag einreichst!");
            return false;
        } else {
            if(args.length == 0) {
                player.sendMessage(this.crimxAPI.getPrefix() + "§7Bitte beschreibe deine Idee etwas genauer!");
                return false;
            } else {
                player.setMetadata("lastSuggestion", new FixedMetadataValue(this.crimxSpigotAPI, currentTime + 1000 * 60));

                StringBuilder stringBuilder = new StringBuilder();
                for (String arg : args) {
                    stringBuilder.append(arg + " ");
                }

                ICloudPlayer cloudPlayer = this.crimxAPI.getPlayerManager().getOnlinePlayer(player.getUniqueId());

                if(cloudPlayer.getConnectedService() == null) {
                    player.sendMessage(this.crimxAPI.getPrefix() + "§cEin Fehler ist aufgetreten!");
                    return false;
                }

                Document suggestion = new Document()
                        .append("_id", UUID.randomUUID().toString())
                        .append("timestamp", System.currentTimeMillis())
                        .append("playerUUID", player.getUniqueId().toString())
                        .append("name", player.getName())
                        .append("serviceUUID", cloudPlayer.getConnectedService().getServiceId().getUniqueId().toString())
                        .append("serverName", cloudPlayer.getConnectedService().getServerName())
                        .append("taskName", cloudPlayer.getConnectedService().getTaskName())
                        .append("suggestion", stringBuilder.toString());

                Bukkit.getScheduler().runTaskAsynchronously(this.crimxSpigotAPI,
                        () -> this.crimxAPI.getMongoDB().getSuggestionsCollection().insertOne(suggestion));

                player.sendMessage(this.crimxAPI.getPrefix() + "§aDein Vorschlag wurde erfolgreich eingereicht!");
            }

        }

        return true;
    }
}
