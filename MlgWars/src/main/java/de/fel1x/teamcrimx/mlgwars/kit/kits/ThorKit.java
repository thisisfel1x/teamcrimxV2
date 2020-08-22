package de.fel1x.teamcrimx.mlgwars.kit.kits;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.kit.IKit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ThorKit implements IKit {

    @Override
    public String getKitName() {
        return "Thor";
    }

    @Override
    public String[] getKitDescription() {
        return new String[]{
                "§7Blitz hier, Blitz da...", "§7...Thor ist da!"
        };
    }

    @Override
    public int getKitCost() {
        return 2500;
    }

    @Override
    public Material getKitMaterial() {
        return Material.GOLD_AXE;
    }

    @Override
    public void setKitInventory(Player player) {
        player.getInventory().setItem(0, new ItemBuilder(Material.GOLD_AXE)
                .setName("§8● §eThors Axt")
                .toItemStack());
    }
}
