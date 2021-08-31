package de.fel1x.teamcrimx.crimxapi.cosmetic.cosmetics.gadgets;

import de.fel1x.teamcrimx.crimxapi.cosmetic.CosmeticCategory;
import de.fel1x.teamcrimx.crimxapi.cosmetic.Gadget;
import de.fel1x.teamcrimx.crimxapi.support.CrimxSpigotAPI;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class MelonThrowerGadget extends Gadget {

    private final List<Item> items;

    public MelonThrowerGadget(Player player, CrimxSpigotAPI crimxSpigotAPI) {
        super(player, crimxSpigotAPI);
        this.items = new ArrayList<>();
    }

    @Override
    public Component getDisplayName() {
        return Component.text("Melonenkanone", NamedTextColor.YELLOW)
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
        return Material.MELON_SLICE;
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
        return new ItemBuilder(Material.MELON_SEEDS)
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
    protected void onRightClickInteract(Player player) {
        World world = player.getWorld();
        Location location = player.getLocation().clone().add(0, 5, 0);
        world.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 2f);

        for(int i = 0; i < 20; i++) {
            int randomX = this.random.nextBoolean() ? 1 : -1 * this.random.nextInt(4);
            int randomZ = this.random.nextBoolean() ? 1 : -1 * this.random.nextInt(4);

            world.dropItem(location.clone().add(randomX, 0, randomZ), new ItemStack(Material.MELON_SLICE), item -> {
               item.setMetadata("melon", new FixedMetadataValue(this.crimxSpigotAPI, null));
               item.setGlowing(true);
               this.items.add(item);
            });
            Bukkit.getScheduler().runTaskLater(this.crimxSpigotAPI, () -> {
                this.items.forEach(Item::remove);
                this.items.clear();
            }, 5 * 20L);
        }
    }

    @EventHandler(ignoreCancelled = false)
    public void on(EntityPickupItemEvent event) {
        if(event.getEntity() instanceof Player player
                && event.getItem().getItemStack().getType() == Material.MELON_SLICE) {
            event.setCancelled(true);
            event.getItem().remove();
            this.items.remove(event.getItem());
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5 * 20, 2));
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 0.5f, 2.5f);
        }
    }
}
