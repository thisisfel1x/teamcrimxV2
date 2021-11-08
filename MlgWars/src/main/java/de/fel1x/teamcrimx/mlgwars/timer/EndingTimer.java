package de.fel1x.teamcrimx.mlgwars.timer;

import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.ext.bridge.BridgeServiceProperty;
import de.dytanic.cloudnet.wrapper.Wrapper;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class EndingTimer implements ITimer {

    private final MlgWars mlgWars = MlgWars.getInstance();

    private boolean running = false;
    private int taskId;
    private int countdown = 20;

    @Override
    public void start() {
        if (!this.running) {

            this.mlgWars.getGamestateHandler().setGamestate(Gamestate.ENDING);
            this.running = true;

            this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.mlgWars, () -> {

                switch (this.countdown) {
                    case 20, 10, 5, 4, 3, 2, 1 -> {
                        Bukkit.broadcastMessage(this.mlgWars.getPrefix() + "§cDer Server startet in "
                                + (this.countdown == 1 ? "einer Sekunde" : this.countdown + " Sekunden") + " neu");
                        Bukkit.getServer().playSound(net.kyori.adventure.sound.Sound.sound(Sound.BLOCK_NOTE_BLOCK_BASS.key(),
                                net.kyori.adventure.sound.Sound.Source.BLOCK, 2f, 3f));
                    }
                    case 0 -> {
                        Bukkit.broadcastMessage(this.mlgWars.getPrefix() + "§cDer Server startet neu");
                        Bukkit.getServer().playSound(net.kyori.adventure.sound.Sound.sound(Sound.ENTITY_CAT_HISS.key(),
                                net.kyori.adventure.sound.Sound.Source.BLOCK, 2f, 3f));
                        Bukkit.getOnlinePlayers().forEach(this::sendToLobby);
                    }
                }

                if (this.countdown < 0 && Bukkit.getOnlinePlayers().isEmpty()) {
                    Bukkit.getServer().shutdown();
                }

                this.countdown--;

            }, 0L, 20L);
        }

    }

    private void sendToLobby(Player player) {
        List<ServiceInfoSnapshot> lobbies = Wrapper.getInstance().getCloudServiceProvider().getCloudServices("Lobby")
                .stream().filter(service -> service.getProperty(BridgeServiceProperty.IS_ONLINE).orElse(false)
                        && !service.getProperty(BridgeServiceProperty.IS_FULL).orElse(true)).collect(Collectors.toList());
        this.mlgWars.getData().getGamePlayers().get(player.getUniqueId())
                .connectToService(lobbies.get(new Random().nextInt(lobbies.size())).getName());
    }

    @Override
    public void stop() {
        if (this.running) {
            Bukkit.getScheduler().cancelTask(this.taskId);
            this.running = false;
            this.countdown = 60;
        }
    }
}
