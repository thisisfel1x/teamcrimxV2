package de.fel1x.teamcrimx.mlgwars.listener.player;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodChangeListener implements Listener {

    private MlgWars mlgWars;

    public FoodChangeListener(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
        this.mlgWars.getPluginManager().registerEvents(this, this.mlgWars);
    }

    @EventHandler
    public void on(FoodLevelChangeEvent event) {

        if(!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        GamePlayer gamePlayer = new GamePlayer(player);

        Gamestate gamestate = this.mlgWars.getGamestateHandler().getGamestate();

        switch (gamestate) {

            case IDLE: case LOBBY: case DELAY: case ENDING:
                event.setCancelled(true);
                break;

            case PREGAME: case INGAME:
                if(gamePlayer.isSpectator()) {
                    event.setCancelled(true);
                }
        }
    }
}