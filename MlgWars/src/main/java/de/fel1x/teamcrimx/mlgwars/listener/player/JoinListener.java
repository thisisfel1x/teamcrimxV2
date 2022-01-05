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

import java.util.concurrent.Callable;

public class JoinListener implements Listener {

    private final MlgWars mlgWars;

    public JoinListener(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
        this.mlgWars.getPluginManager().registerEvents(this, this.mlgWars);
    }

    @EventHandler
    public void on(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = new GamePlayer(this.mlgWars, player);

        event.joinMessage(null);

        gamePlayer.cleanUpOnJoin();

        if (!this.mlgWars.getCrimxAPI().getMongoDB().checkIfDocumentExistsSync(player.getUniqueId(), MongoDBCollection.MLGWARS)) {
            gamePlayer.createPlayerData();
        }

        if(!this.mlgWars.getCrimxAPI().getMongoDB().checkIfDocumentExistsSync(player.getUniqueId(), MongoDBCollection.MLGWARS_TOURNAMENT)) {
            gamePlayer.createTournamentData();
        }

        gamePlayer.initDatabasePlayer();

        Gamestate gamestate = this.mlgWars.getGamestateHandler().getGamestate();

        switch (gamestate) {
            case IDLE, LOBBY -> {
                gamePlayer.addToPlayers();
                gamePlayer.setJoinItems();
                gamePlayer.teleport(Spawns.LOBBY);
                gamePlayer.setLobbyScoreboard();
                BukkitCloudNetCloudPermissionsPlugin.getInstance().updateNameTags(event.getPlayer());
                player.playerListName(player.displayName());
                event.setJoinMessage("§8» " + player.getDisplayName() + " §7hat das Spiel betreten");
            }
            case DELAY, PREGAME, INGAME -> {
                gamePlayer.addToSpectators();
                gamePlayer.activateSpectatorModeOnJoin();
                gamePlayer.teleport(Spawns.SPECTATOR);
                gamePlayer.setInGameScoreboard();
            }
            case ENDING -> {
                player.sendMessage(this.mlgWars.getPrefix() + "§7Das Spiel ist bereits vorbei!");
                gamePlayer.teleport(Spawns.LOBBY);
            }
        }

        if (gamestate == Gamestate.IDLE) {
            if (!this.mlgWars.isInSetup() && !this.mlgWars.isNoMap()) {
                int neededPlayers = (this.mlgWars.getTeamSize() * 2);
                if (this.mlgWars.getData().getPlayers().size() >= neededPlayers) {
                    this.mlgWars.startTimerByClass(LobbyTimer.class);
                }
            }
        }
    }
}
