package de.fel1x.teamcrimx.mlgwars.listener.player;

import de.fel1x.teamcrimx.crimxapi.utils.Cuboid;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.enums.Spawns;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class MoveListener implements Listener {

    private final MlgWars mlgWars;
    private final List<Player> borderPlayers;

    public MoveListener(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
        this.mlgWars.getPluginManager().registerEvents(this, this.mlgWars);

        this.borderPlayers = new ArrayList<>();
    }

    @EventHandler
    public void on(PlayerMoveEvent event) {

        Player player = event.getPlayer();
        Location location = player.getLocation();
        GamePlayer gamePlayer = new GamePlayer(player);

        Gamestate gamestate = this.mlgWars.getGamestateHandler().getGamestate();

        if (gamestate == Gamestate.DELAY) {
            if (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getY() != event.getTo().getY() || event.getFrom().getZ() != event.getTo().getZ()) {
                player.teleport(location.setDirection(event.getTo().getDirection()));
            }
        } else if(gamestate == Gamestate.PREGAME || gamestate == Gamestate.INGAME) {

            Cuboid mapCuboid = this.mlgWars.getData().getMapRegion();
            Location middle = Spawns.SPECTATOR.getLocation();

            if (!mapCuboid.contains(player.getLocation())) {
                int aX = middle.getBlockX();
                int aY = middle.getBlockY();
                int aZ = middle.getBlockZ();

                int bX = player.getLocation().getBlockX();
                int bY = player.getLocation().getBlockY();
                int bZ = player.getLocation().getBlockZ();

                int x = aX - bX;
                int y = aY - bY;
                int z = aZ - bZ;

                Vector vector = new Vector(x, y, z).normalize();
                vector.multiply(1.5D);
                vector.setY(1.2D);

                if (gamePlayer.isPlayer()) {

                    if (!borderPlayers.contains(player)) {
                        player.sendMessage(this.mlgWars.getPrefix() + "§cDu darfst die Map nicht verlassen!");
                        borderPlayers.add(player);

                        player.setVelocity(vector);
                        player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 5, 3);
                        player.playEffect(EntityEffect.HURT);

                        if (player.getHealth() - 5 >= 0) {
                            player.setHealth(player.getHealth() - 5);
                        } else {
                            player.setHealth(0);
                        }

                        Bukkit.getScheduler().scheduleSyncDelayedTask(MlgWars.getInstance(), () -> borderPlayers.remove(player), 30L);
                    }

                } else {
                    player.teleport(Spawns.SPECTATOR.getLocation());
                }
            }
        }
    }
}
