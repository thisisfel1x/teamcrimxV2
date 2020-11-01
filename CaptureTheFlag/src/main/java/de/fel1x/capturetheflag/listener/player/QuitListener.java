package de.fel1x.capturetheflag.listener.player;

import de.fel1x.capturetheflag.CaptureTheFlag;
import de.fel1x.capturetheflag.Data;
import de.fel1x.capturetheflag.gameplayer.GamePlayer;
import de.fel1x.capturetheflag.gamestate.Gamestate;
import de.fel1x.capturetheflag.team.Teams;
import de.fel1x.capturetheflag.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    private CaptureTheFlag captureTheFlag;

    public QuitListener(CaptureTheFlag captureTheFlag) {
        this.captureTheFlag = captureTheFlag;
        this.captureTheFlag.getPluginManager().registerEvents(this, this.captureTheFlag);
    }

    @EventHandler
    public void on(PlayerQuitEvent event) {

        event.setQuitMessage(null);

        Player player = event.getPlayer();
        GamePlayer gamePlayer = new GamePlayer(player);
        Data data = captureTheFlag.getData();

        Gamestate gamestate = this.captureTheFlag.getGamestateHandler().getGamestate();

        gamePlayer.cleanupTeams();
        gamePlayer.removeFromSpectators();
        gamePlayer.removeFromInGamePlayers();

        switch (gamestate) {

            case LOBBY:

                event.setQuitMessage("§c« " + player.getDisplayName() + " §7hat das Spiel verlassen");

                if (this.captureTheFlag.getLobbyTimer().isRunning()) {
                    if (data.getPlayers().size() < 2) {
                        this.captureTheFlag.getLobbyTimer().stop();
                        return;
                    }
                }

                break;

            case INGAME:

                if (gamePlayer.isPlayer()) {
                    event.setQuitMessage("§c« " + player.getDisplayName() + " §7hat das Spiel verlassen");
                }

                if (Teams.RED.getTeamPlayers().size() == 0) {

                    Utils.win(Teams.BLUE);

                } else if (Teams.BLUE.getTeamPlayers().size() == 0) {

                    Utils.win(Teams.RED);

                }


                if (data.getPlayers().size() == 0) {
                    Bukkit.getScheduler().cancelTasks(this.captureTheFlag);
                    this.captureTheFlag.getEndingTimer().start();
                    return;
                }

                if (Bukkit.getOnlinePlayers().size() == 0) {
                    Bukkit.getServer().shutdown();
                    return;
                }

                break;

            case ENDING:

                if (Bukkit.getOnlinePlayers().size() == 0) {
                    Bukkit.getServer().shutdown();
                    return;
                }

                break;

        }

    }

}
