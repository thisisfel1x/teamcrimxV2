package de.fel1x.teamcrimx.mlgwars.kit.rework.kits;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.kit.rework.Kit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class StarterKit extends Kit {

    public StarterKit(Player player, MlgWars mlgWars) {
        super(player, mlgWars);
    }

    @Override
    public void initializeKit() {
        super.initializeKit();
        this.player.getInventory().setItem(0, new ItemBuilder(Material.GOLDEN_SWORD)
                .setName(Component.text("● ", NamedTextColor.DARK_GRAY)
                        .append(Component.text("Müll", NamedTextColor.GOLD))
                        .append(Component.text(" (umtauschen verboten!)", NamedTextColor.GRAY)))
                .setLore(Component.empty(), Component.empty(), Component.empty(), Component.empty(),
                                Component.empty(), Component.empty(), Component.empty(), Component.empty(),
                                Component.text("(kann Gift verursachen)",
                                        TextColor.fromHexString("#00bb2d")).decorate(TextDecoration.ITALIC))
                .setUnbreakable()
                .toItemStack());
    }

    @Override
    protected void onInteract(PlayerInteractEvent event) {

    }

    @Override
    public void run() {

    }
}
