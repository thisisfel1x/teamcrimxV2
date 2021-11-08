package de.fel1x.teamcrimx.mlgwars.kit.rework.kits;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.kit.rework.Kit;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class ThrowerKit extends Kit {

    public ThrowerKit(Player player, MlgWars mlgWars) {
        super(player, mlgWars);
    }

    @Override
    public void initializeKit() {
        super.initializeKit();
        this.player.getInventory().addItem(ItemBuilder.from(Material.TNT).name(this.mlgWars.miniMessage()
                .parse("<red>Werfbares TNT")).amount(16).pdc(persistentDataContainer ->
                persistentDataContainer.set(new NamespacedKey(this.mlgWars, this.getClass().getName()), PersistentDataType.INTEGER, 1))
                .build(),
                ItemBuilder.from(Material.WITHER_SKELETON_SKULL).amount(8)
                        .pdc(persistentDataContainer ->
                                persistentDataContainer.set(new NamespacedKey(this.mlgWars, this.getClass().getName()), PersistentDataType.INTEGER, 1))
                        .build(),
                ItemBuilder.from(Material.FIRE_CHARGE).amount(10)
                        .pdc(persistentDataContainer ->
                                persistentDataContainer.set(new NamespacedKey(this.mlgWars, this.getClass().getName()), PersistentDataType.INTEGER, 1))
                        .build());
    }

    @Override
    protected void onInteract(PlayerInteractEvent event) {
        ItemStack interactedItem = event.getItem();
        if(interactedItem == null
                || !interactedItem.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(this.mlgWars,
                this.getClass().getName()), PersistentDataType.INTEGER)) {
            return;
        }

        Material interactedMaterial = interactedItem.getType();

        switch (interactedMaterial) {
            case TNT -> {
                this.removeItemByAmount(1);
                TNTPrimed tnt = this.player.getWorld().spawn(this.player.getEyeLocation(), TNTPrimed.class);
                tnt.setVelocity(this.player.getEyeLocation().getDirection().multiply(1.75).setY(0.25D));
                tnt.setFuseTicks(50);
            }
            case WITHER_SKELETON_SKULL -> {
                this.removeItemByAmount(1);
                this.player.launchProjectile(WitherSkull.class);
            }
            case FIRE_CHARGE -> {
                this.removeItemByAmount(1);
                this.player.launchProjectile(Fireball.class);
            }
        }
    }

    @Override
    public void run() {

    }
}
