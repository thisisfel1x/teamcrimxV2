package de.fel1x.teamcrimx.crimxlobby.cosmetics.pets.pet;

import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.CosmeticType;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.ICosmetic;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.pets.IPet;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Player;

public class ParrotPet implements ICosmetic, IPet {

    @Override
    public String getCosmeticName() {
        return "§aPapagei";
    }

    @Override
    public String[] getCosmeticDescription() {
        return new String[]{
                "", "§aAb in den Jungel mit dir!"
        };
    }

    @Override
    public Material getCosmeticMaterial() {
        return Material.COCOA_BEANS;
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
        return 1000;
    }

    @Override
    public int effectData() {
        return 0;
    }

    @Override
    public void startTrail(Player player) {
        Parrot parrot = (Parrot) player.getWorld()
                .spawnEntity(player.getLocation().clone().add(0, 0, 1), EntityType.PARROT);
        parrot.setCustomName("§aPapagei von " + this.getCustomNameByPlayer(player));
        parrot.setVariant(Parrot.Variant.BLUE);
        CrimxLobby.getInstance().getData().getPlayerPet().put(player.getUniqueId(), parrot);
    }

    @Override
    public CosmeticType cosmeticType() {
        return null;
    }

    @Override
    public Material itemToDrop() {
        return null;
    }

    @Override
    public void updateInventory(Player player) {

    }

    @Override
    public String getCustomName() {
        return null;
    }

    @Override
    public String getCustomNameByPlayer(Player player) {
        return null;
    }
}
