package de.fel1x.teamcrimx.mlgwars.kit.kits;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.kit.IKit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ValorantKit implements IKit {

    @Override
    public String getKitName() {
        return "Valorant";
    }

    @Override
    public String[] getKitDescription() {
        return new String[]{
                "§dZeige dir die Gegner als Cypher an!"
        };
    }

    @Override
    public int getKitCost() {
        return 3000;
    }

    @Override
    public Material getKitMaterial() {
        return Material.SPECTRAL_ARROW;
    }

    @Override
    public void setKitInventory(Player player) {
        player.getInventory().setItem(0, new ItemBuilder(Material.SPECTRAL_ARROW, 3)
                .setName("§8● §dCypher").toItemStack());
    }
}
