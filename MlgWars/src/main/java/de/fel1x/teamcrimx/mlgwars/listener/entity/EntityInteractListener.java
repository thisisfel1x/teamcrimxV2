package de.fel1x.teamcrimx.mlgwars.listener.entity;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import org.bukkit.Material;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class EntityInteractListener implements Listener {

    private MlgWars mlgWars;

    public EntityInteractListener(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
        this.mlgWars.getPluginManager().registerEvents(this, this.mlgWars);
    }

    @EventHandler
    public void on(PlayerInteractEntityEvent event) {

        Player player = event.getPlayer();

        if (event.getRightClicked() instanceof MushroomCow) {

            MushroomCow mushroomCow = (MushroomCow) event.getRightClicked();

            if (player.getInventory().getItemInHand().getType() != Material.BOWL) {
                return;
            }

            if (!mushroomCow.hasMetadata("usesLeft")) {
                mushroomCow.setMetadata("usesLeft", new FixedMetadataValue(this.mlgWars, 20));
            }

            int usesLeft = mushroomCow.getMetadata("usesLeft").get(0).asInt() - 1;
            mushroomCow.setMetadata("usesLeft", new FixedMetadataValue(this.mlgWars, usesLeft));
            mushroomCow.setCustomName("§a" + usesLeft + " §7Nutzungen übrig");

            if (mushroomCow.getMetadata("usesLeft").get(0).asInt() <= 0) {
                mushroomCow.remove();
            }

        }

    }

    private void removeItem(Player player) {
        ItemStack inHand = player.getInventory().getItemInHand();
        if (inHand.getAmount() > 1) {
            inHand.setAmount(inHand.getAmount() - 1);
        } else {
            player.getInventory().remove(inHand);
        }
    }

}
