package de.fel1x.teamcrimx.crimxlobby.listeners.player;

import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.crimxapi.objects.CrimxPlayer;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.objects.LobbyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private CrimxLobby crimxLobby;

    public JoinListener(CrimxLobby crimxLobby) {
        this.crimxLobby = crimxLobby;
        this.crimxLobby.getPluginManager().registerEvents(this, this.crimxLobby);
    }

    @EventHandler
    public void on(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        event.setJoinMessage(null);

        player.teleport(new Location(player.getWorld(), -164, 64, 142));

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

        lobbyPlayer.spawnPersonalNPC();

        boolean vipPerms = player.hasPermission("crimxlobby.vip");
        int playerState = this.crimxLobby.getData().getPlayerHiderState().get(player.getUniqueId());

        Bukkit.getOnlinePlayers().forEach(loop -> {

            int state = this.crimxLobby.getData().getPlayerHiderState().get(loop.getUniqueId());

            switch (state) {
                case 1:
                    if(!vipPerms) {
                        loop.hidePlayer(player);
                    }
                    break;
                case 2:
                    loop.hidePlayer(player);
                    break;
            }

            switch (playerState) {
                case 1:
                    if(!loop.hasPermission("crimxlobby.vip")) {
                        player.hidePlayer(loop);
                    }
                    break;
                case 2:
                    player.hidePlayer(loop);
                    break;
            }

        });

    }

}
