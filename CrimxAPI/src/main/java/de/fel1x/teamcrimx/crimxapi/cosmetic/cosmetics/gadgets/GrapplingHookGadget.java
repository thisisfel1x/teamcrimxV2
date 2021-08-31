package de.fel1x.teamcrimx.crimxapi.cosmetic.cosmetics.gadgets;

import de.fel1x.teamcrimx.crimxapi.cosmetic.CosmeticCategory;
import de.fel1x.teamcrimx.crimxapi.cosmetic.Gadget;
import de.fel1x.teamcrimx.crimxapi.support.CrimxSpigotAPI;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class GrapplingHookGadget extends Gadget {

    public GrapplingHookGadget(Player player, CrimxSpigotAPI crimxSpigotAPI) {
        super(player, crimxSpigotAPI);
    }

    @Override
    public ItemStack getGadgetItemStack() {
        return new ItemBuilder(Material.FISHING_ROD)
                .setName(Component.text("● ", NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false)
                        .append(this.getDisplayName()))
                .setLore(this.getDescription())
                .setUnbreakable()
                .toItemStack();
    }

    @Override
    public Component getDisplayName() {
        return Component.text("Enterhaken", NamedTextColor.AQUA)
                .decoration(TextDecoration.ITALIC, false);
    }

    @Override
    public Component[] getDescription() {
        return new Component[]{
                Component.empty(), Component.text("Fliege durch die Lüfe (und das gratis!)", NamedTextColor.GRAY)
                .decoration(TextDecoration.ITALIC, false), Component.empty()
        };
    }

    @Override
    public Material getDisplayMaterial() {
        return Material.FISHING_ROD;
    }

    @Override
    public CosmeticCategory getCosmeticCategory() {
        return CosmeticCategory.GADGETS;
    }

    @Override
    public int getCost() {
        return 0;
    }

    @Override
    public boolean shouldListenToInteractEvent() {
        return false;
    }

    @EventHandler
    public void on(PlayerFishEvent event) {
        Player player = event.getPlayer();
        FishHook h = event.getHook();

        if ((event.getState().equals(PlayerFishEvent.State.IN_GROUND)) ||
                (event.getState().equals(PlayerFishEvent.State.CAUGHT_ENTITY)) ||
                (event.getState().equals(PlayerFishEvent.State.FAILED_ATTEMPT))) {
            if (player.getWorld().getBlockAt(h.getLocation().getBlockX(), h.getLocation().getBlockY() - 1, h.getLocation().getBlockZ()).getType() != Material.WATER) {
                Location from = player.getLocation();
                Location to = event.getHook().getLocation();

                double g = -0.08D;
                double t = to.distance(from);
                double v_x = (1.0D + 0.07D * t) * (to.getX() - from.getX()) / t;
                double v_y = (1.0D + 0.07D * t) * (to.getY() - from.getY()) / t - 1.2D * g * t; // Höhe; 0.5 standard
                double v_z = (1.0D + 0.07D * t) * (to.getZ() - from.getZ()) / t;

                Vector v = player.getVelocity();
                v.setX(v_x);
                v.setY(v_y);
                v.setZ(v_z);
                v.multiply(2);
                player.setVelocity(v);

                player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 3.0F, 2.0F);
            }
        }
    }
}
