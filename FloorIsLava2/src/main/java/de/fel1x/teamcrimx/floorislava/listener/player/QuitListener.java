package de.fel1x.teamcrimx.floorislava.listener.player;

import de.fel1x.teamcrimx.floorislava.FloorIsLava;
import de.fel1x.teamcrimx.floorislava.gamehandler.Gamestate;
import de.fel1x.teamcrimx.floorislava.gameplayer.GamePlayer;
import de.fel1x.teamcrimx.floorislava.tasks.EndingTask;
import de.fel1x.teamcrimx.floorislava.tasks.IdleTask;
import de.fel1x.teamcrimx.floorislava.utils.WinDetection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {
    private final FloorIsLava floorIsLava;

    public QuitListener(FloorIsLava floorIsLava) {
        this.floorIsLava = floorIsLava;
        floorIsLava.getPluginManager().registerEvents(this, floorIsLava);
    }

    @EventHandler
    public void on(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = new GamePlayer(player);
        Gamestate gamestate = this.floorIsLava.getGamestateHandler().getGamestate();
        event.setQuitMessage(null);
        int neededPlayers = 3 - this.floorIsLava.getData().getPlayers().size();
        switch (gamestate) {
            case IDLE:
            case LOBBY:
                event.setQuitMessage(this.floorIsLava.getPrefix() + "§a" + player.getName() + " §7hat das Spiel verlassen");
                gamePlayer.removeFromInGamePlayers();
                break;
            case FARMING:
            case RISING:
                if (gamePlayer.isPlayer()) {
                    event.setQuitMessage(this.floorIsLava.getPrefix() + "§a" + player.getName() + " §7hat das Spiel verlassen");
                    gamePlayer.saveStats();
                }
                if (this.floorIsLava.getData().getPlayers().isEmpty() && !Bukkit.getOnlinePlayers().isEmpty()) {
                    this.floorIsLava.startTimerByClass(EndingTask.class);
                    break;
                }
                if (Bukkit.getOnlinePlayers().isEmpty())
                    Bukkit.getServer().shutdown();
                new WinDetection();
                break;
            case PREGAME:
                if (gamePlayer.isPlayer())
                    event.setQuitMessage(this.floorIsLava.getPrefix() + "§a" + player.getName() + " §7hat das Spiel verlassen");
                break;
            case ENDING:
                if (Bukkit.getOnlinePlayers().isEmpty())
                    Bukkit.getServer().shutdown();
                event.setQuitMessage(this.floorIsLava.getPrefix() + "§a" + player.getName() + " §7hat das Spiel verlassen");
                break;
        }
        if (this.floorIsLava.getData().getPlayers().size() < 3 && gamestate
                .equals(Gamestate.LOBBY))
            this.floorIsLava.startTimerByClass(IdleTask.class);
    }
}
