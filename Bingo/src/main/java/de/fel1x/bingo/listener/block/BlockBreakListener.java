package de.fel1x.bingo.listener.block;

import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.gamehandler.Gamestate;
import de.fel1x.bingo.objects.BingoPlayer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class BlockBreakListener implements Listener {

    private final Bingo bingo;

    public BlockBreakListener(Bingo bingo) {
        this.bingo = bingo;
        bingo.getPluginManager().registerEvents(this, bingo);
    }

    @EventHandler
    public void on(BlockBreakEvent event) {

        Player player = event.getPlayer();

        Block block = event.getBlock();

        BingoPlayer bingoPlayer = new BingoPlayer(player);
        Gamestate gamestate = this.bingo.getGamestateHandler().getGamestate();

        switch (gamestate) {
            case IDLE:
            case LOBBY:
            case PREGAME:
            case ENDING:
                event.setCancelled(true);
                break;

            case INGAME:
                if (bingoPlayer.isSpectator()) {
                    event.setCancelled(true);
                }
                if(this.bingo.getData().isRandomizer()) {
                    if(block.getType().equals(Material.AIR)) {
                        return;
                    }

                    event.setCancelled(true);
                    block.setType(Material.AIR);

                    Material blockToDrop = this.bingo.getData().getRandomizedBlocks().get(block.getType());
                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(blockToDrop));
                }
                break;
        }
    }
}
