package de.fel1x.teamcrimx.mlgwars.kit.kits;

import de.fel1x.teamcrimx.mlgwars.kit.IKit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TankKit implements IKit {

    @Override
    public String getKitName() {
        return "Tank";
    }

    @Override
    public String[] getKitDescription() {
        return new String[]{
                "§7Stärke & Langsamkeit", "§7Das sind deine Fähigkeiten", "", "§6[für Xx_LP_KottPvP_xX]"
        };
    }

    @Override
    public int getKitCost() {
        return 4000;
    }

    @Override
    public Material getKitMaterial() {
        return Material.DIAMOND_CHESTPLATE;
    }

    @Override
    public void setKitInventory(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 2, false, false));
    }
}
