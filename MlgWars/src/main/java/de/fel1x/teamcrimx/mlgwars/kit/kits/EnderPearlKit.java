package de.fel1x.teamcrimx.mlgwars.kit.kits;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.kit.IKit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class EnderPearlKit implements IKit {

    @Override
    public String getKitName() {
        return "Enderman";
    }

    @Override
    public String[] getKitDescription() {
        return new String[]{
                "§7Werfe deine §5magische Perle", "§7und komme direkt beim Gegner an!", ""
        };
    }

    @Override
    public int getKitCost() {
        return 5000;
    }

    @Override
    public Material getKitMaterial() {
        return Material.ENDER_PEARL;
    }

    @Override
    public void setKitInventory(Player player) {
        player.getInventory().setItem(0, new ItemBuilder(Material.ENDER_PEARL, 2)
                .setName("§8● §5Funkelnde Enderperle")
                .addGlow()
                .toItemStack());
    }
}
