package de.fel1x.teamcrimx.mlgwars.kit.kits;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.kit.IKit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class NpcKit implements IKit {

    @Override
    public String getKitName() {
        return "BotPvP";
    }

    @Override
    public String[] getKitDescription() {
        return new String[] {
                "§7Die Alternative für alle,", "§7die kein Aim haben!", ""
        };
    }

    @Override
    public int getKitCost() {
        return 5000;
    }

    @Override
    public Material getKitMaterial() {
        return Material.IRON_SWORD;
    }

    @Override
    public void setKitInventory(Player player) {
        player.getInventory().setItem(0, new ItemBuilder(Material.EGG, 5)
                .setName("§8● §bBotgranate")
                .addGlow()
                .toItemStack());
    }
}
