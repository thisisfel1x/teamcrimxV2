package de.fel1x.teamcrimx.crimxapi.cosmetic.cosmetics.gadgets;

import com.destroystokyo.paper.ParticleBuilder;
import de.fel1x.teamcrimx.crimxapi.cosmetic.CosmeticCategory;
import de.fel1x.teamcrimx.crimxapi.cosmetic.Gadget;
import de.fel1x.teamcrimx.crimxapi.support.CrimxSpigotAPI;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class FunGunGadget extends Gadget {

    public FunGunGadget(Player player, CrimxSpigotAPI crimxSpigotAPI) {
        super(player, crimxSpigotAPI);
    }

    @Override
    public Component getDisplayName() {
        return Component.text("FunGun", NamedTextColor.YELLOW)
                .decoration(TextDecoration.ITALIC, false);
    }

    @Override
    public Component[] getDescription() {
        return new Component[]{
                Component.empty(), Component.text("Fun? Gun! - der Klassiker", NamedTextColor.GRAY)
                .asComponent().decoration(TextDecoration.ITALIC, false), Component.empty()
        };
    }

    @Override
    public Material getDisplayMaterial() {
        return Material.BLAZE_ROD;
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
        //player.getInventory().setItem(this.getSlot(player), this.getGadgetItemStack());
    }

    @Override
    public void stopCosmetic(Player player) {
        super.stopCosmetic(player);
        //player.getInventory().setItem(this.getSlot(player), null);
    }

    @Override
    public ItemStack getGadgetItemStack() {
        return new ItemBuilder(Material.BLAZE_ROD)
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
        Snowball snowball = player.launchProjectile(Snowball.class);
        snowball.setGlowing(true);
        snowball.setMetadata(this.getClass().getName(), new FixedMetadataValue(CrimxSpigotAPI.getInstance(), null));
    }

    @Override
    public void onLeftClickInteract(Player player) {
        super.onLeftClickInteract(player);
    }

    @EventHandler
    public void on(ProjectileHitEvent event) {
        Entity entity = event.getEntity();
        Location hitLocation = entity.getLocation();

        if (entity instanceof Snowball && entity.hasMetadata(this.getClass().getName())) {

            event.setCancelled(true);

            new ParticleBuilder(Particle.HEART).allPlayers().location(hitLocation).count(4)
                    .offset(this.random.nextDouble(), this.random.nextDouble(), this.random.nextDouble()).spawn();
            new ParticleBuilder(Particle.LAVA).allPlayers().location(hitLocation).count(4)
                    .offset(this.random.nextDouble(), this.random.nextDouble(), this.random.nextDouble()).spawn();
            new ParticleBuilder(Particle.SNOWBALL).allPlayers().location(hitLocation).count(4)
                    .offset(this.random.nextDouble(), this.random.nextDouble(), this.random.nextDouble()).spawn();

            hitLocation.getWorld().playSound(hitLocation, Sound.ENTITY_CAT_PURREOW, 3f, 0.75f);
        }
    }

    @Override
    public void run() {

    }
}
