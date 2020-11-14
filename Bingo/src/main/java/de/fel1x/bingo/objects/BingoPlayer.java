package de.fel1x.bingo.objects;

import com.google.common.collect.Lists;
import com.mongodb.client.model.Sorts;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.dytanic.cloudnet.ext.cloudperms.bukkit.BukkitCloudNetCloudPermissionsPlugin;
import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.Data;
import de.fel1x.bingo.database.BingoDatabase;
import de.fel1x.bingo.objects.stats.Stats;
import de.fel1x.bingo.tasks.GameTask;
import de.fel1x.bingo.utils.ItemBuilder;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class BingoPlayer {

    private final Player player;

    private final Bingo bingo = Bingo.getInstance();
    private final Data data = Bingo.getInstance().getData();

    private final IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);

    GameTask gameTask = new GameTask();

    public BingoPlayer(Player player) {
        this.player = player;
    }

    public void addToPlayers() {
        this.data.getPlayers().add(this.player);
    }

    public void removeFromPlayers() {
        this.data.getPlayers().remove(this.player);
    }

    public boolean isPlayer() {
        return this.data.getPlayers().contains(this.player);
    }

    public void addToSpectators() {
        this.data.getSpectators().add(this.player);
    }

    public void removeFromSpectators() {
        this.data.getSpectators().remove(this.player);
    }

    public boolean isSpectator() {
        return this.data.getSpectators().contains(this.player);
    }

    public void setScoreboardOnJoin() {
        switch (this.bingo.getGamestateHandler().getGamestate()) {
            case LOBBY: case IDLE:
                this.bingo.getLobbyScoreboard().handleJoin(this.player);
                this.bingo.getLobbyScoreboard().updateBoard(this.player, "§8● §6" + this.bingo.getFormattedBiomeName(),
                        "biome", "§6");
                break;
        }
    }

    public void cleanupOnJoin() {

        this.player.getInventory().clear();
        this.player.getInventory().setArmorContents(null);

        this.player.setHealth(20);
        this.player.setFoodLevel(26);

        this.player.setLevel(0);
        this.player.setExp(0);

        this.player.setGameMode(GameMode.SURVIVAL);

        for (PotionEffect activePotionEffect : this.player.getActivePotionEffects()) {
            this.player.removePotionEffect(activePotionEffect.getType());
        }

        this.player.setPlayerListHeaderFooter("\n §8» §bteamcrimx§lDE §8« \n", "\n §aBingo\n ");

    }

    public void cleanupOnQuit() {

        this.bingo.getLobbyScoreboard().handleQuit(this.player);

        this.data.getPlayers().remove(this.player);
        this.data.getSpectators().remove(this.player);

        this.player.getInventory().clear();
        this.player.getInventory().setArmorContents(null);

        this.player.setHealth(20);
        this.player.setFoodLevel(20);

        for (PotionEffect activePotionEffect : this.player.getActivePotionEffects()) {
            this.player.removePotionEffect(activePotionEffect.getType());
        }

        for (BingoTeam team : BingoTeam.values()) {
            team.getTeamPlayers().remove(this.player);
        }

    }

    public BingoTeam getTeam() {

        for (BingoTeam team : BingoTeam.values()) {

            if (team.getTeamPlayers().contains(this.player)) {
                return team;
            }

        }

        // PLAYER HAS NO TEAM
        return null;

    }

    public void setTeam(BingoTeam bingoTeam) {

        for (BingoTeam team : BingoTeam.values()) {
            team.getTeamPlayers().remove(this.player);
        }

        bingoTeam.getTeamPlayers().add(this.player);

    }

    public void activateSpectatorMode() {

        this.data.getPlayers().forEach(bingoPlayers -> bingoPlayers.hidePlayer(this.bingo, this.player));

        this.player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
        this.player.setHealth(20);
        this.player.setFoodLevel(20);

        this.player.setGameMode(GameMode.SURVIVAL);
        this.player.setAllowFlight(true);
        this.player.setFlying(true);

        this.player.teleport(new Location(Bukkit.getWorlds().get(0), 0.5, 121, 0.5));

        this.player.getInventory().setItem(0, new ItemBuilder(Material.COMPASS).setName("§7Spieler beoabachten").toItemStack());

        this.gameTask.getBossBar().addPlayer(this.player);

    }

    public void createPlayerData() {
        new BingoDatabase().createPlayerData(this.player);
    }

    public void fetchPlayerData() {
        Bukkit.getScheduler().runTaskAsynchronously(this.bingo, () -> {
            Document bingoDocument = this.bingo.getCrimxAPI().getMongoDB().getBingoCollection().
                    find(new Document("_id", this.player.getUniqueId().toString())).first();

            if (bingoDocument == null) {
                this.createPlayerData();
                this.fetchPlayerData();
            }

            int itemsPickedUp = bingoDocument.getInteger("itemsPickedUp");
            int itemsCrafted = bingoDocument.getInteger("itemsCrafted");
            int gamesPlayed = bingoDocument.getInteger("gamesPlayed");
            int gamesWon = bingoDocument.getInteger("gamesWon");

            int placement = this.getRankingPosition();

            Stats stats = new Stats(itemsCrafted, itemsPickedUp, gamesPlayed, gamesWon, placement);
            this.data.getCachedStats().put(this.player, stats);

        });
    }

    public ICloudPlayer getICloudPlayer() {
        return this.playerManager.getOnlinePlayer(this.player.getUniqueId());
    }

    public int getRankingPosition() {
        List<Document> documents = this.bingo.getCrimxAPI().getMongoDB().getCaptureTheFlagCollection().find()
                .sort(Sorts.descending("gamesWon")).into(Lists.newArrayList());

        for (Document document : documents) {
            if (document.getString("_id").equalsIgnoreCase(this.player.getUniqueId().toString())) {
                return documents.indexOf(document) + 1;
            }
        }

        return -1;
    }

    public void saveStats() {
        Bukkit.getScheduler().runTaskAsynchronously(this.bingo, () -> {
            Stats stats = this.data.getCachedStats().get(this.player);

            if (stats == null) {
                return;
            }

            Document toUpdate = new Document();
            toUpdate.append("itemsPickedUp", stats.getItemsPickedUp())
                    .append("itemsCrafted", stats.getItemsCrafted())
                    .append("gamesPlayed", stats.getGamesPlayed())
                    .append("gamesWon", stats.getGamesWon());

            this.saveDocumentInDatabase(toUpdate, MongoDBCollection.BINGO);
            this.saveOnlinetime();
        });
    }

    private void saveOnlinetime() {
        long onlineTimeInMillis;
        Document networkDocument = this.bingo.getCrimxAPI().getMongoDB().getUserCollection().
                find(new Document("_id", this.player.getUniqueId().toString())).first();

        try {
            onlineTimeInMillis = networkDocument.getLong("onlinetime");
        } catch (Exception ignored) {
            onlineTimeInMillis = Integer.toUnsignedLong((Integer) networkDocument.get("onlinetime"));
        }

        long timePlayed = System.currentTimeMillis() - this.data.getPlayTime()
                .get(this.player.getUniqueId());
        long added = timePlayed + onlineTimeInMillis;
        Document document = new Document("onlinetime", added);
        this.saveDocumentInDatabase(document, MongoDBCollection.USERS);
    }

    public void saveDocumentInDatabase(Document update, MongoDBCollection mongoDBCollection) {
        Bukkit.getScheduler().runTaskAsynchronously(this.bingo, () -> {
            Bson updateOperation = new Document("$set", update);
            switch (mongoDBCollection) {
                case BINGO:
                    Document bingoDocument = this.bingo.getCrimxAPI().getMongoDB().getBingoCollection().
                            find(new Document("_id", this.player.getUniqueId().toString())).first();

                    this.bingo.getCrimxAPI().getMongoDB().getBingoCollection().updateOne(bingoDocument, updateOperation);
                    break;
                case USERS:
                    Document networkDocument = this.bingo.getCrimxAPI().getMongoDB().getUserCollection().
                            find(new Document("_id", this.player.getUniqueId().toString())).first();

                    this.bingo.getCrimxAPI().getMongoDB().getBingoCollection().updateOne(networkDocument, updateOperation);
                    break;

            }
        });
    }

    public void saveObjectInDocument(String key, Object value, MongoDBCollection mongoDBCollection) {
        Bukkit.getScheduler().runTaskAsynchronously(this.bingo, () -> {
            Document update = new Document(key, value);
            Bson updateOperation = new Document("$set", update);
            switch (mongoDBCollection) {
                case BINGO:
                    Document bingoDocument = this.bingo.getCrimxAPI().getMongoDB().getBingoCollection().
                            find(new Document("_id", this.player.getUniqueId().toString())).first();

                    this.bingo.getCrimxAPI().getMongoDB().getBingoCollection().updateOne(bingoDocument, updateOperation);
                    break;
                case USERS:
                    Document networkDocument = this.bingo.getCrimxAPI().getMongoDB().getUserCollection().
                            find(new Document("_id", this.player.getUniqueId().toString())).first();

                    this.bingo.getCrimxAPI().getMongoDB().getBingoCollection().updateOne(networkDocument, updateOperation);
                    break;
            }
        });
    }
}
