package de.fel1x.teamcrimx.crimxapi.cosmetic;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

public enum CosmeticCategory {

    /**
     * Boots for specific abilities like higher speed, invisibility on crouch...
     */
    BOOT(new ItemBuilder(Material.LEATHER_BOOTS)
            .setName(Component.text("§8● §5Schuhe")).setLeatherArmorColor(Color.PURPLE)
            .toItemStack(), "Schuh"),
    /**
     * Effects around your body (butterfly curve, circle...)
     */
    EFFECT(new ItemBuilder(Material.TIPPED_ARROW)
            .setName(Component.text("§8● §aEffekte")).setArrowEffect(PotionType.LUCK)
            .toItemStack(), "Effekt"),
    /**
     * Gadgets for interacting in lobby
     */
    GADGETS(new ItemBuilder(Material.BLAZE_ROD)
            .setName(Component.text("§8● §eGadgets"))
            .toItemStack(), "Gadget"),
    /**
     * Pets which follow you in lobby and game lobby (animals or armorstands)
     */
    PET(new ItemBuilder(Material.SADDLE)
            .setName(Component.text("§8● §6Pets"))
            .toItemStack(), "Pet"),
    /**
     * Win-Animations after the round
     */
    WIN_ANIMATION(new ItemBuilder(Material.FIREWORK_ROCKET)
            .setName(Component.text("§8● §cSiegesanimationen"))
            .toItemStack(), "Siegesanimation");

    /**
     * ItemStack which is displayed in shop inventory etc
     */
    private final ItemStack itemStack;
    /**
     * Plain name string
     */
    private final String plainName;

    CosmeticCategory(ItemStack itemStack, String plainName) {
        this.itemStack = itemStack;
        this.plainName = plainName;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public String getPlainName() {
        return this.plainName;
    }
}
