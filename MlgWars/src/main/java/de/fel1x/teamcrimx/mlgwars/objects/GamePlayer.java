package de.fel1x.teamcrimx.mlgwars.objects;

import com.google.common.collect.Lists;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.UpdateOptions;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.fel1x.teamcrimx.crimxapi.coins.CoinsAPI;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.Data;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.database.MlgWarsDatabase;
import de.fel1x.teamcrimx.mlgwars.enums.Spawns;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.kit.IKit;
import de.fel1x.teamcrimx.mlgwars.kit.Kit;
import de.fel1x.teamcrimx.mlgwars.scoreboard.LobbyScoreboard;
import me.libraryaddict.disguise.DisguiseAPI;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.UUID;

public class GamePlayer {

    private final MlgWars mlgWars = MlgWars.getInstance();
    private final Data data = this.mlgWars.getData();

    private final LobbyScoreboard lobbyScoreboard = new LobbyScoreboard();

    private final IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);

    private final Player player;
    private Document networkDocument;
    private Document mlgWarsDocument;

    public GamePlayer(Player player) {
        this.player = player;

        this.mlgWarsDocument = this.data.getMlgWarsPlayerDocument().get(this.player.getUniqueId());
        this.networkDocument = this.data.getNetworkPlayerDocument().get(this.player.getUniqueId());
    }

    public GamePlayer(Player player, boolean documentUpdate) {

        this.player = player;

        if(documentUpdate) {
            Document mlgWarsDocument = this.mlgWars.getCrimxAPI().getMongoDB().getMlgWarsCollection().
                    find(new Document("_id", player.getUniqueId().toString())).first();

            Document networkDocument = this.mlgWars.getCrimxAPI().getMongoDB().getUserCollection().
                    find(new Document("_id", player.getUniqueId().toString())).first();

            this.data.getMlgWarsPlayerDocument().put(this.player.getUniqueId(), mlgWarsDocument);
            this.data.getNetworkPlayerDocument().put(this.player.getUniqueId(), networkDocument);
        }

        this.mlgWarsDocument = this.data.getMlgWarsPlayerDocument().get(this.player.getUniqueId());
        this.networkDocument = this.data.getNetworkPlayerDocument().get(this.player.getUniqueId());
    }

    public void initDatabasePlayer() {
        Document mlgWarsDocument = this.mlgWars.getCrimxAPI().getMongoDB().getMlgWarsCollection().
                find(new Document("_id", player.getUniqueId().toString())).first();

        Document networkDocument = this.mlgWars.getCrimxAPI().getMongoDB().getUserCollection().
                find(new Document("_id", player.getUniqueId().toString())).first();

        this.data.getMlgWarsPlayerDocument().put(this.player.getUniqueId(), mlgWarsDocument);
        this.data.getNetworkPlayerDocument().put(this.player.getUniqueId(), networkDocument);

        this.mlgWarsDocument = this.data.getMlgWarsPlayerDocument().get(this.player.getUniqueId());
        this.networkDocument = this.data.getNetworkPlayerDocument().get(this.player.getUniqueId());

        if(!((String) this.getObjectFromMongoDocument("name", MongoDBCollection.MLGWARS)).equalsIgnoreCase(this.player.getName())) {
            this.saveObjectInDocument("name", player.getName(), MongoDBCollection.MLGWARS);
        }

        String selectedKitName = (String) this.getObjectFromMongoDocument("selectedKit", MongoDBCollection.MLGWARS);
        Kit selectedKit = Kit.valueOf(selectedKitName);

        int gamesPlayed = (int) this.getObjectFromMongoDocument("gamesPlayed", MongoDBCollection.MLGWARS);
        this.player.setMetadata("games", new FixedMetadataValue(this.mlgWars, gamesPlayed));

        Gamestate gamestate = this.mlgWars.getGamestateHandler().getGamestate();

        if(gamestate == Gamestate.IDLE || gamestate == Gamestate.LOBBY) {
            this.setSelectedKit(selectedKit);
        }

    }

    public boolean isPlayer() {
        return this.data.getPlayers().contains(player);
    }

    public void addToPlayers() {
        this.data.getPlayers().add(player);
    }

    public void removeFromPlayers() {
        this.data.getPlayers().remove(player);
    }

    public boolean isSpectator() {
        return this.data.getSpectators().contains(player);
    }

    public void addToSpectators() {
        this.data.getSpectators().add(player);
    }

    public void removeFromSpectators() {
        this.data.getSpectators().remove(player);
    }

    public ICloudPlayer getCloudPlayer() {
        return this.playerManager.getOnlinePlayer(this.player.getUniqueId());
    }

    public void cleanUpOnJoin() {

        this.player.setMetadata("kills", new FixedMetadataValue(this.mlgWars, 0));

        this.player.getInventory().clear();
        this.player.getInventory().setArmorContents(null);

        this.player.setGameMode(GameMode.ADVENTURE);

        this.player.setHealth(20);
        this.player.setFoodLevel(25);

        this.player.setLevel(0);
        this.player.setExp(0);

        this.player.setFlying(false);
        this.player.setAllowFlight(false);
        this.player.setFlying(false);

        this.data.getPlayerGg().put(this.player.getUniqueId(), false);

        this.player.getActivePotionEffects().forEach(potionEffect -> this.player.removePotionEffect(potionEffect.getType()));

        Bukkit.getOnlinePlayers().forEach(onlinePlayers -> {
            onlinePlayers.showPlayer(this.player);
            this.player.showPlayer(onlinePlayers);
        });

    }

    public void cleanUpOnQuit() {

        this.removeFromPlayers();
        this.removeFromPlayers();

    }

    public void setJoinItems() {

        this.player.getInventory().setItem(0, new ItemBuilder(Material.STORAGE_MINECART).setName("§8● §aKitauswahl").toItemStack());

        if(this.player.hasPermission("mlgwars.forcemap") || this.player.isOp()) {
            this.player.getInventory().setItem(1, new ItemBuilder(Material.REDSTONE_TORCH_ON).setName("§8● §cForcemap").toItemStack());
        }

    }

    public void teleport(Spawns spawns) {
        try {
            Bukkit.getScheduler().runTaskLater(this.mlgWars, () -> {
                this.player.teleport(spawns.getLocation());
            }, 1L);
        } catch (NullPointerException exception) {
            this.player.sendMessage(this.mlgWars.getPrefix() + "§cEin Fehler ist aufgetreten!");
            exception.printStackTrace();
        }
    }


    public void activateSpectatorMode() {

        this.removeFromPlayers();
        this.addToSpectators();

        this.player.setGameMode(GameMode.ADVENTURE);

        this.player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, true, false));

        this.player.setAllowFlight(true);
        this.player.setFlying(true);

        this.player.setPlayerListName("§o§8[§4✖§8] §7" + this.player.getName());

        this.player.getInventory().setItem(0, new ItemBuilder(Material.COMPASS)
                .setName("§8● §aSpieler beobachten")
                .toItemStack());

        this.data.getPlayers().forEach(ingamePlayer -> ingamePlayer.hidePlayer(this.player));

    }

    public void createPlayerData() {
        MlgWarsDatabase mlgWarsDatabase = new MlgWarsDatabase();
        mlgWarsDatabase.createPlayerData(this.player);
    }

    public Object getObjectFromMongoDocument(String key, MongoDBCollection mongoDBCollection) {
        switch (mongoDBCollection) {
            case MLGWARS:
                return this.mlgWarsDocument.get(key);
            case USERS:
                return this.networkDocument.get(key);
            default:
                return null;
        }
    }

    public void saveObjectInDocument(String key, Object value, MongoDBCollection mongoDBCollection) {

        Document document = new Document(key, value);
        Bson updateOperation = new Document("$set", document);

        switch (mongoDBCollection) {
            case MLGWARS:
                this.mlgWars.getCrimxAPI().getMongoDB().getMlgWarsCollection().updateOne(this.mlgWarsDocument, updateOperation);
                break;
            case USERS:
                this.mlgWars.getCrimxAPI().getMongoDB().getUserCollection().updateOne(this.networkDocument, updateOperation);
        }
    }

    public void setSelectedKit(Kit kit) {
        try {
            IKit iKit = kit.getClazz().newInstance();
            this.data.getSelectedKit().remove(this.player);
            this.data.getSelectedKit().put(this.player, kit);
            this.player.getInventory().setItem(8, new ItemBuilder(iKit.getKitMaterial())
                    .setName("§8● §a" + iKit.getKitName())
                    .setLore(iKit.getKitDescription()).toItemStack());

            this.saveObjectInDocument("selectedKit", kit.name(), MongoDBCollection.MLGWARS);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Kit getSelectedKit() {
        Kit chosen = this.data.getSelectedKit().get(this.player);
        return chosen == null ? Kit.STARTER : chosen;
    }

    public int getRankingPosition() {

        List<Document> documents = this.mlgWars.getCrimxAPI().getMongoDB().getMlgWarsCollection().find().sort(Sorts.descending("gamesWon")).into(Lists.newArrayList());

        for (Document document : documents) {
            if(document.getString("_id").equalsIgnoreCase(this.player.getUniqueId().toString())) {
                return documents.indexOf(document) + 1;
            }
        }

        return -1;
    }

    public void sendToService(String serviceName) {
        this.playerManager.getPlayerExecutor(this.player.getUniqueId()).connect(serviceName);
    }

    public void setLobbyScoreboard() {
        this.lobbyScoreboard.setLobbyScoreboard(this.player);
    }

    public void setInGameScoreboard() {
        this.lobbyScoreboard.setIngameScoreboard(this.player);
    }

    public void unlockKit(Kit kit) {

        try {
            IKit iKit = kit.getClazz().newInstance();
            CoinsAPI coinsAPI = new CoinsAPI(this.player.getUniqueId());

            int coins = coinsAPI.getCoins();
            int required = kit.getClazz().newInstance().getKitCost();

            if(coins >= required) {
                player.sendMessage(this.mlgWars.getPrefix() + "§7Du hast erfolgreich §e[" + iKit.getKitName() + "] §7freigeschalten");
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 2, 0.5f);
                player.closeInventory();

                coinsAPI.removeCoins(required);
                this.saveObjectInDocument(kit.name(), true, MongoDBCollection.MLGWARS);
                this.setSelectedKit(kit);

            } else {
                player.playSound(player.getLocation(), Sound.NOTE_BASS, 2, 0.5f);
                player.sendMessage(this.mlgWars.getPrefix() + "§7Du hast nicht genügend Coins!");
                player.closeInventory();
            }

        } catch (InstantiationException | IllegalAccessException e) {
            player.closeInventory();
            player.playSound(player.getLocation(), Sound.NOTE_BASS, 2, 0.5f);
            player.sendMessage(this.mlgWars.getPrefix() + "§cEin Fehler ist aufgetreten! Bitte versuche es später erneut.");
        }
    }
}
