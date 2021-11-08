package de.fel1x.teamcrimx.mlgwars.kit.rework.kits;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.kit.rework.Kit;
import de.fel1x.teamcrimx.mlgwars.kit.rework.KitRegistry;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class StinkerKit extends Kit {

    public StinkerKit(Player player, MlgWars mlgWars) {
        super(player, mlgWars);
    }

    @Override
    public void initializeKit() {
        super.initializeKit();
        this.runTaskTimer(this.mlgWars, 0L, 20L);
    }

    @Override
    protected void onInteract(PlayerInteractEvent event) {
    }

    @Override
    public void run() {
        for (Player nearbyPlayer : this.player.getWorld().getNearbyPlayers(this.player.getLocation(), 5, 2, 5)) {
            GamePlayer gamePlayer = this.mlgWars.getData().getGamePlayers().get(nearbyPlayer.getUniqueId());

            if (gamePlayer.isSpectator() || this.player.getUniqueId().equals(nearbyPlayer.getUniqueId())
                    || gamePlayer.getPlayerMlgWarsTeamId() == this.gamePlayer.getPlayerMlgWarsTeamId()) {
                continue;
            }

            if (gamePlayer.getSelectedKit() != KitRegistry.STINKER) {
                nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 3, 0, true, true));
                nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 3, 0, true, true));
            }
        }
    }
}
