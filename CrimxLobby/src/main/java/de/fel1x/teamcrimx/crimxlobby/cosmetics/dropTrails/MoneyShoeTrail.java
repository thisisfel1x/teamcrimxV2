package de.fel1x.teamcrimx.crimxlobby.cosmetics.dropTrails;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.CosmeticType;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.ICosmetic;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class MoneyShoeTrail implements ICosmetic {

    @Override
    public String getCosmeticName() {
        return "§eParatrail";
    }

    @Override
    public String[] getCosmeticDescription() {
        return new String[]{"", "§a§l€€€ HERE COMES THE MONEY €€€"};
    }

    @Override
    public Material getCosmeticMaterial() {
        return Material.DIAMOND;
    }

    @Override
    public Color getLeatherShoeColor() {
        return Color.GREEN;
    }

    @Override
    public Particle getWalkEffect() {
        return null;
    }

    @Override
    public int getCosmeticCost() {
        return 6000;
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
        return CosmeticType.DROP_TRAIL;
    }

    @Override
    public Material itemToDrop() {
        return Material.DIAMOND;
    }

    @Override
    public void updateInventory(Player player) {
        player.getInventory().setBoots(new ItemBuilder(this.getCosmeticMaterial())
                .setName(this.getCosmeticName()).setLeatherArmorColor(this.getLeatherShoeColor()).toItemStack());
    }
}
