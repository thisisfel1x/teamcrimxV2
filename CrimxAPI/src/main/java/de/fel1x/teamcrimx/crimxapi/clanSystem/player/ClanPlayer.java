package de.fel1x.teamcrimx.crimxapi.clanSystem.player;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.clanSystem.clan.Clan;
import de.fel1x.teamcrimx.crimxapi.clanSystem.clan.IClan;
import de.fel1x.teamcrimx.crimxapi.clanSystem.constants.ClanRank;
import de.fel1x.teamcrimx.crimxapi.clanSystem.database.ClanDatabase;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.crimxapi.support.CrimxSpigotAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class ClanPlayer extends ClanDatabase implements IClanPlayer, Serializable {

    private final IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);
    private final String clanPrefix = CrimxAPI.getInstance().getClanPrefix();

    private final CrimxAPI crimxAPI = CrimxAPI.getInstance();
    private final CrimxSpigotAPI crimxSpigotAPI = CrimxSpigotAPI.getInstance();

    private final CompletableFuture<ArrayList<String>> clanPlayers = CompletableFuture.supplyAsync(this::getClanPlayersNameFormatted);

    private final UUID uuid;

    public ClanPlayer(UUID uuid) {
        super(CrimxAPI.getInstance());
        this.uuid = uuid;
    }

    @Override
    public boolean addToClan(UUID clanUniqueId) {

        return false;
    }

    @Override
    public boolean setClan(UUID clanUniqueId) {
        if(this.hasClan()) {
            return false;
        } else {
            this.insertAsyncInCollection("currentClan", clanUniqueId.toString(), this.getUUID().toString(), MongoDBCollection.USERS);
            return true;
        }
    }

    @Override
    public boolean removeFromClan(UUID clanUniqueId) {
        this.insertAsyncInCollection("currentClan", null, this.getUUID().toString(), MongoDBCollection.USERS);
        return true;
    }

    @Override
    public boolean isInClan(UUID clanUniqueId) {
        if(!hasClan()) {
            return false;
        }
        UUID joinedClanUUID = UUID.fromString((String) this.getObject("currentClan",
                this.getMongoDB().getUserCollection(), this.getUUID()));
        return joinedClanUUID.equals(clanUniqueId);
    }

    @Override
    public boolean hasClan() {
        return this.getObject("currentClan", this.getMongoDB().getUserCollection(), this.getUUID()) != null;
    }

    @Override
    public IClan getCurrentClan() {
        if(!hasClan()) {
            return null;
        }
        return new Clan(UUID.fromString((String) this.getObject("currentClan",
                this.getMongoDB().getUserCollection(), this.getUUID())));
    }

    @Override
    public IClanPlayer getByUUID(UUID playerUniqueId) {
        if(this.uuid.equals(playerUniqueId)) {
            return this;
        }
        return null;
    }

    @Override
    public ICloudPlayer getCloudPlayerByUUID(UUID playerUniqueId) {
        return this.playerManager.getOnlinePlayer(playerUniqueId);
    }

    @Override
    public Player getBukkitPlayerByUUID(UUID playerUniqueId) {
        return Bukkit.getPlayer(playerUniqueId);
    }

    @Override
    public IPermissionUser getCloudPermissionUser(UUID playerUniqueId) {
        return CloudNetDriver.getInstance().getPermissionManagement().getUser(playerUniqueId);
    }

    @Override
    public UUID getUUID() {
        return this.uuid;
    }

    @Override
    public void setNewRank(ClanRank clanRank) {

    }

    @Override
    public void sendMessage(String message, boolean prefix, UUID playerUniqueId) {
        this.playerManager.getPlayerExecutor(playerUniqueId)
                .sendChatMessage(prefix ? this.crimxAPI.getClanPrefix() + message : message);
    }

    @Override
    public void removeClanRequestAndAddToClan(IClan iClan) {
        this.setClan(iClan.getClanUniqueId());

        ArrayList<UUID> playerClanRequests = (ArrayList<UUID>) this.getObject("clanRequests",
                this.getMongoDB().getUserCollection(), this.getUUID());

        playerClanRequests.remove(iClan.getClanUniqueId());

        this.insertAsyncInCollection("clanRequests", playerClanRequests,
                this.getUUID().toString(), MongoDBCollection.USERS);
    }

    @Override
    public void sendMembersList() {
        if(!this.hasClan()) {
            this.sendMessage("§cDu bist in keinem Clan", true, this.getUUID());
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(String.format("%s§7Mitglieder des Clans §e%s §8(§6%s§8)", this.clanPrefix,
                    this.getCurrentClan().getClanName(), this.getCurrentClan().getClanTag()) + "\n");

            this.clanPlayers.thenAccept(list -> {
                for (String playerName : list) {
                    stringBuilder.append("      §8● §5").append(playerName).append("\n");
                }
                Objects.requireNonNull(this.getBukkitPlayerByUUID(this.getUUID())).sendMessage(stringBuilder.toString());
            });
        }
    }

    private ArrayList<String> getClanPlayersNameFormatted() {

        ArrayList<String> toReturn = new ArrayList<>();

        for (UUID clanMember : this.getCurrentClan().getClanMembers()) {
            toReturn.add(this.getCurrentClan().getFormattedUserName(clanMember));
        }

        return toReturn;

    }

}
