package de.fel1x.teamcrimx.mlgwars.kit;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public interface IKit {

    String getKitName();

    String[] getKitDescription();

    int getKitCost();

    Material getKitMaterial();

    void setKitInventory(Player player);

}
