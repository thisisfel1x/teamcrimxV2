package de.fel1x.teamcrimx.crimxapi.clanSystem.player;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.clanSystem.clan.IClan;
import de.fel1x.teamcrimx.crimxapi.clanSystem.constants.ClanRank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.UUID;

public class ClanPlayer implements IClanPlayer, Serializable {

    private final IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);
    private final CrimxAPI crimxAPI = CrimxAPI.getInstance();

    private UUID uuid;

    public ClanPlayer(UUID uuid) {
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
        return false;
    }

    @Override
    public boolean hasClan() {
        return false;
    }

    @Override
    public IClan getCurrentClan() {
        return null;
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
