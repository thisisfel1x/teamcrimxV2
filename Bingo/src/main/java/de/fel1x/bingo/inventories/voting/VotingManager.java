package de.fel1x.bingo.inventories.voting;

import de.fel1x.bingo.objects.BingoDifficulty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;

public class VotingManager {

    private ArrayList<UUID> votingPlayersEasy;
    private ArrayList<UUID> votingPlayersNormal;
    private ArrayList<UUID> votingPlayersHard;

    private BingoDifficulty forcedBingoDifficulty;

    public VotingManager(ArrayList<UUID> votingPlayersEasy, ArrayList<UUID> votingPlayersNormal, ArrayList<UUID> votingPlayersHard, BingoDifficulty forcedBingoDifficulty) {
        this.votingPlayersEasy = votingPlayersEasy;
        this.votingPlayersNormal = votingPlayersNormal;
        this.votingPlayersHard = votingPlayersHard;
        this.forcedBingoDifficulty = forcedBingoDifficulty;
    }

    public void addPlayerToDifficulty(UUID uniqueId, BingoDifficulty bingoDifficulty) {
        this.removeFromAll(uniqueId);

        switch (bingoDifficulty) {
            case EASY:
                this.getVotingPlayersEasy().add(uniqueId);
                break;
            case NORMAL:
                this.getVotingPlayersNormal().add(uniqueId);
                break;
            case HARDCORE:
                this.getVotingPlayersHard().add(uniqueId);
        }
    }

    private void removeFromAll(UUID uuid) {
        this.getVotingPlayersEasy().remove(uuid);
        this.getVotingPlayersNormal().remove(uuid);
        this.getVotingPlayersHard().remove(uuid);
    }

    public ArrayList<UUID> getVotingPlayersEasy() {
        return this.votingPlayersEasy;
    }

    public void setVotingPlayersEasy(ArrayList<UUID> votingPlayersEasy) {
        this.votingPlayersEasy = votingPlayersEasy;
    }

    public ArrayList<UUID> getVotingPlayersNormal() {
        return this.votingPlayersNormal;
    }

    public void setVotingPlayersNormal(ArrayList<UUID> votingPlayersNormal) {
        this.votingPlayersNormal = votingPlayersNormal;
    }

    public ArrayList<UUID> getVotingPlayersHard() {
        return this.votingPlayersHard;
    }

    public void setVotingPlayersHard(ArrayList<UUID> votingPlayersHard) {
        this.votingPlayersHard = votingPlayersHard;
    }

    public int getVotingCountEasy() {
        return this.votingPlayersEasy.size();
    }

    public int getVotingCountNormal() {
        return this.votingPlayersNormal.size();
    }

    public int getVotingCountHard() {
        return this.votingPlayersHard.size();
    }


    public BingoDifficulty getForcedBingoDifficulty() {
        return this.forcedBingoDifficulty;
    }

    public void setForcedBingoDifficulty(BingoDifficulty forcedBingoDifficulty) {
        this.forcedBingoDifficulty = forcedBingoDifficulty;
    }

    public int getVotesByDifficulty(BingoDifficulty bingoDifficulty) {
        switch (bingoDifficulty) {
            case EASY:
                return this.getVotingCountEasy();
            case NORMAL:
                return this.getVotingCountNormal();
            case HARDCORE:
                return this.getVotingCountHard();
            default:
                return -1;
        }
    }

    public boolean containsPlayer(UUID uniqueId, BingoDifficulty bingoDifficulty) {
        switch (bingoDifficulty) {
            case EASY:
                return this.getVotingPlayersEasy().contains(uniqueId);
            case NORMAL:
                return this.getVotingPlayersNormal().contains(uniqueId);
            case HARDCORE:
                return this.getVotingPlayersHard().contains(uniqueId);
            default:
                return false;
        }
    }

    public BingoDifficulty getFinalDifficulty() {

        BingoDifficulty.EASY.setFinalVotingCounts(this.getVotingCountEasy());
        BingoDifficulty.NORMAL.setFinalVotingCounts(this.getVotingCountNormal());
        BingoDifficulty.HARDCORE.setFinalVotingCounts(this.getVotingCountHard());

        ArrayList<BingoDifficulty> sorted = Arrays.stream(BingoDifficulty.values())
                .filter(bingoDifficulty -> bingoDifficulty != BingoDifficulty.NOT_FORCED).sorted((o1, o2) -> {

            int i1 = o1.getFinalVotingCounts();
            int i2 = o2.getFinalVotingCounts();

            if (i1 > i2) {
                return 1;
            } else if (i1 < i2) {
                return -1;
            }

            return 0;

        }).collect(Collectors.toCollection(ArrayList::new));

        Collections.reverse(sorted);

        return sorted.get(0);

    }

    public void forceNextDifficulty() {
        switch (this.getForcedBingoDifficulty()) {
            case EASY:
                this.setForcedBingoDifficulty(BingoDifficulty.NORMAL);
                break;
            case NORMAL:
                this.setForcedBingoDifficulty(BingoDifficulty.HARDCORE);
                break;
            case HARDCORE:
                this.setForcedBingoDifficulty(BingoDifficulty.NOT_FORCED);
                break;
            case NOT_FORCED:
                this.setForcedBingoDifficulty(BingoDifficulty.EASY);
                break;
        }

    }
}
