package de.fel1x.teamcrimx.crimxapi.clanSystem.clan;

import de.fel1x.teamcrimx.crimxapi.clanSystem.constants.ClanKickReason;
import de.fel1x.teamcrimx.crimxapi.clanSystem.player.IClanPlayer;
import de.fel1x.teamcrimx.crimxapi.clanSystem.constants.ClanRank;
import org.bukkit.Material;

import java.util.Collection;

public interface IClan {

    int getTotalClanMembers();

    Collection<? extends IClanPlayer> getClanMembers();

    boolean addPlayerToClan(IClanPlayer iClanPlayer);

    boolean removePlayerFromClan(IClanPlayer iClanPlayer);

    boolean kickPlayerFromClan(IClanPlayer iClanPlayer, ClanKickReason clanKickReason);

    boolean isPlayerInClan(IClanPlayer iClanPlayer);

    boolean setNewRank(IClanPlayer iClanPlayer, ClanRank clanRank);

    ClanRank getPlayerRank(IClanPlayer iClanPlayer);

    boolean createClan(String clanName, String clanTag, Material clanItem, IClanPlayer iClanPlayer);

    boolean deleteClan();

    String getClanName();

    boolean setClanName();

    String getClanTag();

    boolean setClanTag();

    Material getClanMaterial();

    boolean setClanMaterial();

}
