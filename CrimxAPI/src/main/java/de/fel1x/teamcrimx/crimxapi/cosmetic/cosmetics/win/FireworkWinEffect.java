package de.fel1x.teamcrimx.crimxapi.cosmetic.cosmetics.win;

import de.fel1x.teamcrimx.crimxapi.cosmetic.CosmeticCategory;
import de.fel1x.teamcrimx.crimxapi.cosmetic.WinAnimationCosmetic;
import de.fel1x.teamcrimx.crimxapi.support.CrimxSpigotAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class FireworkWinEffect extends WinAnimationCosmetic {

    public FireworkWinEffect(Player player, CrimxSpigotAPI crimxSpigotAPI) {
        super(player, crimxSpigotAPI);
    }

    @Override
    public Component getDisplayName() {
        return Component.text("Feuerwerk",  NamedTextColor.YELLOW)
                .decoration(TextDecoration.ITALIC, false);
    }

    @Override
    public Component[] getDescription() {
        return new Component[]{
                Component.empty(), Component.text("Der Klassiker", NamedTextColor.GRAY)
                .decoration(TextDecoration.ITALIC, false), Component.empty()
        };
    }

    @Override
    public Material getDisplayMaterial() {
        return Material.FIREWORK_ROCKET;
    }

    @Override
    public CosmeticCategory getCosmeticCategory() {
        return CosmeticCategory.WIN_ANIMATION;
    }

    @Override
    public int getCost() {
        return 500;
    }

    @Override
    public double maxDiscount() {
        return 0.2;
    }

    @Override
    public void win() {
        super.win();

        AtomicInteger atomicInteger = new AtomicInteger(5);
        new BukkitRunnable() {
            @Override
            public void run() {
                spawnFireworkCircle(FireworkWinEffect.this.player.getLocation(), 5, 10);
                if(atomicInteger.getAndDecrement() <= 0) {
                    this.cancel();
                }
            }
        }.runTaskTimer(this.crimxSpigotAPI, 0L, 20L);
    }

    private void spawnFireworkCircle(Location center, double radius, int amount) {

        World world = center.getWorld();
        double increment = (2 * Math.PI) / amount;
        for (int i = 0; i < amount; i++) {

            double angle = i * increment;
            double x = center.getX() + (radius * Math.cos(angle));
            double z = center.getZ() + (radius * Math.sin(angle));

            Location current = new Location(world, x, center.getY(), z);

            Firework firework = (Firework) current.getWorld().spawnEntity(current, EntityType.FIREWORK);

            FireworkMeta fireworkMeta = firework.getFireworkMeta();

            Random random = new Random();

            fireworkMeta.setPower(2);
            fireworkMeta.addEffect(org.bukkit.FireworkEffect.builder()
                    .with(org.bukkit.FireworkEffect.Type.values()[random.nextInt(org.bukkit.FireworkEffect.Type.values().length)])
                    .withColor(Color.fromBGR(random.nextInt(255), random.nextInt(255), random.nextInt(255))).flicker(true).build());
            firework.setFireworkMeta(fireworkMeta);
        }
    }
}
