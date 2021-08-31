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
            .toItemStack(), "Schuh", BootCosmetic.class),
    /**
     * Effects around your body (butterfly curve, circle...)
     */
    EFFECT(new ItemBuilder(Material.TIPPED_ARROW)
            .setName(Component.text("§8● §aEffekte")).setArrowEffect(PotionType.LUCK)
            .toItemStack(), "Effekt", EffectCosmetic.class),
    /**
     * Gadgets for interacting in lobby
     */
    GADGETS(new ItemBuilder(Material.BLAZE_ROD)
            .setName(Component.text("§8● §eGadgets"))
            .toItemStack(), "Gadget", Gadget.class),
    /**
     * Pets which follow you in lobby and game lobby (animals or armorstands)
     */
    PET(new ItemBuilder(Material.SADDLE)
            .setName(Component.text("§8● §6Pets"))
            .toItemStack(), "Pet", PetCosmetic.class),
    /**
     * Win-Animations after the round
     */
    WIN_ANIMATION(new ItemBuilder(Material.FIREWORK_ROCKET)
            .setName(Component.text("§8● §cSiegesanimationen"))
            .toItemStack(), "Siegesanimation", WinAnimationCosmetic.class);

    /**
     * ItemStack which is displayed in shop inventory etc
     */
    private final ItemStack itemStack;
    /**
     * Plain name string
     */
    private final String plainName;

    /**
     * Class which extends cosmetic
     */
    private final Class<? extends BaseCosmetic> cosmeticClass;

    CosmeticCategory(ItemStack itemStack, String plainName, Class<? extends BaseCosmetic> cosmeticClass) {
        this.itemStack = itemStack;
        this.plainName = plainName;
        this.cosmeticClass = cosmeticClass;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public String getPlainName() {
        return this.plainName;
    }

    public Class<? extends BaseCosmetic> getCosmeticClass() {
        return this.cosmeticClass;
    }
}
