package de.fel1x.bingo.scenarios;

import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.Data;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;

public class InventoryShuffle implements IBingoScenario {

    Bingo bingo = Bingo.getInstance();
    Data data = this.bingo.getData();

    @Override
    public void execute() {

        this.data.getPlayers().forEach(player -> {

            ItemStack[] inventoryContents = player.getInventory().getContents();
            Collections.shuffle(Arrays.asList(inventoryContents));

            player.getInventory().setContents(inventoryContents);

            player.sendMessage(this.bingo.getPrefix() + "§7Dein Inventar wurde vertauscht");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0.5f);

        });

    }

    @Override
    public String getName() {
        return "Inventory Shuffle";
    }

    @Override
    public Material getDisplayMaterial() {
        return Material.REDSTONE;
    }
}
