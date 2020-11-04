package de.fel1x.capturetheflag.listener.player;

import de.fel1x.capturetheflag.CaptureTheFlag;
import de.fel1x.capturetheflag.Data;
import de.fel1x.capturetheflag.gameplayer.GamePlayer;
import de.fel1x.capturetheflag.gamestate.Gamestate;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    private CaptureTheFlag captureTheFlag;
    private Data data;

    public DeathListener(CaptureTheFlag captureTheFlag) {
        this.captureTheFlag = captureTheFlag;
        this.data = this.captureTheFlag.getData();

        this.captureTheFlag.getPluginManager().registerEvents(this, this.captureTheFlag);
    }

    @EventHandler
    public void on(PlayerDeathEvent event) {

        Player player = event.getEntity();
        GamePlayer gamePlayer = new GamePlayer(player);

        event.setDeathMessage(null);

        if (!gamePlayer.isPlayer()) {
            return;
        }

        Gamestate gamestate = this.captureTheFlag.getGamestateHandler().getGamestate();

        Location location = player.getLocation();

        event.getDrops().clear();

        if (gamestate.equals(Gamestate.INGAME)) {
            if (player.equals(data.getRedFlagHolder())) {
                player.sendMessage(this.captureTheFlag.getPrefix() + "§cDu hast die Flagge verloren!");
                player.setGlowing(false);

                data.setRedFlagHolder(null);

                Block block = location.getBlock();

                try {

                    block.setType(Material.RED_BANNER);
                    block.getState().update();

                } catch (Exception ignored) {

                }


            }

            if (player.equals(data.getBlueFlagHolder())) {
                player.sendMessage(this.captureTheFlag.getPrefix() + "§cDu hast die Flagge verloren!");
                player.setGlowing(false);

                data.setBlueFlagHolder(null);

                Block block = location.getBlock();

                try {

                    block.setType(Material.BLUE_BANNER);
                    block.getState().update();

                } catch (Exception ignored) {

                }

            }
        }

        Player attacker = null;

        if (data.getLastHit().get(player) != null) {

            attacker = data.getLastHit().get(player);

        } else {

            event.getEntity().getKiller();

        }

        if (attacker != null) {

            event.setDeathMessage(this.captureTheFlag.getPrefix() + player.getDisplayName() + " §7wurde von " + attacker.getDisplayName() + " §7getötet");
            this.data.getCachedStats().get(attacker).increaseKillsByOne();

        } else {

            event.setDeathMessage(this.captureTheFlag.getPrefix() + player.getDisplayName() + " §7ist gestorben");

        }

        this.data.getCachedStats().get(player).increaseDeathsByOne();

    }

}
