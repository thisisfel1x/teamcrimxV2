package de.fel1x.teamcrimx.mlgwars.kit.kits;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.kit.IKit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class CSGOKit implements IKit {

    @Override
    public String getKitName() {
        return "CSGO";
    }

    @Override
    public String[] getKitDescription() {
        return new String[]{
                "§7Heute ruhiger Stream,", "§7denn hier wirst du nicht gesniped!", "", "§6[für TurbomikLP]"
        };
    }

    @Override
    public int getKitCost() {
        return 2500;
    }

    @Override
    public Material getKitMaterial() {
        return Material.FLINT_AND_STEEL;
    }

    @Override
    public void setKitInventory(Player player) {
        player.getInventory().addItem(
                new ItemBuilder(Material.GLOWSTONE_DUST, 7)
                        .setName("§8● §eFlash")
                        .toItemStack(),
                new ItemBuilder(Material.FIREBALL, 3)
                        .setName("§8● §cMolotowcocktail")
                        .toItemStack(),
                new ItemBuilder(Material.FIREWORK_CHARGE, 7)
                        .setName("§8● §7Smoke")
                        .toItemStack()
        );
    }
}
