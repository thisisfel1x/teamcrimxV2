package de.fel1x.teamcrimx.crimxlobby.cosmetics.heads;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.ICosmetic;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class FlowerHead implements ICosmetic {

    @Override
    public String getCosmeticName() {
        return "§dBlume";
    }

    @Override
    public String[] getCosmeticDescription() {
        return new String[] {
                "", "§dFLOWER POWER"
        };
    }

    @Override
    public Material getCosmeticMaterial() {
        return Material.PINK_TULIP;
    }

    @Override
    public Color getLeatherShoeColor() {
        return null;
    }

    @Override
    public Particle getWalkEffect() {
        return null;
    }

    @Override
    public int getCosmeticCost() {
        return 500;
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
        player.getInventory().setHelmet(new ItemBuilder(this.getCosmeticMaterial())
                .setName(this.getCosmeticName()).toItemStack());
    }
}
