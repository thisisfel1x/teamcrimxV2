package de.fel1x.teamcrimx.mlgwars.kit.kits;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.kit.IKit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SaverKit implements IKit {

    @Override
    public String getKitName() {
        return "Saver";
    }

    @Override
    public String[] getKitDescription() {
        return new String[]{
                "§7Sichere dich vor dem Tod", "§7mit einer praktischen Rettungsplattform", ""
        };
    }

    @Override
    public int getKitCost() {
        return 2000;
    }

    @Override
    public Material getKitMaterial() {
        return Material.BLAZE_ROD;
    }

    @Override
    public void setKitInventory(Player player) {
        player.getInventory().setItem(0, new ItemBuilder(Material.BLAZE_ROD, 3)
                .setName("§8● §eRettungsplattform")
                .addGlow().toItemStack());
    }
}
