package de.fel1x.capturetheflag.timers;

import de.fel1x.capturetheflag.CaptureTheFlag;
import de.fel1x.capturetheflag.Data;
import de.fel1x.capturetheflag.filehandler.SpawnHandler;
import de.fel1x.capturetheflag.gameplayer.GamePlayer;
import de.fel1x.capturetheflag.gamestate.Gamestate;
import de.fel1x.capturetheflag.team.Teams;
import de.fel1x.capturetheflag.utils.particles.ParticleEffects;
import de.fel1x.teamcrimx.crimxapi.utils.Actionbar;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Sound;
import org.bukkit.block.Banner;

public class InGameTimer implements ITimer {

    private final CaptureTheFlag captureTheFlag = CaptureTheFlag.getInstance();
    private final Data data = this.captureTheFlag.getData();

    private int taskId;
    private boolean running;

    private int timer = 0;

    public void start() {

        Bukkit.getOnlinePlayers().forEach(current -> {
            current.setLevel(0);
            current.setExp(0);
        });

        if (!this.running) {

            this.captureTheFlag.getGamestateHandler().setGamestate(Gamestate.INGAME);
            this.running = true;

            this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.captureTheFlag, () -> {

                Bukkit.getOnlinePlayers().forEach(current -> {
                    GamePlayer gamePlayer = new GamePlayer(current);

                    if(gamePlayer.isPlayer()) {
                        if ((this.data.getRedFlagHolder() != null && current.equals(this.data.getRedFlagHolder())
                                || (this.data.getBlueFlagHolder() != null && current.equals(this.data.getBlueFlagHolder())))) {
                            current.setWalkSpeed(0.05f);

                        } else {
                            current.setWalkSpeed(0.2f);
                        }
                        current.sendActionBar("§7Team " + gamePlayer.getTeam().getTeamName());
                    } else {
                        current.sendActionBar("§7Spectator");
                    }
                });

                ParticleEffects.drawBannerCircle(SpawnHandler.loadBannerLocation("redFlag"), 0.25, 20);
                ParticleEffects.drawBannerCircle(SpawnHandler.loadBannerLocation("blueFlag"), 0.25, 20);

                this.checkBanner();

                this.timer++;

            }, 0L, 20L);

        }

    }

    public void stop() {
        if (this.running) {
            this.running = false;
            Bukkit.getScheduler().cancelTask(taskId);
        }
    }


    public void checkBanner() {
        try {
            Banner banner = (Banner) data.getRedFlagBaseLocation().getBlock().getState();

            if (!banner.getBaseColor().equals(DyeColor.RED)) {
                Teams.RED.getTeamPlayers().forEach(player -> {
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR, 7, 2);
                    Actionbar.sendOnlySubtitle(player, "§cDeine Flagge wurde gestohlen!", 0, 40, 20);
                });
            }
        } catch (Exception ignored) { }

        try {
            Banner banner = (Banner) data.getBlueFlagBaseLocation().getBlock().getState();

            if (!banner.getBaseColor().equals(DyeColor.BLUE)) {
                Teams.BLUE.getTeamPlayers().forEach(player -> {
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR, 7, 2);
                    Actionbar.sendOnlySubtitle(player, "§cDeine Flagge wurde gestohlen!", 0, 40, 20);
                });

            }

        } catch (Exception ignored) { }
    }
}
