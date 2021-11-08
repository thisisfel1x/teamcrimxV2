package de.fel1x.teamcrimx.mlgwars.kit.rework.kits;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.kit.rework.Kit;
import de.fel1x.teamcrimx.mlgwars.kit.rework.KitRegistry;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class DumpKit extends Kit {

    private int counter;

    public DumpKit(Player player, MlgWars mlgWars) {
        super(player, mlgWars);
    }

    @Override
    public void initializeKit() {
        super.initializeKit();

        ArrayList<Material> mats = MlgWars.getInstance().getAllMaterials();

        for (int i = 0; i < 40; i++) {
            Material selectedMat = mats.get(this.random.nextInt(mats.size()));
            int amount = (selectedMat.getMaxStackSize() < 0) ? 1 : this.random.nextInt(selectedMat.getMaxStackSize());

            ItemStack toSet = new ItemBuilder(selectedMat, (amount <= 0 ? 1 : amount)).toItemStack();

            this.player.getInventory().setItem(i, toSet);
            mats.remove(selectedMat);
        }

        this.runTaskTimer(this.mlgWars, 0L, 20L);
    }

    @Override
    protected void onInteract(PlayerInteractEvent event) {

    }

    @Override
    public void run() {
        if(this.counter % 20 != 0) {
            return;
        }

        for (Player nearbyPlayer : this.player.getWorld().getNearbyPlayers(this.player.getLocation(), 5, 2, 5)) {
            GamePlayer gamePlayer = this.mlgWars.getData().getGamePlayers().get(nearbyPlayer.getUniqueId());

            if (gamePlayer.isSpectator() || this.player.getUniqueId().equals(nearbyPlayer.getUniqueId())
                    || gamePlayer.getPlayerMlgWarsTeamId() == this.gamePlayer.getPlayerMlgWarsTeamId()) {
                continue;
            }

            if (gamePlayer.getSelectedKit() != KitRegistry.DUMP) {
                ItemStack[] inventoryContents = this.player.getInventory().getContents();
                Collections.shuffle(Arrays.asList(inventoryContents));

                this.player.getInventory().setContents(inventoryContents);

                this.player.dropItem(true);

                this.player.sendMessage(this.mlgWars.getPrefix() + "§7Dein Inventar wurde vertauscht");
                this.player.playSound(this.player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0.5f);
            }
        }

        this.counter++;
    }
}
