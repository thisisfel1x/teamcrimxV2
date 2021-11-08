package de.fel1x.teamcrimx.crimxapi.cosmetic.cosmetics.effects;

import de.fel1x.teamcrimx.crimxapi.cosmetic.CosmeticCategory;
import de.fel1x.teamcrimx.crimxapi.cosmetic.EffectCosmetic;
import de.fel1x.teamcrimx.crimxapi.support.CrimxSpigotAPI;
import dev.esophose.playerparticles.api.PlayerParticlesAPI;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.styles.DefaultStyles;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class RingEffect extends EffectCosmetic {

    public RingEffect(Player player, CrimxSpigotAPI crimxSpigotAPI) {
        super(player, crimxSpigotAPI);
    }

    @Override
    public Component getDisplayName() {
        return Component.text("§eRinge");
    }

    @Override
    public Component[] getDescription() {
        return new Component[]{
                Component.text("§7Ringe machen"),
                Component.text("§7auch ohne Pfeife")
        };
    }

    @Override
    public Material getDisplayMaterial() {
        return Material.LEAD;
    }

    @Override
    public CosmeticCategory getCosmeticCategory() {
        return CosmeticCategory.EFFECT;
    }

    @Override
    public int getCost() {
        return 400;
    }

    @Override
    public double maxDiscount() {
        return 0.2;
    }

    @Override
    public void startCosmetic(Player player) {
        super.startCosmetic(player);
        PlayerParticlesAPI.getInstance().addActivePlayerParticle(player, ParticleEffect.TOTEM_OF_UNDYING, DefaultStyles.RINGS);
    }

    @Override
    public void stopCosmetic(Player player) {
        super.stopCosmetic(player);
        PlayerParticlesAPI.getInstance().removeActivePlayerParticles(player, DefaultStyles.RINGS);
    }

    @Override
    public void run() {

    }
}
