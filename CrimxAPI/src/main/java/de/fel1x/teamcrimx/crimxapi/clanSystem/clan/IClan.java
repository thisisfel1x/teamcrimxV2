package de.fel1x.teamcrimx.crimxapi.clanSystem.clan;

import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import de.dytanic.cloudnet.ext.bridge.player.ICloudOfflinePlayer;
import de.fel1x.teamcrimx.crimxapi.clanSystem.constants.ClanKickReason;
import de.fel1x.teamcrimx.crimxapi.clanSystem.constants.ClanRank;
import de.fel1x.teamcrimx.crimxapi.clanSystem.player.IClanPlayer;
import org.bukkit.Material;

import java.util.Collection;
import java.util.UUID;

public interface IClan {

    int getTotalClanMembers();

    Collection<UUID> getClanMembers();

    boolean addPlayerToClan(IClanPlayer iClanPlayer);

    boolean removePlayerFromClan(IClanPlayer iClanPlayer);

    boolean removePlayerFromClanByName(String playerName);

    boolean kickPlayerFromClan(IClanPlayer iClanPlayer, ClanKickReason clanKickReason);

    boolean isPlayerInClan(IClanPlayer iClanPlayer);

    boolean setNewRank(IClanPlayer iClanPlayer, ClanRank clanRank);

    ClanRank getPlayerRank(IClanPlayer iClanPlayer);

    boolean createClan(String clanName, String clanTag, Material clanItem, IClanPlayer iClanPlayer);

    boolean deleteClan();

    String getClanName();

    boolean setClanName(String newClanName);

    String getClanTag();

    boolean setClanTag(String newClanTag);

    Material getClanMaterial();

    boolean setClanMaterial(Material newClanMaterial);

    UUID getClanUniqueId();

    ICloudOfflinePlayer getOfflinePlayer(UUID playerUniqueId);

    UUID getClanOwner();

    IPermissionUser getCloudPermissionUser(UUID playerUniqueId);

    String getFormattedUserName(UUID playerUniqueId);

    boolean invitePlayer(String playerName);
}
