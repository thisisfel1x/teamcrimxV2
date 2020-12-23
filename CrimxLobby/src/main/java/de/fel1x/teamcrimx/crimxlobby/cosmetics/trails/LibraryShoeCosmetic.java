package de.fel1x.teamcrimx.crimxlobby.cosmetics.trails;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.CosmeticType;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.ICosmetic;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class LibraryShoeCosmetic implements ICosmetic {

    @Override
    public String getCosmeticName() {
        return "§fBibliothekar";
    }

    @Override
    public String[] getCosmeticDescription() {
        return new String[]{
                "", "§7Steigere deine IQ"
        };
    }

    @Override
    public Material getCosmeticMaterial() {
        return Material.LEATHER_BOOTS;
    }

    @Override
    public Color getLeatherShoeColor() {
        return Color.fromRGB(139, 69, 19);
    }

    @Override
    public Particle getWalkEffect() {
        return Particle.ENCHANTMENT_TABLE;
    }

    @Override
    public int getCosmeticCost() {
        return 1000;
    }

    @Override
    public int effectData() {
        return 0;
    }

    @Override
    public void startTrail(Player player) {
        CrimxLobby.getInstance().getData().getCosmetic().put(player.getUniqueId(), this);
        this.updateInventory(player);
    }

    @Override
    public CosmeticType cosmeticType() {
        return CosmeticType.TRAIL;
    }

    @Override
    public Material itemToDrop() {
        return null;
    }

    @Override
    public void updateInventory(Player player) {
        player.getInventory().setBoots(new ItemBuilder(this.getCosmeticMaterial())
                .setName(this.getCosmeticName()).setLeatherArmorColor(this.getLeatherShoeColor()).toItemStack());
    }
}
