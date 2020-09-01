package de.fel1x.teamcrimx.mlgwars.listener.player;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PickUpListener implements Listener {

    private MlgWars mlgWars;

    public PickUpListener(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
        this.mlgWars.getPluginManager().registerEvents(this, this.mlgWars);
    }

    @EventHandler
    public void on(PlayerPickupItemEvent event) {

        Player player = event.getPlayer();
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

                switch (gamePlayer.getSelectedKit()) {
                    case EXPLODER:
                        event.getItem().setItemStack(new ItemBuilder(Material.TNT, 16)
                                .setName("§8● §bExplosives TNT")
                                .setLore("§7Explosiver geht es fast gar", "§7nicht mehr! (wirklich explosiv!)").toItemStack());
                        break;
                }
        }
    }
}
