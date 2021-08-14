package de.fel1x.teamcrimx.crimxapi.cosmetic;

import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.support.CrimxSpigotAPI;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public abstract class Gadget implements Listener {

    public Gadget() {
        Bukkit.getPluginManager().registerEvents(new GadgetListener(), CrimxSpigotAPI.getInstance());
        Bukkit.getPluginManager().registerEvents(this, CrimxSpigotAPI.getInstance());
    }

    public ItemStack getGadgetItemStack() {
        return new ItemBuilder(Material.BARRIER)
                .setName(Component.text("FEHLER", NamedTextColor.DARK_RED))
                .setLore(Component.empty(), Component.text("FEHLER", NamedTextColor.DARK_RED), Component.empty())
                .toItemStack();
    }

    public int getSlot(Player player) {
        return player.hasPermission("crimxlobby.vip") ? 2 : 4;
    }

    public void onRightClickInteract(Player player) {

    }

    public void onLeftClickInteract(Player player) {

    }

    public class GadgetListener implements Listener {

        @EventHandler
        public void on(PlayerInteractEvent event) {
            Player player = event.getPlayer();

            if (event.getPlayer().getInventory().getHeldItemSlot() != getSlot(player)) {
                return;
            }

            if (event.getHand() != EquipmentSlot.HAND) {
                return;
            }

            if (!event.hasItem()) {
                return;
            }

            if (player.hasMetadata("gadgetDelay")) {
                long delay = player.getMetadata("gadgetDelay").get(0).asLong();

                if (delay > System.currentTimeMillis()) {
                    player.sendMessage(Component.text().append(CrimxAPI.gadgetPrefix())
                            .append(Component.text("Bitte warte einen Moment", NamedTextColor.RED))
                            .asComponent().decoration(TextDecoration.ITALIC, false));
                    return;
                }
            }

            player.setMetadata("gadgetDelay", new FixedMetadataValue(CrimxSpigotAPI.getInstance(),
                    System.currentTimeMillis() + (1000 * 5)));

            switch (event.getAction()) {
                case RIGHT_CLICK_AIR, RIGHT_CLICK_BLOCK -> onRightClickInteract(event.getPlayer());
                case LEFT_CLICK_AIR, LEFT_CLICK_BLOCK -> onLeftClickInteract(event.getPlayer());
            }
        }
    }
}
