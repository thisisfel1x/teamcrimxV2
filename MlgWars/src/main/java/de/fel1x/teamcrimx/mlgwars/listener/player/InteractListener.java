package de.fel1x.teamcrimx.mlgwars.listener.player;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractListener implements Listener {

    private final MlgWars mlgWars;

    public InteractListener(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
        this.mlgWars.getPluginManager().registerEvents(this, this.mlgWars);
    }

    @EventHandler
    public void on(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        GamePlayer gamePlayer = new GamePlayer(player);

        Gamestate gamestate = this.mlgWars.getGamestateHandler().getGamestate();

        if(gamestate == Gamestate.IDLE || gamestate == Gamestate.LOBBY) {
            if(event.hasItem()) {
                if(event.getMaterial() == Material.CHEST) {
                    player.sendMessage("kit");
                } else if(event.getMaterial() == Material.REDSTONE_TORCH_ON) {
                    player.sendMessage("forcemap");
                } else {
                    event.setCancelled(true);
                }
            }

        } else if(gamestate == Gamestate.DELAY || gamestate == Gamestate.ENDING) {
            event.setCancelled(true);
        } else {
            if(gamePlayer.isSpectator()) {
                event.setCancelled(true);
                return;
            }
        }

    }

}
