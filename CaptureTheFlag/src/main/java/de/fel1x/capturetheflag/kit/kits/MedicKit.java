package de.fel1x.capturetheflag.kit.kits;

import de.fel1x.capturetheflag.gameplayer.GamePlayer;
import de.fel1x.capturetheflag.kit.IKit;
import de.fel1x.capturetheflag.team.Team;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public class MedicKit implements IKit {
    @Override
    public String getKitName() {
        return "Medic";
    }

    @Override
    public String[] getKitDescription() {
        return new String[] {
                "§aVorteile:",
                " §8» §eEisenschwert (Sharp 1)",
                " §8» §eFull Leather",
                " §8» §e4x Healpotion",
                " §8» §e2x Trank der Regeneration",
                " §8» §e1x Goldapfel"
        };
    }

    @Override
    public int getKitCost() {
        return 0;
    }

    @Override
    public Material getKitMaterial() {
        return Material.GOLDEN_APPLE;
    }

    @Override
    public void setKitInventory(Player player) {
        Color dyeColor = new GamePlayer(player).getTeam() == Team.RED ? Color.RED : Color.BLUE;

        ItemStack helmet = new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(dyeColor).toItemStack();
        ItemStack chestplate = new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(dyeColor).toItemStack();
        ItemStack leggins = new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherArmorColor(dyeColor).toItemStack();
        ItemStack boots = new ItemBuilder(Material.LEATHER_BOOTS).setLeatherArmorColor(dyeColor).toItemStack();

        ItemStack sword = new ItemBuilder(Material.IRON_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 1).toItemStack();

        ItemStack healPotion = new ItemStack(Material.SPLASH_POTION);
        PotionMeta healPotionMeta = (PotionMeta) healPotion.getItemMeta();
        healPotionMeta.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL));
        healPotion.setItemMeta(healPotionMeta);
        healPotion.setAmount(2);

        ItemStack regPotion = new ItemStack(Material.SPLASH_POTION);
        PotionMeta regPotionMeta = (PotionMeta) regPotion.getItemMeta();
        regPotionMeta.setBasePotionData(new PotionData(PotionType.REGEN));
        regPotion.setItemMeta(regPotionMeta);

        ItemStack goldenApple = new ItemBuilder(Material.GOLDEN_APPLE).toItemStack();

        player.getInventory().addItem(sword, healPotion, regPotion, goldenApple);

        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(leggins);
        player.getInventory().setBoots(boots);
    }
}
