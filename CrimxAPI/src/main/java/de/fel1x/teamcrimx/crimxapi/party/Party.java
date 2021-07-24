package de.fel1x.teamcrimx.crimxapi.party;

import de.dytanic.cloudnet.driver.service.ServiceId;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.UUID;

public class Party {

    private UUID partyUUID;
    private UUID partyLeader;
    private ArrayList<UUID> partyMembers;
    private ServiceId leaderServiceId;

    public Party(UUID partyUUID, UUID partyLeader, ArrayList<UUID> partyMembers, @Nullable ServiceId leaderServiceId) {
        this.partyUUID = partyUUID;
        this.partyLeader = partyLeader;
        this.partyMembers = partyMembers;
        this.leaderServiceId = leaderServiceId;
    }

    public UUID getPartyUUID() {
        return this.partyUUID;
    }

    public void setPartyUUID(UUID partyUUID) {
        this.partyUUID = partyUUID;
    }

    public UUID getPartyLeader() {
        return this.partyLeader;
    }

    public void setPartyLeader(UUID partyLeader) {
        this.partyLeader = partyLeader;
    }

    public ArrayList<UUID> getPartyMembers() {
        return this.partyMembers;
    }

    public void setPartyMembers(ArrayList<UUID> partyMembers) {
        this.partyMembers = partyMembers;
    }

    public ServiceId getLeaderServiceId() {
        return this.leaderServiceId;
    }

    public void setLeaderServiceId(ServiceId leaderServiceId) {
        this.leaderServiceId = leaderServiceId;
    }
}
