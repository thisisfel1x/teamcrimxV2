package de.fel1x.teamcrimx.mlgwars.listener.block;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    private final MlgWars mlgWars;

    public BlockBreakListener(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
        this.mlgWars.getPluginManager().registerEvents(this, this.mlgWars);
    }

    @EventHandler
    public void on(BlockBreakEvent event) {

        Player player = event.getPlayer();
        GamePlayer gamePlayer = new GamePlayer(player);

        Gamestate gamestate = this.mlgWars.getGamestateHandler().getGamestate();

        switch (gamestate) {

            case IDLE: case LOBBY: case DELAY: case ENDING:
                event.setCancelled(true);
                break;

            case PREGAME: case INGAME:
                if(gamePlayer.isSpectator()) {
                    event.setCancelled(true);
                    return;
                } else {
                    if(this.mlgWars.isLabor()) {
                        event.getBlock().setType(Material.AIR);
                        event.getBlock().getWorld().spawnEntity(event.getBlock().getLocation(), EntityType.PRIMED_TNT);
                    }
                }

        }

    }

}
