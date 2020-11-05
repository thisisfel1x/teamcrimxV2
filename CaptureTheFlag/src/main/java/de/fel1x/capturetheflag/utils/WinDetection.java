package de.fel1x.capturetheflag.utils;

import de.dytanic.cloudnet.ext.cloudperms.bukkit.BukkitCloudNetCloudPermissionsPlugin;
import de.fel1x.capturetheflag.CaptureTheFlag;
import de.fel1x.capturetheflag.filehandler.SpawnHandler;
import de.fel1x.capturetheflag.gameplayer.GamePlayer;
import de.fel1x.capturetheflag.gamestate.Gamestate;
import de.fel1x.capturetheflag.team.Team;
import de.fel1x.capturetheflag.timers.EndingTimer;
import de.fel1x.teamcrimx.crimxapi.coins.CoinsAPI;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.crimxapi.objects.CrimxPlayer;
import de.fel1x.teamcrimx.crimxapi.utils.Actionbar;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Random;

public class WinDetection {

    private final CaptureTheFlag captureTheFlag = CaptureTheFlag.getInstance();

    public WinDetection(Team team) {
        String name = team.getTeamName();

        /*
        Database related stuff (saving stats, coins etc)
         */
        Bukkit.getScheduler().runTaskAsynchronously(this.captureTheFlag, () -> {
            for (Player inGamePlayer : this.captureTheFlag.getData().getPlayers()) {
                GamePlayer gamePlayer = new GamePlayer(inGamePlayer);
                if(team.getTeamPlayers().contains(inGamePlayer)) {
                    this.captureTheFlag.getData().getCachedStats().get(inGamePlayer).increaseWinsByOne();

                    int earnedCoins = 100 + (4 * (10 + new Random().nextInt(20)));
                    CoinsAPI coinsAPI = new CoinsAPI(inGamePlayer.getUniqueId());
                    coinsAPI.addCoins(earnedCoins);

                    inGamePlayer.sendMessage(String.format("%s§7Du hast §e%s Coins §7erhalten",
                            this.captureTheFlag.getPrefix(), earnedCoins));
                }
                gamePlayer.saveStats();
            }
        });

        /*
        Winning Title etc
         */
        Bukkit.getOnlinePlayers().forEach(current -> {

            Actionbar.sendFullTitle(current, "§7Team " + name,
                    "§7hat das Spiel gewonnen", 5, 30, 5);

            Utils.spawnCircle(SpawnHandler.loadLocation("lobby"), 1.5, 12);

            current.getInventory().clear();
            current.getInventory().setArmorContents(null);
            current.setHealth(20);
            current.setFoodLevel(20);

            current.setGameMode(GameMode.SURVIVAL);

            current.getActivePotionEffects().forEach(potionEffect -> current.removePotionEffect(potionEffect.getType()));

            current.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            BukkitCloudNetCloudPermissionsPlugin.getInstance().updateNameTags(current);
        });

        /*
        Starting Ending Timer
         */
        CaptureTheFlag.getInstance().getGamestateHandler().setGamestate(Gamestate.ENDING);
        CaptureTheFlag.getInstance().startTimerByClass(EndingTimer.class);
    }
}
