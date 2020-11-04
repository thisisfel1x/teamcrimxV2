package de.fel1x.capturetheflag.kit.kits;

import de.fel1x.capturetheflag.kit.IKit;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TankKit implements IKit {
    @Override
    public String getKitName() {
        return "Tank";
    }

    @Override
    public String[] getKitDescription() {
        return new String[] {
                "§aVorteile:",
                " §8» §eSteinschwert (Sharp 1)",
                " §8» §eFull Iron",
                "§cNachteile",
                " §8» §eLangsamkeit 1"
        };
    }

    @Override
    public int getKitCost() {
        return 0;
    }

    @Override
    public Material getKitMaterial() {
        return Material.DIAMOND_CHESTPLATE;
    }

    @Override
    public void setKitInventory(Player player) {
        ItemStack helmet = new ItemBuilder(Material.IRON_HELMET).toItemStack();
        ItemStack chestplate = new ItemBuilder(Material.IRON_CHESTPLATE).toItemStack();
        ItemStack leggins = new ItemBuilder(Material.IRON_LEGGINGS).toItemStack();
        ItemStack boots = new ItemBuilder(Material.IRON_BOOTS).toItemStack();

        ItemStack sword = new ItemStack(Material.WOODEN_SWORD);

        player.getInventory().addItem(sword);
        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(leggins);
        player.getInventory().setBoots(boots);

        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 0, true, false));
    }
}
