package de.fel1x.teamcrimx.mlgwars.kit.rework.kits;

import de.fel1x.teamcrimx.crimxapi.utils.Actionbar;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.kit.rework.Kit;
import de.fel1x.teamcrimx.mlgwars.utils.Tornado;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class TornadoKit extends Kit {

    public TornadoKit(Player player, MlgWars mlgWars) {
        super(player, mlgWars);
    }

    @Override
    public void initializeKit() {
        super.initializeKit();
        this.player.getInventory().setItem(0, this.getInteractionItemStack());
    }

    @Override
    public @Nullable ItemStack getInteractionItemStack() {
        return new ItemBuilder(Material.DEAD_BUSH, 3)
                .setName(Component.text("● ", NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false)
                        .append(Component.text("Tornado", NamedTextColor.WHITE)))
                .setPDC(this.mlgWars, "KIT", this.getClass().getName())
                .toItemStack();
    }

    @Override
    public boolean shouldConsiderCooldown() {
        return true;
    }

    @Override
    protected void onInteract(PlayerInteractEvent event) {
        if(this.mlgWars.getGamestateHandler().getGamestate() != Gamestate.INGAME) {
            return;
        }

        Block block = this.player.getTargetBlock(120);
        if(block == null) {
            return;
        }

        this.removeItemByAmount(1);
        this.player.setCooldown(Material.DEAD_BUSH, 20 * 3);
        this.setCooldown(3000);

        Location location = block.getLocation();
        for (Player nearbyPlayer : location.getNearbyPlayers(5)) {
            nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 5,4));
            nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 5,4));
        }

        Tornado.spawnTornado(MlgWars.getInstance(), location, block.getType(), block.getData(),
                null, 0.6, 50, 20 * 10, true, true);

        location.getWorld().strikeLightningEffect(location.clone().add(1, 0, 0));
        location.getWorld().strikeLightningEffect(location.clone().add(-1, 0, 0));
        location.getWorld().strikeLightningEffect(location.clone().add(0, 0, 1));
        location.getWorld().strikeLightningEffect(location.clone().add(0, 0, -1));

        for (Player player1 : this.mlgWars.getData().getPlayers()) {
            if (player1.equals(this.player)) continue;

            player1.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 5, 1, false, false));
            player1.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 5, 1, false, false));

            Actionbar.sendFullTitle(player1, "§4" + this.player.getDisplayName(),
                    "§7eskaliert komplett!", 5, 20, 5);
        }
    }

    @EventHandler
    public void on(BlockPlaceEvent event) {
        if(!this.compareUniqueIDs(event.getPlayer().getUniqueId())) {
            return;
        }

        ItemStack placedBlockItem = event.getItemInHand();

        if(this.compareItemStack(placedBlockItem)) {
            event.setCancelled(true);
        }
    }

    @Override
    public void run() {

    }
}
