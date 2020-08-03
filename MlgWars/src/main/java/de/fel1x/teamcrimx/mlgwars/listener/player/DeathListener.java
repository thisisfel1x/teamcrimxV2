package de.fel1x.teamcrimx.mlgwars.listener.player;

import de.fel1x.teamcrimx.crimxapi.coins.CoinsAPI;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.kit.Kit;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import de.fel1x.teamcrimx.mlgwars.scoreboard.LobbyScoreboard;
import de.fel1x.teamcrimx.mlgwars.utils.WinDetection;
import me.libraryaddict.disguise.DisguiseAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class DeathListener implements Listener {

    private final MlgWars mlgWars;
    private final LobbyScoreboard lobbyScoreboard = new LobbyScoreboard();

    public DeathListener(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
        this.mlgWars.getPluginManager().registerEvents(this, this.mlgWars);
    }

    @EventHandler
    public void on(PlayerDeathEvent event) {

        Player player = event.getEntity();
        Player killer;
        GamePlayer gamePlayer = new GamePlayer(player, true);

        int deaths = (int) gamePlayer.getObjectFromMongoDocument("deaths", MongoDBCollection.MLGWARS);
        gamePlayer.saveObjectInDocument("deaths", deaths + 1, MongoDBCollection.MLGWARS);

        gamePlayer.activateSpectatorMode();
        WinDetection.stopTasks(player);

        Gamestate gamestate = this.mlgWars.getGamestateHandler().getGamestate();

        if(gamestate == Gamestate.PREGAME || gamestate == Gamestate.INGAME) {

            if (player.getKiller() != null) {
                killer = player.getKiller();
            } else if (this.mlgWars.getData().getLastHit().get(player) != null) {
                killer = this.mlgWars.getData().getLastHit().get(player);
            } else if(player.hasMetadata("lastZombieHit")) {
                killer = Bukkit.getPlayer(player.getMetadata("lastZombieHit").get(0).asString());
            } else {
                killer = null;
            }

            if(killer != null) {

                CoinsAPI coinsAPI = new CoinsAPI(killer.getUniqueId());
                coinsAPI.addCoins(100);

                GamePlayer killerGamePlayer = new GamePlayer(killer);

                if(killerGamePlayer.getSelectedKit() == Kit.KANGAROO) {
                    int currentEssences = 0;
                    if(killer.hasMetadata("essence")) {
                        currentEssences = killer.getMetadata("essence").get(0).asInt();
                    }
                    killer.setMetadata("essence", new FixedMetadataValue(this.mlgWars, currentEssences + 2));
                }

                int gameKills;

                if(killer.hasMetadata("kills")) {
                    gameKills = killer.getMetadata("kills").get(0).asInt() + 1;
                } else {
                    gameKills = 1;
                }

                killer.setMetadata("kills", new FixedMetadataValue(this.mlgWars, gameKills));
                this.lobbyScoreboard.updateBoard(killer, "§8● §b" + gameKills, "kills", "§b");

                int kills = (int) killerGamePlayer.getObjectFromMongoDocument("kills", MongoDBCollection.MLGWARS);
                killerGamePlayer.saveObjectInDocument("kills", kills + 1, MongoDBCollection.MLGWARS);

                killer.sendMessage(this.mlgWars.getPrefix() + "§7Du hast " + player.getDisplayName() + " §7getötet §a(+100 Coins)");
                event.setDeathMessage(String.format("%s %s §7wurde von %s §7getötet", this.mlgWars.getPrefix(),
                        player.getDisplayName(), killer.getDisplayName()));

            } else {
                event.setDeathMessage(this.mlgWars.getPrefix() + player.getDisplayName() + " §7ist gestorben");
            }

            if(DisguiseAPI.isDisguised(player)) {
                DisguiseAPI.undisguiseToAll(player);
            }

            long timePlayed = System.currentTimeMillis() - this.mlgWars.getData().getPlayTime().get(player.getUniqueId());
            long added = timePlayed + (long) gamePlayer.getObjectFromMongoDocument("onlinetime", MongoDBCollection.USERS);
            gamePlayer.saveObjectInDocument("onlinetime", added, MongoDBCollection.USERS);

            int playersLeft = this.mlgWars.getData().getPlayers().size();

            if(playersLeft > 1) {
                Bukkit.broadcastMessage(this.mlgWars.getPrefix() + "§a" + playersLeft + " Spieler verbleiben");
            }

            Bukkit.getOnlinePlayers().forEach(inGamePlayer ->
                    this.lobbyScoreboard.updateBoard(inGamePlayer, "§8● §c" + MlgWars.getInstance().getData().getPlayers().size(), "players", "§c"));

            new WinDetection();
        }

    }

}
