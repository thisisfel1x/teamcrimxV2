package de.fel1x.teamcrimx.crimxlobby.listeners.player;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.IPermissionGroup;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    public ChatListener(CrimxLobby crimxLobby) {
        crimxLobby.getPluginManager().registerEvents(this, crimxLobby);
    }

    @EventHandler
    public void on(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();

        IPermissionUser iPermissionUser = CloudNetDriver.getInstance().getPermissionManagement().getUser(player.getUniqueId());

        if (iPermissionUser == null) return;

        IPermissionGroup permissionGroup = CloudNetDriver.getInstance().getPermissionManagement().getHighestPermissionGroup(iPermissionUser);
        event.setFormat(ChatColor.translateAlternateColorCodes('&', permissionGroup.getDisplay()) + player.getName() + " §8» §f" + event.getMessage());

        // TODO: clan tag in chat

    }
}
