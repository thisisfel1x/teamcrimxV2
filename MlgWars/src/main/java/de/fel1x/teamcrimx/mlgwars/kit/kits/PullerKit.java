package de.fel1x.teamcrimx.mlgwars.kit.kits;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.kit.IKit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PullerKit implements IKit {

    @Override
    public String getKitName() {
        return "Grabber";
    }

    @Override
    public String[] getKitDescription() {
        return new String[]{
                "§7Ziehe Gegner an dich heran", "§7Auf #nohomo-Basis natürlich :P", ""
        };
    }

    @Override
    public int getKitCost() {
        return 3000;
    }

    @Override
    public Material getKitMaterial() {
        return Material.CARROT_STICK;
    }

    @Override
    public void setKitInventory(Player player) {
        player.getInventory().setItem(0, new ItemBuilder(Material.FISHING_ROD)
                .setName("§8● §5Grabscher")
                .addGlow()
                .toItemStack());
    }
}
