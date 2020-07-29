package de.fel1x.teamcrimx.mlgwars.kit.kits;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.kit.IKit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ExploderKit implements IKit {

    @Override
    public String getKitName() {
        return "Sprengmeister";
    }

    @Override
    public String[] getKitDescription() {
        return new String[] { "", "§7Sprenge platziertes TNT", "§7mit deinem Fernzünder in die Luft!" };
    }

    @Override
    public int getKitCost() {
        return 2500;
    }

    @Override
    public Material getKitMaterial() {
        return Material.LEVER;
    }

    @Override
    public ItemStack[] getInventoryContents() {
        return new ItemStack[] {
                new ItemBuilder(Material.LEVER)
                        .setUnbreakable()
                        .setName("§8● §eFernzünder").toItemStack(),
                new ItemBuilder(Material.TNT, 16)
                        .setName("§8● §bExplosives TNT")
                        .setLore("§7Explosiver geht es fast gar", "§7nicht mehr! (wirklich explosiv!)").toItemStack()
        };
    }
}
