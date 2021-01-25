package de.fel1x.teamcrimx.mlgwars.kit.kits;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.kit.IKit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class TornadoKit implements IKit {

    @Override
    public String getKitName() {
        return "Eskalation";
    }

    @Override
    public String[] getKitDescription() {
        return new String[]{
                "§7Mit diesem Kit eskalierst du komplett!", "§7Lass deinen Frust raus!", "", "§6[für feyju]"
        };
    }

    @Override
    public int getKitCost() {
        return 3500;
    }

    @Override
    public Material getKitMaterial() {
        return Material.DEAD_BUSH;
    }

    @Override
    public void setKitInventory(Player player) {
        player.getInventory().setItem(0, new ItemBuilder(Material.DEAD_BUSH)
                .setName("§8● §fTornado")
                .addGlow()
                .toItemStack());
    }
}
