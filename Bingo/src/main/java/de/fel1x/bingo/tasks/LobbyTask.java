package de.fel1x.bingo.tasks;

import com.destroystokyo.paper.Title;
import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.gamehandler.Gamestate;
import de.fel1x.bingo.objects.BingoDifficulty;
import de.fel1x.bingo.objects.BingoPlayer;
import de.fel1x.bingo.objects.BingoTeam;
import de.fel1x.bingo.settings.Settings;
import de.fel1x.bingo.utils.Utils;
import de.fel1x.bingo.utils.world.ArmorstandStatsLoader;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class LobbyTask implements IBingoTask {

    private final Bingo bingo = Bingo.getInstance();
    private int taskId = 0;
    private int countdown = 60;

    //private final CompletableFuture<Boolean> playerTeleport = CompletableFuture.supplyAsync(this::teleportPlayers);

    boolean isRunning = false;

    @Override
    public void start() {

        if (!this.isRunning) {

            this.isRunning = true;
            this.bingo.getGamestateHandler().setGamestate(Gamestate.LOBBY);

            this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.bingo, () -> {

                switch (this.countdown) {

                    case 60:
                    case 50:
                    case 40:
                    case 30:
                    case 20:
                    case 10:
                    case 5:
                    case 4:
                    case 3:
                    case 2:
                    case 1:
                        if (this.countdown == 10) {
                            BingoDifficulty bingoDifficulty = this.bingo.getVotingManager()
                                    .getForcedBingoDifficulty() == BingoDifficulty.NOT_FORCED
                                    ? this.bingo.getVotingManager().getFinalDifficulty()
                                    : this.bingo.getVotingManager().getForcedBingoDifficulty();

                            Bukkit.getScheduler().runTaskAsynchronously(this.bingo, () -> this.bingo.generateItems(bingoDifficulty));

                            Title title = Title.builder()
                                    .title(bingoDifficulty.getDisplayName())
                                    .subtitle("§7Ausgewählte Schwierigkeit")
                                    .fadeIn(10)
                                    .stay(60)
                                    .fadeIn(10)
                                    .build();
                            this.bingo.getData().getPlayers().forEach(player -> player.sendTitle(title));
                        }

                        Bukkit.broadcastMessage(this.bingo.getPrefix() + "§7Die Runde startet in §e"
                                + ((this.countdown == 1) ? "einer Sekunde" : this.countdown + " Sekunden"));

                        this.bingo.getData().getPlayers().forEach(player ->
                                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 0.75f));

                        break;

                    case 0:
                        World world = Bukkit.getWorlds().get(0);
                        world.getEntities().forEach(Entity::remove);
                        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, Settings.DAYLIGHT_CYCLE.isEnabled());
                        world.setGameRule(GameRule.DO_MOB_SPAWNING, Settings.DO_MOB_SPAWN.isEnabled());

                        Bukkit.broadcastMessage(this.bingo.getPrefix() + "§cDie Spieler werden teleportiert...");
                        this.teleportPlayers();
                        break;
                }

                if (this.countdown >= 1) {
                    Bukkit.getOnlinePlayers().forEach(current -> {
                        current.setLevel(this.countdown);
                        current.setExp((float) this.countdown / (float) 60);
                    });
                }

                this.countdown--;

            }, 0L, 20L);
        }

    }

    @Override
    public void stop() {

        if (this.isRunning) {

            this.isRunning = false;
            Bukkit.getScheduler().cancelTask(this.taskId);

            this.countdown = 60;

        }

    }

    int counter;

    private void teleportPlayers() {
        Location worldSpawnLocation = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
        int y = worldSpawnLocation.getWorld().getHighestBlockYAt(0, 0);
        worldSpawnLocation.setY(y);

        ArrayList<Location> spawns = ArmorstandStatsLoader
                .getCirclePoints(worldSpawnLocation, 20, 6 * this.bingo.getTeamSize());

        Collections.shuffle(spawns);

        this.counter = 0;
        int playerSize = this.bingo.getData().getPlayers().size();

        new BukkitRunnable() {
            @Override
            public void run() {
                Player player = LobbyTask.this.bingo.getData().getPlayers().get(LobbyTask.this.counter);
                // TELEPORT
                player.sendActionBar("§aDu wirst teleportiert...");

                Location spawnLocation = spawns.get(LobbyTask.this.counter).getWorld()
                        .getHighestBlockAt(spawns.get(LobbyTask.this.counter)).getLocation();
                spawnLocation.getBlock().setType(player.hasMetadata("block") ?
                        (Material) Objects.requireNonNull(player.getMetadata("block").get(0).value())
                        : Material.GLASS);
                spawnLocation.clone().add(0, 1, 0);

                player.teleport(spawnLocation.toCenterLocation().clone().subtract(0, 0.5, 0));
                Title information = Title.builder().title("§cTeleport " + (LobbyTask.this.counter + 1) + "/" + playerSize)
                        .subtitle("§7Spieler werden teleportiert...")
                        .fadeIn(0).stay(20).fadeOut(10).build();
                Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.sendTitle(information));

                player.getInventory().clear();
                player.setExp(0);
                player.setLevel(0);
                player.getInventory().setItem(8, LobbyTask.this.bingo.getBingoItemsQuickAccess());

                BingoPlayer bingoPlayer = new BingoPlayer(player);
                if (bingoPlayer.getTeam() == null) {
                    for (BingoTeam bingoTeam : BingoTeam.values()) {
                        if (bingoTeam.getTeamPlayers().size() < bingoTeam.getTeamSize()) {
                            bingoPlayer.setTeam(bingoTeam);
                            player.sendMessage(LobbyTask.this.bingo.getPrefix() + "§7Du wurdest zu Team "
                                    + Utils.getChatColor(bingoTeam.getColor()) + bingoTeam.getName() + " zugewiesen");
                            break;
                        }
                    }
                }

                LobbyTask.this.counter++;
                if (LobbyTask.this.counter == playerSize) {
                    Title success = Title.builder().title("§aFertig").subtitle("§7Alle Spieler wurden teleportiert")
                            .fadeIn(0).stay(20).fadeOut(10).build();
                    Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.sendTitle(success));

                    Bukkit.broadcastMessage(LobbyTask.this.bingo.getPrefix() + "§e§lDie Runde beginnt!");
                    LobbyTask.this.bingo.startTimerByClass(PreGameTask.class);
                    this.cancel();
                }
            }
        }.runTaskTimer(this.bingo, 0L, 10L);
    }

    public int getCountdown() {
        return this.countdown;
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }
}
