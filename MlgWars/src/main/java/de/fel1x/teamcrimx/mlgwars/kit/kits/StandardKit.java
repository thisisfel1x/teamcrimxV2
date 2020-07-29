package de.fel1x.teamcrimx.mlgwars.kit.kits;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.kit.IKit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class StandardKit implements IKit {

    @Override
    public String getKitName() {
        return "Starter";
    }

    @Override
    public String[] getKitDescription() {
        return new String[] { "", "§7Jeder fängt mal klein an...", "§7Aber hiermit will keiner anfangen!"};
    }

    @Override
    public int getKitCost() {
        return 0;
    }

    @Override
    public Material getKitMaterial() {
        return Material.IRON_PICKAXE;
    }

    @Override
    public void setKitInventory(Player player) {
        player.getInventory().setItem(0, new ItemBuilder(Material.GOLD_SWORD)
                .setName("§8● §6Müll §7(umtauschen verboten!)").toItemStack());
    }
}
