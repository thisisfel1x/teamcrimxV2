package de.fel1x.teamcrimx.crimxapi.friends.listener;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.event.EventListener;
import de.dytanic.cloudnet.ext.bridge.event.BridgeProxyPlayerDisconnectEvent;
import de.dytanic.cloudnet.ext.bridge.event.BridgeProxyPlayerLoginSuccessEvent;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.cosmetic.BaseCosmetic;
import de.fel1x.teamcrimx.crimxapi.cosmetic.CosmeticCategory;
import de.fel1x.teamcrimx.crimxapi.cosmetic.CosmeticRegistry;
import de.fel1x.teamcrimx.crimxapi.cosmetic.database.ActiveCosmetics;
import de.fel1x.teamcrimx.crimxapi.cosmetic.database.CosmeticPlayer;
import de.fel1x.teamcrimx.crimxapi.friends.database.FriendPlayer;
import de.fel1x.teamcrimx.crimxapi.support.CrimxSpigotAPI;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.UUID;

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
