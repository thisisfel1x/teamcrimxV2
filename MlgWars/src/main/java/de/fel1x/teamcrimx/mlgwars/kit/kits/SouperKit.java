package de.fel1x.teamcrimx.mlgwars.kit.kits;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.kit.IKit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SouperKit implements IKit {

    @Override
    public String getKitName() {
        return "Souper";
    }

    @Override
    public String[] getKitDescription() {
        return new String[]{
                "§7Spawne Brünhilde die Kuh", "§7und hole ihre leckeren Suppen ab!"
        };
    }

    @Override
    public int getKitCost() {
        return 0;
    }

    @Override
    public Material getKitMaterial() {
        return Material.MUSHROOM_STEW;
    }

    @Override
    public void setKitInventory(Player player) {
        player.getInventory().setItem(0, new ItemBuilder(Material.MOOSHROOM_SPAWN_EGG)
                .setName("§8● §cPilzkuh")
                .setColor(96)
                .addGlow()
                .toItemStack());

        player.getInventory().setItem(1, new ItemBuilder(Material.BOWL, 16).toItemStack());
    }
}
