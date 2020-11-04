package de.fel1x.capturetheflag.gameplayer;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.fel1x.capturetheflag.CaptureTheFlag;
import de.fel1x.capturetheflag.Data;
import de.fel1x.capturetheflag.filehandler.SpawnHandler;
import de.fel1x.capturetheflag.kit.IKit;
import de.fel1x.capturetheflag.kit.Kit;
import de.fel1x.capturetheflag.team.Teams;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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

    public void cleanupTeams() {
        Teams.RED.getTeamPlayers().remove(this.player);
        Teams.BLUE.getTeamPlayers().remove(this.player);
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

    public void addTeam(Teams teams) {
        if (teams.getTeamPlayers().size() <= 4) {
            if (!teams.getTeamPlayers().contains(this.player)) {
                teams.getTeamPlayers().add(this.player);
            }
            this.player.sendMessage(this.captureTheFlag.getPrefix() + "§7Du bist Team " + teams.getTeamName() + " §7beigetreten!");

            this.captureTheFlag.getScoreboardHandler().setGameScoreboard(player, teams);

        } else {
            this.player.sendMessage(this.captureTheFlag.getPrefix() + "Dieses Team ist voll!");
            this.player.closeInventory();
        }
    }

    public void removeTeam(Teams teams) {
        teams.getTeamPlayers().remove(this.player);
    }

    public Teams getTeam() {
        if (Teams.BLUE.getTeamPlayers().contains(this.player)) {
            return Teams.BLUE;
        } else if (Teams.RED.getTeamPlayers().contains(this.player)) {
            return Teams.RED;
        } else {
            return Teams.NONE;
        }
    }

    public boolean hasTeam() {
        return (Teams.BLUE.getTeamPlayers().contains(this.player) || Teams.RED.getTeamPlayers().contains(this.player));
    }

    public void teleportToTeamSpawn() {
        if (Teams.BLUE.getTeamPlayers().contains(player)) {
            Location blueSpawn = SpawnHandler.loadLocation("blueSpawn");
            this.player.teleport(blueSpawn);
        } else if (Teams.RED.getTeamPlayers().contains(player)) {
            Location redSpawn = SpawnHandler.loadLocation("redSpawn");
            this.player.teleport(redSpawn);
        } else {
            this.player.sendMessage("§cEs trat ein Fehler auf (NO_TEAM_SELECTED_TELEPORT_ERROR)");
        }
    }

    public Location getRespawnLocation() {
        if (Teams.BLUE.getTeamPlayers().contains(player)) {
            return SpawnHandler.loadLocation("blueSpawn");
        } else if (Teams.RED.getTeamPlayers().contains(player)) {
            return SpawnHandler.loadLocation("redSpawn");
        }
        return SpawnHandler.loadLocation("spectator");
    }

    public void checkForTeam() {
        if (this.hasTeam()) return;

        boolean random = ThreadLocalRandom.current().nextBoolean();

        if (random) {
            this.addTeam(Teams.RED);
        } else {
            this.addTeam(Teams.BLUE);
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
            IKit iKit = this.data.getSelectedKit().get(this.player).getClazz().newInstance();
            iKit.setKitInventory(player);
        } catch (InstantiationException | IllegalAccessException ignored) {
            player.sendMessage(this.captureTheFlag.getPrefix() + "§cEin Fehler ist aufgetreten! Du erhälst keine Kit-Items");
        }
    }
}
