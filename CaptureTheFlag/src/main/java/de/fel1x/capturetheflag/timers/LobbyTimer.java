package de.fel1x.capturetheflag.timers;

import de.dytanic.cloudnet.ext.bridge.bukkit.BukkitCloudNetHelper;
import de.fel1x.capturetheflag.CaptureTheFlag;
import de.fel1x.capturetheflag.gameplayer.GamePlayer;
import de.fel1x.capturetheflag.gamestate.Gamestate;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

public class LobbyTimer implements ITimer {

    private final CaptureTheFlag captureTheFlag = CaptureTheFlag.getInstance();

    private int taskId;
    private boolean running;

    private int countdown = 60;

    public void start() {

        if (!this.running) {

            this.captureTheFlag.getGamestateHandler().setGamestate(Gamestate.LOBBY);
            this.running = true;

            this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.captureTheFlag, () -> {
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
                        Bukkit.broadcastMessage(this.captureTheFlag.getPrefix() + "§7Die Runde startet in §e"
                                + (this.countdown == 1 ? "einer §7Sekunde" : this.countdown + " §7Sekunden"));
                        Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 2f, 3f));
                        break;


                    case 0:
                        Bukkit.broadcastMessage(this.captureTheFlag.getPrefix() + "§a§lDie Runde beginnt!");

                        this.stopFinally();

                        Bukkit.getOnlinePlayers().forEach(current -> current.playSound(current.getLocation(),
                                Sound.ENTITY_PLAYER_LEVELUP, 4f, 0.8f));

                        CaptureTheFlag.getInstance().getData().getPlayers().forEach(current -> {
                            this.captureTheFlag.getData().getPlayTime().put(current.getUniqueId(), System.currentTimeMillis());
                            GamePlayer player = new GamePlayer(current);

                            player.checkForTeam();
                            player.cleanupInventory();
                            player.teleportToTeamSpawn();
                            player.setKitItems();

                            this.captureTheFlag.getData().getCachedStats().get(current).increaseGamesByOne();

                            current.setLevel(0);
                            current.setExp(0);
                        });

                        this.captureTheFlag.startTimerByClass(InGameTimer.class);

                        BukkitCloudNetHelper.changeToIngame();
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

    public void stop() {

        if (this.running) {

            this.running = false;
            this.countdown = 60;

            Bukkit.getOnlinePlayers().forEach(current -> {

                current.setLevel(0);
                current.setExp(0);

            });

            Bukkit.getScheduler().cancelTask(this.taskId);

        }

    }

    private void stopFinally() {

        if (this.running) {

            this.running = false;
            this.countdown = 0;

            Bukkit.getOnlinePlayers().forEach(current -> {

                current.setLevel(0);
                current.setExp(0);

            });

            Bukkit.getScheduler().cancelTask(this.taskId);

        }

    }

    public int getCountdown() {
        return this.countdown;
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

}
