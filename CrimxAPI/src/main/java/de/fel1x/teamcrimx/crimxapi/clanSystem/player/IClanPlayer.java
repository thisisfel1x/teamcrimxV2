package de.fel1x.teamcrimx.crimxapi.clanSystem.player;

import de.fel1x.teamcrimx.crimxapi.clanSystem.clan.IClan;

import java.util.UUID;

public interface IClanPlayer {

    boolean addToClan(UUID clanUniqueId);

    boolean removeFromClan(UUID clanUniqueId);

    boolean isInClan(UUID clanUniqueId);

    boolean hasClan();

    IClan getCurrentClan();

    IClanPlayer getByUUID(UUID playerUniqueId);

    UUID getUUID();

}
