package de.fel1x.bingo.listener.player;

import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.gamehandler.Gamestate;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class CraftListener implements Listener {

    private final Bingo bingo;

    public CraftListener(Bingo bingo) {
        this.bingo = bingo;
        this.bingo.getPluginManager().registerEvents(this, this.bingo);
    }

    @EventHandler
    public void on(PrepareItemCraftEvent event) {

        Gamestate gamestate = this.bingo.getGamestateHandler().getGamestate();

        if(gamestate == Gamestate.INGAME) {
            if(event.isRepair()) {
                return;
            }

            if(this.bingo.getData().isAdvancedRandomizer()) {
                if(event.getRecipe() == null) {
                    return;
                }

                ItemStack defaultItemStack = event.getRecipe().getResult();
                ItemStack replace = new ItemStack(this.bingo.getData().getRandomizedBlocks().getOrDefault(defaultItemStack.getType(),
                        defaultItemStack.getType()), defaultItemStack.getAmount());

                event.getInventory().setResult(replace);
            }
        }
    }
}
