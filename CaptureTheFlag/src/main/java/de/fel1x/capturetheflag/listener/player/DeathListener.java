package de.fel1x.capturetheflag.listener.player;

import de.fel1x.capturetheflag.CaptureTheFlag;
import de.fel1x.capturetheflag.Data;
import de.fel1x.capturetheflag.gameplayer.GamePlayer;
import de.fel1x.capturetheflag.gamestate.Gamestate;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    Data data = CaptureTheFlag.getInstance().getData();

    @EventHandler
    public void on(PlayerDeathEvent event) {

        Player player = event.getEntity();
        GamePlayer gamePlayer = new GamePlayer(player);

        event.setDeathMessage(null);

        if (!gamePlayer.isPlayer()) {
            return;
        }

        Gamestate gamestate = CaptureTheFlag.getInstance().getGamestateHandler().getGamestate();

        Location location = player.getLocation();

        event.getDrops().clear();

        if (gamestate.equals(Gamestate.INGAME)) {

            if (player.equals(data.getRedFlagHolder())) {

                player.sendMessage("§cDu hast die Flagge verloren!");

                data.setRedFlagHolder(null);

                Block block = location.getBlock();

                try {

                    block.setType(Material.STANDING_BANNER);
                    block.getState().update();
                    Banner banner = (Banner) block.getState();
                    banner.setBaseColor(DyeColor.RED);
                    banner.update();

                    data.setRedFlagLocation(block.getLocation());

                } catch (Exception ignored) {

                }


            }

            if (player.equals(data.getBlueFlagHolder())) {

                player.sendMessage("§cDu hast die Flagge verloren!");

                data.setBlueFlagHolder(null);

                Block block = location.getBlock();

                try {

                    block.setType(Material.STANDING_BANNER);
                    block.getState().update();
                    Banner banner = (Banner) block.getState();
                    banner.setBaseColor(DyeColor.BLUE);
                    banner.update();

                    data.setBlueFlagLocation(banner.getLocation());

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

            event.setDeathMessage(player.getDisplayName() + " §7wurde von " + attacker.getDisplayName() + " §7getötet");

        } else {

            event.setDeathMessage(player.getDisplayName() + " §7ist gestorben");

        }

    }

}
