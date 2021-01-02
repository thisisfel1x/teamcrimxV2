package de.fel1x.bingo.objects;

import org.bukkit.Material;

public enum BingoDifficulty {

    EASY(Material.GREEN_DYE, "§aEinfach", 0),
    NORMAL(Material.ORANGE_DYE, "§6Normal", 0),
    HARDCORE(Material.RED_DYE, "§cSchwer", 0);

    private final Material material;
    private final String displayName;
    private int finalVotingCounts;

    BingoDifficulty(Material material, String displayName, int finalVotingCounts) {
        this.material = material;
        this.displayName = displayName;
        this.finalVotingCounts = finalVotingCounts;
    }

    public Material getMaterial() {
        return this.material;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public int getFinalVotingCounts() {
        return this.finalVotingCounts;
    }

    public void setFinalVotingCounts(int finalVotingCounts) {
        this.finalVotingCounts = finalVotingCounts;
    }
}
