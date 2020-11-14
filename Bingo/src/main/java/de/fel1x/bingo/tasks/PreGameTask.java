package de.fel1x.bingo.tasks;

import com.destroystokyo.paper.Title;
import de.dytanic.cloudnet.ext.bridge.BridgeHelper;
import de.dytanic.cloudnet.ext.bridge.bukkit.BukkitCloudNetHelper;
import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.gamehandler.Gamestate;
import de.fel1x.bingo.objects.BingoPlayer;
import de.fel1x.bingo.objects.BingoTeam;
import org.bukkit.*;
import org.bukkit.entity.FallingBlock;
import org.bukkit.util.Vector;

import java.util.Random;

public class PreGameTask implements IBingoTask {

    Bingo bingo = Bingo.getInstance();
    int taskId = 0;
    int timer = 15;

    boolean isRunning = false;

    Random random = new Random();

    @Override
    public void start() {

        if (!this.isRunning) {

            BukkitCloudNetHelper.changeToIngame();
            BridgeHelper.updateServiceInfo();

            this.isRunning = true;
            this.bingo.getGamestateHandler().setGamestate(Gamestate.PREGAME);

            this.bingo.getData().getPlayers().forEach(player -> {
                player.teleport(new Location(Bukkit.getWorlds().get(0), 0.5, 125, 0.5));
                BingoPlayer bingoPlayer = new BingoPlayer(player);
                if (bingoPlayer.getTeam() == null) {
                    for (BingoTeam bingoTeam : BingoTeam.values()) {
                        if (bingoTeam.getTeamPlayers().size() < bingoTeam.getTeamSize()) {
                            bingoPlayer.setTeam(bingoTeam);
                            player.sendMessage(this.bingo.getPrefix() + "§7Du wurdest zu Team "
                                    + bingoTeam.getName() + " zugewiesen");
                            this.bingo.getLobbyScoreboard().setLobbyScoreboard(player);
                            break;
                        }
                    }
                }

            });

            this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.bingo, () -> {

                switch (this.timer) {
                    case 15:
                    case 10:
                    case 5:
                    case 3:
                    case 2:
                    case 1:

                        Bukkit.broadcastMessage(this.bingo.getPrefix() + "§7Das Spiel startet in §e"
                                + ((this.timer == 1) ? "einer Sekunde" : this.timer + " Sekunden"));

                        this.bingo.getData().getPlayers().forEach(player -> {

                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0.75f);
                            player.sendTitle(Title.builder()
                                    .title(((this.timer == 3) ? "§a§l" : ((this.timer == 2) ? "§e§l" : "§c§l")) + this.timer)
                                    .fadeIn(10).stay(20).fadeOut(10).build());

                            player.getInventory().clear();

                        });

                        break;

                    case 0:
                        Bukkit.broadcastMessage(this.bingo.getPrefix() + "§e§lDas Spiel beginnt!");
                        Bukkit.getScheduler().cancelTasks(this.bingo);

                        this.bingo.getGamestateHandler().setGamestate(Gamestate.INGAME);

                        this.bingo.getData().getPlayers().forEach(player -> {

                            player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.5f, 1.25f);
                            player.sendTitle(Title.builder()
                                    .title("§a§lGO").fadeIn(0).stay(40).fadeOut(10).build());

                            int x = this.random.nextInt(20) * (this.random.nextBoolean() ? -1 : 1);
                            int z = this.random.nextInt(20) * (this.random.nextBoolean() ? -1 : 1);

                            player.setVelocity(player.getVelocity().setY(10)
                                    .setX(x).setZ(z));

                            player.setGameMode(GameMode.SURVIVAL);

                        });

                        this.bingo.getWorldGenerator().getBlocks().forEach(block -> {

                            float x = -2.0F + (float) (Math.random() * 4.0D + 1.0D);
                            float y = -3.0F + (float) (Math.random() * 6.0D + 1.0D);
                            float z = -2.0F + (float) (Math.random() * 4.0D + 1.0D);

                            FallingBlock fallingBlock = block.getWorld().spawnFallingBlock(block.getLocation(), block.getBlockData());
                            this.bingo.getFallingGlassBlocks().add(fallingBlock);

                            fallingBlock.setVelocity(new Vector(x, y, z));
                            fallingBlock.setDropItem(false);

                            block.setType(Material.AIR);

                        });


                        Bukkit.getWorlds().get(0).getBlockAt(0, 120, 0).setType(Material.AIR);

                        this.bingo.startTimerByClass(GameTask.class);

                        break;

                }

                this.timer--;

            }, 0L, 20L);
        }

    }

    @Override
    public void stop() {

        if (this.isRunning) {

            this.isRunning = false;
            Bukkit.getScheduler().cancelTask(this.taskId);

            this.timer = 60;

        }

    }

}
