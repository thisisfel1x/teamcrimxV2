package de.fel1x.teamcrimx.mlgwars.kit.rework.kits;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.kit.rework.Kit;
import org.bukkit.Material;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

public class BoatGliderKit extends Kit {

    public BoatGliderKit(Player player, MlgWars mlgWars) {
        super(player, mlgWars);
    }

    @Override
    public void initializeKit() {
        super.initializeKit();
        this.player.getInventory().setItem(0, this.getInteractionItemStack());
    }

    @Override
    public @Nullable ItemStack getInteractionItemStack() {
        return new ItemBuilder(Material.OAK_BOAT, 5)
                .setName(this.mlgWars.miniMessage().deserialize("<rainbow>MLG BOOOOOOOOOT"))
                .setPDC(this.mlgWars, "KIT", this.getClass().getName())
                .toItemStack();
    }

    @Override
    public boolean shouldConsiderCooldown() {
        return true;
    }

    @Override
    protected void onInteract(PlayerInteractEvent event) {
        this.removeItemByAmount(1);
        this.player.setCooldown(Material.OAK_BOAT, 100);
        this.setCooldown(5000);

        this.player.getWorld().spawn(this.player.getLocation(), Boat.class, boat -> {
            boat.addPassenger(this.player);
            Vector playerVector = this.player.getEyeLocation().getDirection().normalize();
            Vector toSet = new Vector(0, 10, 0);
            toSet.add(playerVector.multiply(2));
            boat.setVelocity(toSet);
        });
    }

    @Override
    public void run() {

    }
}
