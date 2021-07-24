package de.fel1x.teamcrimx.crimxapi.friends.listener;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.event.EventListener;
import de.dytanic.cloudnet.ext.bridge.event.BridgeProxyPlayerDisconnectEvent;
import de.dytanic.cloudnet.ext.bridge.event.BridgeProxyPlayerLoginSuccessEvent;
import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.friends.database.FriendPlayer;
import de.fel1x.teamcrimx.crimxapi.support.CrimxSpigotAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.Objects;

public class FriendListener implements Listener {

    public FriendListener() {
        CloudNetDriver.getInstance().getEventManager().registerListener(this);
    }

    @EventListener
    public void on(BridgeProxyPlayerLoginSuccessEvent event) {
        Bukkit.getScheduler().runTaskLater(CrimxSpigotAPI.getInstance(),
                () -> new FriendPlayer(event.getNetworkConnectionInfo().getUniqueId())
                        .sendOnlineFriendsMessageOnProxyJoinAsync().thenAccept(success -> {
                            if (!success) {
                                Objects.requireNonNull(CrimxAPI.getInstance().getPlayerManager()
                                        .getOnlinePlayer(event.getNetworkConnectionInfo().getUniqueId()))
                                        .getPlayerExecutor().sendChatMessage(CrimxAPI.getInstance().getFriendPrefix()
                                        + "§cEin Fehler ist aufgetreten");
                            }
                        }), 10L);
    }

    @EventListener
    public void on(BridgeProxyPlayerDisconnectEvent event) {
        new FriendPlayer(event.getNetworkConnectionInfo().getUniqueId()).notifyOnlineFriendsOnQuit()
                .thenAccept(success ->
                Objects.requireNonNull(CrimxAPI.getInstance().getPlayerManager()
                .getOfflinePlayer(event.getNetworkConnectionInfo().getUniqueId())).getProperties().remove("friend"));
    }

}
