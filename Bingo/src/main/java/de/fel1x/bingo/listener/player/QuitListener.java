package de.fel1x.bingo.listener.player;

import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.gamehandler.Gamestate;
import de.fel1x.bingo.objects.BingoPlayer;
import de.fel1x.bingo.objects.BingoTeam;
import de.fel1x.bingo.tasks.EndingTask;
import de.fel1x.bingo.tasks.IdleTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    private final Bingo bingo;

    public QuitListener(Bingo bingo) {
        this.bingo = bingo;
        bingo.getPluginManager().registerEvents(this, bingo);
    }

    @EventHandler
    public void on(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        BingoPlayer bingoPlayer = new BingoPlayer(player);
        Gamestate gamestate = this.bingo.getGamestateHandler().getGamestate();

        bingoPlayer.cleanupOnQuit();
        event.setQuitMessage(null);

        int neededPlayers = (BingoTeam.RED.getTeamSize() * 2) - this.bingo.getData().getPlayers().size();

        switch (gamestate) {

            case IDLE:
            case LOBBY:
                event.setQuitMessage(this.bingo.getPrefix() + "§a" + player.getName() + " §7hat das Spiel verlassen");
                this.bingo.getData().getPlayers().forEach(players -> this.bingo.getLobbyScoreboard().updateBoard(players,
                        String.format("§8● §a%s§8/§c%s",
                                this.bingo.getData().getPlayers().size(), BingoTeam.RED.getTeamSize() * 6),
                        "players", "§a"));
                break;

            case INGAME:
                if(bingoPlayer.isPlayer()) {
                    event.setQuitMessage(this.bingo.getPrefix() + "§a" + player.getName() + " §7hat das Spiel verlassen");
                    bingoPlayer.saveStats();
                }
                if (this.bingo.getData().getPlayers().isEmpty() && !Bukkit.getOnlinePlayers().isEmpty()) {
                    this.bingo.startTimerByClass(EndingTask.class);
                } else if (Bukkit.getOnlinePlayers().isEmpty()) {
                    Bukkit.getServer().shutdown();
                }
                break;

            case PREGAME:
                if(bingoPlayer.isPlayer()) {
                    event.setQuitMessage(this.bingo.getPrefix() + "§a" + player.getName() + " §7hat das Spiel verlassen");
                }
                break;

            case ENDING:

                if (Bukkit.getOnlinePlayers().isEmpty()) {
                    Bukkit.getServer().shutdown();
                }
                event.setQuitMessage(this.bingo.getPrefix() + "§a" + player.getName() + " §7hat das Spiel verlassen");
                break;

        }

        if (this.bingo.getData().getPlayers().size() < neededPlayers
                && gamestate.equals(Gamestate.LOBBY)) {

            this.bingo.startTimerByClass(IdleTask.class);

        }


    }

}
