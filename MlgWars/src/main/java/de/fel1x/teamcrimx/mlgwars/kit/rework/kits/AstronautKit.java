package de.fel1x.teamcrimx.mlgwars.kit.rework.kits;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.crimxapi.utils.ProgressBar;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.kit.rework.Kit;
import de.fel1x.teamcrimx.mlgwars.kit.rework.KitRegistry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import org.spigotmc.event.entity.EntityDismountEvent;;

public class AstronautKit extends Kit {

    //private Zombie mobToControl = null;
    private ArmorStand mobToControl = null;
    private Firework firework = null;

    private int flightTime;

    public AstronautKit(Player player, MlgWars mlgWars) {
        super(player, mlgWars);
    }

    @Override
    public @Nullable ItemStack getInteractionItemStack() {
        return new ItemBuilder(Material.FIREWORK_ROCKET, 3)
                .setName(Component.text("● ", NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false)
                        .append(Component.text("Rakete", TextColor.fromHexString("#8FE40D"))))
                .setPDC(this.mlgWars, "KIT", this.getClass().getName())
                .toItemStack();
    }

    @Override
    public void initializeKit() {
        super.initializeKit();
        this.player.getInventory().setItem(0, this.getInteractionItemStack());
        this.player.getInventory().setHelmet(dev.triumphteam.gui.builder.item.ItemBuilder.from(Material.GLASS)
                .name(Component.text("Astronautenhelm", NamedTextColor.WHITE))
                .glow(true)
                .enchant(Enchantment.BINDING_CURSE)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                .build());

        this.player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 1));
        this.player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE, 1));

        this.runTaskTimer(this.mlgWars, 5 * 20L, 5L);
    }

    @Override
    public void disableKit() {
        super.disableKit();

        this.firework.remove();
        this.mobToControl.remove();

        this.firework = null;
        this.mobToControl = null;
    }

    @Override
    public boolean shouldConsiderCooldown() {
        return true;
    }

    @Override
    protected void onInteract(PlayerInteractEvent event) {
        if(this.mobToControl != null || this.firework != null) {
            return;
        }

        this.player.getWorld().spawn(this.player.getLocation(), ArmorStand.class, zombie -> {
            this.mobToControl = zombie;

            zombie.setSilent(true);
            zombie.setInvulnerable(true);
            zombie.setInvisible(true);
            zombie.setCollidable(false);

            this.player.getWorld().spawn(this.player.getLocation(), Firework.class, firework -> {
                firework.setShotAtAngle(true);
                firework.setInvulnerable(true);

                FireworkMeta fireworkMeta = firework.getFireworkMeta();
                fireworkMeta.setPower(24); // 1 -> 0,5sec flight time
                firework.setFireworkMeta(fireworkMeta);

                zombie.addPassenger(firework);
                firework.addPassenger(this.player);

                this.firework = firework;
            });
        });

        this.flightTime = 10 * 5; // 10 sec

        this.removeItemByAmount(1);
        this.setCooldown(1000 * 15);
        this.player.setCooldown(Material.FIREWORK_ROCKET, 20 * 15);
    }

    @EventHandler
    public void on(EntityDismountEvent event) {
        if(!this.compareUniqueIDs(event.getEntity().getUniqueId())) {
            return;
        }

        if(this.firework != null) {
            this.firework.remove();
            this.firework = null;
        }
        if(this.mobToControl != null) {
            this.mobToControl.remove();
            this.mobToControl = null;
        }
    }

    @Override
    public void run() {
        if(this.mobToControl != null && this.firework != null) {
            Vector direction = this.player.getEyeLocation().getDirection();
            direction.add(new Vector(0, 0.2, 0));
            direction.multiply(1.1F);

            this.mobToControl.setVelocity(direction);

            if(this.firework != null) {
                this.player.sendActionBar(this.mlgWars.getInventoryKitManager()
                        .getAvailableKits().get(KitRegistry.ASTRONAUT).getScoreboardName()
                        .append(Component.space())
                        .append(LegacyComponentSerializer.legacyAmpersand()
                        .deserialize(ProgressBar.getProgressBar(this.flightTime, 10 * 5, 25,'|', // █
                                this.flightTime < 20 ? (this.flightTime < 10 ? ChatColor.RED : ChatColor.YELLOW)
                                        : ChatColor.GREEN, ChatColor.DARK_GRAY))));

                if(this.flightTime < 20) {
                    if(this.flightTime < 10) {
                        this.player.playSound(this.player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 2f);
                    } else {
                        this.player.playSound(this.player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f);
                    }
                }

                if(this.flightTime > 0) {
                    this.flightTime--;
                }

                if(this.firework.isDead() || !this.firework.isValid()) {
                    this.firework = null;
                    this.mobToControl.remove();
                    this.mobToControl = null;

                    this.player.sendActionBar(Component.empty());
                    this.player.playSound(this.player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST_FAR, 1f, 0.5f);

                    this.player.sendActionBar(Component.empty());

                    try {
                        this.cancel();
                    } catch (Exception ignored) {}
                }
            }
        }
    }
}
