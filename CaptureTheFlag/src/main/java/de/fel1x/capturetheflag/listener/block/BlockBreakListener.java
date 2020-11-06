package de.fel1x.capturetheflag.listener.block;

import de.fel1x.capturetheflag.CaptureTheFlag;
import de.fel1x.capturetheflag.Data;
import de.fel1x.capturetheflag.gameplayer.GamePlayer;
import de.fel1x.capturetheflag.gamestate.Gamestate;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Arrays;

public class BlockBreakListener implements Listener {

    private CaptureTheFlag captureTheFlag;
    private Data data;

    private Material[] allowedBlocksToBreak = {
            Material.GRASS,
            Material.DANDELION,
            Material.POPPY,
            Material.BLUE_ORCHID,
            Material.ALLIUM,
            Material.AZURE_BLUET,
            Material.RED_TULIP,
            Material.ORANGE_TULIP,
            Material.WHITE_TULIP,
            Material.PINK_TULIP,
            Material.OXEYE_DAISY,
            Material.CORNFLOWER,
            Material.LILAC,
            Material.ROSE_BUSH,
            Material.PEONY,
            Material.TALL_GRASS,
            Material.LARGE_FERN,
            Material.FIRE
    };

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

        if (Arrays.stream(this.allowedBlocksToBreak).anyMatch(stream -> stream == block.getType())) {
            return;
        }

        if (!this.data.getPlacedBlocks().contains(block)) {
            event.setCancelled(true);
            return;
        }

        this.data.getPlacedBlocks().remove(block);

    }

}
