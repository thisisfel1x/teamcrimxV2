package de.fel1x.teamcrimx.crimxlobby.cosmetics;

import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public interface ICosmetic {

    String getCosmeticName();

    String[] getCosmeticDescription();

    Material getCosmeticMaterial();

    Color getLeatherShoeColor();

    Effect getWalkEffect();

    int getCosmeticCost();

    int effectData();

    void startTrail(Player player);

    boolean dropItem();

    boolean playerBlock();

    boolean armor();

    boolean gadget();

    Material itemToDrop();

    void updateInventory(Player player);

}
