package de.fel1x.teamcrimx.mlgwars.kit.kits;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.kit.IKit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class NutsKit implements IKit {

    @Override
    public String getKitName() {
        return "Hülsenfrüchte";
    }

    @Override
    public String[] getKitDescription() {
        return new String[]{
                "§7Nutze die magische", "§7Kraft der Hülsenfrüchte", "", "§6[für Pilzkuuh]"
        };
    }

    @Override
    public int getKitCost() {
        return 3000;
    }

    @Override
    public Material getKitMaterial() {
        return Material.ANVIL;
    }

    @Override
    public void setKitInventory(Player player) {
        player.getInventory().setItem(0, new ItemBuilder(Material.SNOWBALL, 3)
                .setName("§8● §aHülsenfrucht")
                .setLore("§7Schieße damit einen Amboss")
                .toItemStack());
    }
}
