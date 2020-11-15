package de.fel1x.bingo.objects;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

public class BingoItem {

    Material material;
    Enchantment enchantment;
    BingoDifficulty bingoDifficulty;

    public BingoItem(Material material, BingoDifficulty bingoDifficulty) {
        this.material = material;
        this.bingoDifficulty = bingoDifficulty;
    }

    public BingoItem(Material material, Enchantment enchantment, BingoDifficulty bingoDifficulty) {
        this.material = material;
        this.enchantment = enchantment;
        this.bingoDifficulty = bingoDifficulty;
    }

    public Material getMaterial() {
        return this.material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Enchantment getEnchantment() {
        return this.enchantment;
    }

    public void setEnchantment(Enchantment enchantment) {
        this.enchantment = enchantment;
    }

    public BingoDifficulty getBingoDifficulty() {
        return this.bingoDifficulty;
    }

    public void setBingoDifficulty(BingoDifficulty bingoDifficulty) {
        this.bingoDifficulty = bingoDifficulty;
    }
}
