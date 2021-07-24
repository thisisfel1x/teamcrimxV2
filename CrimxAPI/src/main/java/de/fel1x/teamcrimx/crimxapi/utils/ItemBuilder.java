package de.fel1x.teamcrimx.crimxapi.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.*;

/**
 * Easily create itemstacks, without messing your hands.
 * <i>Note that if you do use this in one of your projects, leave this notice.</i>
 * <i>Please do credit me if you do use this in one of your projects.</i>
 *
 * @author NonameSL
 * @author fel1x - reworked this for paper 1.16 and the new components {@link net.kyori.adventure.text.Component}
 */
public class ItemBuilder {
    private final ItemStack itemStack;

    /**
     * Create a new ItemBuilder from scratch.
     *
     * @param material The material to create the ItemBuilder with.
     */
    public ItemBuilder(Material material) {
        this(material, 1);
    }

    /**
     * Create a new ItemBuilder over an existing itemstack.
     *
     * @param itemStack The itemstack to create the ItemBuilder over.
     */
    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemBuilder(String base64) {
        this.itemStack = SkullCreator.itemFromBase64(base64);
    }

    /**
     * Create a new ItemBuilder from scratch.
     *
     * @param material The material of the item.
     * @param amount   The amount of the item.
     */
    public ItemBuilder(Material material, int amount) {
        this.itemStack = new ItemStack(material, amount);
    }

    /**
     * Clone the ItemBuilder into a new one.
     *
     * @return The cloned instance.
     */
    public ItemBuilder clone() {
        return new ItemBuilder(this.itemStack);
    }

    /**
     * Set the displayname of the item.
     *
     * @param name The name to change it to.
     * @deprecated Use {@link de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder#setName(Component)}
     */
    @Deprecated
    public ItemBuilder setName(String name) {
        ItemMeta im = this.itemStack.getItemMeta();
        im.setDisplayName(name);
        this.itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Set the displayname of the item.
     *
     * @param component The component to change it to
     */
    public ItemBuilder setName(Component component) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.displayName(component);
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }


    /**
     * Add an unsafe enchantment.
     *
     * @param enchantment The enchantment to add.
     * @param level       The level to put the enchant on.
     */
    public ItemBuilder addUnsafeEnchantment(Enchantment enchantment, int level) {
        this.itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    /**
     * Remove a certain enchant from the item.
     *
     * @param enchantment The enchantment to remove
     */
    public ItemBuilder removeEnchantment(Enchantment enchantment) {
        this.itemStack.removeEnchantment(enchantment);
        return this;
    }

    /**
     * Set the skull owner for the item. Works on skulls only.
     *
     * @param owner The name of the skull's owner.
     */
    public ItemBuilder setSkullOwner(String owner) {
        try {
            SkullMeta im = (SkullMeta) this.itemStack.getItemMeta();
            // TODO: performance
            im.setPlayerProfile(Bukkit.createProfile(owner));
            this.itemStack.setItemMeta(im);
        } catch (ClassCastException expected) {
        }
        return this;
    }

    /**
     * Add an enchant to the item.
     *
     * @param enchantment The enchant to add
     * @param level       The level
     */
    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        ItemMeta im = this.itemStack.getItemMeta();
        im.addEnchant(enchantment, level, true);
        this.itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Add multiple enchants at once.
     *
     * @param enchantments The enchants to add.
     */
    public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments) {
        this.itemStack.addEnchantments(enchantments);
        return this;
    }

    /**
     * Re-sets the lore.
     *
     * @param lore The lore to set it to.
     * @deprecated Use {@link de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder#setLore(Component...)}
     */
    @Deprecated
    public ItemBuilder setLore(String... lore) {
        ItemMeta im = this.itemStack.getItemMeta();
        im.setLore(Arrays.asList(lore));
        this.itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Re-sets the lore.
     *
     * @param lore The lore to set it to.
     */
    public ItemBuilder setLore(Component... lore) {
        ItemMeta im = this.itemStack.getItemMeta();
        im.lore(Arrays.asList(lore));
        this.itemStack.setItemMeta(im);
        return this;
    }


    /**
     * Re-sets the lore.
     *
     * @param lore The lore to set it to.
     * @Deprecated Use {@link de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder#setLoreByComponentList(List)}
     */
    @Deprecated
    public ItemBuilder setLore(List<String> lore) {
        ItemMeta im = this.itemStack.getItemMeta();
        im.setLore(lore);
        this.itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Re-sets the lore.
     *
     * @param lore The lore to set it to.
     */
    public ItemBuilder setLoreByComponentList(List<Component> lore) {
        ItemMeta im = this.itemStack.getItemMeta();
        im.lore(lore);
        this.itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Remove a lore line.
     *
     * @param line The lore to remove.
     * @deprecated Use {@link de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder#removeLoreLine(Component)}
     */
    @Deprecated
    public ItemBuilder removeLoreLine(String line) {
        ItemMeta im = this.itemStack.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        if (!lore.contains(line)) return this;
        lore.remove(line);
        im.setLore(lore);
        this.itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Remove a lore line.
     *
     * @param toRemove The lore to remove.
     */
    public ItemBuilder removeLoreLine(Component toRemove) {
        ItemMeta im = this.itemStack.getItemMeta();
        if (im.lore() == null) {
            return this;
        }

        List<Component> lore = new ArrayList<>(Objects.requireNonNull(im.lore()));
        if (!lore.contains(toRemove)) return this;
        lore.remove(toRemove);
        im.lore(lore);
        this.itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Remove a lore line.
     *
     * @param index The index of the lore line to remove.
     */
    public ItemBuilder removeLoreLine(int index) {
        ItemMeta im = this.itemStack.getItemMeta();
        if (im.lore() == null) {
            return this;
        }

        List<Component> lore = new ArrayList<>(Objects.requireNonNull(im.lore()));
        if (index < 0 || index > lore.size()) return this;
        lore.remove(index);
        im.lore(lore);
        this.itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Add a lore line.
     *
     * @param line The lore line to add.
     * @deprecated Use {@link de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder#addLoreLine(Component)}
     */
    @Deprecated
    public ItemBuilder addLoreLine(String line) {
        ItemMeta im = this.itemStack.getItemMeta();
        List<String> lore = new ArrayList<>();
        if (im.hasLore()) lore = new ArrayList<>(im.getLore());
        lore.add(line);
        im.setLore(lore);
        this.itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Add a lore line.
     *
     * @param toAdd The lore line to add.
     */
    public ItemBuilder addLoreLine(Component toAdd) {
        ItemMeta im = this.itemStack.getItemMeta();
        if (im.lore() == null) {
            return this;
        }

        List<Component> lore = new ArrayList<>();
        if (im.hasLore()) lore = new ArrayList<>(Objects.requireNonNull(im.lore()));
        lore.add(toAdd);
        im.lore(lore);
        this.itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Add a lore line.
     *
     * @param line The lore line to add.
     * @param pos  The index of where to put it.
     * @deprecated Use {@link de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder#addLoreLine(Component, int)}
     */
    @Deprecated
    public ItemBuilder addLoreLine(String line, int pos) {
        ItemMeta im = this.itemStack.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        lore.set(pos, line);
        im.setLore(lore);
        this.itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Add a lore line.
     *
     * @param toAdd The lore line to add.
     * @param pos   The index of where to put it.
     */
    public ItemBuilder addLoreLine(Component toAdd, int pos) {
        ItemMeta im = this.itemStack.getItemMeta();
        if (im.lore() == null) {
            return this;
        }

        List<Component> lore = new ArrayList<>(Objects.requireNonNull(im.lore()));
        lore.set(pos, toAdd);
        im.lore(lore);
        this.itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Adds glow to an itemstack
     */
    public ItemBuilder addGlow() {
        ItemMeta im = this.itemStack.getItemMeta();
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        this.itemStack.setItemMeta(im);
        this.addEnchant(Enchantment.DAMAGE_ARTHROPODS, 1);
        return this;
    }

    /**
     * Adds glow to an itemstack
     *
     * @param glow glow or not
     */
    public ItemBuilder addGlow(boolean glow) {
        if (glow) {
            return this.addGlow();
        }
        return this;
    }

    /**
     * Sets an itemstack unbreakable
     */
    public ItemBuilder setUnbreakable() {
        ItemMeta im = this.itemStack.getItemMeta();
        im.setUnbreakable(true);
        this.itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Sets the dye color of a wool item. Works only on wool.
     *
     * @param color The DyeColor to set the wool item to.
     * @see ItemBuilder@setDyeColor(DyeColor)
     * @deprecated As of version 1.2 changed to setDyeColor.
     */
    @Deprecated
    public ItemBuilder setWoolColor(DyeColor color) {
        return this;
    }

    /**
     * Sets the armor color of a leather armor piece. Works only on leather armor pieces.
     *
     * @param color The color to set it to.
     */
    public ItemBuilder setLeatherArmorColor(Color color) {
        try {
            LeatherArmorMeta im = (LeatherArmorMeta) this.itemStack.getItemMeta();
            im.setColor(color);
            this.itemStack.setItemMeta(im);
        } catch (ClassCastException expected) {
        }
        return this;
    }

    /**
     * Retrieves the itemstack from the ItemBuilder.
     *
     * @return The itemstack created/modified by the ItemBuilder instance.
     */
    public ItemStack toItemStack() {
        return this.itemStack;
    }

    /**
     * @deprecated data isn't used anymore
     */
    public ItemBuilder setColor(int data) {

        this.itemStack.setDurability((short) data);
        return this;

    }

    /**
     * Sets a PotionEffect to a (splash) potion or tipped arrow
     *
     * @param potionEffectType {@link org.bukkit.potion.PotionType} to set
     */
    public ItemBuilder setPotionEffect(PotionType potionEffectType) {
        if (this.itemStack.getType() == Material.SPLASH_POTION
                || this.itemStack.getType() == Material.POTION || this.itemStack.getType() == Material.TIPPED_ARROW) {
            PotionMeta potionMeta = (PotionMeta) this.itemStack.getItemMeta();
            potionMeta.setBasePotionData(new PotionData(potionEffectType));
            this.itemStack.setItemMeta(potionMeta);
        }
        return this;
    }

    /**
     * Sets a PotionEffect to a tipped arrow, used for color
     *
     * @param potionEffectType {@link org.bukkit.potion.PotionType} to set
     */
    public ItemBuilder setArrowEffect(PotionType potionEffectType) {
        if (this.itemStack.getType() == Material.TIPPED_ARROW) {
            PotionMeta potionMeta = (PotionMeta) this.itemStack.getItemMeta();
            potionMeta.setBasePotionData(new PotionData(potionEffectType));
            this.itemStack.setItemMeta(potionMeta);
        }
        return this;
    }
}
