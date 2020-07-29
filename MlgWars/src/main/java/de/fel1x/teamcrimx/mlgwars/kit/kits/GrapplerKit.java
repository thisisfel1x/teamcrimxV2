package de.fel1x.teamcrimx.mlgwars.kit.kits;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.kit.IKit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GrapplerKit implements IKit {

    @Override
    public String getKitName() {
        return "Grappler";
    }

    @Override
    public String[] getKitDescription() {
        return new String[] {
                "", "§7Ziehe dich von einer Insel zur anderen!", "§cAchtung: kann nur 5x verwendet werden!"
        };
    }

    @Override
    public int getKitCost() {
        return 2000;
    }

    @Override
    public Material getKitMaterial() {
        return Material.FISHING_ROD;
    }

    @Override
    public ItemStack[] getInventoryContents() {
        return new ItemStack[] {
                new ItemBuilder(Material.FISHING_ROD).setName("§8● §bEnterhaken").toItemStack()
        };
    }
}
