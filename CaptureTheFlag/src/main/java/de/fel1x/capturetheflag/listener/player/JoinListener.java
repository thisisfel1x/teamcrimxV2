package de.fel1x.capturetheflag.listener.player;

import de.fel1x.capturetheflag.CaptureTheFlag;
import de.fel1x.capturetheflag.Data;
import de.fel1x.capturetheflag.gameplayer.GamePlayer;
import de.fel1x.capturetheflag.gamestate.Gamestate;
import de.fel1x.capturetheflag.timers.LobbyTimer;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.crimxapi.objects.CrimxPlayer;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final CaptureTheFlag captureTheFlag;
    private final Data data;

    public JoinListener(CaptureTheFlag captureTheFlag) {
        this.captureTheFlag = captureTheFlag;
        this.data = this.captureTheFlag.getData();

        this.captureTheFlag.getPluginManager().registerEvents(this, this.captureTheFlag);
    }

    @EventHandler
    public void on(PlayerJoinEvent event) {

        event.setJoinMessage(null);

        Player player = event.getPlayer();
        GamePlayer gamePlayer = new GamePlayer(player);
        CrimxPlayer crimxPlayer = new CrimxPlayer(gamePlayer.getICloudPlayer());

        Gamestate gamestate = this.captureTheFlag.getGamestateHandler().getGamestate();

        if (!crimxPlayer.checkIfPlayerExistsInCollection(player.getUniqueId(), MongoDBCollection.CAPTURE_THE_FLAG)) {
            gamePlayer.createPlayerData();
        }

        this.captureTheFlag.getScoreboardHandler().handleJoin(player);

        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(18);

        switch (gamestate) {

            case IDLE:
            case LOBBY:

                gamePlayer.fetchPlayerData();

                gamePlayer.teleportToLobby();
                gamePlayer.addToInGamePlayers();
                gamePlayer.showToAll();
                gamePlayer.cleanupInventory();
                gamePlayer.cleanupTeams();

                event.setJoinMessage("§8» " + player.getDisplayName() + " §7hat das Spiel betreten!");

                if (this.data.getPlayers().size() >= 6) {
                    this.captureTheFlag.startTimerByClass(LobbyTimer.class);
                }

                player.getInventory().setItem(0, new ItemBuilder(Material.CHEST_MINECART)
                        .setName("§8● §eWähle dein Kit").toItemStack());
                player.getInventory().setItem(1, new ItemBuilder(Material.RED_BED)
                        .setName("§8● §6Wähle dein Team").toItemStack());

                break;

            case PREGAME:
            case INGAME:

                gamePlayer.setSpectator();
                player.sendMessage("§7Du bist nun Spectator");

                break;

            case ENDING:

                gamePlayer.teleportToLobby();

                break;
        }
    }
}
