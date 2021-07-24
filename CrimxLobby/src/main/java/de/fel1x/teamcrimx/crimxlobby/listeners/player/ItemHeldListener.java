package de.fel1x.teamcrimx.crimxlobby.listeners.player;

import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.objects.LobbyPlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;

@Deprecated()
public class ItemHeldListener implements Listener {

    private final CrimxLobby crimxLobby;

    public ItemHeldListener(CrimxLobby crimxLobby) {
        this.crimxLobby = crimxLobby;
        this.crimxLobby.getPluginManager().registerEvents(this, this.crimxLobby);
    }

    @EventHandler
    public void on(PlayerItemHeldEvent event) {

        Player player = event.getPlayer();
        LobbyPlayer lobbyPlayer = new LobbyPlayer(player);

        boolean hotbarSound = this.crimxLobby.getData().getLobbyDatabasePlayer().get(player.getUniqueId()).isHotbarSoundEnabled();

        if (hotbarSound && !lobbyPlayer.isInBuild()) {
            player.playSound(player.getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 3f, 2.5f);
        }

    }

}
