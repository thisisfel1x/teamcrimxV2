package de.fel1x.teamcrimx.mlgwars.objects;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.IPermissionGroup;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.fel1x.teamcrimx.crimxapi.coins.CrimxCoins;
import de.fel1x.teamcrimx.crimxapi.cosmetic.BaseCosmetic;
import de.fel1x.teamcrimx.crimxapi.cosmetic.CosmeticCategory;
import de.fel1x.teamcrimx.crimxapi.cosmetic.WinAnimationCosmetic;
import de.fel1x.teamcrimx.crimxapi.cosmetic.database.ActiveCosmetics;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.crimxapi.support.CrimxSpigotAPI;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.Data;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.enums.Spawns;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.kit.Kit;
import de.fel1x.teamcrimx.mlgwars.kit.rework.InventoryKitManager;
import de.fel1x.teamcrimx.mlgwars.kit.rework.KitRegistry;
import de.fel1x.teamcrimx.mlgwars.maphandler.gametype.types.Tournament;
import de.fel1x.teamcrimx.mlgwars.scoreboard.ScoreboardHandler;
import de.fel1x.teamcrimx.mlgwars.utils.MlgActionbar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bson.Document;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class GamePlayer {

    private final MlgWars mlgWars;
    private final Data data;
    private final CrimxCoins crimxCoins;

    private final Player player;

    private boolean hasWrittenGG = false;
    private boolean isActionbarOverridden = false;
    private KitRegistry selectedKit;
    private final Map<KitRegistry, Boolean> boughtKits;
    private long gameStartTime;
    private Stats stats;

    private de.fel1x.teamcrimx.mlgwars.kit.rework.Kit activeKit;

    private final ScoreboardHandler scoreboardHandler;
    private int playerMlgWarsTeamId = -1;

    private String formattedChatName;

    private final IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);

    private final MlgActionbar mlgActionbar;

    public GamePlayer(MlgWars mlgWars, Player player) {
        this.mlgWars = mlgWars;
        this.data = this.mlgWars.getData();
        this.player = player;

        this.crimxCoins = new CrimxCoins(this.player.getUniqueId());
        this.scoreboardHandler = this.mlgWars.getScoreboardHandler();

        this.boughtKits = new HashMap<>();
        this.setFormattedChatName();

        this.data.getGamePlayers().put(this.player.getUniqueId(), this);

        this.mlgActionbar = new MlgActionbar(this.mlgWars, this.player.getUniqueId());
    }

    public void initDatabasePlayer() {
        // Update Kits
        Bukkit.getScheduler().runTaskAsynchronously(this.mlgWars, () -> {
            for (KitRegistry kit : KitRegistry.values()) {
                if(this.mlgWars.getCrimxAPI().getMongoDB().getObjectFromDocumentSync(this.player.getUniqueId(),
                        MongoDBCollection.MLGWARS,kit.name()) == null) {
                    this.mlgWars.getCrimxAPI().getMongoDB().insertObjectInDocument(this.player.getUniqueId(),
                            MongoDBCollection.MLGWARS, kit.name(), this.player.hasPermission("mlgwars.kits"));
                }
                boolean bought = (boolean) this.mlgWars.getCrimxAPI().getMongoDB().getObjectFromDocumentSync(this.player.getUniqueId(),
                        MongoDBCollection.MLGWARS, kit.name());
                this.boughtKits.put(kit, bought);
            }

            int kills = (int) this.mlgWars.getCrimxAPI().getMongoDB().getObjectFromDocumentSync(this.player.getUniqueId(),
                    MongoDBCollection.MLGWARS, "kills");
            int deaths = (int) this.mlgWars.getCrimxAPI().getMongoDB().getObjectFromDocumentSync(this.player.getUniqueId(),
                    MongoDBCollection.MLGWARS, "deaths");
            int gamesPlayed = (int) this.mlgWars.getCrimxAPI().getMongoDB().getObjectFromDocumentSync(this.player.getUniqueId(),
                    MongoDBCollection.MLGWARS, "gamesPlayed");
            int gamesWon = (int) this.mlgWars.getCrimxAPI().getMongoDB().getObjectFromDocumentSync(this.player.getUniqueId(),
                    MongoDBCollection.MLGWARS, "gamesWon");

            int placement = -1;
            try {
                placement = this.mlgWars.getRankingPosition(this.player.getUniqueId()).get();
            } catch (InterruptedException | ExecutionException ignored) {}

            this.stats = new Stats(kills, deaths, gamesPlayed, gamesWon, placement);

        });

        // Update name if needed
        this.mlgWars.getCrimxAPI().getMongoDB().getObjectFromDocumentAsync(this.player.getUniqueId(),
                MongoDBCollection.MLGWARS, "name").thenAccept(name -> {
            if(!String.valueOf(name).equalsIgnoreCase(this.player.getName())) {
                this.mlgWars.getCrimxAPI().getMongoDB().insertObjectInDocument(this.player.getUniqueId(),
                        MongoDBCollection.MLGWARS, "name", this.player.getName());
            }
        });

        // Get selected Kit
        this.mlgWars.getCrimxAPI().getMongoDB().getObjectFromDocumentAsync(this.player.getUniqueId(),
                MongoDBCollection.MLGWARS, "selectedKit").thenAccept(kit -> {
            try {
                this.selectedKit = KitRegistry.valueOf(String.valueOf(kit));
            } catch (Exception ignored) {
                this.selectedKit = KitRegistry.STARTER;
            }
            if (this.getCurrentGamestate() == Gamestate.IDLE
                    || this.getCurrentGamestate() == Gamestate.LOBBY) {
                this.setSelectedKit(this.selectedKit);
            }
        });

        // Get total played games
        this.mlgWars.getCrimxAPI().getMongoDB().getObjectFromDocumentAsync(this.player.getUniqueId(),
                MongoDBCollection.MLGWARS, "gamesPlayed").thenAccept(gamesPlayed -> {
            this.player.setMetadata("games", new FixedMetadataValue(this.mlgWars, gamesPlayed));
        });

    }

    private void setFormattedChatName() {
        IPermissionUser iPermissionUser = CloudNetDriver.getInstance().getPermissionManagement().getUser(this.player.getUniqueId());
        if (iPermissionUser == null) return;
        IPermissionGroup permissionGroup = CloudNetDriver.getInstance().getPermissionManagement().getHighestPermissionGroup(iPermissionUser);
        this.formattedChatName = ChatColor.translateAlternateColorCodes('&', permissionGroup.getDisplay())
                + this.player.getName() + " §8» §f";
    }

    public String getFormattedChatName() {
        return this.formattedChatName;
    }

    public void connectToService(String serviceName) {
        ICloudPlayer cloudPlayer = this.playerManager.getOnlinePlayer(this.player.getUniqueId());
        if(cloudPlayer == null) {
            this.player.kick(Component.empty());
            return;
        }
        cloudPlayer.getPlayerExecutor().connect(serviceName);
    }

    public Gamestate getCurrentGamestate() {
        return this.mlgWars.getGamestateHandler().getGamestate();
    }

    public ScoreboardHandler getScoreboardHandler() {
        return this.scoreboardHandler;
    }

    public Map<KitRegistry, Boolean> getBoughtKits() {
        return this.boughtKits;
    }

    public de.fel1x.teamcrimx.mlgwars.kit.rework.Kit getActiveKit() {
        return this.activeKit;
    }

    public void setActiveKit(de.fel1x.teamcrimx.mlgwars.kit.rework.Kit activeKit) {
        this.activeKit = activeKit;
    }

    public boolean isPlayer() {
        return this.data.getPlayers().contains(this.player);
    }

    public MlgActionbar getMlgActionbar() {
        return mlgActionbar;
    }

    public void updateOnlineTime() {
        Bukkit.getScheduler().runTaskAsynchronously(this.mlgWars, () -> {
           Object onlineTimeInMillis = this.mlgWars.getCrimxAPI().getMongoDB().getObjectFromDocumentSync(this.player.getUniqueId(),
                   MongoDBCollection.USERS, "onlinetime");
            if(onlineTimeInMillis == null) {
                return;
            }
            long onlineTime;
            try {
                onlineTime = (long) onlineTimeInMillis;
            } catch (Exception ignored) {
                onlineTime = Integer.toUnsignedLong((Integer) onlineTimeInMillis);
            }

            long timePlayed = System.currentTimeMillis() - this.gameStartTime;
            long added = timePlayed + onlineTime;

            this.mlgWars.getCrimxAPI().getMongoDB().insertObjectInDocument(this.player.getUniqueId(),
                    MongoDBCollection.USERS, "onlinetime", added);
        });
    }

    public void saveStats() {
        Document toUpdate = new Document();
        toUpdate.append("kills", this.stats.getKills())
                .append("deaths", this.stats.getDeaths())
                .append("gamesPlayed", this.stats.getGamesPlayed())
                .append("gamesWon", this.stats.getGamesWon());

        this.mlgWars.getCrimxAPI().getMongoDB().updateDocumentInCollectionSync(this.player.getUniqueId(),
                MongoDBCollection.MLGWARS, toUpdate);

        if(this.mlgWars.getGameType() instanceof Tournament) {
            int currentPoints = (int) this.mlgWars.getCrimxAPI().getMongoDB().getObjectFromDocumentSync(this.player.getUniqueId(),
                    MongoDBCollection.MLGWARS_TOURNAMENT, "points");
            currentPoints = currentPoints + this.stats.getGamePoints() + (this.stats.isWin() ? 1 : 0);
            toUpdate.append("points", currentPoints);

            this.mlgWars.getCrimxAPI().getMongoDB().updateDocumentInCollectionSync(this.player.getUniqueId(),
                    MongoDBCollection.MLGWARS_TOURNAMENT, toUpdate);
        }
    }

    public void setGameStartTime(long gameStartTime) {
        this.gameStartTime = gameStartTime;
    }

    public void addToPlayers() {
        this.data.getPlayers().add(this.player);
    }

    public void removeFromPlayers() {
        this.data.getPlayers().remove(this.player);
    }

    public boolean isSpectator() {
        return this.data.getSpectators().contains(this.player);
    }

    public void addToSpectators() {
        this.data.getSpectators().add(this.player);
    }

    public void removeFromSpectators() {
        this.data.getSpectators().remove(this.player);
    }

    public CrimxCoins getCrimxCoins() {
        return this.crimxCoins;
    }

    public Stats getStats() {
        return this.stats;
    }

    public boolean isActionbarOverridden() {
        return this.isActionbarOverridden;
    }

    public void setActionbarOverridden(boolean actionbarOverridden) {
        this.isActionbarOverridden = actionbarOverridden;
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


        this.player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(24);

        this.player.getActivePotionEffects().forEach(potionEffect -> this.player.removePotionEffect(potionEffect.getType()));

        Bukkit.getOnlinePlayers().forEach(onlinePlayers -> {
            onlinePlayers.showPlayer(this.mlgWars, this.player);
            this.player.showPlayer(this.mlgWars, onlinePlayers);
        });

    }

    public void cleanUpOnQuit() {
        this.removeFromPlayers();
        this.activeKit.disableKit();

        Location lastLocation = this.player.getLocation();

        for (ItemStack content : this.getPlayer().getInventory().getContents()) {
            if(content == null) {
                continue;
            }
            lastLocation.getWorld().dropItemNaturally(lastLocation, content);
        }
        lastLocation.getWorld().spawn(lastLocation,
                ExperienceOrb.class, experienceOrb -> experienceOrb.setExperience(this.player.getTotalExperience()));

        int playersLeft = this.mlgWars.getData().getPlayers().size();

        Gamestate gamestate = this.mlgWars.getGamestateHandler().getGamestate();

        if (gamestate == Gamestate.DELAY || gamestate == Gamestate.PREGAME || gamestate == Gamestate.INGAME) {
            String playersLeftMessage = null;

            this.mlgWars.getData().getGameTeams().get(this.playerMlgWarsTeamId)
                    .getAlivePlayers().remove(this.player);
            this.mlgWars.getData().getGameTeams().get(this.playerMlgWarsTeamId)
                    .getTeamPlayers().remove(this.player);

            if (this.mlgWars.getData().getGameTeams().get(this.playerMlgWarsTeamId)
                    .getAlivePlayers().isEmpty()) {
                this.mlgWars.getData().getGameTeams().remove(this.playerMlgWarsTeamId);
            }

            if (this.mlgWars.getTeamSize() > 1) {
                if (this.playerMlgWarsTeamId != - 1) {
                    playersLeftMessage = "§a" + playersLeft + " Spieler verbleiben §8(§a"
                            + this.mlgWars.getData().getGameTeams().size() + " Teams§8)";
                }
            } else if (this.mlgWars.getTeamSize() == 1) {
                playersLeftMessage = "§a" + playersLeft + " Spieler verbleiben";
            }

            if (playersLeftMessage != null && playersLeft > 1) {
                Bukkit.broadcastMessage(this.mlgWars.getPrefix() + playersLeftMessage);
            }
        } else if (gamestate == Gamestate.IDLE || gamestate == Gamestate.LOBBY) {
                if (this.playerMlgWarsTeamId != -1) {
                    this.mlgWars.getData().getGameTeams().get(this.playerMlgWarsTeamId)
                            .getTeamPlayers().remove(this.player);

                    this.mlgWars.getTeamReworkInventory().getGui().updateItem(this.playerMlgWarsTeamId, // Update inventory item with
                            this.mlgWars.getData().getGameTeams().get(this.playerMlgWarsTeamId).getGUIItemStack());  // player team list

                    this.playerMlgWarsTeamId = -1;
                }
        }
    }

    public void setJoinItems() {

        this.player.getInventory().setItem(0, new ItemBuilder(Material.CHEST_MINECART).setName("§8● §aKitauswahl").toItemStack());

        if (this.player.hasPermission("mlgwars.forcemap") || this.player.isOp()) {
            int slot = 2; //this.mlgWars.getTeamSize() > 1 ? 2 : 1;
            this.player.getInventory().setItem(slot, new ItemBuilder(Material.REDSTONE_TORCH).setName("§8● §cForcemap").toItemStack());
        }

        if(this.mlgWars.getTeamSize() == 1) {
            this.player.getInventory().setItem(7, new ItemBuilder(Material.GLOW_BERRIES)
                    .setName("§8● §fFür Spielmodus abstimmen").toItemStack());
        }

        //if (this.mlgWars.getTeamSize() > 1) {
            this.player.getInventory().setItem(1, new ItemBuilder(Material.AMETHYST_SHARD).setName("§8● §eTeamauswahl").toItemStack());
        //}

    }

    public void teleport(Spawns spawns) {
        try {
            Bukkit.getScheduler().runTaskLater(this.mlgWars, () -> this.player.teleport(spawns.getLocation()), 1L);
        } catch (NullPointerException exception) {
            this.player.sendMessage(this.mlgWars.getPrefix() + "§cEin Fehler ist aufgetreten!");
            exception.printStackTrace();
        }
    }


    public void activateSpectatorMode() {

        this.player.setGameMode(GameMode.ADVENTURE);

        this.player.getInventory().clear();
        this.player.getInventory().setArmorContents(null);

        this.player.setHealth(20);
        this.player.setFoodLevel(25);

        this.player.setLevel(0);
        this.player.setExp(0);

        this.player.getActivePotionEffects().forEach(potionEffect -> this.player.removePotionEffect(potionEffect.getType()));
        this.player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, true, false));

        this.player.setAllowFlight(true);
        this.player.setFlying(true);

        this.player.setPlayerListName("§o§8[§4✖§8] §7" + this.player.getName());

        this.player.getInventory().setItem(0, new ItemBuilder(Material.COMPASS)
                .setName("§8● §aSpieler beobachten")
                .toItemStack());

        this.player.setFlying(true);

    }

    public void activateSpectatorModeOnJoin() {

        this.player.setGameMode(GameMode.ADVENTURE);

        this.player.getInventory().clear();
        this.player.getInventory().setArmorContents(null);

        this.player.setHealth(20);
        this.player.setFoodLevel(25);

        this.player.setLevel(0);
        this.player.setExp(0);

        this.player.setFireTicks(0);

        this.player.getActivePotionEffects().forEach(potionEffect -> this.player.removePotionEffect(potionEffect.getType()));
        this.player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, true, false));

        this.player.setAllowFlight(true);
        this.player.setFlying(true);

        this.player.setPlayerListName("§o§8[§4✖§8] §7" + this.player.getName());

        this.player.getInventory().setItem(0, new ItemBuilder(Material.COMPASS)
                .setName("§8● §aSpieler beobachten")
                .toItemStack());

        this.player.setAllowFlight(true);
        this.player.setFlying(true);

        this.removeFromPlayers();
        this.addToSpectators();

        this.data.getPlayers().forEach(ingamePlayer -> ingamePlayer.hidePlayer(this.player));
        this.data.getSpectators().forEach(this.player::showPlayer);

    }

    public void onDeath() {
        // Disable kit
        try {
            this.activeKit.disableKit();
        } catch (Exception exception) {
            this.player.sendMessage(this.mlgWars.getPrefix() + "§cEin Fehler beim deaktivieren deines Kits ist aufgetreten:");
            this.player.sendMessage("§c" + exception.getMessage());
        }

        this.player.setAllowFlight(true);
        this.player.setFlying(true);

        this.removeFromPlayers();
        this.addToSpectators();

        this.data.getPlayers().forEach(ingamePlayer -> ingamePlayer.hidePlayer(this.mlgWars, this.player));
        this.data.getSpectators().forEach(spectators -> spectators.showPlayer(this.mlgWars, this.player));

        this.stats.increaseDeathsByOne();
        this.saveStats();
    }

    public void createPlayerData() {
        this.player.sendMessage(this.mlgWars.getPrefix() + "§7Nutzerdaten werden angelegt...");

        Document basicDBObject = new Document("_id", this.player.getUniqueId().toString())
                .append("name", this.player.getName())
                .append("kills", 0)
                .append("deaths", 0)
                .append("gamesPlayed", 0)
                .append("gamesWon", 0)
                .append("selectedKit", Kit.STARTER.name());

        for (KitRegistry kit : KitRegistry.values()) {
            basicDBObject.append(kit.name(), this.player.hasPermission("mlgwars.kits")
                    || (kit == KitRegistry.STARTER));
        }

        this.mlgWars.getCrimxAPI().getMongoDB().insertDocumentInCollectionSync(basicDBObject, MongoDBCollection.MLGWARS);
        this.player.sendMessage(this.mlgWars.getPrefix() + "§7Nutzerdaten wurden erfolgreich angelegt!");
    }

    public KitRegistry getSelectedKit() {
        return this.selectedKit;
    }

    public InventoryKitManager.InventoryKit getSelectedInventoryKit() {
        return this.mlgWars.getInventoryKitManager().getAvailableKits().get(this.selectedKit);
    }

    public void setSelectedKit(KitRegistry kitRegistry) {
        this.selectedKit = kitRegistry;

        InventoryKitManager.InventoryKit selectedKit = this.mlgWars.getInventoryKitManager().getAvailableKits().get(kitRegistry);

        this.player.getInventory().setItem(8, selectedKit.toItemStack());
        this.scoreboardHandler.updateBoard(this.player, selectedKit.getScoreboardName(), "kit");

        this.mlgWars.getCrimxAPI().getMongoDB().insertObjectInDocumentAsync(this.player.getUniqueId(),
                MongoDBCollection.MLGWARS, "selectedKit", kitRegistry.name());
    }

    public void setLobbyScoreboard() {
        this.scoreboardHandler.setLobbyScoreboard(this.player);
    }

    public void setInGameScoreboard() {
        this.scoreboardHandler.setIngameScoreboard(this.player);
    }

    public boolean hasWrittenGG() {
        return this.hasWrittenGG;
    }

    public void setHasWrittenGG(boolean hasWrittenGG) {
        this.hasWrittenGG = hasWrittenGG;
    }

    public void unlockKit(KitRegistry kitRegistry) {
        this.player.closeInventory();

        InventoryKitManager.InventoryKit inventoryKit = this.mlgWars.getInventoryKitManager().getAvailableKits().get(kitRegistry);
        CrimxCoins crimxCoins = new CrimxCoins(this.player.getUniqueId());

        int currentCoins = crimxCoins.getCoinsSync();
        int requiredCoins = inventoryKit.getKitCost();

        if (currentCoins >= requiredCoins) {
            this.player.sendMessage(this.mlgWars.prefix()
                    .append(Component.text("Du hast erfolgreich [", NamedTextColor.GRAY)
                    .append(inventoryKit.getKitName().color(NamedTextColor.YELLOW))
                    .append(Component.text("] freigeschalten", NamedTextColor.GRAY))));

            this.player.playSound(this.player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2, 0.5f);

            crimxCoins.removeCoinsAsync(requiredCoins);
            this.mlgWars.getCrimxAPI().getMongoDB().insertObjectInDocument(this.player.getUniqueId(),
                    MongoDBCollection.MLGWARS, kitRegistry.name(), true);

            this.setSelectedKit(kitRegistry);
            this.boughtKits.put(kitRegistry, true);

        } else {
            this.player.playSound(this.player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 2, 0.5f);
            this.player.sendMessage(this.mlgWars.getPrefix() + "§7Du hast nicht genügend Coins!");
        }
    }

    public int setTeam(MlgWarsTeam mlgWarsTeam) {
        this.player.closeInventory();

        if(mlgWarsTeam.getId() == this.playerMlgWarsTeamId) {
            return -1;
        }

        if(mlgWarsTeam.getTeamPlayers().size() == mlgWarsTeam.getMaxPlayers()) {
            this.player.sendMessage(this.mlgWars.prefix().append(this.mlgWars.miniMessage()
                    .parse("<#" + mlgWarsTeam.getColor().getRGB() + ">Team " + mlgWarsTeam.getTeamId()
                            + " <gray>ist bereits voll")));
            return -1;
        }

        if(this.playerMlgWarsTeamId != -1) {
            this.data.getGameTeams().get(this.playerMlgWarsTeamId).getTeamPlayers().remove(this.player);
        }

        int oldSlot = this.playerMlgWarsTeamId;

        this.playerMlgWarsTeamId = mlgWarsTeam.getId();
        this.data.getGameTeams().get(this.playerMlgWarsTeamId).getTeamPlayers().add(this.player);

        this.player.setMetadata("team", new FixedMetadataValue(this.mlgWars, this.playerMlgWarsTeamId));

        this.player.sendMessage(this.mlgWars.getPrefix() + "§7Du bist nun in §aTeam #" + mlgWarsTeam.getTeamId());

        return oldSlot;
    }

    public void checkForTeam() {
        if (this.playerMlgWarsTeamId == -1) {
            for (MlgWarsTeam value : this.data.getGameTeams().values()) {
                if (!value.getTeamPlayers().contains(this.player)
                        && value.getTeamPlayers().size() != value.getMaxPlayers()) {
                    this.setTeam(value);
                    this.player.playerListName(this.player.name().color(NamedTextColor.WHITE));
                    break;
                }
            }
        }
    }

    public int getPlayerMlgWarsTeamId() {
        return this.playerMlgWarsTeamId;
    }

    public Player getPlayer() {
        return this.player;
    }

    // TEMPORARY
    public void stopCosmetics() {
        ActiveCosmetics activeCosmetics = CrimxSpigotAPI.getInstance().getActiveCosmeticsHashMap().get(this.player.getUniqueId());
        for (CosmeticCategory value : CosmeticCategory.values()) {
            BaseCosmetic baseCosmetic = activeCosmetics.getSelectedCosmetic().get(value);
            if(baseCosmetic == null || baseCosmetic.getCosmeticCategory() == CosmeticCategory.WIN_ANIMATION) {
                continue;
            }
            baseCosmetic.stopCosmetic(this.player);
        }
    }

    @Deprecated
    // fix cosmetic system lol its not pausing just stopping
    public void startCosmetics() {
        /*ActiveCosmetics activeCosmetics = CrimxSpigotAPI.getInstance().getActiveCosmeticsHashMap().get(this.player.getUniqueId());
        for (CosmeticCategory value : CosmeticCategory.values()) {
            BaseCosmetic baseCosmetic = activeCosmetics.getSelectedCosmetic().get(value);
            if(baseCosmetic == null) {
                continue;
            }
            baseCosmetic.startCosmetic(this.player);
        } */
    }

    public void createTournamentData() {
        Document basicDBObject = new Document("_id", this.player.getUniqueId().toString())
                .append("name", this.player.getName())
                .append("kills", 0)
                .append("deaths", 0)
                .append("gamesPlayed", 0)
                .append("gamesWon", 0)
                .append("points", 0);

        this.mlgWars.getCrimxAPI().getMongoDB().insertDocumentInCollectionSync(basicDBObject, MongoDBCollection.MLGWARS_TOURNAMENT);
    }

    public void winAnimation() {
        ActiveCosmetics activeCosmetics = CrimxSpigotAPI.getInstance().getActiveCosmeticsHashMap().get(this.player.getUniqueId());
        BaseCosmetic baseCosmetic = activeCosmetics.getSelectedCosmetic().get(CosmeticCategory.WIN_ANIMATION);
        if(baseCosmetic != null) {
            if(baseCosmetic instanceof WinAnimationCosmetic winAnimationCosmetic) {
                winAnimationCosmetic.win();
            }
        }
    }
}
