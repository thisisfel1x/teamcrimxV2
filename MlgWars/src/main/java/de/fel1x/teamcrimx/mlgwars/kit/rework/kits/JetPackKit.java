package de.fel1x.teamcrimx.mlgwars.kit.rework.kits;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.kit.rework.Kit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

public class JetPackKit extends Kit {

    private int durability = 8;

    public JetPackKit(Player player, MlgWars mlgWars) {
        super(player, mlgWars);
    }

    @Override
    public void initializeKit() {
        super.initializeKit();
        this.player.getInventory().setItem(0, this.getInteractionItemStack());
    }

    @Override
    public @Nullable ItemStack getInteractionItemStack() {
        return new ItemBuilder(Material.BREWING_STAND)
                .setName(Component.text("● ", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false)
                        .append(Component.text("Jetpack ", NamedTextColor.GOLD)
                                .append(Component.text("x" + this.durability, NamedTextColor.WHITE, TextDecoration.ITALIC))))
                .setPDC(this.mlgWars, "KIT", this.getClass().getName())
                .toItemStack();
    }

    @Override
    public boolean shouldConsiderCooldown() {
        return true;
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        if(!event.hasItem() || event.hasItem() && event.getMaterial() != Material.BREWING_STAND) {
            return;
        }

        this.player.getWorld().spawnParticle(Particle.LAVA, this.player.getLocation(), 5, this.random.nextDouble(),
                this.random.nextDouble(), this.random.nextDouble());
        this.player.getWorld().spawnParticle(Particle.CLOUD, this.player.getLocation(), 5, this.random.nextDouble(),
                this.random.nextDouble(), this.random.nextDouble());

        this.player.playSound(this.player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1f, 2f);

        Vector velocity = this.player.getEyeLocation().getDirection();
        Vector toAdd = new Vector(0, 1, 0);

        toAdd.multiply(1.3F);
        toAdd.add(velocity.multiply(0.7F));

        this.player.setVelocity(toAdd);
        this.player.setFallDistance(0F);

        this.durability--;

        this.player.setCooldown(this.getInteractionItemStack().getType(), 30);
        this.setCooldown(1500);

        if(this.durability != 0) {
            this.updateDisplayName();
        }

        if(this.durability == 0) {
            this.player.playSound(this.player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1f, 1f);

            ItemStack held = this.player.getInventory().getItem(this.player.getInventory().getHeldItemSlot());
            if(held == null) {
                return;
            }

            this.player.getInventory().remove(held);
        }
    }

    private void updateDisplayName() {
        ItemStack itemStack = this.player.getInventory().getItemInMainHand();
        if(itemStack.getItemMeta() == null || itemStack.getItemMeta().displayName() == null) {
            return;
        }

        String displayName = LegacyComponentSerializer.legacySection().serialize(itemStack.getItemMeta().displayName());
        String first = displayName.split("x")[0];
        first+= "§fx" + this.durability;
        TextComponent component = LegacyComponentSerializer.legacySection().deserialize(first);
        itemStack.editMeta(itemMeta -> itemMeta.displayName(component));

        this.player.getInventory().setItemInMainHand(itemStack);
    }

    @EventHandler
    public void on(BlockPlaceEvent event) {
        if(!this.compareUniqueIDs(event.getPlayer().getUniqueId())) {
            return;
        }

        if(event.getItemInHand().equals(this.getInteractionItemStack())) {
            event.setCancelled(true);
        }

    }

    @Override
    public void run() {

    }
}
