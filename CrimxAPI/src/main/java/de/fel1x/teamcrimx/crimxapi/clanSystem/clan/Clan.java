package de.fel1x.teamcrimx.crimxapi.clanSystem.clan;

import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.clanSystem.constants.ClanKickReason;
import de.fel1x.teamcrimx.crimxapi.clanSystem.database.ClanDatabase;
import de.fel1x.teamcrimx.crimxapi.clanSystem.player.IClanPlayer;
import de.fel1x.teamcrimx.crimxapi.clanSystem.constants.ClanRank;
import org.bson.Document;
import org.bukkit.Material;

import java.util.Collection;
import java.util.UUID;

public class Clan extends ClanDatabase implements IClan {

    public Clan(CrimxAPI crimxAPI) {
        super(crimxAPI);
    }

    @Override
    public int getTotalClanMembers() {
        return 0;
    }

    @Override
    public Collection<? extends IClanPlayer> getClanMembers() {
        return null;
    }

    @Override
    public boolean addPlayerToClan(IClanPlayer iClanPlayer) {
        return false;
    }

    @Override
    public boolean removePlayerFromClan(IClanPlayer iClanPlayer) {
        return false;
    }

    @Override
    public boolean kickPlayerFromClan(IClanPlayer iClanPlayer, ClanKickReason clanKickReason) {
        return false;
    }

    @Override
    public boolean isPlayerInClan(IClanPlayer iClanPlayer) {
        return false;
    }

    @Override
    public boolean setNewRank(IClanPlayer iClanPlayer, ClanRank clanRank) {
        return false;
    }

    @Override
    public ClanRank getPlayerRank(IClanPlayer iClanPlayer) {
        return null;
    }

    @Override
    public boolean createClan(String clanName, String clanTag, Material clanItem, IClanPlayer iClanPlayer) {
        try {
            Document clanDocument = new Document("_id", UUID.randomUUID().toString())
                    .append("clanName", clanName)
                    .append("clanTag", clanTag)
                    .append("clanItem", clanItem.name())
                    .append("owner", iClanPlayer.getUUID())
                    .append("members", this.getClanMembers());

        } catch (Exception exception) {
        }

        return false;
    }

    @Override
    public boolean deleteClan() {
        return false;
    }

    @Override
    public String getClanName() {
        return null;
    }

    @Override
    public boolean setClanName() {
        return false;
    }

    @Override
    public String getClanTag() {
        return null;
    }

    @Override
    public boolean setClanTag() {
        return false;
    }

    @Override
    public Material getClanMaterial() {
        return null;
    }

    @Override
    public boolean setClanMaterial() {
        return false;
    }
}
