package de.fel1x.teamcrimx.mlgwars.timer;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class InGameTimer implements ITimer {

    private final MlgWars mlgWars = MlgWars.getInstance();

    private boolean running = false;
    private int taskId;

    private int gameTime = 0;

    private final ItemStack dumpItem = new ItemBuilder(Material.GOLD_SWORD)
            .setName("§8● §6Müll §7(umtauschen verboten!)")
            .setLore("", "", "", "", "", "", "", "", "§7§o(kann Gift verursachen)")
            .toItemStack();

    @Override
    public void start() {

        if(!this.running) {
            this.running = true;
            this.mlgWars.getGamestateHandler().setGamestate(Gamestate.INGAME);

            this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.mlgWars, () -> {

                this.mlgWars.getData().getPlayers().forEach(player -> {
                    if(player.getInventory().contains(this.dumpItem)) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 40, 0, false, false));
                    }
                });

                this.gameTime++;

            }, 0L, 20L);
        }

    }

    @Override
    public void stop() {
        if(this.running) {
            Bukkit.getScheduler().cancelTask(taskId);
            this.running = false;
        }
    }
}
