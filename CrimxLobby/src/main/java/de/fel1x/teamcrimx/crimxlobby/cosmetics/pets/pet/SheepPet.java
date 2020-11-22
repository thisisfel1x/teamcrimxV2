package de.fel1x.teamcrimx.crimxlobby.cosmetics.pets.pet;

import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.CosmeticType;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.ICosmetic;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.pets.IPet;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;

public class SheepPet implements ICosmetic, IPet {

    @Override
    public String getCosmeticName() {
        return "Schaf";
    }

    @Override
    public String[] getCosmeticDescription() {
        return new String[] {
                "", "§fMäh? Näh!"
        };
    }

    @Override
    public Material getCosmeticMaterial() {
        return Material.SHEEP_SPAWN_EGG;
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
        Sheep sheep = (Sheep) player.getWorld()
                .spawnEntity(player.getLocation().clone().add(0, 0, 1), EntityType.SHEEP);
        CrimxLobby.getInstance().getData().getPlayerPet().put(player.getUniqueId(), sheep);
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
        return player.getDisplayName();
    }
}
