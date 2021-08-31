package de.fel1x.teamcrimx.crimxapi.cosmetic.cosmetics.gadgets;

import de.fel1x.teamcrimx.crimxapi.cosmetic.CosmeticCategory;
import de.fel1x.teamcrimx.crimxapi.cosmetic.Gadget;
import de.fel1x.teamcrimx.crimxapi.support.CrimxSpigotAPI;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class ChickenBombGadget extends Gadget {

    private final List<Item> items = new ArrayList<>();

    public ChickenBombGadget(Player player, CrimxSpigotAPI crimxSpigotAPI) {
        super(player, crimxSpigotAPI);
    }

    @Override
    public Component getDisplayName() {
        return Component.text("Chickenator", NamedTextColor.YELLOW)
                .decoration(TextDecoration.ITALIC, false);
    }

    @Override
    public Component[] getDescription() {
        return new Component[]{
                Component.empty(), Component.text("Beschreibung folgt", NamedTextColor.GRAY)
                .decoration(TextDecoration.ITALIC, false), Component.empty()
        };
    }

    @Override
    public Material getDisplayMaterial() {
        return Material.FEATHER;
    }

    @Override
    public CosmeticCategory getCosmeticCategory() {
        return CosmeticCategory.GADGETS;
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
    public void startCosmetic(Player player) {
        super.startCosmetic(player);
    }

    @Override
    public void stopCosmetic(Player player) {
        super.stopCosmetic(player);
    }

    @Override
    public ItemStack getGadgetItemStack() {
        return new ItemBuilder(Material.FEATHER)
                .setName(Component.text("● ", NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false)
                        .append(this.getDisplayName()))
                .setLore(this.getDescription())
                .toItemStack();
    }

    @Override
    public int getSlot(Player player) {
        return super.getSlot(player);
    }

    @Override
    public void onRightClickInteract(Player player) {
        final Chicken chicken = (Chicken) player.getWorld().spawnEntity(player.getEyeLocation(), EntityType.CHICKEN);
        chicken.setNoDamageTicks(500);
        chicken.setVelocity(player.getLocation().getDirection().multiply(Math.PI / 1.5));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_CHICKEN_AMBIENT, 1.4f, 1.5f);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.4f, 1.5f);
        Bukkit.getScheduler().runTaskLater(CrimxSpigotAPI.getInstance(), () -> {
            spawnRandomFirework(chicken.getLocation());
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_CHICKEN_HURT, 1.4f, 1.5f);
            chicken.remove();
            for (int i = 0; i < 20; i++) {
                final Item ITEM = chicken.getWorld().dropItem(chicken.getLocation(), new ItemBuilder(Material.COOKED_CHICKEN).addGlow(this.random.nextBoolean()).toItemStack());
                ITEM.setPickupDelay(30000);
                ITEM.setVelocity(new Vector(this.random.nextDouble() - 0.5, this.random.nextDouble() / 2.0, this.random.nextDouble() - 0.5));
                this.items.add(ITEM);
            }
            Bukkit.getScheduler().runTaskLater(CrimxSpigotAPI.getInstance(), () -> this.items.forEach(Item::remove), 50);
        }, 9);
        player.updateInventory();
    }

    @Override
    public void onLeftClickInteract(Player player) {
        super.onLeftClickInteract(player);
    }

    public static FireworkEffect getRandomFireworkEffect() {
        FireworkEffect.Builder builder = FireworkEffect.builder();
        return builder.flicker(false).trail(false).with(FireworkEffect.Type.BALL_LARGE)
                .withColor(Color.WHITE).withFade(Color.WHITE).build();
    }

    public void spawnRandomFirework(Location location) {
        final ArrayList<Firework> fireworks = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Firework f = location.getWorld().spawn(location, Firework.class);

            FireworkMeta fm = f.getFireworkMeta();
            fm.addEffect(getRandomFireworkEffect());
            f.setFireworkMeta(fm);
            fireworks.add(f);
        }
        Bukkit.getScheduler().runTaskLater(CrimxSpigotAPI.getInstance(), () -> {
            for (Firework f : fireworks)
                f.detonate();
        }, 2);
    }

    @Override
    public void run() {

    }
}
