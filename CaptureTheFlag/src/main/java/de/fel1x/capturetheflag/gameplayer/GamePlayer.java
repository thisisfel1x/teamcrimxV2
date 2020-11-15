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
import de.fel1x.capturetheflag.scoreboard.ScoreboardHandler;
import de.fel1x.capturetheflag.team.Team;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GamePlayer {

    private final CaptureTheFlag captureTheFlag = CaptureTheFlag.getInstance();
    private final Data data = this.captureTheFlag.getData();

    private final ScoreboardHandler scoreboardHandler = CaptureTheFlag.getInstance().getScoreboardHandler();

    private final Player player;

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
        this.player.teleport(SpawnHandler.loadLocation("spectator"));
        this.player.setGameMode(GameMode.SPECTATOR);

        this.addToSpectators();

        this.data.getPlayers().forEach(gamePlayers -> gamePlayers.hidePlayer(this.captureTheFlag, this.player));
    }

    public void fetchPlayerData() {
        Bukkit.getScheduler().runTaskAsynchronously(this.captureTheFlag, () -> {
            Document ctfDocument = this.captureTheFlag.getCrimxAPI().getMongoDB().getCaptureTheFlagCollection().
                    find(new Document("_id", this.player.getUniqueId().toString())).first();

            if (ctfDocument == null) {
                this.createPlayerData();
                this.fetchPlayerData();
            }

            int kills = ctfDocument.getInteger("kills");
            int deaths = ctfDocument.getInteger("deaths");
            int gamesPlayed = ctfDocument.getInteger("gamesPlayed");
            int gamesWon = ctfDocument.getInteger("gamesWon");

            int placement = this.getRankingPosition();

            Stats stats = new Stats(kills, deaths, gamesPlayed, gamesWon, placement);
            this.data.getCachedStats().put(this.player, stats);

            Kit savedKit = Kit.valueOf(ctfDocument.getString("selectedKit"));

            if (savedKit != Kit.NONE) {
                this.selectKit(savedKit);
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
            this.player.teleport(location);
        } catch (Exception ignored) {
            this.player.sendMessage("§cEs trat ein Fehler auf (TELEPORT_SPAWN)");
        }
    }

    public void addTeam(Team team) {
        if (team.getTeamPlayers().size() <= 4) {
            if (!team.getTeamPlayers().contains(this.player)) {
                team.getTeamPlayers().add(this.player);
            }
            this.player.sendMessage(this.captureTheFlag.getPrefix() + "§7Du bist Team " + team.getTeamName() + " §7beigetreten!");

            this.captureTheFlag.getScoreboardHandler().setGameScoreboard(this.player, team);

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
        if (Team.BLUE.getTeamPlayers().contains(this.player)) {
            Location blueSpawn = SpawnHandler.loadLocation("blueSpawn");
            this.player.teleport(blueSpawn);
        } else if (Team.RED.getTeamPlayers().contains(this.player)) {
            Location redSpawn = SpawnHandler.loadLocation("redSpawn");
            this.player.teleport(redSpawn);
        } else {
            this.player.sendMessage("§cEs trat ein Fehler auf (NO_TEAM_SELECTED_TELEPORT_ERROR)");
        }
    }

    public Location getRespawnLocation() {
        if (Team.BLUE.getTeamPlayers().contains(this.player)) {
            return SpawnHandler.loadLocation("blueSpawn");
        } else if (Team.RED.getTeamPlayers().contains(this.player)) {
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

        return this.data.getSpectators().contains(this.player);

    }

    public boolean isPlayer() {

        return this.data.getPlayers().contains(this.player);

    }

    public void selectKit(Kit kit) {
        this.captureTheFlag.getData().getSelectedKit().put(this.player, kit);

        try {
            IKit iKit = kit.getClazz().newInstance();
            this.player.getInventory().setItem(8, new ItemBuilder(iKit.getKitMaterial())
                    .setName("§8● §a" + iKit.getKitName())
                    .setLore(iKit.getKitDescription()).toItemStack());
            this.scoreboardHandler.updateBoard(this.player, "§8● §6" + iKit.getKitName(), "kit", "§6");
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Class<? extends IKit> getSelectedKit() {
        return this.captureTheFlag.getData().getSelectedKit().get(this.player).getClazz();
    }

    public void setKitItems() {
        try {
            IKit iKit = (this.data.getSelectedKit().get(this.player) != null)
                    ? this.data.getSelectedKit().get(this.player).getClazz().newInstance()
                    : ArcherKit.class.newInstance();
            iKit.setKitInventory(this.player);
        } catch (InstantiationException | IllegalAccessException ignored) {
            this.player.sendMessage(this.captureTheFlag.getPrefix() + "§cEin Fehler ist aufgetreten! Du erhälst keine Kit-Items");
        }
    }

    public void createPlayerData() {
        new CaptureTheFlagDatabase().createPlayerData(this.player);
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
                    find(new Document("_id", this.player.getUniqueId().toString())).first();
            Document networkDocument = this.captureTheFlag.getCrimxAPI().getMongoDB().getUserCollection().
                    find(new Document("_id", this.player.getUniqueId().toString())).first();

            Stats stats = this.data.getCachedStats().get(this.player);

            if (stats == null) {
                return;
            }

            if (networkDocument == null) {
                return;
            }

            Document toUpdate = new Document();
            toUpdate.append("kills", stats.getKills())
                    .append("deaths", stats.getDeaths())
                    .append("gamesPlayed", stats.getGamesPlayed())
                    .append("gamesWon", stats.getGamesWon());

            Bson statsUpdateOperation = new Document("$set", toUpdate);
            this.captureTheFlag.getCrimxAPI().getMongoDB().getCaptureTheFlagCollection().updateOne(ctfDocument , statsUpdateOperation);

            long onlineTimeInMillis;

            try {
                onlineTimeInMillis = (long) networkDocument.get("onlinetime");
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

    public void saveKitToDatabase() {
        Document ctfDocument = this.captureTheFlag.getCrimxAPI().getMongoDB().getCaptureTheFlagCollection().
                find(new Document("_id", this.player.getUniqueId().toString())).first();
        this.saveObjectInDocument("selectedKit", this.data.getSelectedKit().get(this.player).name(), ctfDocument);
    }
}
