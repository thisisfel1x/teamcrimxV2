package de.fel1x.teamcrimx.crimxapi.cosmetic;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class InventoryCosmetic extends BukkitRunnable {

    public Component getDisplayName() {
        return Component.text("FEHLER", NamedTextColor.DARK_RED);
    }

    public Component[] getDescription() {
        return new Component[] {
                Component.empty(),
                Component.text("Dieses Gadget ist nicht fertig und sollte dir nicht angezeigt werden!", NamedTextColor.RED),
                Component.empty()
        };
    }

    public Material getDisplayMaterial() {
        return Material.BARRIER;
    }

    public CosmeticCategory getCosmeticCategory() {
        return CosmeticCategory.GADGETS;
    }

    public int getCost() {
        return -1;
    }

    public double maxDiscount() {
        return 0.0;
    }

    public boolean canBeFoundInItemChests() {
        return true;
    }

    public boolean canBeBoughtInItemShop() {
        return true;
    }

    @Override
    public void run() {

    }
}
