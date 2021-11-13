package de.fel1x.teamcrimx.mlgwars.kit.rework.kits;

import de.fel1x.teamcrimx.crimxapi.utils.ProgressBar;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.kit.rework.Kit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class KangarooKit extends Kit {

    private boolean hasDelay;
    private int timer;

    public KangarooKit(Player player, MlgWars mlgWars) {
        super(player, mlgWars);
    }

    @Override
    public void initializeKit() {
        super.initializeKit();
        this.player.setAllowFlight(true);
        this.hasDelay = false;
    }

    @Override
    protected void onInteract(PlayerInteractEvent event) {

    }

    @EventHandler
    public void on(PlayerToggleFlightEvent event) {
        if(!this.compareUniqueIDs(event.getPlayer().getUniqueId())) {
            return;
        }

        if(this.hasDelay) {
            return;
        }

        event.setCancelled(true);
        this.player.setFlying(false);
        this.player.setAllowFlight(false);

        this.player.setVelocity(this.player.getLocation().getDirection().multiply(2.0D).setY(0.9D));
        this.player.playSound(this.player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 5, 3);

        this.timer = 5;
        this.hasDelay = true;

        this.runTaskTimer(this.mlgWars, 0L, 20L);

    }

    @Override
    public void run() {
        this.gamePlayer.getMlgActionbar().sendActionbar(this.player, "§aKänguru  §8● "
                + ProgressBar.getProgressBar(this.timer, 60, 15,
                '█', ChatColor.GREEN, ChatColor.DARK_GRAY));

        if(this.timer <= 0) {
            this.hasDelay = false;
            this.player.setAllowFlight(true);
            this.cancel();
        }

        this.timer--;
    }
}
