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
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {

    private final CaptureTheFlag captureTheFlag;
    private final Data data;

    public BlockPlaceListener(CaptureTheFlag captureTheFlag) {
        this.captureTheFlag = captureTheFlag;
        this.data = this.captureTheFlag.getData();

        this.captureTheFlag.getPluginManager().registerEvents(this, this.captureTheFlag);
    }

    @EventHandler
    public void on(BlockPlaceEvent event) {

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

        if (this.data.getBlueSpawnCuboid().contains(block) || this.data.getRedSpawnCuboid().contains(block)) {
            player.sendMessage("§cDu darfst nicht im Spawnbereich bauen!");
            event.setCancelled(true);
            return;
        }


        this.captureTheFlag.getData().getPlacedBlocks().add(block);

    }

}
