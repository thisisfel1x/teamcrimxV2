package de.fel1x.teamcrimx.crimxapi.clanSystem.player;

import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.fel1x.teamcrimx.crimxapi.clanSystem.clan.IClan;
import de.fel1x.teamcrimx.crimxapi.clanSystem.constants.ClanRank;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface IClanPlayer {

    boolean addToClan(UUID clanUniqueId);

    boolean removeFromClan(UUID clanUniqueId);

    boolean isInClan(UUID clanUniqueId);

    boolean hasClan();

    IClan getCurrentClan();

    IClanPlayer getByUUID(UUID playerUniqueId);

    ICloudPlayer getCloudPlayerByUUID(UUID playerUniqueId);

    /**
     * Get a BukkitPlayer by UUID
     * @param playerUniqueId UniqueId of the player
     * @return returns {@link Player}
     */
    @Nullable
    Player getBukkitPlayerByUUID(UUID playerUniqueId);

    UUID getUUID();

    void setNewRank(ClanRank clanRank);

    void sendMessage(String message, boolean prefix, UUID playerUniqueId);
}
