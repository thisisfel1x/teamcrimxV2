package de.fel1x.teamcrimx.mlgwars.listener.player;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.kit.Kit;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.util.Vector;

public class FishListener implements Listener {

    private MlgWars mlgWars;

    public FishListener(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
        this.mlgWars.getPluginManager().registerEvents(this, this.mlgWars);
    }

    @EventHandler
    public void on(PlayerFishEvent event) {

        Player player = event.getPlayer();
        GamePlayer gamePlayer = new GamePlayer(player);
        Kit kit = gamePlayer.getSelectedKit();

        FishHook fish = event.getHook();
        Location fishLocation = fish.getLocation().subtract(0, 1, 0);

        Gamestate gamestate = this.mlgWars.getGamestateHandler().getGamestate();

        if (gamestate != Gamestate.PREGAME && gamestate != Gamestate.INGAME) return;

        if (kit == Kit.GRAPPLER) {
            if ((event.getState().equals(PlayerFishEvent.State.IN_GROUND)) ||
                    (event.getState().equals(PlayerFishEvent.State.CAUGHT_ENTITY)) ||
                    (event.getState().equals(PlayerFishEvent.State.FAILED_ATTEMPT))) {
                if (player.getWorld().getBlockAt(fishLocation).getType() != Material.AIR) {
                    if (player.getWorld().getBlockAt(fishLocation).getType() != Material.WATER) {
                        Location from = player.getLocation();
                        Location to = event.getHook().getLocation();

                        from.setY(from.getY() + 0.5D);
                        player.teleport(from);

                        double g = -0.08D;
                        double t = to.distance(from);
                        double v_x = (1.0D + 0.07D * t) * (to.getX() - from.getX()) / t;
                        double v_y = (1.0D + 0.07D * t) * (to.getY() - from.getY()) / t - 1.2D * g * t; // Höhe; 0.5 standard
                        double v_z = (1.0D + 0.07D * t) * (to.getZ() - from.getZ()) / t;

                        Vector v = player.getVelocity();
                        v.setX(v_x);
                        v.setY(v_y);
                        v.setZ(v_z);
                        player.setVelocity(v);

                        player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 3.0F, 2.0F);
                    }
                }
            }
        } else if (kit == Kit.PULLER) {
            if (event.getCaught() instanceof Player) {
                Player caughtPlayer = (Player) event.getCaught();

                Vector direction = player.getLocation().toVector().subtract(caughtPlayer.getLocation().toVector()).normalize();
                direction.multiply(1.5D).setY(1);
                caughtPlayer.setVelocity(direction);
            }
        }
    }
}
