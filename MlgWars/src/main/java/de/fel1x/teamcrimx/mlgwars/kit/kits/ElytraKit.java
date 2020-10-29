package de.fel1x.teamcrimx.mlgwars.kit.kits;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.kit.IKit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ElytraKit implements IKit {

    @Override
    public String getKitName() {
        return "Elytra";
    }

    @Override
    public String[] getKitDescription() {
        return new String[] {
                "§7Fliege durch die Welt"
        };
    }

    @Override
    public int getKitCost() {
        return 6000;
    }

    @Override
    public Material getKitMaterial() {
        return Material.ELYTRA;
    }

    @Override
    public void setKitInventory(Player player) {
        player.getInventory().setItem(0, new ItemBuilder(Material.CLAY_BALL, 3)
                .setName("§8● §dElytra").toItemStack());
    }
}
