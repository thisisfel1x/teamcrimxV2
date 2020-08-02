package de.fel1x.teamcrimx.mlgwars.listener.player;

import de.dytanic.cloudnet.ext.cloudperms.bukkit.BukkitCloudNetCloudPermissionsPlugin;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.crimxapi.objects.CrimxPlayer;
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
        CrimxPlayer crimxPlayer = new CrimxPlayer(gamePlayer.getCloudPlayer());

        event.setJoinMessage(null);

        gamePlayer.cleanUpOnJoin();

        if (!crimxPlayer.checkIfPlayerExistsInCollection(player.getUniqueId(), MongoDBCollection.MLGWARS)) {
            gamePlayer.createPlayerData();
        }

        BukkitCloudNetCloudPermissionsPlugin.getInstance().updateNameTags(event.getPlayer());

        gamePlayer.initDatabasePlayer();

        Gamestate gamestate = this.mlgWars.getGamestateHandler().getGamestate();

        switch (gamestate) {

            case IDLE: case LOBBY:

                gamePlayer.addToPlayers();
                gamePlayer.setJoinItems();
                gamePlayer.teleport(Spawns.LOBBY);

                event.setJoinMessage("§8» " + player.getDisplayName() + " §7hat das Spiel betreten");

                break;


            case DELAY: case PREGAME: case INGAME:

                gamePlayer.addToSpectators();
                gamePlayer.activateSpectatorMode();
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
