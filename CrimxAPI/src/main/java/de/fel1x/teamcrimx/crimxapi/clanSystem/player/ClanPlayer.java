package de.fel1x.teamcrimx.crimxapi.clanSystem.player;

import de.fel1x.teamcrimx.crimxapi.clanSystem.clan.IClan;

import java.io.Serializable;
import java.util.UUID;

public class ClanPlayer implements IClanPlayer, Serializable {

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
        return null;
    }

    @Override
    public UUID getUUID() {
        return null;
    }


}
