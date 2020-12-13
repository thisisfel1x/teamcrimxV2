package de.fel1x.teamcrimx.crimxlobby.cosmetics;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public interface ICosmetic {

    String getCosmeticName();

    String[] getCosmeticDescription();

    Material getCosmeticMaterial();

    Color getLeatherShoeColor();

    Particle getWalkEffect();

    int getCosmeticCost();

    int effectData();

    void startTrail(Player player);

    CosmeticType cosmeticType();

    Material itemToDrop();

    void updateInventory(Player player);

}
