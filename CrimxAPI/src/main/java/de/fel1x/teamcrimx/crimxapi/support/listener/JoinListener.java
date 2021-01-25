package de.fel1x.teamcrimx.crimxapi.support.listener;

import de.dytanic.cloudnet.CloudNet;
import de.dytanic.cloudnet.ext.bridge.node.CloudNetBridgeModule;
import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.clanSystem.player.ClanPlayer;
import de.fel1x.teamcrimx.crimxapi.clanSystem.player.IClanPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void on(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        String taskName = CrimxAPI.getInstance().getPlayerManager().getOnlinePlayer(player.getUniqueId()).getConnectedService().getTaskName();

        if(taskName != null && taskName.equalsIgnoreCase("Lobby")) {
            IClanPlayer clanPlayer = new ClanPlayer(player.getUniqueId());
            clanPlayer.sendClanRequestMessage();
        }
    }
}
