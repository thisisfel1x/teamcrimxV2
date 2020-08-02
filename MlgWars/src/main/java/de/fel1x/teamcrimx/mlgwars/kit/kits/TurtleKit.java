package de.fel1x.teamcrimx.mlgwars.kit.kits;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.kit.IKit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class TurtleKit implements IKit {

    @Override
    public String getKitName() {
        return "Builder";
    }

    @Override
    public String[] getKitDescription() {
        return new String[] {
                "§7Sei ein Kenner", "§7und baue dich fix ein!", "", "§6[für dxxniz]"
        };
    }

    @Override
    public int getKitCost() {
        return 3000;
    }

    @Override
    public Material getKitMaterial() {
        return Material.PAPER;
    }

    @Override
    public void setKitInventory(Player player) {
        player.getInventory().setItem(0, new ItemBuilder(Material.EGG, 3)
                .setName("§8● §aTurtle")
                .addGlow()
                .toItemStack());
    }
}
