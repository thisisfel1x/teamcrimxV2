package de.fel1x.teamcrimx.crimxlobby.objects;

import com.destroystokyo.paper.MaterialTags;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.fel1x.teamcrimx.crimxapi.coins.CoinsAPI;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.Data;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.Cosmetic;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.ICosmetic;
import de.fel1x.teamcrimx.crimxlobby.database.LobbyDatabase;
import de.fel1x.teamcrimx.crimxlobby.database.LobbyDatabasePlayer;
import de.fel1x.teamcrimx.crimxlobby.inventories.CosmeticInventory;
import org.bson.Document;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class LobbyPlayer {

    private final CrimxLobby crimxLobby = CrimxLobby.getInstance();
    private final Data data = this.crimxLobby.getData();
    private final LobbyDatabase lobbyDatabase = new LobbyDatabase();

    private final Player player;

    private final IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);

    private final Material[] glassTypes = MaterialTags.STAINED_GLASS.getValues().toArray(new Material[0]);
    private final Material[] concreteTypes = MaterialTags.CONCRETES.getValues().toArray(new Material[0]);

    public LobbyPlayer(Player player) {
        this.player = player;
    }

    public ICloudPlayer getCloudPlayer() {
        return this.playerManager.getOnlinePlayer(this.player.getUniqueId());
    }

    public boolean isInBuild() {
        return this.data.getBuilders().contains(this.player.getUniqueId());
    }

    public void activateBuild() {
        this.data.getBuilders().add(this.player.getUniqueId());

        this.cleanUpPlayer();
        this.player.setGameMode(GameMode.CREATIVE);

        this.player.sendMessage(this.crimxLobby.getPrefix() + "§7Du bist nun im Baumodus");
    }

    public void removeFromBuild() {
        this.data.getBuilders().remove(this.player.getUniqueId());

        this.player.sendMessage(this.crimxLobby.getPrefix() + "§7Du bist nun nicht mehr im Baumodus");

        this.setLobbyInventory();
    }

    public void grantOrRemoveBuildModeToPlayer(Player player) {
        LobbyPlayer lobbyPlayer = new LobbyPlayer(player);
        lobbyPlayer.cleanUpPlayer();

        if ((lobbyPlayer.isInBuild())) {
            lobbyPlayer.removeFromBuild();
        } else {
            lobbyPlayer.activateBuild();
        }
    }

    public void cleanUpPlayer() {
        this.player.getInventory().clear();
        this.player.getInventory().setArmorContents(null);

        this.player.getActivePotionEffects().forEach(potionEffect -> this.player.removePotionEffect(potionEffect.getType()));

        this.player.setFoodLevel(25);
        this.player.setHealth(20);
        this.player.setGameMode(GameMode.ADVENTURE);
    }

    public void setLobbyInventory() {
        boolean hasPermission = this.player.hasPermission("crimxlobby.vip");

        this.player.getInventory().setItem(0, new ItemBuilder(Material.MUSIC_DISC_CAT).setName("§8● §aTeleporter").toItemStack());
        this.player.getInventory().setItem(1, new ItemBuilder(this.getPlayerHiderItemData(), 1)
                .setName(this.getPlayerHiderDisplayName()).toItemStack());
        this.player.getInventory().setItem(3, new ItemBuilder(Material.FISHING_ROD).setName("§8● §bEnterhaken").setUnbreakable().toItemStack());
        this.player.getInventory().setItem(5, new ItemBuilder(Material.CHEST_MINECART).setName("§8● §eCosmetics").toItemStack());
        this.player.getInventory().setItem(7, new ItemBuilder(Material.MOJANG_BANNER_PATTERN).setName("§8● §eLobby wechseln").toItemStack());
        this.player.getInventory().setItem(8, new ItemBuilder(Material.PLAYER_HEAD)
                .setName("§8● §6Profil").setSkullOwner(this.player.getName()).toItemStack());

        if (hasPermission) {
            this.player.getInventory().setItem(4, new ItemBuilder(Material.NAME_TAG).setName("§8● §5Nick").toItemStack());
        }
    }

    public ICosmetic getSelectedCosmetic() {
        return this.data.getCosmetic().get(this.player.getUniqueId());
    }

    public void updatePlayerHiderState() {
        int state = this.data.getPlayerHiderState().get(this.player.getUniqueId());

        state++;

        if (state == 1) {
            Bukkit.getOnlinePlayers().forEach(loop -> {
                if (!loop.hasPermission("crimxlobby.vip")) {
                    this.player.hidePlayer(this.crimxLobby, loop);
                }
            });
        } else if (state == 2) {
            Bukkit.getOnlinePlayers().forEach(loop -> this.player.hidePlayer(this.crimxLobby, loop));
        } else {
            state = 0;
            Bukkit.getOnlinePlayers().forEach(loop -> this.player.showPlayer(this.crimxLobby, loop));
        }

        this.data.getPlayerHiderState().put(this.player.getUniqueId(), state);

        this.player.getInventory().getItem(1).setType(this.getPlayerHiderItemData());
        this.setPlayerHiderDisplayName();
    }

    public Material getPlayerHiderItemData() {
        int state = this.data.getPlayerHiderState().get(this.player.getUniqueId());

        if (state == 1) {
            return Material.PURPLE_DYE;
        } else if (state == 2) {
            return Material.RED_DYE;
        } else {
            return Material.LIME_DYE;
        }
    }

    public void setPlayerHiderDisplayName() {
        ItemStack itemStack = this.player.getInventory().getItem(1);
        if(itemStack == null) {
            return;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(this.getPlayerHiderDisplayName());
        itemStack.setItemMeta(itemMeta);
    }

    public String getPlayerHiderDisplayName() {
        int state = this.data.getPlayerHiderState().get(this.player.getUniqueId());

        if (state == 1) {
            return "§8● §7Du siehst §5nur VIPs";
        } else if (state == 2) {
            return "§8● §7Du siehst §ckeine Spieler";
        } else {
            return "§8● §7Du siehst §aalle Spieler";
        }
    }

    public void teleportToSpawn() {
        boolean spawnAtLastLocation = this.data.getLobbyDatabasePlayer().get(this.player.getUniqueId()).isSpawnAtLastLocation();

        if (spawnAtLastLocation) {
            String serializedLocation = (String) this.crimxLobby.getCrimxAPI().getMongoDB()
                    .getObjectFromDocumentSync(this.player.getUniqueId(), MongoDBCollection.LOBBY, "lastLocation");

            if(serializedLocation != null &&
                    !serializedLocation.trim().equalsIgnoreCase("")
                    && serializedLocation.split(":").length == 6) {

                String[] split = serializedLocation.split(":");

                String worldName = split[0];
                double x = Double.parseDouble(split[1]);
                double y = Double.parseDouble(split[2]);
                double z = Double.parseDouble(split[3]);
                double pitch = Double.parseDouble(split[4]);
                double yaw = Double.parseDouble(split[5]);

                Location lastLocation = new Location(Bukkit.getWorld(worldName), x, y, z, (float) yaw, (float) pitch);

                this.player.teleport(lastLocation);
                return;
            }
        }
        this.teleport(Spawn.SPAWN);
    }

    public void teleport(Spawn spawn) {
        try {
            this.player.teleport(spawn.getPlayerSpawn());
        } catch (NullPointerException exception) {
            this.player.sendMessage(this.crimxLobby.getPrefix() + "§cEin Fehler ist aufgetreten! " +
                    "Bitte versuche es später erneut");
        }
    }

    public void createPlayerData() {
        this.lobbyDatabase.createPlayerData(this.player);
    }

    public void saveNewLocation() {

        final Location spawnLocation = this.player.getLocation();
        String locationSerialized = spawnLocation.getWorld().getName() + ":" + spawnLocation.getX()
                + ":" + spawnLocation.getY() + ":" + spawnLocation.getZ()
                + ":" + spawnLocation.getPitch() + ":" + spawnLocation.getYaw() ;

        Document toUpdate = new Document();
        toUpdate.append("lastLocation", locationSerialized)
                .append("hotbarSound", this.data.getLobbyDatabasePlayer()
                        .get(this.player.getUniqueId()).isHotbarSoundEnabled())
                .append("defaultSpawn", this.data.getLobbyDatabasePlayer()
                        .get(this.player.getUniqueId()).isSpawnAtLastLocation())
                .append("playerhiderState", this.data.getPlayerHiderState().get(this.player.getUniqueId()));

        this.crimxLobby.getCrimxAPI().getMongoDB()
                .updateDocumentInCollectionSync(this.player.getUniqueId(), MongoDBCollection.LOBBY, toUpdate);

    }

    public void initPlayerHider() {
        int state = (int) this.crimxLobby.getCrimxAPI().getMongoDB()
                .getObjectFromDocumentSync(this.player.getUniqueId(), MongoDBCollection.LOBBY, "playerhiderState");

        this.data.getPlayerHiderState().put(this.player.getUniqueId(), state);
    }

    public void loadMongoDocument() {
        // TODO : cache?
        Document lobbyDocument = this.crimxLobby.getCrimxAPI().getMongoDB()
                .getDocumentSync(this.player.getUniqueId(), MongoDBCollection.LOBBY);

        if(lobbyDocument == null) {
            return;
        }

        boolean hotbarSoundEnabled = lobbyDocument.getBoolean("hotbarSound");
        boolean spawnAtLastLocation = lobbyDocument.getBoolean("defaultSpawn");
        long lastReward = lobbyDocument.getLong("lastReward");

        this.data.getLobbyDatabasePlayer().put(this.player.getUniqueId(), new LobbyDatabasePlayer(hotbarSoundEnabled, spawnAtLastLocation, lastReward));

        for (Cosmetic cosmetic : Cosmetic.values()) {
            if (this.crimxLobby.getCrimxAPI().getMongoDB()
                    .getObjectFromDocumentSync(this.player.getUniqueId(), MongoDBCollection.LOBBY, cosmetic.name()) == null) {
                this.crimxLobby.getCrimxAPI().getMongoDB().insertObjectInDocument(this.player.getUniqueId(),
                        MongoDBCollection.LOBBY, cosmetic.name(), false);
            }
        }
    }

    /**
     * Unlocks a cosmetic if the player has enough coins <br>
     * NO LONGER SUPPORTED - FOR REMOVAL - MIGHT NOT WORK PROPERLY ANYMORE <br>
     * Deprecated - use package {@link de.fel1x.teamcrimx.crimxapi.cosmetic} for more
     * @param cosmetic the cosmetic to unlock
     */
    @Deprecated
    public void unlockCosmetic(Cosmetic cosmetic) {
        // TODO: new cosmetic api
        try {
            ICosmetic iCosmetic = cosmetic.getCosmeticClass().newInstance();
            CoinsAPI coinsAPI = new CoinsAPI(this.player.getUniqueId());

            int coins = coinsAPI.getCoins();
            int required = cosmetic.getCosmeticClass().newInstance().getCosmeticCost();

            if (coins >= required) {
                this.player.sendMessage(this.crimxLobby.getPrefix() + "§7Du hast erfolgreich §a" + iCosmetic.getCosmeticName() + " §7freigeschalten");
                this.player.playSound(this.player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2, 0.5f);
                CosmeticInventory.COSMETICS_INVENTORY.open(this.player);

                coinsAPI.removeCoins(required);
                Bukkit.broadcastMessage(String.valueOf(this.crimxLobby.getCrimxAPI().getMongoDB().insertObjectInDocument(this.player.getUniqueId(),
                        MongoDBCollection.LOBBY, cosmetic.name(), true)));
            } else {
                this.player.playSound(this.player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 2, 0.5f);
                this.player.sendMessage(this.crimxLobby.getPrefix() + "§7Du hast nicht genügend Coins!");
                this.player.closeInventory();
            }
        } catch (InstantiationException | IllegalAccessException e) {
            this.player.closeInventory();
            this.player.playSound(this.player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 2, 0.5f);
            this.player.sendMessage(this.crimxLobby.getPrefix() + "§cEin Fehler ist aufgetreten! Bitte versuche es später erneut.");
        }
    }

    public void setScoreboard() {
        this.crimxLobby.getLobbyScoreboard().setDefaultLobbyScoreboard(this.player);
    }
}
