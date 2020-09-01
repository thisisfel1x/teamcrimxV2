package de.fel1x.teamcrimx.mlgwars.kit.kits;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.kit.IKit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class GrapplerKit implements IKit {

    @Override
    public String getKitName() {
        return "Grappler";
    }

    @Override
    public String[] getKitDescription() {
        return new String[]{
                "§7Ziehe dich von einer Insel zur anderen!", "§cAchtung: kann nur 5x verwendet werden!", ""
        };
    }

    @Override
    public int getKitCost() {
        return 3000;
    }

    @Override
    public Material getKitMaterial() {
        return Material.FISHING_ROD;
    }

    @Override
    public void setKitInventory(Player player) {
        player.getInventory().setItem(0, new ItemBuilder(Material.FISHING_ROD).setName("§8● §bEnterhaken").toItemStack());
    }
}
