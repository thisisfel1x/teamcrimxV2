package de.fel1x.teamcrimx.mlgwars.kit.kits;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.kit.IKit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

public class AstronautKit implements IKit {

    @Override
    public String getKitName() {
        return "Astronaut";
    }

    @Override
    public String[] getKitDescription() {
        return new String[] {
                "§7Einmal ins All - der Traum...", "§7...der mit diesem Kit erreicht werden kann!", ""
        };
    }

    @Override
    public int getKitCost() {
        return 2500;
    }

    @Override
    public Material getKitMaterial() {
        return Material.FIREWORK;
    }

    @Override
    public void setKitInventory(Player player) {
        player.getInventory().setItem(0, new ItemBuilder(Material.FIREWORK, 3)
                .setName("§8● §cRakete")
                .addEnchant(Enchantment.DAMAGE_ARTHROPODS, 0)
                .addGlow()
                .toItemStack());

        player.getInventory().setHelmet(new ItemBuilder(Material.GLASS)
                .setName("§8● §fAstronautenhelm")
                .addGlow()
                .toItemStack());
    }
}
