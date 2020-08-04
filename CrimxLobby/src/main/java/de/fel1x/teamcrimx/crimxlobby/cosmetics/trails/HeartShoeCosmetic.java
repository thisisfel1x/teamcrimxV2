package de.fel1x.teamcrimx.crimxlobby.cosmetics.trails;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.ICosmetic;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class HeartShoeCosmetic implements ICosmetic {

    @Override
    public String getCosmeticName() {
        return "§cHerzentrail";
    }

    @Override
    public String[] getCosmeticDescription() {
        return new String[] {
                "", "§7Zeige deine §5Liebe, §7und sei glücklich (#nohomo)", ""
        };
    }

    @Override
    public Material getCosmeticMaterial() {
        return Material.LEATHER_BOOTS;
    }

    @Override
    public Color getLeatherShoeColor() {
        return Color.RED;
    }

    @Override
    public Effect getWalkEffect() {
        return Effect.HEART;
    }

    @Override
    public int getCosmeticCost() {
        return 2500;
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
        player.getInventory().setBoots(new ItemBuilder(this.getCosmeticMaterial())
                .setName(this.getCosmeticName()).setLeatherArmorColor(this.getLeatherShoeColor()).toItemStack());
    }

}
