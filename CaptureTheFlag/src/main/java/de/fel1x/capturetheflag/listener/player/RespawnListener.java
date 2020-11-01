package de.fel1x.capturetheflag.listener.player;

import de.fel1x.capturetheflag.CaptureTheFlag;
import de.fel1x.capturetheflag.filehandler.SpawnHandler;
import de.fel1x.capturetheflag.gameplayer.GamePlayer;
import de.fel1x.capturetheflag.gamestate.Gamestate;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnListener implements Listener {

    private CaptureTheFlag captureTheFlag;

    public RespawnListener(CaptureTheFlag captureTheFlag) {
        this.captureTheFlag = captureTheFlag;
        this.captureTheFlag.getPluginManager().registerEvents(this, this.captureTheFlag);
    }

    @EventHandler
    public void on(PlayerRespawnEvent event) {

        Player player = event.getPlayer();
        GamePlayer gamePlayer = new GamePlayer(player);

        Gamestate gamestate = this.captureTheFlag.getGamestateHandler().getGamestate();

        switch (gamestate) {

            case INGAME:
            case PREGAME:

                if (gamePlayer.isPlayer()) {

                    event.setRespawnLocation(gamePlayer.getRespawnLocation());
                    player.playSound(player.getLocation(), Sound.ENTITY_WOLF_HURT, 20, 15);
                    gamePlayer.setKitItems();

                } else {

                    try {
                        event.setRespawnLocation(SpawnHandler.loadLocation("spectator"));
                    } catch (Exception ignored) {

                    }

                }

                break;

            default:

                SpawnHandler.loadLocation("lobby");

        }

    }

}
