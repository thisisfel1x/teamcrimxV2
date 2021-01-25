package de.fel1x.teamcrimx.floorislava.tasks;

import com.destroystokyo.paper.Title;
import de.fel1x.teamcrimx.floorislava.FloorIsLava;
import de.fel1x.teamcrimx.floorislava.gamehandler.Gamestate;
import de.fel1x.teamcrimx.floorislava.utils.scoreboard.GameScoreboard;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class RisingTask implements IFloorIsLavaTask {
    private final FloorIsLava floorIsLava = FloorIsLava.getInstance();
    private final GameScoreboard scoreboard = new GameScoreboard();
    private final int increasePerRise = 1;
    private final String save = String.format("§a%s §8%s §7Du bist §asicher!", FloorIsLava.SAVE, FloorIsLava.DOT);
    private final String notSave = String.format("§c%s §8%s §7Du bist §cnicht sicher!", FloorIsLava.ATTENTION, FloorIsLava.DOT);
    private final Title notSaveTitle = new Title.Builder().title(" ").subtitle("§cDu bist nicht sicher").fadeIn(0).stay(30).fadeOut(10).build();
    private final Location spawnLocation = this.floorIsLava.getWorldSpawnLocation();
    private final Title pvpTitle = Title.builder().title("§7『§aPVP§7§7』").subtitle("§aaktiviert").fadeIn(10).stay(60).fadeOut(10).build();
    private final Title infoTitle = Title.builder().title("§4Achtung").subtitle("§cDie Lava steigt!").fadeIn(10).stay(60).fadeOut(10).build();
    private int delay = 5;
    private int height = 0;
    private Location bottomRight;
    private Location topLeft;
    private boolean isRunning = false;

    private int taskId;
    private int heightToGo;

    public void start() {
        if (!this.isRunning) {
            this.isRunning = true;
            this.heightToGo = Bukkit.getWorlds().get(0).getHighestBlockYAt(this.floorIsLava.getWorldSpawnLocation());
            this.floorIsLava.getGamestateHandler().setGamestate(Gamestate.RISING);
            int size = 50;
            this.bottomRight = this.spawnLocation.clone().subtract(size / 2.0D, this.height, size / 2.0D);
            this.topLeft = this.spawnLocation.clone().add(size / 2.0D, this.height, size / 2.0D);

            Bukkit.getOnlinePlayers().forEach(player -> {
                player.sendTitle(this.infoTitle);
                player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 2f, 0.75f);
            });

            this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.floorIsLava, () -> {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    int height = (int) player.getLocation().getY();
                    this.scoreboard.updateBoard(player, "                                   ", "attention", "");

                    if (height > this.height + 2) {
                        this.scoreboard.updateBoard(player, this.save, "attention", "§a");
                    } else {
                        this.scoreboard.updateBoard(player, this.notSave, "attention", "§c");
                        player.sendTitle(this.notSaveTitle);
                    }

                    this.scoreboard.updateBoard(player, String.format("§8%s §e%s", FloorIsLava.DOT,
                            this.delay == 0 ? "§cJetzt!" : this.delay + " §e" + (this.delay > 1 ? "Sekunden" : "Sekunde")),
                            "countdown", "§e");
                });

                if (this.delay == 0) {
                    World world = this.bottomRight.getWorld();
                    this.bottomRight.setY(this.height);
                    this.topLeft.setY((this.height + 1));

                    for (int x = this.bottomRight.getBlockX(); x <= this.topLeft.getBlockX(); x++) {
                        for (int y = this.bottomRight.getBlockY(); y <= this.topLeft.getBlockY(); y++) {
                            for (int z = this.bottomRight.getBlockZ(); z <= this.topLeft.getBlockZ(); z++) {
                                Block block = Bukkit.getWorlds().get(0).getBlockAt(new Location(world, x, y, z));
                                block.setType(Material.LAVA, false);
                            }
                        }
                    }
                    if (this.height == this.heightToGo) {
                        this.floorIsLava.setPvpEnabled(true);
                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                            onlinePlayer.sendTitle(this.pvpTitle);
                            onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 2f, 1.5f);
                        }
                    }
                    this.height++;
                    this.delay = 5;
                } else {
                    this.delay--;
                }
            }, 0L, 20L);
        }
    }

    public void stop() {
        if (this.isRunning) {
            this.isRunning = false;
            Bukkit.getScheduler().cancelTask(this.taskId);
        }
    }
}
