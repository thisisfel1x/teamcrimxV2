package de.fel1x.teamcrimx.mlgwars.kit.rework.kits;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.kit.rework.Kit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

public class PullerKit extends Kit {

    public PullerKit(Player player, MlgWars mlgWars) {
        super(player, mlgWars);
    }

    @Override
    public void initializeKit() {
        super.initializeKit();
        this.player.getInventory().setItem(0, this.getInteractionItemStack());
    }

    @Override
    public @Nullable ItemStack getInteractionItemStack() {
        return new ItemBuilder(Material.FISHING_ROD)
                .setName(Component.text("● ", NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false)
                        .append(Component.text("Grabscher", NamedTextColor.DARK_AQUA)))
                .setPDC(this.mlgWars, "KIT", this.getClass().getName())
                .toItemStack();
    }

    @Override
    protected void onInteract(PlayerInteractEvent event) {

    }

    @EventHandler
    public void on(PlayerFishEvent event) {
        Player player = event.getPlayer();

        if(!this.compareUniqueIDs(player.getUniqueId()))

        if(!this.compareItemStack(event.getPlayer().getInventory().getItemInMainHand())) {
            return;
        }

        if (event.getCaught() instanceof LivingEntity livingEntity) {
            Vector direction = player.getLocation().toVector().subtract(livingEntity.getLocation().toVector()).normalize();
            direction.multiply(1.5D).setY(1);
            livingEntity.setVelocity(direction);
        }
    }

    @Override
    public void run() {
    }
}
