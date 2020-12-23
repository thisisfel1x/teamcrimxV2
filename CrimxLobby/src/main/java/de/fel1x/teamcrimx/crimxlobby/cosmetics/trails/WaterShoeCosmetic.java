package de.fel1x.teamcrimx.crimxlobby.cosmetics.trails;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.CosmeticType;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.ICosmetic;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class WaterShoeCosmetic implements ICosmetic {

    @Override
    public String getCosmeticName() {
        return "§bWasserschuhe";
    }

    @Override
    public String[] getCosmeticDescription() {
        return new String[]{"", "§bPlitsch Platsch", ""};
    }

    @Override
    public Material getCosmeticMaterial() {
        return Material.LEATHER_BOOTS;
    }

    @Override
    public Color getLeatherShoeColor() {
        return Color.AQUA;
    }

    @Override
    public Particle getWalkEffect() {
        return Particle.WATER_BUBBLE;
    }

    @Override
    public int getCosmeticCost() {
        return 1000;
    }

    @Override
    public int effectData() {
        return 1;
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
