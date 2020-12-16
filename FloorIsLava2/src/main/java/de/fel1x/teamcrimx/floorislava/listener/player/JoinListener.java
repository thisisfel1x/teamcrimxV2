package de.fel1x.teamcrimx.floorislava.listener.player;

import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.crimxapi.objects.CrimxPlayer;
import de.fel1x.teamcrimx.floorislava.FloorIsLava;
import de.fel1x.teamcrimx.floorislava.gamehandler.Gamestate;
import de.fel1x.teamcrimx.floorislava.gameplayer.GamePlayer;
import de.fel1x.teamcrimx.floorislava.tasks.LobbyTask;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    private final FloorIsLava floorIsLava;

    public JoinListener(FloorIsLava floorIsLava) {
        this.floorIsLava = floorIsLava;
        floorIsLava.getPluginManager().registerEvents(this, floorIsLava);
    }

    @EventHandler
    public void on(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = new GamePlayer(player);
        CrimxPlayer crimxPlayer = new CrimxPlayer(gamePlayer.getICloudPlayer());
        Gamestate gamestate = this.floorIsLava.getGamestateHandler().getGamestate();
        if (!crimxPlayer.checkIfPlayerExistsInCollection(player.getUniqueId(), MongoDBCollection.FLOOR_IS_LAVA))
            gamePlayer.createPlayerData();
        gamePlayer.fetchPlayerData();
        event.setJoinMessage(null);
        int neededPlayers = 3 - this.floorIsLava.getData().getPlayers().size();
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(18.0D);
        this.floorIsLava.getData().getPlayerGG().put(player.getUniqueId(), Boolean.FALSE);
        gamePlayer.setScoreboard();
        switch (gamestate) {
            case IDLE:
            case LOBBY:
                gamePlayer.loadSelectedPerks();
                gamePlayer.addToInGamePlayers();
                player.teleport(this.floorIsLava.getSpawnLocation());
                event.setJoinMessage(this.floorIsLava.getPrefix() + "§a" + player.getDisplayName() + " §7hat das Spiel betreten");
                break;
            case PREGAME:
            case FARMING:
            case RISING:
                gamePlayer.addToSpectators();
                gamePlayer.setSpectator();
                break;
            case ENDING:
                player.teleport(this.floorIsLava.getSpawnLocation());
                break;
        }
        if (this.floorIsLava.getData().getPlayers().size() >= 3 && gamestate
                .equals(Gamestate.IDLE))
            this.floorIsLava.startTimerByClass(LobbyTask.class);
    }
}
