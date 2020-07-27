package de.fel1x.teamcrimx.mlgwars.kit.kits;

import de.fel1x.teamcrimx.mlgwars.kit.IKit;
import org.bukkit.Material;

public class StandardKit implements IKit {

    @Override
    public String kitName() {
        return "Starter";
    }

    @Override
    public String[] kitDescription() {
        return new String[] { "", "§7Jeder fängt mal klein an...", "§7Aber hiermit will keiner anfangen!", ""};
    }

    @Override
    public int kitCost() {
        return 0;
    }

    @Override
    public Material kitMaterial() {
        return Material.IRON_PICKAXE;
    }
}
