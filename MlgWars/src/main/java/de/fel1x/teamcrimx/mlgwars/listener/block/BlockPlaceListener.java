package de.fel1x.teamcrimx.mlgwars.listener.block;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;

public class BlockPlaceListener implements Listener {

    private final MlgWars mlgWars;

    public BlockPlaceListener(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
        this.mlgWars.getPluginManager().registerEvents(this, this.mlgWars);
    }

    @EventHandler
    public void on(BlockPlaceEvent event) {

        Player player = event.getPlayer();
        GamePlayer gamePlayer = new GamePlayer(player);

        Block placedBlock = event.getBlockPlaced();

        Gamestate gamestate = this.mlgWars.getGamestateHandler().getGamestate();

        switch (gamestate) {

            case IDLE: case LOBBY: case DELAY: case ENDING:
                event.setCancelled(true);
                break;

            case PREGAME: case INGAME:
                if(gamePlayer.isSpectator()) {
                    event.setCancelled(true);
                    return;
                }

                switch (gamePlayer.getSelectedKit()) {
                    case EXPLODER:
                        if(event.getBlockPlaced().getType() == Material.TNT) {
                            placedBlock.setMetadata("owner", new FixedMetadataValue(this.mlgWars, player.getName()));

                            this.mlgWars.getData().getPlacedExploderTnt().computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>());
                            this.mlgWars.getData().getPlacedExploderTnt().get(player.getUniqueId()).add(placedBlock);
                        }

                }

                break;
        }

    }

}
