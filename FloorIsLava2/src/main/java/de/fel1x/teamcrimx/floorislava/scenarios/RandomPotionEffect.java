package de.fel1x.teamcrimx.floorislava.scenarios;

import de.fel1x.teamcrimx.floorislava.Data;
import de.fel1x.teamcrimx.floorislava.FloorIsLava;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class RandomPotionEffect implements ILavaScenario {
    private final FloorIsLava floorIsLava = FloorIsLava.getInstance();

    private final Data data = this.floorIsLava.getData();

    private final Random random = new Random();

    public void execute() {
        PotionEffectType[] potionEffectTypes = PotionEffectType.values();
        this.data.getPlayers().forEach(player -> {
            player.addPotionEffect(new PotionEffect(potionEffectTypes[this.random.nextInt(potionEffectTypes.length)], (this.random.nextInt(30) + 45) * 20, this.random.nextInt(2)));
            player.sendMessage(this.floorIsLava.getPrefix() + "§7Du hast einen §ezufälligen Effekt §7erhalten!");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 0.5F);
        });
    }

    public String getName() {
        return "Random Potion Effect";
    }

    public Material getDisplayMaterial() {
        return Material.POTION;
    }
}
