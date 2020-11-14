package de.fel1x.bingo.scenarios;


import org.bukkit.Material;

public interface IBingoScenario {

    void execute();

    String getName();

    Material getDisplayMaterial();

}
