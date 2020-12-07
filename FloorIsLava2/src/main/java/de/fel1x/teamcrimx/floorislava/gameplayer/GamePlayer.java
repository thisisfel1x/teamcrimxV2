package de.fel1x.teamcrimx.floorislava.gameplayer;

import com.google.common.collect.Lists;
import com.mongodb.client.model.Sorts;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.node.CloudNetBridgeModule;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.dytanic.cloudnet.ext.cloudperms.bukkit.BukkitCloudNetCloudPermissionsPlugin;
import de.dytanic.cloudnet.ext.cloudperms.node.CloudNetCloudPermissionsModule;
import de.fel1x.teamcrimx.floorislava.Data;
import de.fel1x.teamcrimx.floorislava.FloorIsLava;
import de.fel1x.teamcrimx.floorislava.database.FloorIsLavaDatabase;
import de.fel1x.teamcrimx.floorislava.database.Stats;
import de.fel1x.teamcrimx.floorislava.utils.scoreboard.GameScoreboard;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.List;

public class GamePlayer {
    private final FloorIsLava floorIsLava = FloorIsLava.getInstance();
    private final Data data = this.floorIsLava.getData();
    private final Player player;
    private final GameScoreboard gameScoreboard = new GameScoreboard();
    private final IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);

    public GamePlayer(Player player) {
        this.player = player;
    }

    public void addToInGamePlayers() {
        this.data.getPlayers().add(this.player);
    }

    public void removeFromInGamePlayers() {
        this.data.getPlayers().remove(this.player);
    }

    public void addToSpectators() {
        this.data.getSpectators().add(this.player);
    }

    public void removeFromSpectators() {
        this.data.getSpectators().remove(this.player);
    }

    public void setSpectator() {
        this.player.setGameMode(GameMode.ADVENTURE);
        this.player.setAllowFlight(true);
        this.player.setFlying(true);
        this.addToSpectators();
        this.removeFromInGamePlayers();
        this.data.getPlayers().forEach(gamePlayers -> gamePlayers.hidePlayer(this.floorIsLava, this.player));
        this.data.getSpectators().forEach(spectator -> this.player.showPlayer(this.floorIsLava, spectator));
    }

    public void fetchPlayerData() {
        Bukkit.getScheduler().runTaskAsynchronously(this.floorIsLava, () -> {
            Document lavaDocument = this.floorIsLava.getCrimxAPI().getMongoDB().getFloorIsLavaCollection().find(new Document("_id", this.player.getUniqueId().toString())).first();
            if (lavaDocument == null) {
                createPlayerData();
                fetchPlayerData();
            }
            int kills = lavaDocument.getInteger("kills");
            int deaths = lavaDocument.getInteger("deaths");
            int gamesPlayed = lavaDocument.getInteger("gamesPlayed");
            int gamesWon = lavaDocument.getInteger("gamesWon");
            int placement = getRankingPosition();
            Stats stats = new Stats(kills, deaths, gamesPlayed, gamesWon, placement);
            this.data.getCachedStats().put(this.player, stats);
        });
        this.data.getPlayerGG().put(this.player.getUniqueId(), Boolean.FALSE);
    }

    public ICloudPlayer getICloudPlayer() {
        return this.playerManager.getOnlinePlayer(this.player.getUniqueId());
    }

    public void cleanupInventory() {
        this.player.getInventory().clear();
        this.player.getInventory().setArmorContents(null);
        this.player.setFoodLevel(28);
        this.player.setHealth(20.0D);
        this.player.setGameMode(GameMode.SURVIVAL);
        this.player.setLevel(0);
        this.player.setExp(0.0F);
        this.player.setWalkSpeed(0.2F);
        this.player.getActivePotionEffects().forEach(effect -> this.player.removePotionEffect(effect.getType()));
    }

    public void teleportToLobby() {
    }

    public boolean isSpectator() {
        return this.data.getSpectators().contains(this.player);
    }

    public boolean isPlayer() {
        return this.data.getPlayers().contains(this.player);
    }

    public void createPlayerData() {
        (new FloorIsLavaDatabase()).createPlayerData(this.player);
    }

    public int getRankingPosition() {
        List<Document> documents = this.floorIsLava.getCrimxAPI().getMongoDB().getFloorIsLavaCollection().find().sort(Sorts.descending("gamesWon")).into(Lists.newArrayList());
        for (Document document : documents) {
            if (document.getString("_id").equalsIgnoreCase(this.player.getUniqueId().toString()))
                return documents.indexOf(document) + 1;
        }
        return -1;
    }

    public void saveStats() {
        Bukkit.getScheduler().runTaskAsynchronously(this.floorIsLava, () -> {
            long onlineTimeInMillis;
            Document floorIsLavaDocument = this.floorIsLava.getCrimxAPI().getMongoDB().getFloorIsLavaCollection().find(new Document("_id", this.player.getUniqueId().toString())).first();
            Document networkDocument = this.floorIsLava.getCrimxAPI().getMongoDB().getUserCollection().find(new Document("_id", this.player.getUniqueId().toString())).first();
            Stats stats = this.data.getCachedStats().get(this.player);
            if (stats == null)
                return;
            if (networkDocument == null)
                return;
            Document toUpdate = new Document();
            toUpdate.append("kills", stats.getKills()).append("deaths", stats.getDeaths()).append("gamesPlayed", stats.getGamesPlayed()).append("gamesWon", stats.getGamesWon());
            Document document1 = new Document("$set", toUpdate);
            this.floorIsLava.getCrimxAPI().getMongoDB().getFloorIsLavaCollection().updateOne(floorIsLavaDocument, document1);
            try {
                onlineTimeInMillis = (Long) networkDocument.get("onlinetime");
            } catch (Exception ignored) {
                onlineTimeInMillis = Integer.toUnsignedLong((Integer) networkDocument.get("onlinetime"));
            }
            long timePlayed = System.currentTimeMillis() - this.floorIsLava.getData().getPlayTime().get(this.player.getUniqueId());
            long added = timePlayed + onlineTimeInMillis;
            Document document = new Document("onlinetime", added);
            Document document2 = new Document("$set", document);
            this.floorIsLava.getCrimxAPI().getMongoDB().getUserCollection().updateOne(networkDocument, document2);
        });
    }

    public void saveObjectInDocument(String key, Object value, Document gamemodeDocument) {
        Document document = new Document(key, value);
        Document document1 = new Document("$set", document);
        this.floorIsLava.getCrimxAPI().getMongoDB().getCaptureTheFlagCollection().updateOne(gamemodeDocument, document1);
    }

    public void setScoreboard() {
        this.gameScoreboard.setGameScoreboard(this.player);
        BukkitCloudNetCloudPermissionsPlugin.getInstance().updateNameTags(this.player);
    }
}
