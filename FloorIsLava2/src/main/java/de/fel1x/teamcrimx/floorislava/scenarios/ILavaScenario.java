package de.fel1x.teamcrimx.floorislava.scenarios;

import org.bukkit.Material;

public interface ILavaScenario {
    void execute();

    String getName();

    Material getDisplayMaterial();
}
