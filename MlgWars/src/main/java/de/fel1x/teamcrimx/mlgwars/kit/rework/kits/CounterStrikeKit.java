package de.fel1x.teamcrimx.mlgwars.kit.rework.kits;

import com.destroystokyo.paper.ParticleBuilder;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.kit.rework.Kit;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicInteger;

public class CounterStrikeKit extends Kit {

    public CounterStrikeKit(Player player, MlgWars mlgWars) {
        super(player, mlgWars);
    }

    @Override
    public void initializeKit() {
        super.initializeKit();
        this.player.getInventory().addItem(
                ItemBuilder.star()
                        .effect(FireworkEffect.builder().withColor(Color.YELLOW).build())
                        .name(Component.text("Flash", NamedTextColor.YELLOW))
                        .pdc(persistentDataContainer ->
                                persistentDataContainer.set(new NamespacedKey(this.mlgWars,
                                        this.getClass().getName()), PersistentDataType.INTEGER, 1))
                        .amount(5)
                        .build(),
                ItemBuilder.star()
                        .effect(FireworkEffect.builder().withColor(Color.RED).build())
                        .name(Component.text("Moli", NamedTextColor.RED))
                        .pdc(persistentDataContainer ->
                                persistentDataContainer.set(new NamespacedKey(this.mlgWars,
                                        this.getClass().getName()), PersistentDataType.INTEGER, 1))
                        .amount(5)
                        .build(),
                ItemBuilder.star()
                        .effect(FireworkEffect.builder().withColor(Color.WHITE).build())
                        .name(Component.text("Smoke", NamedTextColor.WHITE))
                        .pdc(persistentDataContainer ->
                                persistentDataContainer.set(new NamespacedKey(this.mlgWars,
                                        this.getClass().getName()), PersistentDataType.INTEGER, 1))
                        .amount(5)
                        .build());
    }

    @Override
    public void disableKit() {
        super.disableKit();
    }

    @Override
    protected void onInteract(PlayerInteractEvent event) {
        ItemStack interactedItem = event.getItem();

        if(interactedItem == null || !this.checkPersistentDataContainer(interactedItem, this.getClass())) {
            return;
        }

        if(interactedItem.getItemMeta() == null ||
                !interactedItem.getItemMeta().hasDisplayName()
                || interactedItem.getItemMeta().displayName() == null) {
            return;
        }

        switch (PlainTextComponentSerializer.plainText().serialize(interactedItem.getItemMeta().displayName())) {
            case "Flash" -> {
                this.removeItemByAmount(1);
                this.player.launchProjectile(Snowball.class)
                        .setMetadata("FLASH", new FixedMetadataValue(this.mlgWars, true));
            }
            case "Moli" -> {
                this.removeItemByAmount(1);
                this.player.launchProjectile(Fireball.class).getVelocity().multiply(1.2f);
            }
            case "Smoke" -> {
                this.removeItemByAmount(1);
                this.player.launchProjectile(Snowball.class)
                        .setMetadata("SMOKE", new FixedMetadataValue(this.mlgWars, true));
            }
        }

    }

    @EventHandler
    public void on(ProjectileHitEvent event) {
        if(!(event.getEntity() instanceof Snowball snowball)) {
            return;
        }

        if(!(event.getEntity().getShooter() instanceof Player player)) {
            return;
        }

        if(!this.compareUniqueIDs(player.getUniqueId())) {
            return;
        }

        if(snowball.hasMetadata("FLASH")) {
            AtomicInteger atomicInteger = new AtomicInteger(20);
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.getWorld().spawn(snowball.getLocation().clone().add(0, 1, 0), Firework.class, firework -> {
                       FireworkMeta fireworkMeta = firework.getFireworkMeta();
                       fireworkMeta.setPower(0);
                       fireworkMeta.addEffect(FireworkEffect.builder()
                               .withColor(Color.WHITE, Color.YELLOW, Color.GRAY).build());
                       firework.setFireworkMeta(fireworkMeta);
                       firework.detonate();
                    });
                    snowball.getNearbyEntities(5, 5, 5).forEach(entity -> {
                        if (!(entity instanceof LivingEntity livingEntity)) return;
                        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,
                                20 * 2, 4, true, true));
                    });

                    if(atomicInteger.getAndDecrement() <= 0) {
                        this.cancel();
                    }
                }
            }.runTaskTimer(this.mlgWars, 0L, 10L);
        } else if(snowball.hasMetadata("SMOKE")) {
            AtomicInteger atomicInteger = new AtomicInteger(20);
            new BukkitRunnable() {
                @Override
                public void run() {
                    new ParticleBuilder(Particle.EXPLOSION_HUGE)
                            .location(snowball.getLocation().clone().add(0, 2, 0))
                            .count(3)
                            .spawn();

                    snowball.getNearbyEntities(5, 5, 5).forEach(entity -> {
                        if (!(entity instanceof LivingEntity livingEntity)) return;
                        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,
                                35, 1, true, true));
                    });

                    if(atomicInteger.getAndDecrement() <= 0) {
                        this.cancel();
                    }
                }
            }.runTaskTimer(this.mlgWars, 0L, 10L);
        }

    }

    @Override
    public void run() {

    }
}
