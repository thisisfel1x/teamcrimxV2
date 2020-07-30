package de.fel1x.teamcrimx.mlgwars.kit.kits;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.kit.IKit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ThrowerKit implements IKit {

    @Override
    public String getKitName() {
        return "Werfer";
    }

    @Override
    public String[] getKitDescription() {
        return new String[] {
                "§7Werfe mit TNT um dich", "§7und verwirre deine Gegner", ""
        };
    }

    @Override
    public int getKitCost() {
        return 3000;
    }

    @Override
    public Material getKitMaterial() {
        return Material.STICK;
    }

    @Override
    public void setKitInventory(Player player) {
        player.getInventory().addItem(
                new ItemBuilder(Material.TNT, 12)
                        .setName("§8● §7Werfbares §cTNT")
                        .toItemStack(),
                new ItemBuilder(Material.FIREBALL, 16)
                        .setName("§8● §7Werfbare §6Feuerkugeln")
                        .toItemStack(),
                new ItemBuilder(Material.SKULL_ITEM, 16)
                        .setColor(1)
                        .setName("§8● §7Werfbare §0Witherköpfe")
                        .toItemStack());
    }
}
