package de.fel1x.teamcrimx.crimxlobby.listeners.player;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.IPermissionGroup;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import net.labymod.serverapi.bukkit.event.LabyModPlayerJoinEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scoreboard.Team;

public class LabyModPlayerJoinListener implements Listener {

    private CrimxLobby crimxLobby;

    public LabyModPlayerJoinListener(CrimxLobby crimxLobby) {
        this.crimxLobby = crimxLobby;
        this.crimxLobby.getPluginManager().registerEvents(this, this.crimxLobby);
    }

    @EventHandler
    public void on(LabyModPlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.setMetadata("labymod", new FixedMetadataValue(this.crimxLobby, true));

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!onlinePlayer.hasMetadata("labymod")) {
                continue;
            }

            IPermissionUser iPermissionUser = CloudNetDriver.getInstance().getPermissionManagement().getUser(onlinePlayer.getUniqueId());
            if (iPermissionUser == null) return;

            IPermissionGroup permissionGroup = CloudNetDriver.getInstance().getPermissionManagement().getHighestPermissionGroup(iPermissionUser);

            int sortIdInt = permissionGroup.getSortId();

            String sortId;

            if (sortIdInt < 10) {
                sortId = String.format("%02d", sortIdInt);
            } else {
                sortId = String.valueOf(sortIdInt);
            }

            String playerPermGroup = sortId + permissionGroup.getName();

            if (playerPermGroup.length() > 16) {
                playerPermGroup = playerPermGroup.substring(0, 16);
            }

            Team team = onlinePlayer.getScoreboard().getTeam(playerPermGroup);

            if (team == null) {
                team = onlinePlayer.getScoreboard().registerNewTeam(playerPermGroup);
            }

            String color = permissionGroup.getColor().replace('&', '§');
            String prefix = permissionGroup.getPrefix().replace('&', '§');
            String suffix = color + " ✔";

            team.setPrefix(prefix);
            team.setSuffix(suffix);
            team.addEntry(onlinePlayer.getName());

            onlinePlayer.setDisplayName(prefix + color + onlinePlayer.getName() + suffix);
            onlinePlayer.setPlayerListName(prefix + color + onlinePlayer.getName() + suffix);
        }

    }

}
