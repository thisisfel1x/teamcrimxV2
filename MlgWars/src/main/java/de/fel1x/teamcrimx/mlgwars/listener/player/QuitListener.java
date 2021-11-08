package de.fel1x.teamcrimx.mlgwars.listener.player;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import de.fel1x.teamcrimx.mlgwars.timer.IdleTimer;
import de.fel1x.teamcrimx.mlgwars.utils.WinDetection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    private final MlgWars mlgWars;

    public QuitListener(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
        this.mlgWars.getPluginManager().registerEvents(this, this.mlgWars);
    }

    @EventHandler
    public void on(PlayerQuitEvent event) {

        Player player = event.getPlayer();
        GamePlayer gamePlayer = this.mlgWars.getData().getGamePlayers().get(player.getUniqueId());

        String message = null;
        if (gamePlayer.isPlayer()) {
            message = "§8« " + player.getDisplayName() + " §7hat das Spiel verlassen";
        }
        event.setQuitMessage(message);

        Gamestate gamestate = this.mlgWars.getGamestateHandler().getGamestate();

        this.mlgWars.getGameTypeVoteInventory().quit(player);

        gamePlayer.cleanUpOnQuit();
        this.checkWinOnQuit(gamestate);

    }

    private void checkWinOnQuit(Gamestate gamestate) {

        switch (gamestate) {
            case LOBBY:
                int neededPlayers = (this.mlgWars.getTeamSize() * 2);
                if (this.mlgWars.getData().getPlayers().size() < neededPlayers) {
                    Bukkit.broadcastMessage(this.mlgWars.getPrefix() + "§cDer Countdown wurde abgebrochen, da zu wenige Spieler online sind");
                    Bukkit.getScheduler().cancelTasks(this.mlgWars);
                    this.mlgWars.setLobbyCountdown(60);
                    this.mlgWars.startTimerByClass(IdleTimer.class);
                }
                break;

            case DELAY:
            case PREGAME:
            case INGAME:
                if (Bukkit.getOnlinePlayers().isEmpty()) {
                    Bukkit.getServer().shutdown();
                } else {
                    new WinDetection(this.mlgWars);
                }
                break;
        }

    }

}
