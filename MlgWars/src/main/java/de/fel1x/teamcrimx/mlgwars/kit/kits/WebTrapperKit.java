package de.fel1x.teamcrimx.mlgwars.kit.kits;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.kit.IKit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class WebTrapperKit implements IKit {

    @Override
    public String getKitName() {
        return "Trapper";
    }

    @Override
    public String[] getKitDescription() {
        return new String[]{
                "§7Trappe Spieler in deinem gesponnenen Netz", ""
        };
    }

    @Override
    public int getKitCost() {
        return 2500;
    }

    @Override
    public Material getKitMaterial() {
        return Material.WEB;
    }

    @Override
    public void setKitInventory(Player player) {
        player.getInventory().setItem(0, new ItemBuilder(Material.EGG, 3)
                .setName("§8● §fWebtrap")
                .addGlow()
                .toItemStack());
    }
}
