package de.fel1x.teamcrimx.crimxapi.clanSystem.player;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.clanSystem.clan.Clan;
import de.fel1x.teamcrimx.crimxapi.clanSystem.clan.IClan;
import de.fel1x.teamcrimx.crimxapi.clanSystem.constants.ClanRank;
import de.fel1x.teamcrimx.crimxapi.clanSystem.database.ClanDatabase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.UUID;

public class ClanPlayer extends ClanDatabase implements IClanPlayer, Serializable {

    private final IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);
    private final CrimxAPI crimxAPI = CrimxAPI.getInstance();

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
    public boolean removeFromClan(UUID clanUniqueId) {
        return false;
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


}
