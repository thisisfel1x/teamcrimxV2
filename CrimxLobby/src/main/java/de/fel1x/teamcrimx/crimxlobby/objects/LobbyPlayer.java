package de.fel1x.teamcrimx.crimxlobby.objects;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.crimxapi.objects.CrimxPlayer;
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
import net.jitse.npclib.api.NPC;
import net.jitse.npclib.api.skin.Skin;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class LobbyPlayer {

    private final IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);

    Player player;

    CrimxLobby crimxLobby = CrimxLobby.getInstance();
    Data data = this.crimxLobby.getData();

    SpawnManager spawnManager = this.crimxLobby.getSpawnManager();
    LobbyDatabase lobbyDatabase = new LobbyDatabase();

    Document lobbyDocument;
    Document networkDocument;
    private final WaterMlgHandler waterMlgHandler = this.crimxLobby.getWaterMlgHandler();

    public LobbyPlayer(Player player) {
        this.player = player;

        this.lobbyDocument = this.data.getPlayerMongoDocument().get(player.getUniqueId());
        this.networkDocument = this.data.getPlayerMongoNetworkDocument().get(player.getUniqueId());

    }

    public LobbyPlayer(Player player, boolean updateDocument) {
        this.player = player;

        if(updateDocument) {
            Document found = this.crimxLobby.getCrimxAPI().getMongoDB().getLobbyCollection().
                    find(new Document("_id", player.getUniqueId().toString())).first();

            Document foundNetwork = this.crimxLobby.getCrimxAPI().getMongoDB().getUserCollection().
                    find(new Document("_id", player.getUniqueId().toString())).first();

            this.crimxLobby.getData().getPlayerMongoDocument().put(player.getUniqueId(), found);
            this.crimxLobby.getData().getPlayerMongoNetworkDocument().put(player.getUniqueId(), foundNetwork);
        }

        this.lobbyDocument = this.data.getPlayerMongoDocument().get(player.getUniqueId());
        this.networkDocument = this.data.getPlayerMongoNetworkDocument().get(player.getUniqueId());

        int coins = (int) this.getObjectFromMongoDocument("coins", MongoDBCollection.USERS);
        this.crimxLobby.getLobbyScoreboard().updateBoard(player, String.format("§8● §e%s Coins", coins), "coins", "§e");
        this.crimxLobby.getLobbyScoreboard().updateBoard(player, "§8● §6" + this.getOnlineTimeForScoreboard(), "playtime", "§6");
    }

    public ICloudPlayer getCloudPlayer() {
        return this.playerManager.getOnlinePlayer(player.getUniqueId());
    }

    public boolean isInBuild() {
        return data.getBuilders().contains(player.getUniqueId());
    }

    public void activateBuild() {

        data.getBuilders().add(player.getUniqueId());

        this.cleanUpPlayer();
        this.player.setGameMode(GameMode.CREATIVE);

        player.sendMessage(crimxLobby.getPrefix() + "§7Du bist nun im Baumodus");

    }

    public void removeFromBuild() {

        data.getBuilders().remove(player.getUniqueId());

        player.sendMessage(crimxLobby.getPrefix() + "§7Du bist nun nicht mehr im Baumodus");

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

        boolean hasPermission = this.player.hasPermission("crimxlobby.admin");

        this.player.getInventory().setItem(0, new ItemBuilder(Material.GREEN_RECORD).setName("§8● §aTeleporter").toItemStack());
        this.player.getInventory().setItem(1, new ItemBuilder(Material.INK_SACK, 1)
                .setColor(this.getPlayerHiderItemData())
                .setName(this.getPlayerHiderDisplayName()).toItemStack());
        this.player.getInventory().setItem(3, new ItemBuilder(Material.FISHING_ROD).setName("§8● §bEnterhaken").setUnbreakable().toItemStack());
        this.player.getInventory().setItem(5, new ItemBuilder(Material.STORAGE_MINECART).setName("§8● §eCosmetics").toItemStack());
        this.player.getInventory().setItem(7, new ItemBuilder(Material.DIODE).setName("§8● §cEinstellungen").toItemStack());
        this.player.getInventory().setItem(8, new ItemBuilder(Material.NETHER_STAR).addGlow().setName("§8● §6Lobby Minispiele").toItemStack());

        if (hasPermission) {
            this.player.getInventory().setItem(4, new ItemBuilder(Material.NAME_TAG).setName("§8● §5Nick").toItemStack());
        }

        this.player.setGameMode(GameMode.SURVIVAL);


    }

    public ICosmetic getSelectedCosmetic() {
        return this.data.getCosmetic().get(this.player.getUniqueId());
    }

    public void spawnPersonalNPC() {

        CrimxPlayer crimxPlayer = new CrimxPlayer(this.getCloudPlayer());

        String[] values = crimxPlayer.getPlayerSkin();

        NPC playerNPC = this.crimxLobby.getNpcLib().createNPC(Collections.singletonList("§a§lDein Profil"));
        playerNPC.setSkin(new Skin(values[0], values[1]));
        playerNPC.setLocation(new Location(Bukkit.getWorlds().get(0), -154, 64, 132));

        playerNPC.create();
        playerNPC.show(this.player);

        this.crimxLobby.getData().getPlayerNPCs().put(this.player.getUniqueId(), playerNPC);
    }

    public void updatePlayerHiderState() {

        // STATES: 0: all shown, 1: vip shown, 2: nobody shown
        int state = this.data.getPlayerHiderState().get(player.getUniqueId());

        state++;

        if (state == 1) {
            this.player.getInventory().getItem(1).setDurability((short) 5);
            Bukkit.getOnlinePlayers().forEach(loop -> {
                if(!loop.hasPermission("crimxlobby.vip")) {
                    this.player.hidePlayer(loop);
                }
            });
        } else if (state == 2) {
            this.player.getInventory().getItem(1).setDurability((short) 1);
            Bukkit.getOnlinePlayers().forEach(loop -> this.player.hidePlayer(loop));
        } else {
            state = 0;
            this.player.getInventory().getItem(1).setDurability((short) 10);
            Bukkit.getOnlinePlayers().forEach(loop -> this.player.showPlayer(loop));
        }

        this.data.getPlayerHiderState().put(player.getUniqueId(), state);
        this.setPlayerHiderDisplayName();

        Document toUpdate = new Document("playerhider-state", state);
        Bson updateOperation = new Document("$set", toUpdate);
        this.crimxLobby.getCrimxAPI().getMongoDB().getLobbyCollection().updateOne(this.lobbyDocument, updateOperation);

    }

    public String getOnlineTimeForScoreboard() {

        long onlineTimeInMillis = (long) this.getObjectFromMongoDocument("onlinetime", MongoDBCollection.USERS);

        if(onlineTimeInMillis < 1000 * 60) {
            return "Keine Daten";
        } else if(onlineTimeInMillis > 1000 * 60 && onlineTimeInMillis < 1000 * 60 * 60) {
            long minutes = TimeUtils.splitTime(onlineTimeInMillis)[2];
            return  minutes == 1 ? minutes + " Minute" : minutes + " Minuten";
        } else {
            long hours = TimeUtils.splitTime(onlineTimeInMillis)[1];
            return  hours == 1 ? hours + " Stunde" : hours + " Stunden";
        }
    }

    public int getPlayerHiderItemData() {

        int state = this.data.getPlayerHiderState().get(player.getUniqueId());

        if (state == 1) {
            return 5;
        } else if (state == 2) {
            return 1;
        } else {
            return 10;
        }

    }

    public void setPlayerHiderDisplayName() {

        int state = this.data.getPlayerHiderState().get(player.getUniqueId());

        ItemStack itemStack = this.player.getInventory().getItem(1);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (state == 1) {
            itemMeta.setDisplayName("§8● §7Du siehst §5nur VIPs");
        } else if (state == 2) {
            itemMeta.setDisplayName("§8● §7Du siehst §ckeinen Spieler");
        } else {
            itemMeta.setDisplayName("§8● §7Du siehst §aalle Spieler");
        }

        itemStack.setItemMeta(itemMeta);

    }

    public String getPlayerHiderDisplayName() {

        int state = this.data.getPlayerHiderState().get(player.getUniqueId());

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

        if(defaultSpawn) {
            String worldName = this.lobbyDocument.getString("last-location-world");
            double x = this.lobbyDocument.getDouble("last-location-x");
            double y = this.lobbyDocument.getDouble("last-location-y");
            double z = this.lobbyDocument.getDouble("last-location-z");
            double pitch = this.lobbyDocument.getDouble("last-location-pitch");
            double yaw = this.lobbyDocument.getDouble("last-location-yaw");

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
        this.lobbyDatabase.createPlayerData(player);
    }

    public void saveNewLocation() {

        Document toUpdate = new Document();
        toUpdate.append("last-location-world", this.player.getLocation().getWorld().getName())
                .append("last-location-x", this.player.getLocation().getX())
                .append("last-location-y", this.player.getLocation().getY())
                .append("last-location-z", this.player.getLocation().getZ())
                .append("last-location-pitch", this.player.getLocation().getPitch())
                .append("last-location-yaw", this.player.getLocation().getYaw());

        Bson updateOperation = new Document("$set", toUpdate);
        this.crimxLobby.getCrimxAPI().getMongoDB().getLobbyCollection().updateOne(this.lobbyDocument, updateOperation);

    }

    public void initPlayerHider() {

        int state = this.lobbyDocument.getInteger("playerhider-state");
        this.data.getPlayerHiderState().put(player.getUniqueId(), state);

    }

    public void loadMongoDocument() {
        Document found = this.crimxLobby.getCrimxAPI().getMongoDB().getLobbyCollection().
                find(new Document("_id", player.getUniqueId().toString())).first();

        Document foundNetwork = this.crimxLobby.getCrimxAPI().getMongoDB().getUserCollection().
                find(new Document("_id", player.getUniqueId().toString())).first();

        this.crimxLobby.getData().getPlayerMongoDocument().put(player.getUniqueId(), found);
        this.crimxLobby.getData().getPlayerMongoNetworkDocument().put(player.getUniqueId(), foundNetwork);

        this.lobbyDocument = found;
        this.networkDocument = foundNetwork;

        boolean hotbarSoundEnabled = this.lobbyDocument.getBoolean("hotbarSound");
        boolean spawnAtLastLocation = this.lobbyDocument.getBoolean("defaultSpawn");
        long lastReward = this.lobbyDocument.getLong("last-reward");

        this.data.getLobbyDatabasePlayer().put(this.player.getUniqueId(), new LobbyDatabasePlayer(hotbarSoundEnabled, spawnAtLastLocation, lastReward));

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
        int woolColor = random.nextInt(15);

        ArrayList<Location> possibleJumps = new ArrayList<>();

        for(int x = 2; x < 4; x++) {
            for(int z = 2; z < 4; z++) {

                x = random.nextBoolean() ? x : -1 * x;
                z = random.nextBoolean() ? z : -1 * z;

                possibleJumps.add(location.clone().add(x, height, z));

            }
        }

        Block startBlock = possibleJumps.get(random.nextInt(possibleJumps.size())).getBlock();

        int y = random.nextInt(1);
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

            startBlock.setType(Material.STAINED_CLAY);
            startBlock.setData((byte) woolColor);

        } else {

            player.sendMessage(this.crimxLobby.getPrefix() + "§cEs konnte kein passender Start gefunden werden! Bitte gehe zu einer offenen Stelle und versuche es erneut");
            return;

        }

        if (nextBlock.getType().equals(Material.AIR)) {

            nextBlock.setType(Material.WOOL);
            nextBlock.setData((byte) woolColor);

        } else {

            player.sendMessage(this.crimxLobby.getPrefix() + "§cEs konnte kein passender Sprung gefunden werden! Bitte gehe zu einer offenen Stelle und versuche es erneut");
            startBlock.setType(Material.AIR);
            return;

        }

        this.player.getInventory().clear();

        player.teleport(startBlock.getLocation().clone().add(0, 2, 0));
        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 2, 4);

        player.getInventory().setItem(4, new ItemBuilder(Material.INK_SACK, 1, (byte) 1).setName("§cAbbrechen").toItemStack());

        JumpAndRunPlayer jumpAndRunPlayer = new JumpAndRunPlayer(player, woolColor, null, startBlock, nextBlock, possibleJumps);

        this.data.getJumpAndRunPlayers().put(player.getUniqueId(), jumpAndRunPlayer);
        this.data.getJumpers().add(player.getUniqueId());

    }

    public void generateNextJump() {

        JumpAndRunPlayer jumpAndRunPlayer = this.data.getJumpAndRunPlayers().get(this.player.getUniqueId());

        Location location;
        Random random = new Random();

        jumpAndRunPlayer.setLastBlock(jumpAndRunPlayer.getCurrentBlock());
        jumpAndRunPlayer.getLastBlock().setType(Material.AIR);

        jumpAndRunPlayer.setCurrentBlock(jumpAndRunPlayer.getNextBlock());
        jumpAndRunPlayer.getCurrentBlock().setType(Material.STAINED_CLAY);
        jumpAndRunPlayer.getCurrentBlock().setData((byte) jumpAndRunPlayer.getWoolColor());

        location = jumpAndRunPlayer.getCurrentBlock().getLocation();

        ArrayList<Location> possibleJumps = new ArrayList<>();

        for(int x = 2; x < 4; x++) {
            for(int z = 2; z < 4; z++) {

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

            nextBlock.setType(Material.WOOL);
            nextBlock.setData((byte) jumpAndRunPlayer.getWoolColor());

            jumpAndRunPlayer.setNextBlock(nextBlock);

        } else {

            int c = 0;

            while (!nextBlock.getType().equals(Material.AIR) && c < 5) {

                nextBlock = jumpAndRunPlayer.getPossibleJumps().get(random.nextInt(jumpAndRunPlayer.getPossibleJumps().size())).getBlock();
                c++;

            }

            if (nextBlock.getType().equals(Material.AIR)) {

                nextBlock.setType(Material.WOOL);
                nextBlock.setData((byte) jumpAndRunPlayer.getWoolColor());

                jumpAndRunPlayer.setNextBlock(nextBlock);

            } else {

                player.sendMessage("§cEs konnte kein passender Sprung gefunden werden! Bitte gehe zu einer offenen Stelle und versuche es erneut");
                this.endJumpAndRun();

            }

        }

    }

    public boolean isInJumpAndRun() {
        return this.data.getJumpers().contains(this.player.getUniqueId());
    }

    public void endJumpAndRun() {

        this.player.getInventory().clear();

        this.data.getJumpers().remove(player.getUniqueId());
        this.setLobbyInventory();

        this.data.getJumpAndRunPlayers().get(player.getUniqueId()).getCurrentBlock().setType(Material.AIR);
        this.data.getJumpAndRunPlayers().get(player.getUniqueId()).getNextBlock().setType(Material.AIR);

        this.data.getJumpAndRunPlayers().remove(player.getUniqueId());

    }

    public boolean isInWaterMLG() {
        return this.waterMlgHandler.getWaterMlgPlayers().contains(player);
    }

    public void startWaterMLG() {

        int height = new Random().nextInt(15) + 5;

        player.closeInventory();

        player.setVelocity(player.getVelocity().setY(height));

        this.cleanUpPlayer();
        player.getInventory().setItem(3, new ItemBuilder(Material.WATER_BUCKET).setName("§9Wassereimer").toItemStack());
        player.getInventory().setItem(5, new ItemBuilder(Material.INK_SACK, 1, (byte) 1).setName("§cAbbrechen").toItemStack());

        this.waterMlgHandler.getWaterMlgPlayers().add(player);
        this.waterMlgHandler.getFailed().put(player, false);

    }

    public void endWaterMLG() {

        this.cleanUpPlayer();
        this.setLobbyInventory();
        this.waterMlgHandler.getWaterMlgPlayers().remove(player);

    }

    public boolean isMlgFailed() {
        return this.waterMlgHandler.getFailed().get(player);
    }

    public void unlockCosmetic(Cosmetic cosmetic) {

        try {
            ICosmetic iCosmetic = cosmetic.getCosmeticClass().newInstance();

            int coins = (int) this.getObjectFromMongoDocument("coins", MongoDBCollection.USERS);
            int required = cosmetic.getCosmeticClass().newInstance().getCosmeticCost();

            if(coins >= required) {
                coins -= required;
                player.sendMessage(this.crimxLobby.getPrefix() + "§7Du hast erfolgreich §a" + iCosmetic.getCosmeticName() + " §7freigeschalten");
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 2, 0.5f);
                CosmeticInventory.COSMETICS_INVENTORY.open(player);

                this.saveObjectInDocument("coins", coins, MongoDBCollection.USERS);
                this.saveObjectInDocument(cosmetic.name(), true, MongoDBCollection.LOBBY);

            } else {
                player.playSound(player.getLocation(), Sound.NOTE_BASS, 2, 0.5f);
                player.sendMessage(this.crimxLobby.getPrefix() + "§7Du hast nicht genügend Coins!");
                player.closeInventory();
            }

        } catch (InstantiationException | IllegalAccessException e) {
            player.closeInventory();
            player.playSound(player.getLocation(), Sound.NOTE_BASS, 2, 0.5f);
            player.sendMessage(this.crimxLobby.getPrefix() + "§cEin Fehler ist aufgetreten! Bitte versuche es später erneut.");
        }
    }

    public void setScoreboard() {
        this.crimxLobby.getLobbyScoreboard().setGameScoreboard(player);
    }
}
