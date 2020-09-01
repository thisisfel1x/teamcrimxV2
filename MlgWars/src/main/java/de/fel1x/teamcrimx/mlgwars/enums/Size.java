package de.fel1x.teamcrimx.mlgwars.enums;

public enum Size {

    SIZE_8x1("8x1", 8, 1, 8),
    SIZE_12x1("12x1", 12, 1, 12),
    SIZE_16x1("16x1", 16, 1, 16),
    SIZE_24x1("24x1", 24, 1, 24),
    SIZE_8x2("8x2", 16, 2, 8),
    SIZE_12x2("12x2", 24, 2, 12),
    SIZE_8x3("8x3", 24, 3, 8);

    private String name;
    private int size;
    private int teamSize;
    private int maxTeams;

    Size(String name, int size, int teamSize, int maxTeams) {
        this.name = name;
        this.size = size;
        this.teamSize = teamSize;
        this.maxTeams = maxTeams;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public int getMaxTeams() {
        return maxTeams;
    }
}
