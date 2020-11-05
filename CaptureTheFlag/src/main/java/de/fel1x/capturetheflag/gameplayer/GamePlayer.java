package de.fel1x.capturetheflag.gameplayer;

import com.google.common.collect.Lists;
import com.mongodb.client.model.Sorts;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.fel1x.capturetheflag.CaptureTheFlag;
import de.fel1x.capturetheflag.Data;
import de.fel1x.capturetheflag.database.CaptureTheFlagDatabase;
import de.fel1x.capturetheflag.database.Stats;
import de.fel1x.capturetheflag.filehandler.SpawnHandler;
import de.fel1x.capturetheflag.kit.IKit;
import de.fel1x.capturetheflag.kit.Kit;
import de.fel1x.capturetheflag.kit.kits.ArcherKit;
import de.fel1x.capturetheflag.team.Team;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GamePlayer {

    private final CaptureTheFlag captureTheFlag = CaptureTheFlag.getInstance();
    private final Data data = captureTheFlag.getData();

    private final Player player;

    private final IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);

    public GamePlayer(Player player) {
        this.player = player;
    }

    public void addToInGamePlayers() {
        this.data.getPlayers().add(player);
    }

    public void removeFromInGamePlayers() {
        this.data.getPlayers().remove(player);
    }

    public void addToSpectators() {
        this.data.getSpectators().add(player);
    }

    public void removeFromSpectators() {
        this.data.getSpectators().remove(player);
    }

    public void setSpectator() {
        this.player.teleport(SpawnHandler.loadLocation("spectator"));
        this.player.setGameMode(GameMode.SPECTATOR);

        this.addToSpectators();

        this.data.getPlayers().forEach(gamePlayers -> gamePlayers.hidePlayer(this.captureTheFlag, this.player));
    }

    public void fetchPlayerData() {
        Bukkit.getScheduler().runTaskAsynchronously(this.captureTheFlag, () -> {
            Document ctfDocument = this.captureTheFlag.getCrimxAPI().getMongoDB().getCaptureTheFlagCollection().
                    find(new Document("_id", player.getUniqueId().toString())).first();

            if(ctfDocument == null) {
                this.createPlayerData();
                this.fetchPlayerData();
            }

            int kills = ctfDocument.getInteger("kills");
            int deaths = ctfDocument.getInteger("deaths");
            int gamesPlayed = ctfDocument.getInteger("gamesPlayed");
            int gamesWon = ctfDocument.getInteger("gamesWon");

            int placement = this.getRankingPosition();

            Stats stats = new Stats(kills, deaths, gamesPlayed, gamesWon, placement);
            this.data.getCachedStats().put(player, stats);

            Kit savedKit = Kit.valueOf(ctfDocument.getString("selectedKit"));

            if(savedKit != Kit.NONE) {
                this.data.getSelectedKit().put(this.player, savedKit);
            }

        });
    }

    public void cleanupTeams() {
        Team.RED.getTeamPlayers().remove(this.player);
        Team.BLUE.getTeamPlayers().remove(this.player);
    }

    public ICloudPlayer getICloudPlayer() {
        return this.playerManager.getOnlinePlayer(this.player.getUniqueId());
    }

    public void cleanupInventory() {
        this.player.getInventory().clear();
        this.player.getInventory().setArmorContents(null);
        this.player.setFoodLevel(28);
        this.player.setHealth(20);
        this.player.setGameMode(GameMode.SURVIVAL);
        this.player.setLevel(0);
        this.player.setExp(0);
        this.player.setWalkSpeed(0.2f);
        this.player.getActivePotionEffects().forEach(effect -> this.player.removePotionEffect(effect.getType()));
    }

    public void showToAll() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(this.captureTheFlag,
                () -> Bukkit.getOnlinePlayers().forEach(current -> {
                    current.showPlayer(this.captureTheFlag, this.player);
                    this.player.showPlayer(this.captureTheFlag, current);
                }), 20L);
    }

    public void teleportToLobby() {
        try {
            Location location = SpawnHandler.loadLocation("lobby");
            player.teleport(location);
        } catch (Exception ignored) {
            player.sendMessage("§cEs trat ein Fehler auf (TELEPORT_SPAWN)");
        }
    }

    public void addTeam(Team team) {
        if (team.getTeamPlayers().size() <= 4) {
            if (!team.getTeamPlayers().contains(this.player)) {
                team.getTeamPlayers().add(this.player);
            }
            this.player.sendMessage(this.captureTheFlag.getPrefix() + "§7Du bist Team " + team.getTeamName() + " §7beigetreten!");

            this.captureTheFlag.getScoreboardHandler().setGameScoreboard(player, team);

        } else {
            this.player.sendMessage(this.captureTheFlag.getPrefix() + "Dieses Team ist voll!");
            this.player.closeInventory();
        }
    }

    public void removeTeam(Team team) {
        team.getTeamPlayers().remove(this.player);
    }

    public Team getTeam() {
        if (Team.BLUE.getTeamPlayers().contains(this.player)) {
            return Team.BLUE;
        } else if (Team.RED.getTeamPlayers().contains(this.player)) {
            return Team.RED;
        } else {
            return Team.NONE;
        }
    }

    public boolean hasTeam() {
        return (Team.BLUE.getTeamPlayers().contains(this.player) || Team.RED.getTeamPlayers().contains(this.player));
    }

    public void teleportToTeamSpawn() {
        if (Team.BLUE.getTeamPlayers().contains(player)) {
            Location blueSpawn = SpawnHandler.loadLocation("blueSpawn");
            this.player.teleport(blueSpawn);
        } else if (Team.RED.getTeamPlayers().contains(player)) {
            Location redSpawn = SpawnHandler.loadLocation("redSpawn");
            this.player.teleport(redSpawn);
        } else {
            this.player.sendMessage("§cEs trat ein Fehler auf (NO_TEAM_SELECTED_TELEPORT_ERROR)");
        }
    }

    public Location getRespawnLocation() {
        if (Team.BLUE.getTeamPlayers().contains(player)) {
            return SpawnHandler.loadLocation("blueSpawn");
        } else if (Team.RED.getTeamPlayers().contains(player)) {
            return SpawnHandler.loadLocation("redSpawn");
        }
        return SpawnHandler.loadLocation("spectator");
    }

    public void checkForTeam() {
        if (this.hasTeam()) return;

        boolean random = ThreadLocalRandom.current().nextBoolean();

        if (random) {
            this.addTeam(Team.RED);
        } else {
            this.addTeam(Team.BLUE);
        }
    }

    public boolean isSpectator() {

        return this.data.getSpectators().contains(player);

    }

    public boolean isPlayer() {

        return this.data.getPlayers().contains(player);

    }

    public void selectKit(Kit kit) {
        this.captureTheFlag.getData().getSelectedKit().put(player, kit);

        this.player.closeInventory();
        this.player.playSound(player.getLocation(), Sound.ENTITY_CAT_PURREOW, 5, 8);
    }

    public Class<? extends IKit> getSelectedKit() {
        return this.captureTheFlag.getData().getSelectedKit().get(player).getClazz();
    }

    public void setKitItems() {
        try {
            IKit iKit = (this.data.getSelectedKit().get(this.player) != null)
                    ? this.data.getSelectedKit().get(this.player).getClazz().newInstance()
                    : ArcherKit.class.newInstance();
            iKit.setKitInventory(player);
        } catch (InstantiationException | IllegalAccessException ignored) {
            player.sendMessage(this.captureTheFlag.getPrefix() + "§cEin Fehler ist aufgetreten! Du erhälst keine Kit-Items");
        }
    }

    public void createPlayerData() {
        new CaptureTheFlagDatabase().createPlayerData(player);
    }

    public int getRankingPosition() {
        List<Document> documents = this.captureTheFlag.getCrimxAPI().getMongoDB().getCaptureTheFlagCollection().find()
                .sort(Sorts.descending("gamesWon")).into(Lists.newArrayList());

        for (Document document : documents) {
            if (document.getString("_id").equalsIgnoreCase(this.player.getUniqueId().toString())) {
                return documents.indexOf(document) + 1;
            }
        }

        return -1;
    }

    public void saveStats() {
        Bukkit.getScheduler().runTaskAsynchronously(this.captureTheFlag, () -> {
            Document ctfDocument = this.captureTheFlag.getCrimxAPI().getMongoDB().getCaptureTheFlagCollection().
                    find(new Document("_id", player.getUniqueId().toString())).first();
            Document networkDocument = this.captureTheFlag.getCrimxAPI().getMongoDB().getUserCollection().
                    find(new Document("_id", player.getUniqueId().toString())).first();

            Stats stats = this.data.getCachedStats().get(this.player);

            if(stats == null) {
                return;
            }

            if(networkDocument == null) {
                return;
            }

            this.saveObjectInDocument("kills", stats.getKills(), ctfDocument);
            this.saveObjectInDocument("deaths", stats.getDeaths(), ctfDocument);
            this.saveObjectInDocument("gamesPlayed", stats.getGamesPlayed(), ctfDocument);
            this.saveObjectInDocument("gamesWon", stats.getGamesWon(), ctfDocument);

            long onlineTimeInMillis;

            try {
                onlineTimeInMillis = (long)  networkDocument.get("onlinetime");
            } catch (Exception ignored) {
                onlineTimeInMillis = Integer.toUnsignedLong((Integer) networkDocument.get("onlinetime"));
            }

            long timePlayed = System.currentTimeMillis() - this.captureTheFlag.getData().getPlayTime()
                    .get(this.player.getUniqueId());
            long added = timePlayed + onlineTimeInMillis;
            Document document = new Document("onlinetime", added);
            Bson updateOperation = new Document("$set", document);
            this.captureTheFlag.getCrimxAPI().getMongoDB().getUserCollection().updateOne(networkDocument, updateOperation);
        });
    }

    public void saveObjectInDocument(String key, Object value, Document gamemodeDocument) {
        Document document = new Document(key, value);
        Bson updateOperation = new Document("$set", document);

        this.captureTheFlag.getCrimxAPI().getMongoDB().getCaptureTheFlagCollection().updateOne(gamemodeDocument, updateOperation);
    }
}
