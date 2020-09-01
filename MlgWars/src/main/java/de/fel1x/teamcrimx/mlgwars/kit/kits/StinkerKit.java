package de.fel1x.teamcrimx.mlgwars.kit.kits;

import de.fel1x.teamcrimx.mlgwars.kit.IKit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class StinkerKit implements IKit {

    @Override
    public String getKitName() {
        return "Stinker";
    }

    @Override
    public String[] getKitDescription() {
        return new String[]{
                "§7Scheisse, der Deo hat versagt!", "§7Betäube deine Mitspieler mit deinem Geruch",
                "§c(andere Stinker sind immun!)", "", "§6[für Boerek_Obama]"
        };
    }

    @Override
    public int getKitCost() {
        return 0;
    }

    @Override
    public Material getKitMaterial() {
        return Material.POISONOUS_POTATO;
    }

    @Override
    public void setKitInventory(Player player) {

    }
}
