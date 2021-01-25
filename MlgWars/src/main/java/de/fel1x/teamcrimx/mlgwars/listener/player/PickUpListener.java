package de.fel1x.teamcrimx.mlgwars.listener.player;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.kit.Kit;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class PickUpListener implements Listener {

    private final MlgWars mlgWars;

    public PickUpListener(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
        this.mlgWars.getPluginManager().registerEvents(this, this.mlgWars);
    }

    @EventHandler
    public void on(EntityPickupItemEvent event) {

        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        GamePlayer gamePlayer = new GamePlayer(player);

        Gamestate gamestate = this.mlgWars.getGamestateHandler().getGamestate();

        switch (gamestate) {

            case IDLE:
            case LOBBY:
            case DELAY:
            case ENDING:
                event.setCancelled(true);
                break;

            case PREGAME:
            case INGAME:
                if (gamePlayer.isSpectator()) {
                    event.setCancelled(true);
                    return;
                }

                if (gamePlayer.getSelectedKit() == Kit.DUMP) {
                    event.getItem()
                            .setItemStack(new ItemStack(this.mlgWars.getAllMaterials()
                                    .get(new Random().nextInt(this.mlgWars.getAllMaterials().size())),
                                    event.getItem().getItemStack().getAmount()));
                }
        }
    }
}
