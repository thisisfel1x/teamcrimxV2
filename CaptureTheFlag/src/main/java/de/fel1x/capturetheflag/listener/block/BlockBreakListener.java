package de.fel1x.capturetheflag.listener.block;

import de.fel1x.capturetheflag.CaptureTheFlag;
import de.fel1x.capturetheflag.Data;
import de.fel1x.capturetheflag.gameplayer.GamePlayer;
import de.fel1x.capturetheflag.gamestate.Gamestate;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    Data data = CaptureTheFlag.getInstance().getData();

    @EventHandler
    public void on(BlockBreakEvent event) {

        Block block = event.getBlock();

        Player player = event.getPlayer();
        GamePlayer gamePlayer = new GamePlayer(player);

        Gamestate gamestate = CaptureTheFlag.getInstance().getGamestateHandler().getGamestate();

        if (!gamestate.equals(Gamestate.INGAME)) {
            event.setCancelled(true);
            return;
        }

        if (!gamePlayer.isPlayer()) {
            event.setCancelled(true);
            return;
        }

        if (event.getBlock().getState() instanceof Banner) {
            event.setCancelled(true);
        }

        if (data.getBlueSpawnCuboid().contains(block) || data.getRedSpawnCuboid().contains(block)) {
            player.sendMessage("§cDu darfst nicht im Spawnbereich abbauen!");
            event.setCancelled(true);
            return;
        }

        if (!CaptureTheFlag.getInstance().getData().getPlacedBlocks().contains(block)) {
            event.setCancelled(true);
            return;
        }

        CaptureTheFlag.getInstance().getData().getPlacedBlocks().remove(block);

    }

}
