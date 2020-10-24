package de.fel1x.teamcrimx.crimxlobby.cosmetics.trails;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.ICosmetic;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class WitchShoeCosmetic implements ICosmetic {

    @Override
    public String getCosmeticName() {
        return "§5Magieschuhe";
    }

    @Override
    public String[] getCosmeticDescription() {
        return new String[]{"", "§5Hex Hex", ""};
    }

    @Override
    public Material getCosmeticMaterial() {
        return Material.LEATHER_BOOTS;
    }

    @Override
    public Color getLeatherShoeColor() {
        return Color.PURPLE;
    }

    @Override
    public EntityEffect getWalkEffect() {
        return EntityEffect.WITCH_MAGIC;
    }

    @Override
    public int getCosmeticCost() {
        return 2500;
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
    public boolean dropItem() {
        return false;
    }

    @Override
    public boolean playerBlock() {
        return false;
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
        player.getInventory().setBoots(new ItemBuilder(this.getCosmeticMaterial())
                .setName(this.getCosmeticName()).setLeatherArmorColor(this.getLeatherShoeColor()).toItemStack());
    }
}
