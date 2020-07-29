package de.fel1x.teamcrimx.mlgwars.kit;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface IKit {

    String getKitName();

    String[] getKitDescription();

    int getKitCost();

    Material getKitMaterial();

    ItemStack[] getInventoryContents();

}
