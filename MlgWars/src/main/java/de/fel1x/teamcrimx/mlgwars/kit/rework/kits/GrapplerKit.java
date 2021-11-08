package de.fel1x.teamcrimx.mlgwars.kit.rework.kits;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.kit.rework.Kit;
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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.util.Vector;

public class GrapplerKit extends Kit {

    public GrapplerKit(Player player, MlgWars mlgWars) {
        super(player, mlgWars);
    }

    @Override
    public void initializeKit() {
        super.initializeKit();
        this.player.getInventory().setItem(0, new ItemBuilder(Material.FISHING_ROD)
                .setName(Component.text("● ", NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false)
                        .append(Component.text("Enterhaken", NamedTextColor.AQUA)))
                .toItemStack());
    }

    @Override
    protected void onInteract(PlayerInteractEvent event) {

    }

    @EventHandler
    public void on(PlayerFishEvent event) {
        Player player = event.getPlayer();

        if(!this.player.getUniqueId().equals(player.getUniqueId())) {
            return;
        }

        FishHook fish = event.getHook();
        Location fishLocation = fish.getLocation().subtract(0, 1, 0);

        if ((event.getState() == PlayerFishEvent.State.IN_GROUND) ||
                (event.getState() == PlayerFishEvent.State.CAUGHT_ENTITY) ||
                (event.getState() == PlayerFishEvent.State.FAILED_ATTEMPT)) {
            if (player.getWorld().getBlockAt(fishLocation).getType() != Material.AIR) {
                if (player.getWorld().getBlockAt(fishLocation).getType() != Material.WATER) {
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
                    v.multiply(1.2);
                    player.setVelocity(v);

                    player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 3.0F, 2.0F);

                    ItemStack fishingRod = player.getInventory().getItem(player.getInventory().getHeldItemSlot());
                    if(fishingRod == null) {
                        return;
                    }

                    fishingRod.editMeta(itemMeta -> {
                        if(itemMeta instanceof Damageable damageable) {
                            damageable.setDamage(damageable.getDamage() + 9);
                        }
                    });
                }
            }
        }
    }

    @Override
    public void run() {

    }
}
