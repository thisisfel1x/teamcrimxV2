package de.fel1x.teamcrimx.crimxlobby.listeners.player;

import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.minigames.connectfour.objects.Game;
import de.fel1x.teamcrimx.crimxlobby.objects.LobbyPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    private final CrimxLobby crimxLobby;

    public QuitListener(CrimxLobby crimxLobby) {
        this.crimxLobby = crimxLobby;
        crimxLobby.getPluginManager().registerEvents(this, crimxLobby);
    }

    @EventHandler
    public void on(PlayerQuitEvent event) {

        Player player = event.getPlayer();
        LobbyPlayer lobbyPlayer = new LobbyPlayer(player);

        event.quitMessage(null);

        if(this.crimxLobby.getConnectFourGameManager().isPlaying(player)) {
            this.crimxLobby.getConnectFourGameManager().getGame(player).stopGame(player, Game.FinishReason.PLAYER_LEFT_MATCH);
        }

        lobbyPlayer.saveNewLocation();

        if (lobbyPlayer.isInBuild()) {
            lobbyPlayer.removeFromBuild();
        }

    }

}
