package de.fel1x.teamcrimx.mlgwars.timer;

import de.fel1x.teamcrimx.crimxapi.utils.Actionbar;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.kit.Kit;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class PreGameTimer implements ITimer {

    private final MlgWars mlgWars = MlgWars.getInstance();

    private boolean running = false;
    private int taskId;
    private int countdown = this.mlgWars.isLabor() ? 10 : 40;

    @Override
    public void start() {
        if(!this.running) {

            this.mlgWars.getGamestateHandler().setGamestate(Gamestate.PREGAME);
            this.running = true;

            this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.mlgWars, () -> {

                switch (countdown) {
                    case 30: case 20: case 10: case 5: case 4: case 3: case 2: case 1:
                        Bukkit.broadcastMessage(this.mlgWars.getPrefix() + "§7Die Schutzzeit endet in §e"
                                + (countdown == 1 ? "einer §7Sekunde" : this.countdown + " §7Sekunden"));
                        Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.NOTE_BASS, 2f, 3f));
                        break;

                    case 0:
                        Bukkit.broadcastMessage(this.mlgWars.getPrefix() + "§cDie Schutzzeit ist vorbei! Kämpft!");
                        Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.LEVEL_UP, 3f, 5f));
                        this.mlgWars.startTimerByClass(InGameTimer.class);
                        break;
                }

                this.mlgWars.getData().getPlayers().forEach(player -> {
                    GamePlayer gamePlayer = new GamePlayer(player);

                    if(gamePlayer.isPlayer()) {

                        if(player.hasMetadata("team")) {
                            int team = player.getMetadata("team").get(0).asInt() + 1;
                            Actionbar.sendActiobar(player, "§7Team §a#" + team);
                        }

                        if(gamePlayer.getSelectedKit() == Kit.KANGAROO) {
                            if(!this.mlgWars.getData().getKangarooTask().containsKey(player.getUniqueId())) {
                                int currentEssences = 0;
                                if(player.hasMetadata("essence")) {
                                    currentEssences = player.getMetadata("essence").get(0).asInt();
                                }
                                Actionbar.sendActiobar(player, "§6Känguru §8● §a" + currentEssences + " §7Essenzen übrig");
                            }
                        }
                    }

                    if(this.mlgWars.getData().getPlayers().size() > 1) {
                        Player nearest = this.getClosestEntity(player, player.getLocation());
                        if(nearest != null) {
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
        if(this.running) {
            Bukkit.getScheduler().cancelTask(taskId);
            this.running = false;
            this.countdown = 60;
        }
    }

    private Player getClosestEntity(Player owner, Location center){
        Player closestEntity = null;
        double closestDistance = 0.0;

        for(Entity entity : center.getWorld().getNearbyEntities(center, 200, 200, 200)){
            if(!(entity instanceof Player)) continue;
            if(entity.getUniqueId().equals(owner.getUniqueId())) continue;

            double distance = entity.getLocation().distanceSquared(center);
            if(closestEntity == null || distance < closestDistance){
                closestDistance = distance;
                closestEntity = (Player) entity;
            }
        }
        return closestEntity;
    }
}
