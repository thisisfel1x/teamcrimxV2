package de.fel1x.teamcrimx.mlgwars.listener.player;

import de.fel1x.teamcrimx.crimxapi.coins.CoinsAPI;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import de.fel1x.teamcrimx.mlgwars.utils.WinDetection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    private final MlgWars mlgWars;

    public DeathListener(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
        this.mlgWars.getPluginManager().registerEvents(this, this.mlgWars);
    }

    @EventHandler
    public void on(PlayerDeathEvent event) {

        Player player = event.getEntity();
        GamePlayer gamePlayer = new GamePlayer(player);

        gamePlayer.activateSpectatorMode();

        Gamestate gamestate = this.mlgWars.getGamestateHandler().getGamestate();

        if(gamestate == Gamestate.PREGAME || gamestate == Gamestate.INGAME) {

            if(this.mlgWars.getData().getLastHit().get(player) != null) {

                Player killer = this.mlgWars.getData().getLastHit().get(player);
                CoinsAPI coinsAPI = new CoinsAPI(killer.getUniqueId());
                coinsAPI.addCoins(100);

                killer.sendMessage(this.mlgWars.getPrefix() + "§7Du hast " + player.getDisplayName() + " §7getötet §a(+100 Coins)");
                event.setDeathMessage(String.format("%s %s §7wurde von %s §7getötet", this.mlgWars.getPrefix(),
                        player.getDisplayName(), killer.getDisplayName()));

            } else {
                event.setDeathMessage(this.mlgWars.getPrefix() + player.getDisplayName() + " §7ist gestorben");
            }

            new WinDetection();
        }

    }

}
