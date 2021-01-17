package de.fel1x.teamcrimx.crimxlobby.listeners.player;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.IPermissionGroup;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import de.dytanic.cloudnet.ext.cloudperms.bukkit.BukkitCloudNetCloudPermissionsPlugin;
import de.fel1x.teamcrimx.crimxapi.clanSystem.player.ClanPlayer;
import de.fel1x.teamcrimx.crimxapi.clanSystem.player.IClanPlayer;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.crimxapi.objects.CrimxPlayer;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.objects.LobbyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class JoinListener implements Listener {

    private final CrimxLobby crimxLobby;

    public JoinListener(CrimxLobby crimxLobby) {
        this.crimxLobby = crimxLobby;
        this.crimxLobby.getPluginManager().registerEvents(this, this.crimxLobby);
    }

    @EventHandler
    public void on(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        event.setJoinMessage(null);

        player.setMetadata("iClanPlayer", new FixedMetadataValue(this.crimxLobby, new ClanPlayer(player.getUniqueId())));

        LobbyPlayer lobbyPlayer = new LobbyPlayer(player);
        CrimxPlayer crimxPlayer = new CrimxPlayer(lobbyPlayer.getCloudPlayer());

        lobbyPlayer.cleanUpPlayer();

        if (!crimxPlayer.checkIfPlayerExistsInCollection(player.getUniqueId(), MongoDBCollection.USERS)) {
            crimxPlayer.createPlayerData();
        } else {
            crimxPlayer.updateUserData();
        }

        if (!crimxPlayer.checkIfPlayerExistsInCollection(player.getUniqueId(), MongoDBCollection.LOBBY)) {
            lobbyPlayer.createPlayerData();
        }

        lobbyPlayer.loadMongoDocument();
        lobbyPlayer.initPlayerHider();
        lobbyPlayer.setLobbyInventory();
        lobbyPlayer.teleportToSpawn();

        lobbyPlayer.setScoreboard();
        BukkitCloudNetCloudPermissionsPlugin.getInstance().updateNameTags(player, player1 -> {
            IPermissionUser permissionUser = CloudNetDriver.getInstance().getPermissionManagement().getUser(player1.getUniqueId());
            IPermissionGroup permissionGroup = CloudNetDriver.getInstance().getPermissionManagement().getHighestPermissionGroup(permissionUser);

            IClanPlayer clanPlayer;

            if(player1.getMetadata("iClanPlayer").get(0) != null) {
                clanPlayer = (IClanPlayer) player1.getMetadata("iClanPlayer").get(0);
            } else {
                clanPlayer = new ClanPlayer(player1.getUniqueId());
            }

            if(clanPlayer.hasClan()) {
                permissionGroup.setSuffix(" §7[§e" + clanPlayer.getCurrentClan().getClanTag() + "§7]");
            }

            return permissionGroup;
        });

        boolean vipPerms = player.hasPermission("crimxlobby.vip");
        int playerState = this.crimxLobby.getData().getPlayerHiderState().get(player.getUniqueId());

        Bukkit.getOnlinePlayers().forEach(loop -> {

            int state = this.crimxLobby.getData().getPlayerHiderState().get(loop.getUniqueId());

            switch (state) {
                case 1:
                    if (!vipPerms) {
                        loop.hidePlayer(this.crimxLobby, player);
                    }
                    break;
                case 2:
                    loop.hidePlayer(this.crimxLobby, player);
                    break;
            }

            switch (playerState) {
                case 1:
                    if (!loop.hasPermission("crimxlobby.vip")) {
                        player.hidePlayer(this.crimxLobby, loop);
                    }
                    break;
                case 2:
                    player.hidePlayer(this.crimxLobby, loop);
                    break;
            }

        });

    }

}
