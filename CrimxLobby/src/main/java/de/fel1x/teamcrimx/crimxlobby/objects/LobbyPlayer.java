package de.fel1x.teamcrimx.crimxlobby.objects;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.IPermissionGroup;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.Data;
import de.fel1x.teamcrimx.crimxlobby.database.LobbyDatabase;
import de.fel1x.teamcrimx.crimxlobby.database.LobbyDatabasePlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bson.Document;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LobbyPlayer {

    private final CrimxLobby crimxLobby = CrimxLobby.getInstance();
    private final Data data = this.crimxLobby.getData();
    private final LobbyDatabase lobbyDatabase = new LobbyDatabase();

    private final Player player;

    private final IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);

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

        this.player.getInventory().setItem(0, new ItemBuilder(Material.MUSIC_DISC_CAT)
                .setName(Component.text("● ", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false)
                        .append(Component.text("Teleporter", NamedTextColor.GREEN))).toItemStack());
        this.player.getInventory().setItem(1, new ItemBuilder(this.getPlayerHiderItemData(), 1)
                .setName(this.getPlayerHiderDisplayName()).toItemStack());
        this.player.getInventory().setItem(3, new ItemBuilder(Material.SHEARS)
                        .setName(Component.text("● ", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false)
                                .append(Component.text("Duell", TextColor.fromHexString("#ebcc34")))).toItemStack());
        this.player.getInventory().setItem(5, new ItemBuilder(Material.CHEST_MINECART)
                .setName(Component.text("● ", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false)
                        .append(Component.text("Dein Inventar", NamedTextColor.YELLOW))).toItemStack());
        this.player.getInventory().setItem(7, new ItemBuilder(Material.MOJANG_BANNER_PATTERN)
                .setName(Component.text("● ", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false)
                        .append(Component.text("Lobby wechseln", NamedTextColor.YELLOW))).toItemStack());
        this.player.getInventory().setItem(8, new ItemBuilder(Material.PLAYER_HEAD)
                .setName(Component.text("● ", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false)
                        .append(Component.text("Profil", NamedTextColor.GOLD)))
                .setSkullOwner(this.player.getName()).toItemStack());

        if (hasPermission) {
            boolean nickActivated = (boolean) this.crimxLobby.getCrimxAPI().getMongoDB()
                    .getObjectFromDocumentSync(this.player.getUniqueId(), MongoDBCollection.USERS, "nick");
            this.player.getInventory().setItem(4, new ItemBuilder(Material.NAME_TAG)
                    .setName(Component.text("● ", NamedTextColor.DARK_GRAY)
                            .append(Component.text("Nick ", NamedTextColor.DARK_PURPLE))
                            .append(Component.text("» ", NamedTextColor.GRAY))
                            .append(Component.text((nickActivated ? "aktiviert" : "deaktiviert"),
                                    (nickActivated ? NamedTextColor.GREEN : NamedTextColor.RED))).asComponent().decoration(TextDecoration.ITALIC, false))
                    .toItemStack());
        }
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
        if (itemStack == null) {
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

            if (serializedLocation != null &&
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
            this.player.teleport(spawn.getSpawn());
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
                + ":" + spawnLocation.getPitch() + ":" + spawnLocation.getYaw();

        Document toUpdate = new Document();
        toUpdate.append("lastLocation", locationSerialized)
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

        if (lobbyDocument == null) {
            return;
        }

        boolean spawnAtLastLocation = lobbyDocument.getBoolean("defaultSpawn");
        long lastReward = lobbyDocument.getLong("lastReward");

        this.data.getLobbyDatabasePlayer().put(this.player.getUniqueId(), new LobbyDatabasePlayer(spawnAtLastLocation, lastReward));
    }

    public void setScoreboard() {
        this.crimxLobby.getLobbyScoreboard().setDefaultLobbyScoreboard(this.player);
    }
}
