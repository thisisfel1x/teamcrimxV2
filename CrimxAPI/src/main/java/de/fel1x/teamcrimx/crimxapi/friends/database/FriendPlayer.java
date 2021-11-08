package de.fel1x.teamcrimx.crimxapi.friends.database;

import de.dytanic.cloudnet.ext.bridge.AdventureComponentMessenger;
import de.dytanic.cloudnet.ext.bridge.player.ICloudOfflinePlayer;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.crimxapi.friends.FriendSettings;
import de.fel1x.teamcrimx.crimxapi.friends.InventoryFriend;
import de.fel1x.teamcrimx.crimxapi.friends.events.FriendOfflineEvent;
import de.fel1x.teamcrimx.crimxapi.friends.events.FriendOnlineEvent;
import de.fel1x.teamcrimx.crimxapi.server.ServerType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class FriendPlayer implements IFriendPlayer {

    private final CrimxAPI crimxAPI = CrimxAPI.getInstance();
    private final String friendPrefix = this.crimxAPI.getFriendPrefix();

    private final UUID uuid;

    public FriendPlayer(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public void createPlayerData() {
        Document friendDocument = new Document("_id", this.uuid.toString());

        Document personalSettings = new Document();
        for (FriendSettings friendSettings : FriendSettings.values()) {
            personalSettings.put(friendSettings.name(), true);
        }

        friendDocument.append("friends", new ArrayList<String>())
                .append("friendRequests", new ArrayList<String>())
                .append("settings", personalSettings);
        this.crimxAPI.getMongoDB().insertDocumentInCollectionSync(friendDocument, MongoDBCollection.FRIENDS);
    }

    @Override
    public CompletableFuture<Boolean> sendFriendRequest(UUID friendUUID) {
        return CompletableFuture.supplyAsync(() -> {
            //Step 1: Insert player uuid to requested player requests
            ArrayList<String> friendRequests = this.crimxAPI.getMongoDB()
                    .getStringArrayListFromDocumentSync(friendUUID, MongoDBCollection.FRIENDS, "friendRequests");
            friendRequests.add(this.uuid.toString());

            // Step 2: Check if target player is currently online and send him a message
            ICloudPlayer cloudPlayer = this.crimxAPI.getPlayerManager().getOnlinePlayer(friendUUID);
            ICloudOfflinePlayer requestPlayer = this.crimxAPI.getPlayerManager().getOfflinePlayer(this.uuid);
            if (cloudPlayer != null && requestPlayer != null) {
                TextComponent info = Component.text(this.friendPrefix)
                        .append(Component.text("§7Du hast eine Freundschaftsanfrage von "))
                        .append(Component.text(requestPlayer.getName(), NamedTextColor.YELLOW))
                        .append(Component.text(" §7erhalten"))
                        .append(Component.newline())
                        .append(Component.text(this.friendPrefix + "§7Klicke zum "))
                        .append(Component.text("§a§lANNEHMEN")
                                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/friend ?add=" + this.uuid)))
                        .append(Component.text(" §8● "))
                        .append(Component.text("§c§lABLEHNEN")
                                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/friend ?deny=" + this.uuid)));

                //BaseComponentMessenger.sendMessage(friendUUID, ComponentSerializer.parse(GsonComponentSerializer.gson().serialize(info)));
                AdventureComponentMessenger.sendMessage(cloudPlayer, info);
            }
            return this.crimxAPI.getMongoDB().insertObjectInDocument(friendUUID, MongoDBCollection.FRIENDS,
                    "friendRequests", friendRequests);
        });
    }

    @Override
    public CompletableFuture<Boolean> addFriend(UUID friendUUID) {
        return CompletableFuture.supplyAsync(() -> {
            if (!this.removeFriendRequest(friendUUID)) {
                return false;
            }

            // Step 2: put uuid in both tables
            if (!this.areAlreadyFriends(this.uuid, friendUUID)) {
                ArrayList<String> friendList = this.crimxAPI.getMongoDB()
                        .getStringArrayListFromDocumentSync(this.uuid, MongoDBCollection.FRIENDS, "friends");
                friendList.add(friendUUID.toString());
                this.crimxAPI.getMongoDB().insertObjectInDocument(this.uuid, MongoDBCollection.FRIENDS,
                        "friends", friendList);
            }
            if (!this.areAlreadyFriends(friendUUID, this.uuid)) {
                ArrayList<String> friendList1 = this.crimxAPI.getMongoDB()
                        .getStringArrayListFromDocumentSync(friendUUID, MongoDBCollection.FRIENDS, "friends");
                friendList1.add(this.uuid.toString());
                this.crimxAPI.getMongoDB().insertObjectInDocument(friendUUID, MongoDBCollection.FRIENDS,
                        "friends", friendList1);
            }

            // Step 3: Send message
            this.sendMessage(this.friendPrefix + "§7Du hast die " +
                    "Freundschaftsanfrage von §e" + this.getNameFromUUID(friendUUID) + " §7akzeptiert", this.uuid);
            this.sendMessage(this.friendPrefix + "§7Du bist nun mit §e"
                    + this.getNameFromUUID(this.uuid) + " §7befreundet", friendUUID);

            return true;
        });
    }

    @Override
    public CompletableFuture<Boolean> removeFriend(UUID friendUUID) {
        return CompletableFuture.supplyAsync(() -> {
            // Step 1: Remove UUID from both friend lists
            if (this.areAlreadyFriends(this.uuid, friendUUID)) {
                ArrayList<String> friendList = this.crimxAPI.getMongoDB()
                        .getStringArrayListFromDocumentSync(this.uuid, MongoDBCollection.FRIENDS, "friends");
                friendList.remove(friendUUID.toString());
                this.crimxAPI.getMongoDB().insertObjectInDocument(this.uuid, MongoDBCollection.FRIENDS,
                        "friends", friendList);
            }
            if (this.areAlreadyFriends(friendUUID, this.uuid)) {
                ArrayList<String> friendList1 = this.crimxAPI.getMongoDB()
                        .getStringArrayListFromDocumentSync(friendUUID, MongoDBCollection.FRIENDS, "friends");
                friendList1.remove(this.uuid.toString());
                this.crimxAPI.getMongoDB().insertObjectInDocument(friendUUID, MongoDBCollection.FRIENDS, "friends",
                        friendList1);
            }

            // Step 2: Send message
            String playerNameWhoRemovedFriend = Objects.requireNonNull(this.crimxAPI.getPlayerManager().getOfflinePlayer(this.uuid)).getName();
            String removedPlayerName = Objects.requireNonNull(this.crimxAPI.getPlayerManager().getOfflinePlayer(friendUUID)).getName();

            this.sendMessage(this.friendPrefix + "§7Deine Freundschaft mit §e"
                    + removedPlayerName + " §7wurde aufgelöst", this.uuid);

            this.sendMessage(this.friendPrefix + "§e" + playerNameWhoRemovedFriend
                    + " §7hat eure Freundschaft aufgelöst", friendUUID);
            return true;
        });
    }

    @Override
    public void listFriends(int page) {

    }

    @Override
    public CompletableFuture<Boolean> acceptFriendRequest(UUID friendUUID) {
        return this.addFriend(friendUUID);
    }

    @Override
    public CompletableFuture<Boolean> denyFriendRequest(UUID friendUUID) {
        return CompletableFuture.supplyAsync(() -> {
            // Step 1: remove friend requests
            this.removeFriendRequest(friendUUID);

            // Step 2: send message
            String playerNameWhoDeniedRequest = this.getNameFromUUID(this.uuid);
            String requestPlayerName = this.getNameFromUUID(friendUUID);

            // TODO: prefix
            this.sendMessage(this.friendPrefix + "§7Du hast die Freundschaftsanfrage von §e"
                    + requestPlayerName + " §7abgelehnt", this.uuid);
            this.sendMessage(this.friendPrefix + "§e" + playerNameWhoDeniedRequest + " §7hat deine " +
                    "Freundschaftsanfrage abgelehnt", friendUUID);

            return true;
        });
    }

    @Override
    public CompletableFuture<Boolean> denyAllRequests() {
        return CompletableFuture.supplyAsync(() -> this.crimxAPI.getMongoDB()
                .insertObjectInDocument(this.uuid, MongoDBCollection.FRIENDS,
                        "friendRequests", new ArrayList<String>()));
    }

    @Override
    public boolean areAlreadyFriends(UUID targetUUID) {
        return this.areAlreadyFriends(this.uuid, targetUUID);
    }

    @Override
    public boolean areAlreadyFriends(UUID player, UUID targetUUID) {
        return this.crimxAPI.getMongoDB().getStringArrayListFromDocumentSync(player,
                MongoDBCollection.FRIENDS, "friends").contains(targetUUID.toString());
    }

    @Override
    public boolean hasOpenFriendRequest(UUID targetUUID) {
        return this.hasOpenFriendRequest(this.uuid, targetUUID);
    }

    @Override
    public boolean hasOpenFriendRequest(UUID player, UUID targetUUID) {
        return this.crimxAPI.getMongoDB().getStringArrayListFromDocumentSync(player,
                MongoDBCollection.FRIENDS, "friendRequests").contains(targetUUID.toString());
    }

    @Override
    public boolean removeFriendRequest(UUID friendUUID) {
        if (!this.hasOpenFriendRequest(this.uuid, friendUUID) && this.hasOpenFriendRequest(friendUUID, this.uuid)) {
            return false;
        }

        ArrayList<String> friendRequests = this.crimxAPI.getMongoDB()
                .getStringArrayListFromDocumentSync(this.uuid, MongoDBCollection.FRIENDS, "friendRequests");
        friendRequests.remove(friendUUID.toString());
        this.crimxAPI.getMongoDB().insertObjectInDocument(this.uuid, MongoDBCollection.FRIENDS,
                "friendRequests", friendRequests);

        ArrayList<String> friendRequests1 = this.crimxAPI.getMongoDB()
                .getStringArrayListFromDocumentSync(friendUUID, MongoDBCollection.FRIENDS, "friendRequests");
        friendRequests1.remove(this.uuid.toString());
        this.crimxAPI.getMongoDB().insertObjectInDocument(friendUUID, MongoDBCollection.FRIENDS,
                "friendRequests", friendRequests1);
        return true;
    }

    @Override
    public String getNameFromUUID(UUID targetUUID) {
        return Objects.requireNonNull(this.crimxAPI.getPlayerManager().getOfflinePlayer(targetUUID)).getName();
    }

    @Override
    public ICloudPlayer getOnlinePlayer(UUID targetUUID) {
        return this.crimxAPI.getPlayerManager().getOnlinePlayer(targetUUID);
    }

    @Override
    public void sendMessage(String message, UUID player) {
        /*ICloudPlayer cloudPlayer = this.getOnlinePlayer(player);
        if (cloudPlayer != null) {
            cloudPlayer.getPlayerExecutor().sendChatMessage(message);
        }*/
        Player player1 = Bukkit.getPlayer(player);
        if (player1 != null) {
            player1.sendMessage(message); // TODO: fix duplicated messages caused by multiple API initialization
        }
    }

    @Override
    public String getConnectedServerName(UUID targetUUID) {
        ICloudPlayer cloudPlayer = this.getOnlinePlayer(targetUUID);

        if (cloudPlayer == null) {
            return "N/A";
        }

        return cloudPlayer.getConnectedService().getServerName();
    }

    @Override
    public CompletableFuture<ArrayList<InventoryFriend>> getFriends() {
        return CompletableFuture.supplyAsync(() -> {
            ArrayList<InventoryFriend> inventoryFriends = new ArrayList<>();

            ArrayList<String> friends = this.crimxAPI.getMongoDB().getStringArrayListFromDocumentSync(this.uuid,
                    MongoDBCollection.FRIENDS, "friends");

            if (!friends.isEmpty()) {
                for (String friendUUIDString : friends) {
                    UUID friendUUID = UUID.fromString(friendUUIDString);

                    InventoryFriend inventoryFriend = new InventoryFriend(
                            friendUUID,
                            this.getNameFromUUID(friendUUID),
                            this.getOnlinePlayer(friendUUID) != null,
                            this.getConnectedServerName(friendUUID)
                    );
                    inventoryFriends.add(inventoryFriend);

                }
            }
            // SORT
            inventoryFriends = inventoryFriends.stream()
                    .sorted((friend1, friend2) -> Boolean.compare(friend1.isOnline(), friend2.isOnline()))
                    .collect(Collectors.toCollection(ArrayList::new));
            Collections.reverse(inventoryFriends);

            return inventoryFriends;
        });
    }

    @Override
    public boolean getSetting(UUID targetUUID, FriendSettings friendSettings) {
        return (boolean) ((Document) this.crimxAPI.getMongoDB()
                .getDocumentSync(targetUUID, MongoDBCollection.FRIENDS).get("settings")).get(friendSettings.name());
    }

    @Override
    public void jumpToFriend(UUID targetUUID, boolean force) {
        if (!force) {
            if (!this.getSetting(targetUUID, FriendSettings.CAN_JUMP_TO_FRIEND)) {
                this.sendMessage(this.friendPrefix + "§cDu kannst nicht zu diesem Spieler springen", this.uuid);
                return;
            }
        }

        ICloudPlayer targetPlayer = this.getOnlinePlayer(targetUUID);
        ICloudPlayer player = this.getOnlinePlayer(this.uuid);
        if (targetPlayer == null || player == null) {
            Bukkit.getServer().sendMessage(Component.text("fehler", NamedTextColor.YELLOW));
            return;
        }

        if (targetPlayer.getConnectedService().getServerName()
                .equalsIgnoreCase(player.getConnectedService().getServerName())) {
            // TODO: prefix
            this.sendMessage(this.friendPrefix + "§cDu befindest dich bereits auf dem gleichen Server von §e"
                    + this.getNameFromUUID(targetUUID) + " " +
                    "§8(§a" + targetPlayer.getConnectedService().getServerName() + "§8)", this.uuid);

            // TODO: unsafe
            Objects.requireNonNull(Bukkit.getPlayer(this.uuid)).teleport(Objects.requireNonNull(Bukkit.getPlayer(targetUUID)));
            return;
        }
        this.getOnlinePlayer(this.uuid).getPlayerExecutor().connect(targetPlayer.getConnectedService().getServerName());
    }

    @Override
    public ArrayList<UUID> getOnlineFriends() {
        ArrayList<UUID> uuidArrayList = new ArrayList<>();
        try {
            this.crimxAPI.getMongoDB().getStringArrayListFromDocumentSync(this.uuid, MongoDBCollection.FRIENDS, "friends")
                    .forEach(string -> uuidArrayList.add(UUID.fromString(string)));

            return uuidArrayList.stream().filter(playerUUID -> this.getOnlinePlayer(playerUUID) != null)
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception ignored) {
            return uuidArrayList;
        }
    }

    @Override
    public CompletableFuture<Boolean> sendOnlineFriendsMessageOnProxyJoinAsync() {
        return CompletableFuture.supplyAsync(() -> {

            if (this.crimxAPI.getServerType() != ServerType.LOBBY_SERVER) {
                return false;
            }

            ArrayList<UUID> onlineFriendsUUIDs = this.getOnlineFriends();

            if (onlineFriendsUUIDs == null) {
                return false;
            }

            for (UUID onlineFriendUUID : onlineFriendsUUIDs) {
                ICloudPlayer onlineFriend = this.getOnlinePlayer(onlineFriendUUID);
                if (onlineFriend == null) {
                    continue;
                }
                // TODO: prefix
                this.sendMessage(this.friendPrefix + "§e" + this.getNameFromUUID(this.uuid) + " §7ist nun §aonline",
                        onlineFriendUUID);
            }

            switch (onlineFriendsUUIDs.size()) {
                case 0 -> this.sendMessage(this.friendPrefix + "§7Keine deiner Freunde sind online", this.uuid);
                case 1 ->
                        // TODO: prefix
                        this.sendMessage(String.format("%s§7Momentan ist nur §e%s §7online", this.friendPrefix,
                                this.getNameFromUUID(onlineFriendsUUIDs.get(0))), this.uuid);
                case 2 -> this.sendMessage(String.format("%s§7Momentan sind nur §e%s §7und §e%s §7online", this.friendPrefix,
                        this.getNameFromUUID(onlineFriendsUUIDs.get(0)),
                        this.getNameFromUUID(onlineFriendsUUIDs.get(1))), this.uuid);
                case 3 -> this.sendMessage(String.format("%s§7Momentan sind nur §e%s§7, §e%s §7und §e%s §7online", this.friendPrefix,
                        this.getNameFromUUID(onlineFriendsUUIDs.get(0)),
                        this.getNameFromUUID(onlineFriendsUUIDs.get(1)),
                        this.getNameFromUUID(onlineFriendsUUIDs.get(2))), this.uuid);
                default -> this.sendMessage(String.format("%s§7Momentan sind nur §e%s§7, §e%s §7, §e%s " +
                                "und §e%s §7weitere Freunde online", this.friendPrefix,
                        this.getNameFromUUID(onlineFriendsUUIDs.get(0)),
                        this.getNameFromUUID(onlineFriendsUUIDs.get(1)),
                        this.getNameFromUUID(onlineFriendsUUIDs.get(2)),
                        onlineFriendsUUIDs.size() - 3), this.uuid);
            }
            Bukkit.getPluginManager().callEvent(new FriendOnlineEvent(true, this));
            return true;
        });
    }

    @Override
    public CompletableFuture<Boolean> notifyOnlineFriendsOnQuit() {
        return CompletableFuture.supplyAsync(() -> {
            ArrayList<UUID> onlineFriendsUUIDs = this.getOnlineFriends();

            if (onlineFriendsUUIDs == null) {
                return false;
            }

            for (UUID onlineFriendUUID : onlineFriendsUUIDs) {
                ICloudPlayer onlineFriend = this.getOnlinePlayer(onlineFriendUUID);
                if (onlineFriend == null) {
                    continue;
                }
                // TODO: prefix
                this.sendMessage(this.friendPrefix + "§e" + this.getNameFromUUID(this.uuid) + " §7ist nun §coffline",
                        onlineFriendUUID);
            }
            Bukkit.getPluginManager().callEvent(new FriendOfflineEvent(this));
            return true;
        });
    }

    @Override
    public int getOnlineFriendsCount() {
        return this.getOnlineFriends().size();
    }

    @Override
    public int getTotalFriendsCount() {
        return this.crimxAPI.getMongoDB()
                .getStringArrayListFromDocumentSync(this.uuid, MongoDBCollection.FRIENDS, "friends").size();
    }
}
