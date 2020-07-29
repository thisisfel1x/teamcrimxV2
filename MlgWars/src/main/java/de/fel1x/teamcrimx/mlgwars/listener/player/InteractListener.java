package de.fel1x.teamcrimx.mlgwars.listener.player;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.inventories.KitInventory;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractListener implements Listener {

    private final MlgWars mlgWars;

    public InteractListener(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
        this.mlgWars.getPluginManager().registerEvents(this, this.mlgWars);
    }

    @EventHandler
    public void on(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        GamePlayer gamePlayer = new GamePlayer(player);

        Material interactedMaterial = event.getMaterial();

        Gamestate gamestate = this.mlgWars.getGamestateHandler().getGamestate();

        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            if(gamestate == Gamestate.IDLE || gamestate == Gamestate.LOBBY) {
                if(event.hasItem()) {
                    if(event.getMaterial() == Material.STORAGE_MINECART) {
                        player.playSound(player.getLocation(), Sound.CHEST_OPEN, 2f, 0.75f);
                        KitInventory.KIT_OVERVIEW_INVENTORY.open(player);
                    } else if(event.getMaterial() == Material.REDSTONE_TORCH_ON) {
                        player.sendMessage("forcemap");
                    } else {
                        event.setCancelled(true);
                    }
                }

            } else if(gamestate == Gamestate.DELAY || gamestate == Gamestate.ENDING) {
                event.setCancelled(true);
            } else {
                if(gamePlayer.isSpectator()) {
                    event.setCancelled(true);
                    return;
                }

                switch (gamePlayer.getSelectedKit()) {
                    case EXPLODER:
                        if(interactedMaterial == Material.LEVER) {
                            for (Block block : this.mlgWars.getData().getPlacedExploderTnt().get(player.getUniqueId())) {
                                if(block.getType() != Material.TNT) continue;
                                if(block.hasMetadata("owner")) {
                                    String owner = block.getMetadata("owner").get(0).asString();
                                    if(owner.equalsIgnoreCase(player.getName())) {
                                        block.setType(Material.AIR);

                                        TNTPrimed tnt = player.getWorld().spawn(block.getLocation(), TNTPrimed.class);
                                        tnt.setFuseTicks(0);
                                    }
                                }
                            }
                        }
                        break;
                }
            }

        }

    }

}
