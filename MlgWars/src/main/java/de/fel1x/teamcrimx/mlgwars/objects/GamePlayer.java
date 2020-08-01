package de.fel1x.teamcrimx.mlgwars.objects;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.Data;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.database.MlgWarsDatabase;
import de.fel1x.teamcrimx.mlgwars.enums.Spawns;
import de.fel1x.teamcrimx.mlgwars.kit.IKit;
import de.fel1x.teamcrimx.mlgwars.kit.Kit;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GamePlayer {

    private final MlgWars mlgWars = MlgWars.getInstance();
    private final Data data = this.mlgWars.getData();

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

        this.player.getInventory().clear();
        this.player.getInventory().setArmorContents(null);

        this.player.setGameMode(GameMode.ADVENTURE);

        this.player.setHealth(20);
        this.player.setFoodLevel(25);

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
        this.player.getInventory().setItem(8, new ItemBuilder(Material.BARRIER).setName("§8● §cKein Kit ausgewählt").toItemStack());

        if(this.player.hasPermission("mlgwars.forcemap") || this.player.isOp()) {
            this.player.getInventory().setItem(1, new ItemBuilder(Material.REDSTONE_TORCH_ON).setName("§8● §cForcemap").toItemStack());
        }

    }

    public void teleport(Spawns spawns) {
        try {
            Bukkit.getScheduler().runTaskLater(this.mlgWars, () -> {
                this.player.teleport(spawns.getLocation());
            }, 3L);
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
            this.data.getSelectedKit().put(this.player, kit);
            this.player.getInventory().setItem(8, new ItemBuilder(iKit.getKitMaterial())
                    .setName("§8● §a" + iKit.getKitName())
                    .setLore(iKit.getKitDescription()).toItemStack());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Kit getSelectedKit() {
        Kit chosen = this.data.getSelectedKit().get(this.player);
        return chosen == null ? Kit.STARTER : chosen;
    }
}
