package de.fel1x.teamcrimx.crimxapi.commands;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.IPermissionGroup;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.ext.bridge.BaseComponentMessenger;
import de.dytanic.cloudnet.ext.bridge.BridgeServiceProperty;
import de.dytanic.cloudnet.ext.bridge.player.CloudPlayer;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.dytanic.cloudnet.ext.bridge.player.NetworkServiceInfo;
import de.dytanic.cloudnet.wrapper.Wrapper;
import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.objects.CrimxPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinMeCommand implements CommandExecutor {

    private final CrimxAPI crimxAPI = CrimxAPI.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {

        if(!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;
        ICloudPlayer cloudPlayer = this.crimxAPI.getPlayerManager().getOnlinePlayer(player.getUniqueId());
        CrimxPlayer crimxPlayer = new CrimxPlayer(cloudPlayer);
        IPermissionUser permissionUser = CloudNetDriver.getInstance().getPermissionManagement().getUser(player.getUniqueId());

        if(permissionUser == null || cloudPlayer == null) {
            return false;
        }

        if(args.length == 0) {

            if(cloudPlayer.getConnectedService().getTaskName().equalsIgnoreCase("Lobby")) {
                player.sendMessage(this.crimxAPI.getPrefix() + "§7Du kannst auf der Lobby kein JoinMe erstellen");
                return false;
            }

            IPermissionGroup permissionGroup = CloudNetDriver.getInstance().getPermissionManagement().getHighestPermissionGroup(permissionUser);
            NetworkServiceInfo networkServiceInfo = cloudPlayer.getConnectedService();

            ServiceInfoSnapshot serviceInfoSnapshot = Wrapper.getInstance().getCloudServiceProvider().getCloudService(networkServiceInfo.getUniqueId());

            if(serviceInfoSnapshot == null) {
                return false;
            }

            int onlinePlayers = serviceInfoSnapshot.getProperty(BridgeServiceProperty.ONLINE_COUNT).orElse(0);
            int maxPlayers = serviceInfoSnapshot.getProperty(BridgeServiceProperty.MAX_PLAYERS).orElse(0);
            String map = serviceInfoSnapshot.getProperty(BridgeServiceProperty.MOTD).orElse("???")
                    .replace("A CloudNet V3 service", "???");

            String[] toSend = crimxPlayer.getPlayerSkinForChat(player.getName(), 8);
            toSend[0] = "\n" + toSend[0] + " §7§m----------------------------------";
            toSend[7] = toSend[7] + " §7§m----------------------------------";

            BaseComponent[] message = new BaseComponent[8];

            for (int i = 0; i < toSend.length; i++) {
                if(i == 0 || i == 7) {
                    message[i] = new TextComponent(TextComponent.fromLegacyText(toSend[i] + "\n"));
                } else if(i == 2) {
                    message[i] = new TextComponent(TextComponent.fromLegacyText(toSend[i] + " " +
                            permissionGroup.getDisplay().replace('&', '§') + player.getName() +
                            " §7spielt nun auf §a" + cloudPlayer.getConnectedService().getServerName() + " \n"));
                } else if(i == 3) {
                    BaseComponent baseComponents = new TextComponent(new ComponentBuilder(toSend[i] + " §7Klicke ")
                            .append("§e§l*hier*")
                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/joinme ?server=" + cloudPlayer.getConnectedService().getServerName()))
                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§eKlicke, um zu joinen")))
                            .append("§r§7, um zu joinen").color(ChatColor.GRAY)
                            .append(" \n")
                            .create());
                    message[i] = baseComponents;
                } else if(i == 5) {
                    message[i] = new TextComponent(TextComponent.fromLegacyText(toSend[i] +
                            String.format(" §7Spieler online: §a%s§8/§c%s §8● §7Map: §e%s\n", onlinePlayers, maxPlayers, map)));
                } else {
                    message[i] = new TextComponent(TextComponent.fromLegacyText(toSend[i] + "\n"));
                }
            }

            BaseComponentMessenger.broadcastMessage(message);

        } else if(args.length == 1) {
            String givenServer = args[0];

            if(!givenServer.startsWith("?server=")) {
                player.sendMessage(this.crimxAPI.getPrefix() + "§7Du hast einen ungültigen JoinMe Command ausgeführt");
                return false;
            } else {
                String serverToConnect = givenServer.split("=")[1];
                if(!serverToConnect.equalsIgnoreCase(cloudPlayer.getConnectedService().getServerName())){
                    cloudPlayer.getPlayerExecutor().connect(serverToConnect);
                    return true;
                } else {
                    player.sendMessage(this.crimxAPI.getPrefix() + "§7Du bist bereits auf dem JoinMe Server");
                    return false;
                }
            }
        }

        return true;
    }
}
