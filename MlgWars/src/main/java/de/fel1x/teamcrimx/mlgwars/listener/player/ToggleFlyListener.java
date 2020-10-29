package de.fel1x.teamcrimx.mlgwars.listener.player;

import de.fel1x.teamcrimx.crimxapi.utils.Actionbar;
import de.fel1x.teamcrimx.crimxapi.utils.ProgressBar;
import de.fel1x.teamcrimx.mlgwars.Data;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.kit.Kit;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

public class ToggleFlyListener implements Listener {

    private final MlgWars mlgWars;
    private final Data data;

    private int count;

    public ToggleFlyListener(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
        this.data = this.mlgWars.getData();
        this.mlgWars.getPluginManager().registerEvents(this, this.mlgWars);
    }

    @EventHandler
    public void on(PlayerToggleFlightEvent event) {

        Player player = event.getPlayer();
        GamePlayer gamePlayer = new GamePlayer(player);

        Gamestate gamestate = this.mlgWars.getGamestateHandler().getGamestate();
        Kit selectedKit = gamePlayer.getSelectedKit();

        if (gamePlayer.isSpectator()) return;

        if (gamestate == Gamestate.PREGAME || gamestate == Gamestate.INGAME) {
            if (selectedKit == Kit.KANGAROO) {

                event.setCancelled(true);
                player.setFlying(false);
                player.setAllowFlight(false);

                if (player.hasMetadata("essence")) {
                    int essences = player.getMetadata("essence").get(0).asInt();
                    if (essences <= 0) {
                        player.sendMessage(this.mlgWars.getPrefix() + "§7Alle Essenzen §aaufgebraucht! §7Töte Gegner um neue zu erhalten!");
                        player.playSound(player.getLocation(), Sound.ENTITY_BAT_HURT, 3f, 0.75f);
                        return;
                    }
                }

                int currentEssences = 0;
                if (player.hasMetadata("essence")) {
                    currentEssences = player.getMetadata("essence").get(0).asInt();
                }
                player.setMetadata("essence", new FixedMetadataValue(this.mlgWars, currentEssences - 1));

                if (player.hasMetadata("kangaroo")) {
                    long nextUse = player.getMetadata("kangaroo").get(0).asLong();
                    if (nextUse > System.currentTimeMillis()) {
                        player.sendMessage(this.mlgWars.getPrefix() + "§7Bitte warte einen Moment bis du den Sprung wieder nutzen kannst!");
                        return;
                    }
                }

                player.setVelocity(player.getLocation().getDirection().multiply(2.0D).setY(0.9D));
                player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 5, 3);

                player.setMetadata("kangaroo", new FixedMetadataValue(this.mlgWars, System.currentTimeMillis() + (1000 * 10)));
                player.setMetadata("kangaroocount", new FixedMetadataValue(this.mlgWars, 0));

                this.data.getKangarooTask().put(player.getUniqueId(), new BukkitRunnable() {
                    @Override
                    public void run() {

                        count = player.getMetadata("kangaroocount").get(0).asInt();

                        Actionbar.sendActionbar(player, "§6Känguru §8● " + ProgressBar.getProgressBar(count, 10, 10,
                                '█', ChatColor.GREEN, ChatColor.DARK_GRAY));

                        if (count == 10) {
                            data.getKangarooTask().get(player.getUniqueId()).cancel();
                            data.getKangarooTask().remove(player.getUniqueId());
                            Actionbar.sendActionbar(player, "§6Känguru §8● §7Sprung wieder §abereit!");
                            player.setAllowFlight(true);
                        } else {
                            player.setMetadata("kangaroocount", new FixedMetadataValue(mlgWars, count + 1));
                        }
                    }
                });

                this.data.getKangarooTask().get(player.getUniqueId()).runTaskTimer(this.mlgWars, 0L, 20L);

            }
        }

    }

}
