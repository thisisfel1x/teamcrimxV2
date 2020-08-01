package de.fel1x.teamcrimx.mlgwars.kit.kits;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.kit.IKit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ChickenBridgeKit implements IKit {

    @Override
    public String getKitName() {
        return "Hühnerbrücke";
    }

    @Override
    public String[] getKitDescription() {
        return new String[] {
               "§7Nutze deine §cEier (höhöhö)", "§7um direkt zum Gegner zu gelangen", ""
        };
    }

    @Override
    public int getKitCost() {
        return 2500;
    }

    @Override
    public Material getKitMaterial() {
        return Material.EGG;
    }

    @Override
    public void setKitInventory(Player player) {
        player.getInventory().setItem(0, new ItemBuilder(Material.EGG, 3)
                .setName("§8● §eHühnerbrücke")
                .addGlow()
                .toItemStack());
    }
}
