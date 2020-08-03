package de.fel1x.teamcrimx.mlgwars.kit.kits;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.kit.IKit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class KangarooKit implements IKit {

    @Override
    public String getKitName() {
        return "Känguru";
    }

    @Override
    public String[] getKitDescription() {
        return new String[] {
                "§7Springe höher als die höchsten,", "§7und weiter als die weitesten!", ""
        };
    }

    @Override
    public int getKitCost() {
        return 3500;
    }

    @Override
    public Material getKitMaterial() {
        return Material.SLIME_BALL;
    }

    @Override
    public void setKitInventory(Player player) {
        player.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE)
                .setLeatherArmorColor(Color.ORANGE)
                .toItemStack());

        player.setAllowFlight(true);
        player.setMetadata("essence", new FixedMetadataValue(MlgWars.getInstance(), 10));
    }
}
