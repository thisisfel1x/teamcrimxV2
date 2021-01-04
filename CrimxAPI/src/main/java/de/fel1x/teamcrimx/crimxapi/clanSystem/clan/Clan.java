package de.fel1x.teamcrimx.crimxapi.clanSystem.clan;

import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.clanSystem.constants.ClanKickReason;
import de.fel1x.teamcrimx.crimxapi.clanSystem.database.ClanDatabase;
import de.fel1x.teamcrimx.crimxapi.clanSystem.player.IClanPlayer;
import de.fel1x.teamcrimx.crimxapi.clanSystem.constants.ClanRank;
import org.bson.Document;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.UUID;

public class Clan extends ClanDatabase implements IClan {

    private final ArrayList<IClanPlayer> clanPlayers = new ArrayList<>();
    private final UUID clanUniqueId;

    public Clan(UUID clanUniqueId) {
        super(CrimxAPI.getInstance());
        this.clanUniqueId = clanUniqueId;
    }

    @Override
    public int getTotalClanMembers() {
        return 0;
    }

    @Override
    public ArrayList<IClanPlayer> getClanMembers() {
        return this.clanPlayers;
    }

    @Override
    public boolean addPlayerToClan(IClanPlayer iClanPlayer) {

        this.clanPlayers.add(iClanPlayer);

        return false;
    }

    @Override
    public boolean removePlayerFromClan(IClanPlayer iClanPlayer) {

        this.clanPlayers.remove(iClanPlayer);

        return false;
    }

    @Override
    public boolean kickPlayerFromClan(IClanPlayer iClanPlayer, ClanKickReason clanKickReason) {

        this.removePlayerFromClan(iClanPlayer);

        return false;
    }

    @Override
    public boolean isPlayerInClan(IClanPlayer iClanPlayer) {
        return this.clanPlayers.contains(iClanPlayer);
    }

    @Override
    public boolean setNewRank(IClanPlayer iClanPlayer, ClanRank clanRank) {
        iClanPlayer.setNewRank(clanRank);

        int index = this.clanPlayers.indexOf(iClanPlayer);

        this.clanPlayers.remove(iClanPlayer);
        this.clanPlayers.add(index, iClanPlayer);

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
        }

        Document clanDocument = new Document("_id", UUID.randomUUID().toString())
                .append("clanName", clanName)
                .append("clanTag", clanTag)
                .append("clanItem", clanItem.name())
                .append("owner", iClanPlayer.getUUID())
                .append("members", this.getClanMembers());
        this.insertAsync(clanDocument);
        return true;
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
