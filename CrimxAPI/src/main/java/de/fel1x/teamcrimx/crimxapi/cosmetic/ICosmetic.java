package de.fel1x.teamcrimx.crimxapi.cosmetic;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public interface ICosmetic {

    Component getDisplayName();

    Component[] getDescription();

    Material getDisplayMaterial();

    CosmeticCategory getCosmeticCategory();

    int getCost();

    double maxDiscount();

    void initializeCosmetic(Player player);

    void updateAfterTicks(long ticksToGo);

    void stopCosmetic(Player player);


}
