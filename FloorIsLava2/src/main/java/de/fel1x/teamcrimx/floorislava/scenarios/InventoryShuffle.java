package de.fel1x.teamcrimx.floorislava.scenarios;

import de.fel1x.teamcrimx.floorislava.Data;
import de.fel1x.teamcrimx.floorislava.FloorIsLava;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;

public class InventoryShuffle implements ILavaScenario {
    private final FloorIsLava floorIsLava = FloorIsLava.getInstance();

    private final Data data = this.floorIsLava.getData();

    public void execute() {
        this.data.getPlayers().forEach(player -> {
            ItemStack[] inventoryContents = player.getInventory().getContents();
            Collections.shuffle(Arrays.asList(inventoryContents));
            player.getInventory().setContents(inventoryContents);
            player.sendMessage(this.floorIsLava.getPrefix() + "§7Dein Inventar wurde vertauscht");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 0.5F);
        });
    }

    public String getName() {
        return "Inventory Shuffle";
    }

    public Material getDisplayMaterial() {
        return Material.REDSTONE;
    }
}
