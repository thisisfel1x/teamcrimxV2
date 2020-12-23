package de.fel1x.teamcrimx.crimxlobby.objects;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.fel1x.teamcrimx.crimxapi.coins.CoinsAPI;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.crimxapi.utils.TimeUtils;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.Data;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.Cosmetic;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.ICosmetic;
import de.fel1x.teamcrimx.crimxlobby.database.LobbyDatabase;
import de.fel1x.teamcrimx.crimxlobby.database.LobbyDatabasePlayer;
import de.fel1x.teamcrimx.crimxlobby.inventories.CosmeticInventory;
import de.fel1x.teamcrimx.crimxlobby.manager.SpawnManager;
import de.fel1x.teamcrimx.crimxlobby.minigames.jumpandrun.JumpAndRunPlayer;
import de.fel1x.teamcrimx.crimxlobby.minigames.watermlg.WaterMlgHandler;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class LobbyPlayer {

    final Player player;
    final CrimxLobby crimxLobby = CrimxLobby.getInstance();
    final Data data = this.crimxLobby.getData();
    final LobbyDatabase lobbyDatabase = new LobbyDatabase();
    private final IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);
    private final WaterMlgHandler waterMlgHandler = this.crimxLobby.getWaterMlgHandler();
    private final Material[] woolTypes = {
            Material.WHITE_WOOL, Material.ORANGE_WOOL, Material.MAGENTA_WOOL, Material.LIGHT_BLUE_WOOL, Material.YELLOW_WOOL,
            Material.LIME_WOOL, Material.PINK_WOOL, Material.GRAY_WOOL, Material.LIGHT_GRAY_WOOL, Material.CYAN_WOOL,
            Material.PURPLE_WOOL, Material.BLUE_WOOL, Material.GREEN_WOOL, Material.RED_WOOL
    };
    private final Material[] concreteTypes = {
            Material.WHITE_CONCRETE, Material.ORANGE_CONCRETE, Material.MAGENTA_CONCRETE, Material.LIGHT_BLUE_CONCRETE, Material.YELLOW_CONCRETE,
            Material.LIME_CONCRETE, Material.PINK_CONCRETE, Material.GRAY_CONCRETE, Material.LIGHT_GRAY_CONCRETE, Material.CYAN_CONCRETE,
            Material.PURPLE_CONCRETE, Material.BLUE_CONCRETE, Material.GREEN_CONCRETE, Material.RED_CONCRETE
    };
    SpawnManager spawnManager = this.crimxLobby.getSpawnManager();
    Document lobbyDocument;
    Document networkDocument;

    public LobbyPlayer(Player player) {
        this.player = player;

        this.lobbyDocument = this.data.getPlayerMongoDocument().get(player.getUniqueId());
        this.networkDocument = this.data.getPlayerMongoNetworkDocument().get(player.getUniqueId());

    }

    public LobbyPlayer(Player player, boolean updateDocument) {
        this.player = player;

        if (updateDocument) {
            Document found = this.crimxLobby.getCrimxAPI().getMongoDB().getLobbyCollection().
                    find(new Document("_id", player.getUniqueId().toString())).first();

            Document foundNetwork = this.crimxLobby.getCrimxAPI().getMongoDB().getUserCollection().
                    find(new Document("_id", player.getUniqueId().toString())).first();

            this.crimxLobby.getData().getPlayerMongoDocument().put(player.getUniqueId(), found);
            this.crimxLobby.getData().getPlayerMongoNetworkDocument().put(player.getUniqueId(), foundNetwork);
        }

        this.lobbyDocument = this.data.getPlayerMongoDocument().get(player.getUniqueId());
        this.networkDocument = this.data.getPlayerMongoNetworkDocument().get(player.getUniqueId());

        CoinsAPI coinsAPI = new CoinsAPI(this.player.getUniqueId());
        int coins = coinsAPI.getCoins();

        this.crimxLobby.getLobbyScoreboard().updateBoard(player, String.format("§8● §e%s Coins", coins), "coins", "§e");
        this.crimxLobby.getLobbyScoreboard().updateBoard(player, "§8● §6" + this.getOnlineTimeForScoreboard(), "playtime", "§6");
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

        this.cleanUpPlayer();

        if ((lobbyPlayer.isInBuild())) {
            lobbyPlayer.removeFromBuild();
        } else {
            lobbyPlayer.activateBuild();
        }

    }

    public void cleanUpPlayer() {

        this.player.getInventory().clear();
        this.player.getActivePotionEffects().forEach(potionEffect -> this.player.removePotionEffect(potionEffect.getType()));

        this.player.setFoodLevel(25);
        this.player.setGameMode(GameMode.SURVIVAL);

        this.player.setHealth(20);

    }

    public void setLobbyInventory() {

        boolean hasPermission = this.player.hasPermission("crimxlobby.vip");

        this.player.getInventory().setItem(0, new ItemBuilder(Material.MUSIC_DISC_CAT).setName("§8● §aTeleporter").toItemStack());
        this.player.getInventory().setItem(1, new ItemBuilder(this.getPlayerHiderItemData(), 1)
                .setName(this.getPlayerHiderDisplayName()).toItemStack());
        this.player.getInventory().setItem(3, new ItemBuilder(Material.FISHING_ROD).setName("§8● §bEnterhaken").setUnbreakable().toItemStack());
        this.player.getInventory().setItem(5, new ItemBuilder(Material.CHEST_MINECART).setName("§8● §eCosmetics").toItemStack());
        this.player.getInventory().setItem(7, new ItemBuilder(Material.REPEATER).setName("§8● §cEinstellungen").toItemStack());
        this.player.getInventory().setItem(8, new ItemBuilder(Material.NETHER_STAR).addGlow().setName("§8● §6Lobby Minispiele").toItemStack());

        if (hasPermission) {
            this.player.getInventory().setItem(4, new ItemBuilder(Material.NAME_TAG).setName("§8● §5Nick").toItemStack());
        }

        try {

            Cosmetic selectedCosmetic = Cosmetic.valueOf((String) this.getObjectFromMongoDocument("selectedCosmetic", MongoDBCollection.LOBBY));
            ICosmetic iCosmetic = selectedCosmetic.getCosmeticClass().newInstance();

            Bukkit.getScheduler().runTaskLater(this.crimxLobby, () -> iCosmetic.startTrail(this.player), 10L);

        } catch (Exception ignored) {
            //this.player.sendMessage(this.crimxLobby.getPrefix() + "§cEin Fehler ist aufgetreten");
        }

        this.player.setGameMode(GameMode.SURVIVAL);

    }

    public ICosmetic getSelectedCosmetic() {
        return this.data.getCosmetic().get(this.player.getUniqueId());
    }

    public void updatePlayerHiderState() {

        // STATES: 0: all shown, 1: vip shown, 2: nobody shown
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

    public String getOnlineTimeForScoreboard() {

        long onlineTimeInMillis;

        try {
            onlineTimeInMillis = (long) this.getObjectFromMongoDocument("onlinetime", MongoDBCollection.USERS);
        } catch (Exception ignored) {
            onlineTimeInMillis = Integer.toUnsignedLong((Integer) this.getObjectFromMongoDocument("onlinetime", MongoDBCollection.USERS));
        }

        if (onlineTimeInMillis < 1000 * 60) {
            return "Keine Daten";
        } else if (onlineTimeInMillis > 1000 * 60 && onlineTimeInMillis < 1000 * 60 * 60) {
            long minutes = TimeUtils.splitTime(onlineTimeInMillis)[2];
            return minutes == 1 ? minutes + " Minute" : minutes + " Minuten";
        } else {
            long hours = TimeUtils.splitTime(onlineTimeInMillis)[1];
            return hours == 1 ? hours + " Stunde" : hours + " Stunden";
        }
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

        int state = this.data.getPlayerHiderState().get(this.player.getUniqueId());

        ItemStack itemStack = this.player.getInventory().getItem(1);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (state == 1) {
            itemMeta.setDisplayName("§8● §7Du siehst §5nur VIPs");
        } else if (state == 2) {
            itemMeta.setDisplayName("§8● §7Du siehst §ckeinen Spieler");
        } else {
            itemMeta.setDisplayName("§8● §7Du siehst §aalle Spieler");
            this.player.playEffect(EntityEffect.WITCH_MAGIC);
        }

        itemStack.setItemMeta(itemMeta);

    }

    public String getPlayerHiderDisplayName() {

        int state = this.data.getPlayerHiderState().get(this.player.getUniqueId());

        if (state == 1) {
            return "§8● §7Du siehst §5nur VIPs";
        } else if (state == 2) {
            return "§8● §7Du siehst §ckeinen Spieler";
        } else {
            return "§8● §7Du siehst §aalle Spieler";
        }
    }

    public void teleportToSpawn() {

        boolean defaultSpawn = this.data.getLobbyDatabasePlayer().get(this.player.getUniqueId()).isSpawnAtLastLocation();

        if (defaultSpawn) {
            String worldName = this.lobbyDocument.getString("lastLocationWorld");
            double x = this.lobbyDocument.getDouble("lastLocationX");
            double y = this.lobbyDocument.getDouble("lastLocationY");
            double z = this.lobbyDocument.getDouble("lastLocationZ");
            double pitch = this.lobbyDocument.getDouble("lastLocationPitch");
            double yaw = this.lobbyDocument.getDouble("lastLocationYaw");

            Location lastLocation = new Location(Bukkit.getWorld(worldName), x, y, z, (float) yaw, (float) pitch);

            this.player.teleport(lastLocation);
        } else {
            this.teleport(Spawn.SPAWN);
        }

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

        Document toUpdate = new Document();
        toUpdate.append("lastLocationWorld", this.player.getLocation().getWorld().getName())
                .append("lastLocationX", this.player.getLocation().getX())
                .append("lastLocationY", this.player.getLocation().getY())
                .append("lastLocationZ", this.player.getLocation().getZ())
                .append("lastLocationPitch", this.player.getLocation().getPitch())
                .append("lastLocationYaw", this.player.getLocation().getYaw())
                .append("playerhiderState", this.data.getPlayerHiderState().get(this.player.getUniqueId()));

        Bson updateOperation = new Document("$set", toUpdate);
        this.crimxLobby.getCrimxAPI().getMongoDB().getLobbyCollection().updateOne(this.lobbyDocument, updateOperation);

    }

    public void initPlayerHider() {

        int state = this.lobbyDocument.getInteger("playerhiderState");
        this.data.getPlayerHiderState().put(this.player.getUniqueId(), state);

    }

    public void loadMongoDocument() {
        Document found = this.crimxLobby.getCrimxAPI().getMongoDB().getLobbyCollection().
                find(new Document("_id", this.player.getUniqueId().toString())).first();

        Document foundNetwork = this.crimxLobby.getCrimxAPI().getMongoDB().getUserCollection().
                find(new Document("_id", this.player.getUniqueId().toString())).first();

        this.crimxLobby.getData().getPlayerMongoDocument().put(this.player.getUniqueId(), found);
        this.crimxLobby.getData().getPlayerMongoNetworkDocument().put(this.player.getUniqueId(), foundNetwork);

        this.lobbyDocument = found;
        this.networkDocument = foundNetwork;

        boolean hotbarSoundEnabled = this.lobbyDocument.getBoolean("hotbarSound");
        boolean spawnAtLastLocation = this.lobbyDocument.getBoolean("defaultSpawn");
        long lastReward = this.lobbyDocument.getLong("lastReward");

        this.data.getLobbyDatabasePlayer().put(this.player.getUniqueId(), new LobbyDatabasePlayer(hotbarSoundEnabled, spawnAtLastLocation, lastReward));

        for (Cosmetic cosmetic : Cosmetic.values()) {
            if (this.getObjectFromMongoDocument(cosmetic.name(), MongoDBCollection.LOBBY) == null) {
                this.saveObjectInDocument(cosmetic.name(), false, MongoDBCollection.LOBBY);
            }
        }

    }

    public Object getObjectFromMongoDocument(String key, MongoDBCollection mongoDBCollection) {
        switch (mongoDBCollection) {
            case LOBBY:
                return this.lobbyDocument.get(key);
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
            case LOBBY:
                this.crimxLobby.getCrimxAPI().getMongoDB().getLobbyCollection().updateOne(this.lobbyDocument, updateOperation);
                break;
            case USERS:
                this.crimxLobby.getCrimxAPI().getMongoDB().getUserCollection().updateOne(this.networkDocument, updateOperation);
        }
    }

    public void startJumpAndRun() {

        Location location = this.player.getLocation();
        Random random = new Random();

        int height = random.nextInt(20) + 10;

        ArrayList<Location> possibleJumps = new ArrayList<>();

        for (int x = 2; x < 4; x++) {
            for (int z = 2; z < 4; z++) {

                x = random.nextBoolean() ? x : -1 * x;
                z = random.nextBoolean() ? z : -1 * z;

                possibleJumps.add(location.clone().add(x, height, z));

            }
        }

        Block startBlock = possibleJumps.get(random.nextInt(possibleJumps.size())).getBlock();

        int y = 0;
        Block nextBlock = possibleJumps.get(random.nextInt(possibleJumps.size())).getBlock();

        boolean notOk = nextBlock.getX() == startBlock.getX() + 1 || nextBlock.getX() == startBlock.getX() - 1
                || nextBlock.getZ() == startBlock.getZ() + 1 || nextBlock.getZ() == startBlock.getZ() - 1
                || nextBlock.getX() == startBlock.getX() && nextBlock.getY() == startBlock.getY() + 1 && nextBlock.getZ() == startBlock.getZ();

        while (notOk) {
            nextBlock = possibleJumps.get(random.nextInt(possibleJumps.size())).clone().add(0, y, 0).getBlock();
            notOk = nextBlock.getX() == startBlock.getX() + 1 || nextBlock.getX() == startBlock.getX() - 1
                    || nextBlock.getZ() == startBlock.getZ() + 1 || nextBlock.getZ() == startBlock.getZ() - 1
                    || nextBlock.getX() == startBlock.getX() && nextBlock.getY() == startBlock.getY() + 1 && nextBlock.getZ() == startBlock.getZ();
        }

        if (startBlock.getType().equals(Material.AIR)) {
            startBlock.setType(Arrays.asList(this.concreteTypes).get(random.nextInt(this.concreteTypes.length)), true);
        } else {
            this.player.sendMessage(this.crimxLobby.getPrefix() + "§cEs konnte kein passender Start gefunden werden! Bitte gehe zu einer offenen Stelle und versuche es erneut");
            return;
        }

        if (nextBlock.getType().equals(Material.AIR)) {
            nextBlock.setType(Arrays.asList(this.woolTypes).get(random.nextInt(this.woolTypes.length)), true);
        } else {

            this.player.sendMessage(this.crimxLobby.getPrefix() + "§cEs konnte kein passender Sprung gefunden werden! Bitte gehe zu einer offenen Stelle und versuche es erneut");
            startBlock.setType(Material.AIR);
            return;

        }

        this.player.getInventory().clear();

        this.player.teleport(startBlock.getLocation().clone().add(0, 2, 0));
        this.player.playSound(this.player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 2, 4);

        this.player.getInventory().setItem(4, new ItemBuilder(Material.RED_DYE, 1).setName("§cAbbrechen").toItemStack());

        JumpAndRunPlayer jumpAndRunPlayer = new JumpAndRunPlayer(this.player, 0, null, startBlock, nextBlock, possibleJumps);

        this.data.getJumpAndRunPlayers().put(this.player.getUniqueId(), jumpAndRunPlayer);
        this.data.getJumpers().add(this.player.getUniqueId());

    }

    public void generateNextJump() {

        JumpAndRunPlayer jumpAndRunPlayer = this.data.getJumpAndRunPlayers().get(this.player.getUniqueId());

        Location location;
        Random random = new Random();

        jumpAndRunPlayer.setLastBlock(jumpAndRunPlayer.getCurrentBlock());
        jumpAndRunPlayer.getLastBlock().setType(Material.AIR);

        jumpAndRunPlayer.setCurrentBlock(jumpAndRunPlayer.getNextBlock());
        jumpAndRunPlayer.getCurrentBlock().setType(Arrays.asList(this.concreteTypes).get(random.nextInt(this.concreteTypes.length)), true);

        location = jumpAndRunPlayer.getCurrentBlock().getLocation();

        ArrayList<Location> possibleJumps = new ArrayList<>();

        for (int x = 2; x < 4; x++) {
            for (int z = 2; z < 4; z++) {

                x = random.nextBoolean() ? x : -1 * x;
                z = random.nextBoolean() ? z : -1 * z;

                possibleJumps.add(location.clone().add(x, 0, z));

            }
        }

        jumpAndRunPlayer.getPossibleJumps().clear();
        jumpAndRunPlayer.setPossibleJumps(possibleJumps);

        int y = random.nextBoolean() ? 0 : 1;

        Block currentBlock = jumpAndRunPlayer.getCurrentBlock();
        Block nextBlock = possibleJumps.get(random.nextInt(possibleJumps.size())).clone().add(0, y, 0).getBlock();

        boolean notOk = nextBlock.getX() == currentBlock.getX() + 1 || nextBlock.getX() == currentBlock.getX() - 1
                || nextBlock.getZ() == currentBlock.getZ() + 1 || nextBlock.getZ() == currentBlock.getZ() - 1
                || nextBlock.getX() == currentBlock.getX() && nextBlock.getY() == currentBlock.getY() + 1 && nextBlock.getZ() == currentBlock.getZ();

        while (notOk) {
            nextBlock = possibleJumps.get(random.nextInt(possibleJumps.size())).clone().add(0, y, 0).getBlock();
            notOk = nextBlock.getX() == currentBlock.getX() + 1 || nextBlock.getX() == currentBlock.getX() - 1
                    || nextBlock.getZ() == currentBlock.getZ() + 1 || nextBlock.getZ() == currentBlock.getZ() - 1
                    || nextBlock.getX() == currentBlock.getX() && nextBlock.getY() == currentBlock.getY() + 1 && nextBlock.getZ() == currentBlock.getZ();
        }

        if (nextBlock.getType().equals(Material.AIR)) {
            nextBlock.setType(Arrays.asList(this.woolTypes).get(random.nextInt(this.woolTypes.length)), true);

            jumpAndRunPlayer.setNextBlock(nextBlock);
        } else {

            int c = 0;

            while (!nextBlock.getType().equals(Material.AIR) && c < 5) {

                nextBlock = jumpAndRunPlayer.getPossibleJumps().get(random.nextInt(jumpAndRunPlayer.getPossibleJumps().size())).getBlock();
                c++;

            }

            if (nextBlock.getType().equals(Material.AIR)) {

                nextBlock.setType(Arrays.asList(this.woolTypes).get(random.nextInt(this.woolTypes.length)), true);
                jumpAndRunPlayer.setNextBlock(nextBlock);

            } else {

                this.player.sendMessage("§cEs konnte kein passender Sprung gefunden werden! Bitte gehe zu einer offenen Stelle und versuche es erneut");
                this.endJumpAndRun();

            }

        }

    }

    public boolean isInJumpAndRun() {
        return this.data.getJumpers().contains(this.player.getUniqueId());
    }

    public void endJumpAndRun() {

        this.player.getInventory().clear();

        this.data.getJumpers().remove(this.player.getUniqueId());
        this.setLobbyInventory();

        this.data.getJumpAndRunPlayers().get(this.player.getUniqueId()).getCurrentBlock().setType(Material.AIR);
        this.data.getJumpAndRunPlayers().get(this.player.getUniqueId()).getNextBlock().setType(Material.AIR);

        this.data.getJumpAndRunPlayers().remove(this.player.getUniqueId());

    }

    public boolean isInWaterMLG() {
        return this.waterMlgHandler.getWaterMlgPlayers().contains(this.player);
    }

    public void startWaterMLG() {

        int height = new Random().nextInt(15) + 5;

        this.player.closeInventory();

        this.player.setVelocity(this.player.getVelocity().setY(height));

        this.cleanUpPlayer();
        this.player.getInventory().setItem(3, new ItemBuilder(Material.WATER_BUCKET).setName("§9Wassereimer").toItemStack());
        this.player.getInventory().setItem(5, new ItemBuilder(Material.RED_DYE, 1).setName("§cAbbrechen").toItemStack());

        this.waterMlgHandler.getWaterMlgPlayers().add(this.player);
        this.waterMlgHandler.getFailed().put(this.player, false);

    }

    public void endWaterMLG() {

        this.cleanUpPlayer();
        this.setLobbyInventory();
        this.waterMlgHandler.getWaterMlgPlayers().remove(this.player);

    }

    public boolean isMlgFailed() {
        return this.waterMlgHandler.getFailed().get(this.player);
    }

    public void unlockCosmetic(Cosmetic cosmetic) {

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
                this.saveObjectInDocument(cosmetic.name(), true, MongoDBCollection.LOBBY);

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
        this.crimxLobby.getLobbyScoreboard().setGameScoreboard(this.player);
    }
}
