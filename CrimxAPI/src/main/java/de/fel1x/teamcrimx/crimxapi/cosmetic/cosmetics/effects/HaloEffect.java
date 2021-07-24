package de.fel1x.teamcrimx.crimxapi.cosmetic.cosmetics.effects;

import de.fel1x.teamcrimx.crimxapi.cosmetic.CosmeticCategory;
import de.fel1x.teamcrimx.crimxapi.cosmetic.ICosmetic;
import de.fel1x.teamcrimx.crimxapi.cosmetic.IEffect;
import de.fel1x.teamcrimx.crimxapi.support.CrimxSpigotAPI;
import dev.esophose.playerparticles.api.PlayerParticlesAPI;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.data.OrdinaryColor;
import dev.esophose.playerparticles.styles.DefaultStyles;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class HaloEffect implements ICosmetic, IEffect {

    @Override
    public Component getDisplayName() {
        return Component.text("§eHalo");
    }

    @Override
    public Component[] getDescription() {
        return new Component[]{
                Component.text("§7Der Unschuldige :)"),
        };
    }

    @Override
    public Material getDisplayMaterial() {
        return Material.YELLOW_DYE;
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
    public void initializeCosmetic(Player player) {
        CrimxSpigotAPI.getInstance().getCosmeticTask().getActiveCosmetics().put(player.getUniqueId(), this);
        PlayerParticlesAPI.getInstance().addActivePlayerParticle(player, ParticleEffect.DUST, DefaultStyles.HALO, new OrdinaryColor(255, 255, 0));
    }

    @Override
    public void updateAfterTicks(long ticksToGo) {

    }

    @Override
    public void stopCosmetic(Player player) {
        CrimxSpigotAPI.getInstance().getCosmeticTask().getActiveCosmetics().remove(player.getUniqueId());
        PlayerParticlesAPI.getInstance().removeActivePlayerParticles(player, DefaultStyles.HALO);
    }

    @Override
    public Object calculateMath(Object[] args) {
        return null;
    }

    @Override
    public void displayParticles() {

    }

}
