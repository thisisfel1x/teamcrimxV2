package de.fel1x.teamcrimx.crimxapi.clanSystem.clan;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.IPermissionGroup;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import de.dytanic.cloudnet.ext.bridge.BaseComponentMessenger;
import de.dytanic.cloudnet.ext.bridge.player.ICloudOfflinePlayer;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.clanSystem.constants.ClanKickReason;
import de.fel1x.teamcrimx.crimxapi.clanSystem.database.ClanDatabase;
import de.fel1x.teamcrimx.crimxapi.clanSystem.events.ClanUpdateEvent;
import de.fel1x.teamcrimx.crimxapi.clanSystem.player.ClanPlayer;
import de.fel1x.teamcrimx.crimxapi.clanSystem.player.IClanPlayer;
import de.fel1x.teamcrimx.crimxapi.clanSystem.constants.ClanRank;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.TextComponent;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.UUID;

public class Clan extends ClanDatabase implements IClan {

    private final IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);

    private final UUID clanUniqueId;

    private String clanTag;
    private String clanName;
    private ArrayList<UUID> clanPlayers = new ArrayList<>();
    private UUID clanOwnerUniqueId;

    public Clan(UUID clanUniqueId) {
        super(CrimxAPI.getInstance());
        this.clanUniqueId = clanUniqueId;

        // TODO: do this async!
        this.fetchDataSync();
    }

    public Clan(UUID clanUniqueId, boolean shouldFetchData) {
        super(CrimxAPI.getInstance());
        this.clanUniqueId = clanUniqueId;

        if(shouldFetchData) {
            this.fetchDataSync();
        }
    }

    private void fetchDataSync() {
        this.clanTag = (String) this.getObject("clanTag", this.getMongoDB().getClanCollection(), this.clanUniqueId);
        this.clanName = (String) this.getObject("clanName", this.getMongoDB().getClanCollection(), this.clanUniqueId);
        this.clanPlayers = (ArrayList<UUID>) this.getObject("members", this.getMongoDB().getClanCollection(),
                this.clanUniqueId);
        this.clanOwnerUniqueId = UUID.fromString((String) this.getObject("owner",
                this.getMongoDB().getClanCollection(), this.clanUniqueId));
    }

    @Override
    public int getTotalClanMembers() {
        return this.clanPlayers.size();
    }

    @Override
    public ArrayList<UUID> getClanMembers() {
        return this.clanPlayers;
    }

    @Override
    public boolean addPlayerToClan(IClanPlayer iClanPlayer) {
        if(this.clanPlayers.contains(iClanPlayer.getUUID())) {
            return false;
        }

        this.clanPlayers.add(iClanPlayer.getUUID());
        this.insertAsyncInCollection("members", this.clanPlayers,
                this.clanUniqueId.toString(), MongoDBCollection.CLAN);
        iClanPlayer.removeClanRequestAndAddToClan(this);
        return true;
    }

    @Override
    public boolean removePlayerFromClan(IClanPlayer iClanPlayer) {
        this.clanPlayers.remove(iClanPlayer.getUUID());
        this.insertAsyncInCollection("members", this.clanPlayers,
                this.clanUniqueId.toString(), MongoDBCollection.CLAN);
        iClanPlayer.removeFromClan(this.clanUniqueId);
        return true;
    }

    @Override
    public boolean removePlayerFromClanByName(String playerName) {
        ICloudOfflinePlayer cloudOfflinePlayer = this.playerManager.getFirstOfflinePlayer(playerName);
        if(cloudOfflinePlayer == null) {
            return false;
        }

        IClanPlayer clanPlayer = new ClanPlayer(cloudOfflinePlayer.getUniqueId());

        return this.removePlayerFromClan(clanPlayer);
    }

    @Override
    public boolean kickPlayerFromClan(IClanPlayer iClanPlayer, ClanKickReason clanKickReason) {
        return this.removePlayerFromClan(iClanPlayer);
    }

    @Override
    public boolean isPlayerInClan(IClanPlayer iClanPlayer) {
        return this.clanPlayers.contains(iClanPlayer.getUUID());
    }

    @Override
    public boolean setNewRank(IClanPlayer iClanPlayer, ClanRank clanRank) {
        iClanPlayer.setNewRank(clanRank);

        int index = this.clanPlayers.indexOf(iClanPlayer.getUUID());

        this.clanPlayers.remove(iClanPlayer.getUUID());
        this.clanPlayers.add(index, iClanPlayer.getUUID());

        return true;
    }

    @Override
    public ClanRank getPlayerRank(IClanPlayer iClanPlayer) {
        return null;
    }

    @Override
    public boolean createClan(String clanName, String clanTag, Material clanItem, IClanPlayer iClanPlayer) {

        if(this.clanTagAlreadyTaken(clanTag)) {
            iClanPlayer.sendMessage(String.format("§cEin Clan mit dem gewählten Tag §e(%s) §cexistiert bereits!", clanTag),
                    true, iClanPlayer.getUUID());
            return false;
        }

        UUID clanUUID = UUID.randomUUID();

        this.clanPlayers.add(iClanPlayer.getUUID());

        IClanPlayer owner = new ClanPlayer(iClanPlayer.getUUID());
        owner.setClan(clanUUID);

        Document clanDocument = new Document("_id", clanUUID.toString())
                .append("clanName", clanName)
                .append("clanTag", clanTag)
                .append("clanItem", clanItem.name())
                .append("owner", iClanPlayer.getUUID().toString())
                .append("members", this.getClanMembers());
        this.insertAsyncInClanCollection(clanDocument);

        Bukkit.getPluginManager().callEvent(new ClanUpdateEvent(owner));

        return true;
    }

    @Override
    public boolean deleteClan() {
        return false;
    }

    @Override
    public String getClanName() {
        return this.clanName;
    }

    @Override
    public boolean setClanName(String newClanName) {
        return false;
    }

    @Override
    public String getClanTag() {
        return this.clanTag;
    }

    @Override
    public boolean setClanTag(String newClanTag) {
        return false;
    }

    @Override
    public Material getClanMaterial() {
        return null;
    }

    @Override
    public boolean setClanMaterial(Material newClanMaterial) {
        return false;
    }

    @Override
    public UUID getClanUniqueId() {
        return this.clanUniqueId;
    }

    @Override
    public ICloudOfflinePlayer getOfflinePlayer(UUID playerUniqueId) {
        return this.playerManager.getOfflinePlayer(playerUniqueId);
    }

    @Override
    public UUID getClanOwner() {
        return this.clanOwnerUniqueId;
    }

    @Override
    public IPermissionUser getCloudPermissionUser(UUID playerUniqueId) {
        return CloudNetDriver.getInstance().getPermissionManagement().getUser(playerUniqueId);
    }

    @Override
    public String getFormattedUserName(UUID playerUniqueId) {
        IPermissionGroup permissionGroup = CloudNetDriver.getInstance().getPermissionManagement()
                .getHighestPermissionGroup(this.getCloudPermissionUser(playerUniqueId));

        return permissionGroup.getDisplay().replace('&', '§')
                + this.getCloudPermissionUser(playerUniqueId).getName();
    }

    @Override
    public boolean invitePlayer(String playerName) {
        ICloudOfflinePlayer offlinePlayer = this.playerManager.getFirstOfflinePlayer(playerName);
        ICloudPlayer iCloudPlayer = this.playerManager.getFirstOnlinePlayer(playerName);

        if(offlinePlayer != null) {
            IClanPlayer iClanPlayer = new ClanPlayer(offlinePlayer.getUniqueId());
            if(iClanPlayer.hasClan()) {
                return false;
            }

            ArrayList<UUID> playerClanRequests = (ArrayList<UUID>) this.getObject("clanRequests",
                    this.getMongoDB().getUserCollection(), offlinePlayer.getUniqueId());

            if(playerClanRequests == null) {
                playerClanRequests = new ArrayList<>();
            }

            playerClanRequests.add(this.clanUniqueId);

            this.insertAsyncInCollection("clanRequests", playerClanRequests,
                    offlinePlayer.getUniqueId().toString(), MongoDBCollection.USERS);

        } else {
            return false;
        }

        if(iCloudPlayer != null) {
            BaseComponent[] messageComponent = {
                    new TextComponent(new ComponentBuilder(this.getCrimxAPI().getClanPrefix()
                            + " §7Klicke ").append("§e§l*hier*")
                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan ?join=" + this.clanUniqueId.toString()))
                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§eKlicke, um zu joinen")))
                            .append("§r§7, um dem Clan §e" + this.clanName + " §8(§6" + this.clanTag + "§8) §7zu joinen")
                            .create())
            };
            BaseComponentMessenger.sendMessage(iCloudPlayer, messageComponent);
        } else {
            return false;
        }
        return true;
    }
}
