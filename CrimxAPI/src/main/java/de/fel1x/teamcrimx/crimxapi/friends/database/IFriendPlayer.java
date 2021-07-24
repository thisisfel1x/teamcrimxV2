package de.fel1x.teamcrimx.crimxapi.friends.database;

import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.fel1x.teamcrimx.crimxapi.friends.FriendSettings;
import de.fel1x.teamcrimx.crimxapi.friends.InventoryFriend;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IFriendPlayer {

    /**
     * Creates required data in database if not exist
     */
    void createPlayerData();

    /**
     *
     */
    CompletableFuture<Boolean> sendFriendRequest(UUID friendUUID);

    /**
     *
     */
    CompletableFuture<Boolean> addFriend(UUID friendUUID);

    /**
     *
     */
    CompletableFuture<Boolean> removeFriend(UUID friendUUID);

    /**
     * List every friends
     *
     * @param page friend page
     */
    void listFriends(int page);

    /**
     *
     */
    CompletableFuture<Boolean> acceptFriendRequest(UUID friendUUID);

    /**
     *
     */
    CompletableFuture<Boolean> denyFriendRequest(UUID friendUUID);

    /**
     * Denies all open friend requests
     */
    CompletableFuture<Boolean> denyAllRequests();

    /**
     *
     */
    boolean areAlreadyFriends(UUID targetUUID);

    /**
     *
     */
    boolean areAlreadyFriends(UUID player, UUID targetUUID);

    /**
     *
     */
    boolean hasOpenFriendRequest(UUID targetUUID);

    /**
     *
     */
    boolean hasOpenFriendRequest(UUID player, UUID targetUUID);

    /**
     *
     */
    boolean removeFriendRequest(UUID targetUUID);

    /**
     *
     */
    String getNameFromUUID(UUID targetUUID);

    /**
     *
     */
    ICloudPlayer getOnlinePlayer(UUID targetUUID);

    /**
     *
     */
    void sendMessage(String message, UUID player);

    /**
     *
     */
    String getConnectedServerName(UUID targetUUID);

    /**
     *
     */
    CompletableFuture<ArrayList<InventoryFriend>> getFriends();

    /**
     *
     */
    boolean getSetting(UUID targetUUID, FriendSettings friendSettings);

    /**
     *
     */
    void jumpToFriend(UUID targetUUID, boolean force);

    ArrayList<UUID> getOnlineFriends();

    /**
     *
     */
    CompletableFuture<Boolean> sendOnlineFriendsMessageOnProxyJoinAsync();

    /**
     * @return
     */
    CompletableFuture<Boolean> notifyOnlineFriendsOnQuit();
}
