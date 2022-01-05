package de.fel1x.teamcrimx.mlgwars.kit.rework.kits;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.kit.rework.Kit;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class ThorKit extends Kit {

    public ThorKit(Player player, MlgWars mlgWars) {
        super(player, mlgWars);
    }

    @Override
    public void initializeKit() {
        super.initializeKit();
        this.player.getInventory().setItem(0, this.getInteractionItemStack());
    }

    @Override
    public @Nullable ItemStack getInteractionItemStack() {
        return ItemBuilder.from(Material.GOLDEN_AXE)
                .name(this.mlgWars.miniMessage().parse("<rainbow>Thors Axt LOLOLOLOLOL"))
                .enchant(Enchantment.DAMAGE_ALL)
                .unbreakable()
                .pdc(persistentDataContainer -> persistentDataContainer.set(new NamespacedKey(this.mlgWars, "KIT"),
                        PersistentDataType.STRING, this.getClass().getName()))
                .build();
    }

    @Override
    public boolean shouldConsiderCooldown() {
        return true;
    }

    @Override
    public void disableKit() {
        super.disableKit();
    }

    @Override
    protected void onInteract(PlayerInteractEvent event) {
        if(this.mlgWars.getGamestateHandler().getGamestate() != Gamestate.INGAME) {
            return;
        }

        this.player.setCooldown(Material.GOLDEN_AXE, 20 * 7); // 5 sec cooldown
        this.setCooldown(1000 * 7);

        Block targetBlock = this.player.getTargetBlock(100);
        Entity targetEntity = this.player.getTargetEntity(100);

        if (targetBlock == null && targetEntity == null) {
            return;
        }

        if (targetBlock != null && targetBlock.getType() == Material.AIR) {
            targetBlock = null;
        }

        Location targetLocation = null;

        if (targetEntity instanceof Player) {
            targetLocation = targetEntity.getLocation();
        } else if (targetBlock != null) {
            targetLocation = targetBlock.getLocation();
        }

        if (targetLocation == null) {
            return;
        }

        Player closestPlayer = null;
        if (targetEntity == null) {
            double closestDistance = -1.0;
            for (Player possiblePlayer : targetLocation.getNearbyEntitiesByType(Player.class, 5)) {
                if(this.mlgWars.getData().getSpectators().contains(possiblePlayer)) {
                    continue;
                }
                double distance = targetLocation.distanceSquared(possiblePlayer.getLocation());
                if (closestDistance != -1.0 && !(distance < closestDistance)) {
                    continue;
                }
                closestDistance = distance;
                closestPlayer = possiblePlayer;
            }
        }

        Location finalLocation = closestPlayer != null ? closestPlayer.getLocation() : targetLocation;

        this.player.getWorld().strikeLightningEffect(finalLocation).setFlashCount(5);
        this.player.getWorld().spawn(finalLocation, TNTPrimed.class, tnt -> {
            tnt.setMetadata("THOR", new FixedMetadataValue(this.mlgWars, true));
            tnt.setFuseTicks(0);
        });
    }

    @EventHandler
    public void on(EntityExplodeEvent event) {
        if(!(event.getEntity() instanceof TNTPrimed tntPrimed)) {
            return;
        }
        if(!tntPrimed.hasMetadata("THOR")) {
            return;
        }
        for (Block block : event.blockList()) {
            FallingBlock fallingBlock = block.getWorld().spawnFallingBlock(block.getLocation(), Bukkit.createBlockData(block.getType()));
            fallingBlock.setDropItem(false);
            Vector velocity = fallingBlock.getVelocity().add(block.getLocation().toVector());
            velocity.setY(0.75f);
            velocity.multiply(0.43f);
            fallingBlock.setVelocity(velocity);
        }
    }

    @Override
    public void run() {

    }
}
