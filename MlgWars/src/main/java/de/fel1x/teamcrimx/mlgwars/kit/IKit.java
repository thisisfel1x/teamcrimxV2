package de.fel1x.teamcrimx.mlgwars.kit;

import org.bukkit.Material;

public interface IKit {

    String kitName();

    String[] kitDescription();

    int kitCost();

    Material kitMaterial();

}
