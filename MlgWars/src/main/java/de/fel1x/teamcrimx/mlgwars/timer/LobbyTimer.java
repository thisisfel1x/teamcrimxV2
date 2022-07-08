package de.fel1x.teamcrimx.mlgwars.timer;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.inventories.rework.GameTypeVoteInventory;
import de.fel1x.teamcrimx.mlgwars.maphandler.ChestFiller;
import de.fel1x.teamcrimx.mlgwars.maphandler.gametype.GameType;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

import java.lang.reflect.InvocationTargetException;

public class LobbyTimer implements ITimer {

    private final MlgWars mlgWars = MlgWars.getInstance();

    private boolean running = false;
    private int taskId;
    private int countdown = 60;

    public void start(int countdown) {
        this.countdown = countdown;
        this.mlgWars.setLobbyCountdown(this.countdown);
        this.start();
    }

    @Override
    public void start() {
        if (!this.running) {

            this.mlgWars.getGamestateHandler().setGamestate(Gamestate.LOBBY);
            this.running = true;

            this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.mlgWars, () -> {
                switch (this.countdown) {
                    case 60, 50, 40, 30, 20, 10, 5, 4, 3, 2, 1 -> {
                        Bukkit.broadcastMessage(this.mlgWars.getPrefix() + "§7Die Runde startet in §e"
                                + (this.countdown == 1 ? "einer §7Sekunde" : this.countdown + " §7Sekunden"));
                        Bukkit.getServer().playSound(net.kyori.adventure.sound.Sound.sound(Sound.BLOCK_NOTE_BLOCK_BASS.key(),
                                net.kyori.adventure.sound.Sound.Source.BLOCK, 2f, 3f));
                    }
                    case 0 -> {
                        Bukkit.broadcastMessage(this.mlgWars.getPrefix() + "§aDie Runde beginnt!");
                        Bukkit.getServer().playSound(net.kyori.adventure.sound.Sound.sound(Sound.ENTITY_PLAYER_LEVELUP.key(),
                                net.kyori.adventure.sound.Sound.Source.PLAYER, 2f, 3f));
                        Bukkit.getOnlinePlayers().forEach(player -> {
                            player.setLevel(0);
                            player.setExp(0);
                        });
                        this.stop();
                        this.mlgWars.startTimerByClass(DelayTimer.class);
                    }
                }

                if(this.countdown == 10) {
                    GameTypeVoteInventory.ImplementedMode implementedMode = this.mlgWars.getForcedMode();
                    if(implementedMode == null) {
                        implementedMode = this.mlgWars.getGameTypeVoteInventory().calculateMode();
                    }

                    if(implementedMode != GameTypeVoteInventory.ImplementedMode.NORMAL) {
                        try {
                            GameType gameType = implementedMode.getGameType().getDeclaredConstructor(MlgWars.class).newInstance(this.mlgWars);
                            this.mlgWars.setGameType(gameType);
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) {}
                    }
                    Bukkit.getOnlinePlayers().forEach(player -> this.mlgWars.getScoreboardHandler()
                            .updateBoardMiniMessage(player, "<#5035f4>" + this.mlgWars.getGameType().getGameTypeName(), "mode"));
                }

                if (this.countdown == 5) {
                    this.mlgWars.getGameType().fillChests();
                    this.mlgWars.getServer()
                            .showTitle(Title.title(this.mlgWars.miniMessage().deserialize("<yellow>" + this.mlgWars.getSelectedMap().getMapName()),
                                    this.mlgWars.miniMessage().deserialize("<gray>Gebaut von <yellow>" + this.mlgWars.getSelectedMap().getMapBuilder())));
                }

                if (this.countdown >= 1) {
                    Bukkit.getOnlinePlayers().forEach(current -> {
                        current.setLevel(this.countdown);
                        current.setExp((float) this.countdown / (float) 60);
                    });
                }

                this.countdown--;
                this.mlgWars.setLobbyCountdown(this.countdown);

            }, 0L, 20L);
        }

    }

    @Override
    public void stop() {
        if (this.running) {
            Bukkit.getScheduler().cancelTask(this.taskId);
            this.running = false;
            this.setCountdown(60);
            this.mlgWars.setLobbyCountdown(60);

            Bukkit.getOnlinePlayers().forEach(player -> {
                player.setLevel(0);
                player.setExp(0);
            });
        }
    }

    public int getCountdown() {
        return this.countdown;
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }
}
