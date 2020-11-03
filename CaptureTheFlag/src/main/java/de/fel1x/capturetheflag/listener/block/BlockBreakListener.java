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

    private CaptureTheFlag captureTheFlag;
    private Data data;

    public BlockBreakListener(CaptureTheFlag captureTheFlag) {
        this.captureTheFlag = captureTheFlag;
        this.data = this.captureTheFlag.getData();

        this.captureTheFlag.getPluginManager().registerEvents(this, this.captureTheFlag);
    }

    @EventHandler
    public void on(BlockBreakEvent event) {

        Block block = event.getBlock();

        Player player = event.getPlayer();
        GamePlayer gamePlayer = new GamePlayer(player);

        Gamestate gamestate = this.captureTheFlag.getGamestateHandler().getGamestate();

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

        if (!this.data.getPlacedBlocks().contains(block)) {
            event.setCancelled(true);
            return;
        }

        this.data.getPlacedBlocks().remove(block);

    }

}