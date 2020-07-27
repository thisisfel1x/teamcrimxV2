package de.fel1x.teamcrimx.mlgwars.enums;

public enum Size {

    SIZE_8x1("8x1", 8),
    SIZE_12x1("12x1", 12),
    SIZE_16x1("16x1", 16),
    SIZE_24x1("24x1", 24);

    private String name;
    private int size;

    Size(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

}
