package de.fel1x.bingo.listener.entity;

import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.objects.BingoPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

public class EntityTargetListener implements Listener {

    public EntityTargetListener(Bingo bingo) {
        bingo.getPluginManager().registerEvents(this, bingo);
    }

    @EventHandler
    public void on(EntityTargetEvent event) {

        if (event.getTarget() instanceof Player) {

            Player player = (Player) event.getTarget();
            BingoPlayer bingoPlayer = new BingoPlayer(player);

            if (bingoPlayer.isSpectator()) {
                event.setCancelled(true);
            }

        }

    }

}
