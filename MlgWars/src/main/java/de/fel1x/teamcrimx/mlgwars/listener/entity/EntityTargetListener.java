package de.fel1x.teamcrimx.mlgwars.listener.entity;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

public class EntityTargetListener implements Listener {

    private MlgWars mlgWars;

    public EntityTargetListener(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
        this.mlgWars.getPluginManager().registerEvents(this, this.mlgWars);
    }

    @EventHandler
    public void on(EntityTargetEvent event) {
        Entity target = event.getTarget();
        Entity entity = event.getEntity();

        if(target instanceof Player) {
            Player player = (Player) target;

            if(entity.getCustomName() != null) {
                if(entity.getCustomName().equalsIgnoreCase(player.getDisplayName())) {
                    event.setTarget(null);
                }
            }
        } else if (target instanceof Zombie) {
            Zombie zombie = (Zombie) target;
            if(entity.getCustomName() != null && zombie.getCustomName() != null) {
                if(entity.getCustomName().equalsIgnoreCase(zombie.getCustomName())) {
                    event.setTarget(null);
                }
            }
        }
    }
}
