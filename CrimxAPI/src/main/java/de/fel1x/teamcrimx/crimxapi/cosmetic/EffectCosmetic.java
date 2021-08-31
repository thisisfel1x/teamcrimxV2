package de.fel1x.teamcrimx.crimxapi.cosmetic;

import de.fel1x.teamcrimx.crimxapi.support.CrimxSpigotAPI;
import org.bukkit.entity.Player;

public abstract class EffectCosmetic extends BaseCosmetic {

    public EffectCosmetic(Player player, CrimxSpigotAPI crimxSpigotAPI) {
        super(player, crimxSpigotAPI);
    }
}
