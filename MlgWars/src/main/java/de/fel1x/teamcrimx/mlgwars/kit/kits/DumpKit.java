package de.fel1x.teamcrimx.mlgwars.kit.kits;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.kit.IKit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class DumpKit implements IKit {

    @Override
    public String getKitName() {
        return "Pure Dummheit";
    }

    @Override
    public String[] getKitDescription() {
        return new String[]{
                "§7Wer mit diesem Kit spielt", "§7erhält automatisch Dummheit Stufe 1000", "", "§6[für Pilzkuuh]"
        };
    }

    @Override
    public int getKitCost() {
        return 5000;
    }

    @Override
    public Material getKitMaterial() {
        return Material.BREAD;
    }

    @Override
    public void setKitInventory(Player player) {
        Random random = new Random();
        ArrayList<Material> mats = MlgWars.getInstance().getAllMaterials();

        for (int i = 0; i < 40; i++) {

            Material selectedMat = mats.get(random.nextInt(mats.size()));
            int amount = (selectedMat.getMaxStackSize() < 0) ? 1 : random.nextInt(selectedMat.getMaxStackSize());

            ItemStack toSet = new ItemBuilder(selectedMat, (amount <= 0 ? 1 : amount)).toItemStack();

            player.getInventory().setItem(i, toSet);
            mats.remove(selectedMat);

        }

    }
}
