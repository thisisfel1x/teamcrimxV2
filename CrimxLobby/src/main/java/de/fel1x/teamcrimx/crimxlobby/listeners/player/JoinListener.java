package de.fel1x.teamcrimx.crimxlobby.listeners.player;

import de.dytanic.cloudnet.ext.cloudperms.bukkit.BukkitCloudNetCloudPermissionsPlugin;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.crimxapi.objects.CrimxPlayer;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.objects.LobbyPlayer;
import de.fel1x.teamcrimx.crimxlobby.objects.Spawn;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Bee;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

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
        BukkitCloudNetCloudPermissionsPlugin.getInstance().updateNameTags(player);

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
