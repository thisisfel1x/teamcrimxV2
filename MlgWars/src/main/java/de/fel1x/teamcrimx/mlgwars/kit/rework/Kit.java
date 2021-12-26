package de.fel1x.teamcrimx.mlgwars.kit.rework;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.UUID;

public abstract class Kit extends BukkitRunnable implements Listener {

    private final NamespacedKey kitNamespaceKey;

    public Player player;
    public MlgWars mlgWars;
    public GamePlayer gamePlayer;

    public Random random = new Random();

    public Kit(Player player, MlgWars mlgWars) {
        this.player = player;
        this.mlgWars = mlgWars;
        this.gamePlayer = this.mlgWars.getData().getGamePlayers().get(this.player.getUniqueId());

        this.kitNamespaceKey = new NamespacedKey(Kit.this.mlgWars, "KIT");

        // Register Listener
        this.mlgWars.getPluginManager().registerEvents(this, this.mlgWars);
        this.mlgWars.getPluginManager().registerEvents(new KitInteractionListener(), this.mlgWars);
    }

    public @Nullable ItemStack getInteractionItemStack() {
        return null;
    }

    public void initializeKit() {
        this.mlgWars.getData().getGamePlayers().get(this.player.getUniqueId()).setActiveKit(this);
    }

    public void disableKit() {
        // Unregister all listeners
        HandlerList.unregisterAll(this);

        try {
            this.cancel();
        } catch (Exception ignored) {

        }
    }

    protected abstract void onInteract(PlayerInteractEvent event);

    public boolean shouldConsiderCooldown() {
        return false;
    }

    public void setCooldown(long timeInMillis) {
        this.player.setMetadata("kitDelay", new FixedMetadataValue(this.mlgWars,
                System.currentTimeMillis() + timeInMillis));
    }

    public void drawCooldownIndicator() {
        // TODO
    }

    public boolean compareUniqueIDs(UUID toCompare) {
        return toCompare.equals(this.player.getUniqueId());
    }

    public void removeItemByAmount(int amount) {
        int heldItemSlot = this.player.getInventory().getHeldItemSlot();
        ItemStack heldItem = this.player.getInventory().getItem(heldItemSlot);

        if(heldItem == null) {
            return;
        }

        if(heldItem.getAmount() - amount <= 0) {
            this.player.getInventory().setItem(heldItemSlot, null);
            return;
        }

        heldItem.setAmount(heldItem.getAmount() - amount);

        this.player.getInventory().setItem(heldItemSlot, heldItem);

        if(this.getInteractionItemStack() != null) {
            this.getInteractionItemStack().setAmount(heldItem.getAmount() - amount);
        }

    }

    /**
     * Sets the new owner of the kit
     * @param player new player who 'equipped' the kit
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    public class KitInteractionListener implements Listener {
        @EventHandler(priority = EventPriority.HIGHEST)
        private void on(PlayerInteractEvent event) {
            if(!event.getPlayer().getUniqueId().equals(Kit.this.player.getUniqueId())) {
                return;
            }

            if(!event.hasItem()) {
                return;
            }

            if(Kit.this.getInteractionItemStack() != null) {
                if(!Kit.this.compareItemStack(event.getItem())) {
                    return;
                }
            }

            if(Kit.this.shouldConsiderCooldown()) {
                if (Kit.this.player.hasMetadata("kitDelay")) {
                    long delay = Kit.this.player.getMetadata("kitDelay").get(0).asLong();

                    if (delay > System.currentTimeMillis()) {
                        // TODO: send cooldown message
                        double secs = (delay - System.currentTimeMillis()) / 1000.0;
                        secs = Math.round(secs * 10);
                        Kit.this.player.sendMessage(Kit.this.mlgWars.prefix()
                                .append(Component.text(String.format("Bitte warte einen Moment (Item-Cooldown: %ss)",
                                                secs / 10),
                                        TextColor.fromHexString("#e5680b"))));
                        return;
                    }
                }
            }

            onInteract(event);
        }
    }

    public boolean compareItemStack(ItemStack itemStack) {
        if(this.getInteractionItemStack() != null && itemStack != null) {
            return itemStack.getType() == getInteractionItemStack().getType()
                    && itemStack.getItemMeta().getPersistentDataContainer()
                    .has(this.kitNamespaceKey, PersistentDataType.STRING);
        }
        return false;
    }

    public boolean checkPersistentDataContainer(@NotNull ItemStack itemStack, @NotNull Class<? extends Kit> clazz) {
       return itemStack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(this.mlgWars,
               clazz.getName()), PersistentDataType.INTEGER);
    }

}
