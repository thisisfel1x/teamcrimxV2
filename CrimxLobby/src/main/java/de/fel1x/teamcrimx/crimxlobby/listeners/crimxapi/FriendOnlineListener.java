package de.fel1x.teamcrimx.crimxlobby.listeners.crimxapi;

import de.fel1x.teamcrimx.crimxapi.friends.database.FriendPlayer;
import de.fel1x.teamcrimx.crimxapi.friends.events.FriendOnlineEvent;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class FriendOnlineListener implements Listener {

    private final CrimxLobby crimxLobby;

    public FriendOnlineListener(CrimxLobby crimxLobby) {
        this.crimxLobby = crimxLobby;
        this.crimxLobby.getPluginManager().registerEvents(this, this.crimxLobby);
    }

    @EventHandler
    public void on(FriendOnlineEvent event) {
        for (UUID onlineFriendUUID : event.getFriendPlayer().getOnlineFriends()) {
            Player player = Bukkit.getPlayer(onlineFriendUUID);
            if (player == null) {
                continue;
            }
            FriendPlayer friendPlayer = new FriendPlayer(onlineFriendUUID);
            int onlineFriendsCount = friendPlayer.getOnlineFriendsCount();
            int totalFriendsCount = friendPlayer.getTotalFriendsCount();

            this.crimxLobby.getLobbyScoreboard().updateBoard(player,
                    Component.text("●", NamedTextColor.DARK_GRAY)
                            .append(Component.space()).append(Component.text(onlineFriendsCount, NamedTextColor.GREEN)
                            .append(Component.text("/", NamedTextColor.DARK_GRAY)
                                    .append(Component.text(totalFriendsCount, NamedTextColor.GRAY))
                                    .asComponent().decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))),
                    "friends");
        }

    }

}
