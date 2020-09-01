package de.fel1x.teamcrimx.crimxlobby.cosmetics.gadgets;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.ICosmetic;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class FunGunGadget implements ICosmetic {

    @Override
    public String getCosmeticName() {
        return "§6FunGun";
    }

    @Override
    public String[] getCosmeticDescription() {
        return new String[]{
                "", "§eFun!", "§6Gun!", ""
        };
    }

    @Override
    public Material getCosmeticMaterial() {
        return Material.BLAZE_ROD;
    }

    @Override
    public Color getLeatherShoeColor() {
        return null;
    }

    @Override
    public Effect getWalkEffect() {
        return null;
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
        player.setMetadata("gadgetDelay", new FixedMetadataValue(CrimxLobby.getInstance(), 0));
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
        return true;
    }

    @Override
    public Material itemToDrop() {
        return null;
    }

    @Override
    public void updateInventory(Player player) {

        int slot = (player.hasPermission("crimxlobby.vip") ? 2 : 4);

        player.getInventory().setItem(slot, new ItemBuilder(Material.BLAZE_ROD)
                .setName("§8● " + this.getCosmeticName())
                .setLore(this.getCosmeticDescription())
                .addGlow()
                .toItemStack());
    }
}
