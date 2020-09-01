package de.fel1x.teamcrimx.mlgwars.kit.kits;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.kit.IKit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class FarmerKit implements IKit {

    @Override
    public String getKitName() {
        return "Farmer";
    }

    @Override
    public String[] getKitDescription() {
        return new String[]{
                "§7Willkommen auf deiner Farm!", "§7Züchte Tiere und werde selbst zum Tier!", "", "§cAchtung: Du hast weniger Leben bei einer Verwandlung", "",
        };
    }

    @Override
    public int getKitCost() {
        return 4500;
    }

    @Override
    public Material getKitMaterial() {
        return Material.HAY_BLOCK;
    }

    @Override
    public void setKitInventory(Player player) {
        player.getInventory().setItem(0, new ItemBuilder(Material.GOLD_NUGGET)
                .setName("§8● §dVerwandler")
                .addGlow()
                .toItemStack());
    }
}
