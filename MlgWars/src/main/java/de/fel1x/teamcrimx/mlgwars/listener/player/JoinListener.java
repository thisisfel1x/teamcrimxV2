package de.fel1x.teamcrimx.mlgwars.listener.player;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.enums.Spawns;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import de.fel1x.teamcrimx.mlgwars.timer.LobbyTimer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final MlgWars mlgWars;

    public JoinListener(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
        this.mlgWars.getPluginManager().registerEvents(this, this.mlgWars);
    }

    @EventHandler
    public void on(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        GamePlayer gamePlayer = new GamePlayer(player);

        event.setJoinMessage(null);

        gamePlayer.cleanUpOnJoin();

        Gamestate gamestate = this.mlgWars.getGamestateHandler().getGamestate();

        switch (gamestate) {

            case IDLE: case LOBBY:

                gamePlayer.addToPlayers();
                gamePlayer.setJoinItems();
                gamePlayer.teleport(Spawns.LOBBY);

                break;


            case DELAY: case PREGAME: case INGAME:

                gamePlayer.addToSpectators();
                gamePlayer.teleport(Spawns.SPECTATOR);

                break;

            case ENDING:

                player.sendMessage(this.mlgWars.getPrefix() + "§7Das Spiel ist bereits vorbei!");
                gamePlayer.teleport(Spawns.LOBBY);

                break;

        }

        if(gamestate == Gamestate.IDLE) {
           if(!this.mlgWars.isInSetup() && !this.mlgWars.isNoMap()) {
               int neededPlayers = (this.mlgWars.getTeamSize() * 2);
               if(this.mlgWars.getData().getPlayers().size() >= neededPlayers) {
                   this.mlgWars.startTimerByClass(LobbyTimer.class);
               }
           }
        }

    }

}
