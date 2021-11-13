package de.fel1x.teamcrimx.mlgwars.timer;

import de.fel1x.teamcrimx.crimxapi.utils.Actionbar;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.maphandler.gametype.types.TntMadnessGameType;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class PreGameTimer implements ITimer {

    private final MlgWars mlgWars = MlgWars.getInstance();

    private boolean running = false;
    private int taskId;
    private int countdown = this.mlgWars.getGameType().getClass() == TntMadnessGameType.class ? 10 : 40;

    @Override
    public void start() {
        if (!this.running) {

            this.mlgWars.getGamestateHandler().setGamestate(Gamestate.PREGAME);
            this.running = true;

            this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.mlgWars, () -> {

                switch (this.countdown) {
                    case 30, 20, 10, 5, 4, 3, 2, 1 -> {
                        Bukkit.broadcastMessage(this.mlgWars.getPrefix() + "§7Die Schutzzeit endet in §e"
                                + (this.countdown == 1 ? "einer §7Sekunde" : this.countdown + " §7Sekunden"));
                        Bukkit.getServer().playSound(net.kyori.adventure.sound.Sound.sound(Sound.BLOCK_NOTE_BLOCK_BASS.key(),
                                net.kyori.adventure.sound.Sound.Source.BLOCK, 2f, 3f));
                    }
                    case 0 -> {
                        Bukkit.broadcastMessage(this.mlgWars.getPrefix() + "§cDie Schutzzeit ist vorbei! Kämpft!");
                        Bukkit.getServer().playSound(net.kyori.adventure.sound.Sound.sound(Sound.ENTITY_FIREWORK_ROCKET_TWINKLE.key(),
                                net.kyori.adventure.sound.Sound.Source.BLOCK, 2f, 3f));
                        this.mlgWars.startTimerByClass(InGameTimer.class);
                    }
                }

                this.mlgWars.getData().getPlayers().forEach(player -> {
                    GamePlayer gamePlayer = this.mlgWars.getData().getGamePlayers().get(player.getUniqueId());

                    if (gamePlayer.isPlayer()) {
                        gamePlayer.getMlgActionbar().sendActionbar(player, (Component) null);
                    }

                    if (this.mlgWars.getData().getPlayers().size() > 1) {
                        Player nearest = this.getClosestEntity(player, player.getLocation());
                        if (nearest != null) {
                            player.setCompassTarget(nearest.getLocation());
                        }
                    }
                });

                this.countdown--;

            }, 0L, 20L);
        }

    }

    @Override
    public void stop() {
        if (this.running) {
            Bukkit.getScheduler().cancelTask(this.taskId);
            this.running = false;
            this.countdown = 60;
        }
    }

    private Player getClosestEntity(Player owner, Location center) {
        Player closestEntity = null;
        double closestDistance = 0.0;

        for (Entity entity : center.getWorld().getNearbyEntities(center, 200, 200, 200)) {
            if (!(entity instanceof Player)) continue;
            if (entity.getUniqueId().equals(owner.getUniqueId())) continue;

            double distance = entity.getLocation().distanceSquared(center);
            if (closestEntity == null || distance < closestDistance) {
                closestDistance = distance;
                closestEntity = (Player) entity;
            }
        }
        return closestEntity;
    }
}
