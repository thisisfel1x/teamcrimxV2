package de.fel1x.teamcrimx.mlgwars.kit.kits;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.kit.IKit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class BoatGliderKit implements IKit {

    @Override
    public String getKitName() {
        return "Boatglider";
    }

    @Override
    public String[] getKitDescription() {
        return new String[] {
                "§7Katapultiere dich mit", "§7 einem Stoß in die Luft", ""
        };
    }

    @Override
    public int getKitCost() {
        return 3000;
    }

    @Override
    public Material getKitMaterial() {
        return Material.BOAT;
    }

    @Override
    public void setKitInventory(Player player) {
        player.getInventory().setItem(0, new ItemBuilder(Material.BOAT, 3)
                .setName("§8● §7MLG-Boot")
                .addGlow()
                .toItemStack());
    }
}
