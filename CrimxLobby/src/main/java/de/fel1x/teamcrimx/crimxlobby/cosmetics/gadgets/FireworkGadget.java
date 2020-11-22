package de.fel1x.teamcrimx.crimxlobby.cosmetics.gadgets;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.CosmeticType;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.ICosmetic;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class FireworkGadget implements ICosmetic {

    @Override
    public String getCosmeticName() {
        return "§dFeuerwerk";
    }

    @Override
    public String[] getCosmeticDescription() {
        return new String[]{
                "", "§d§lFROHES NEUES", ""
        };
    }

    @Override
    public Material getCosmeticMaterial() {
        return Material.FIREWORK_ROCKET;
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
        return 4000;
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
    public CosmeticType cosmeticType() {
        return CosmeticType.GADGET;
    }

    @Override
    public Material itemToDrop() {
        return null;
    }

    @Override
    public void updateInventory(Player player) {
        int slot = (player.hasPermission("crimxlobby.vip") ? 2 : 4);

        player.getInventory().setItem(slot, new ItemBuilder(Material.STICK)
                .setName("§8● " + this.getCosmeticName())
                .setLore(this.getCosmeticDescription())
                .addGlow()
                .toItemStack());
    }
}
