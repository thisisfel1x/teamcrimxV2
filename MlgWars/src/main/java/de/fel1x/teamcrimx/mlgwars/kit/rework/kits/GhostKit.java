package de.fel1x.teamcrimx.mlgwars.kit.rework.kits;

import de.fel1x.teamcrimx.crimxapi.utils.ProgressBar;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.kit.rework.Kit;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

public class GhostKit extends Kit {

    private int timer = 80;
    private boolean active = false;
    private boolean debugFlag = false;

    public GhostKit(Player player, MlgWars mlgWars) {
        super(player, mlgWars);
    }

    @Override
    public void initializeKit() {
        super.initializeKit();
        this.player.getInventory().setItem(0, this.getInteractionItemStack());
    }

    @Override
    public @Nullable ItemStack getInteractionItemStack() {
        return ItemBuilder.from(Material.POTION)
                .name(this.mlgWars.miniMessage().parse("<white>Trank der Unsichtbarkeit"))
                .glow()
                .pdc(persistentDataContainer -> persistentDataContainer.set(new NamespacedKey(this.mlgWars, "KIT"),
                        PersistentDataType.STRING, this.getClass().getName()))
                .build();
    }

    @Override
    public void disableKit() {
        super.disableKit();

        this.active = false;

        this.player.setInvisible(false);
        this.gamePlayer.setActionbarOverridden(false);
    }

    @Override
    protected void onInteract(PlayerInteractEvent event) {
        if(!this.debugFlag) {
            this.removeItemByAmount(1);
        }

        this.active = true;

        this.player.setInvisible(true);
        this.player.getWorld().playSound(this.player.getLocation(), Sound.ENTITY_VILLAGER_WORK_CLERIC, 1f, 1f);
        this.gamePlayer.setActionbarOverridden(true);

        this.runTaskTimer(this.mlgWars, 0L, 5L);
    }

    @EventHandler
    public void on(PlayerMoveEvent event) {
        if(!this.compareUniqueIDs(event.getPlayer().getUniqueId())) {
            return;
        }
        if(!this.active) {
            return;
        }

        /*Vector vectorFrom = event.getFrom().toVector();
        Vector vectorTo = event.getTo().toVector();
        Vector subtract = vectorTo.subtract(vectorFrom);

        if(this.player.getLocation().getBlock().isSolid()) {
            if(this.player.isSneaking()) {
                //subtract.add(this.player.getEyeLocation().toVector());
                subtract.setY(-0.075);
            } else {
                subtract.setY(1.25).multiply(2);
            }
        }

        this.player.setVelocity(subtract);
        this.player.getWorld().spawnParticle(Particle.GLOW, this.player.getLocation(), 1); */
    }

    @Override
    public void run() {
        this.gamePlayer.getMlgActionbar().sendActionbar(this.player, "§fGeist  §8● "
                + ProgressBar.getProgressBar(this.timer, 80, 5,
                '█', ChatColor.GREEN, ChatColor.DARK_GRAY));

        if(this.timer <= 0) {
            this.cancel();
            this.active = false;

            this.player.setInvisible(false);
            this.gamePlayer.setActionbarOverridden(false);
        }

        this.timer--;
    }
}
