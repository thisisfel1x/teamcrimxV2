package de.fel1x.teamcrimx.crimxlobby.cosmetics.dropTrails;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.ICosmetic;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class SnowShoeTrail implements ICosmetic {

    @Override
    public String getCosmeticName() {
        return "§fSchneemann";
    }

    @Override
    public String[] getCosmeticDescription() {
        return new String[]{"", "§fBrrrr... kalt!", ""};
    }

    @Override
    public Material getCosmeticMaterial() {
        return Material.SNOW_BLOCK;
    }

    @Override
    public Color getLeatherShoeColor() {
        return Color.WHITE;
    }

    @Override
    public Particle getWalkEffect() {
        return null;
    }

    @Override
    public int getCosmeticCost() {
        return 5000;
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
    public boolean dropItem() {
        return false;
    }

    @Override
    public boolean playerBlock() {
        return true;
    }

    @Override
    public boolean armor() {
        return false;
    }

    @Override
    public boolean gadget() {
        return false;
    }

    @Override
    public Material itemToDrop() {
        return null;
    }

    @Override
    public void updateInventory(Player player) {
        player.getInventory().setBoots(new ItemBuilder(Material.LEATHER_BOOTS)
                .setName(this.getCosmeticName()).setLeatherArmorColor(this.getLeatherShoeColor()).toItemStack());
    }
}
