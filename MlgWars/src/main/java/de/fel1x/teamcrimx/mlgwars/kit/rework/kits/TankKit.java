package de.fel1x.teamcrimx.mlgwars.kit.rework.kits;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.kit.rework.Kit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TankKit extends Kit {

    public TankKit(Player player, MlgWars mlgWars) {
        super(player, mlgWars);
    }

    @Override
    public void initializeKit() {
        super.initializeKit();

        this.player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
        this.player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 2, false, false));

        this.player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)
                .setBaseValue(this.player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).getDefaultValue() - 5D);
    }

    @Override
    protected void onInteract(PlayerInteractEvent event) {

    }

    @Override
    public void run() {

    }
}
