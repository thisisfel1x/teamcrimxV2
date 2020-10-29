package de.fel1x.teamcrimx.crimxlobby.cosmetics.armor;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.ICosmetic;
import org.bukkit.*;
import org.bukkit.entity.Player;

public class RainbowArmor implements ICosmetic {

    @Override
    public String getCosmeticName() {
        return "§cR§aG§1B §7Armor";
    }

    @Override
    public String[] getCosmeticDescription() {
        return new String[]{"", "§7Umso mehr RGB", "§7umso mehr FPS & Kills", ""};
    }

    @Override
    public Material getCosmeticMaterial() {
        return Material.GLOWSTONE_DUST;
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
        return 10000;
    }

    @Override
    public int effectData() {
        return 0;
    }

    @Override
    public void startTrail(Player player) {
        CrimxLobby.getInstance().getData().getCosmetic().remove(player.getUniqueId());
        CrimxLobby.getInstance().getData().getHueMap().put(player.getUniqueId(), 0.0f);
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
    }

    public boolean rgbArmor() {
        return true;
    }

    public float handleColor(float hue, float speed) {
        hue += speed;

        if (hue >= 1.0f) {
            hue = 0;
        }

        return hue;
    }

    public java.awt.Color getRGB(float hue) {
        return java.awt.Color.getHSBColor(hue, 1f, 1f);
    }

    public void setArmor(Player player, float hue, float gradientSpeed) {

        ItemBuilder helmet = new ItemBuilder(Material.LEATHER_HELMET);
        ItemBuilder chestplate = new ItemBuilder(Material.LEATHER_CHESTPLATE);
        ItemBuilder leggins = new ItemBuilder(Material.LEATHER_LEGGINGS);
        ItemBuilder boots = new ItemBuilder(Material.LEATHER_BOOTS);

        helmet.setLeatherArmorColor(Color.fromBGR(this.getRGB(hue).getBlue(), this.getRGB(hue).getGreen(), this.getRGB(hue).getRed()));
        this.handleColor(hue, gradientSpeed);
        chestplate.setLeatherArmorColor(Color.fromBGR(this.getRGB(hue).getBlue(), this.getRGB(hue).getGreen(), this.getRGB(hue).getRed()));
        this.handleColor(hue, gradientSpeed);
        leggins.setLeatherArmorColor(Color.fromBGR(this.getRGB(hue).getBlue(), this.getRGB(hue).getGreen(), this.getRGB(hue).getRed()));
        this.handleColor(hue, gradientSpeed);
        boots.setLeatherArmorColor(Color.fromBGR(this.getRGB(hue).getBlue(), this.getRGB(hue).getGreen(), this.getRGB(hue).getRed()));

        player.getInventory().setHelmet(helmet.toItemStack());
        player.getInventory().setChestplate(chestplate.toItemStack());
        player.getInventory().setLeggings(leggins.toItemStack());
        player.getInventory().setBoots(boots.toItemStack());

    }

}
