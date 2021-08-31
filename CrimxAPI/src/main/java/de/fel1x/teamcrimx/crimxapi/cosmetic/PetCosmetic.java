package de.fel1x.teamcrimx.crimxapi.cosmetic;

import de.fel1x.teamcrimx.crimxapi.support.CrimxSpigotAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class PetCosmetic extends BaseCosmetic {

    public PetCosmetic(Player player, CrimxSpigotAPI crimxSpigotAPI) {
        super(player, crimxSpigotAPI);
    }

    public UUID owner() {
        return this.player.getUniqueId();
    }

    public Component customName() {
        return Component.text("FEHLER", NamedTextColor.DARK_RED);
    }

}
