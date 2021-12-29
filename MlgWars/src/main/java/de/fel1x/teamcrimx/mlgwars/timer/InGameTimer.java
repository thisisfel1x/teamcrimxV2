package de.fel1x.teamcrimx.mlgwars.timer;

import de.fel1x.teamcrimx.crimxapi.utils.Actionbar;
import de.fel1x.teamcrimx.crimxapi.utils.Cuboid;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.kit.Kit;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Random;

public class InGameTimer implements ITimer {

    private final MlgWars mlgWars = MlgWars.getInstance();
    private final Random random = new Random();
    private final ItemStack dumpItem = new ItemBuilder(Material.GOLDEN_SWORD)
            .setName(Component.text("● ", NamedTextColor.DARK_GRAY)
                    .append(Component.text("Müll", NamedTextColor.GOLD))
                    .append(Component.text(" (umtauschen verboten!)", NamedTextColor.GRAY)))
            .setLore(Component.empty(), Component.empty(), Component.empty(), Component.empty(),
                    Component.empty(), Component.empty(), Component.empty(), Component.empty(),
                    Component.text("(kann Gift verursachen)",
                            TextColor.fromHexString("#00bb2d")).decorate(TextDecoration.ITALIC))
            .setUnbreakable()
            .toItemStack();

    private boolean running = false;
    private int taskId;
    private int gameTime = 0;

    @Override
    public void start() {
        if (!this.running) {
            this.running = true;
            this.mlgWars.getGamestateHandler().setGamestate(Gamestate.INGAME);

            this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.mlgWars, () -> {

                this.mlgWars.getGameType().gameTick();

                this.mlgWars.getData().getPlayers().forEach(player -> {
                    GamePlayer gamePlayer = this.mlgWars.getData().getGamePlayers().get(player.getUniqueId());
                    if (gamePlayer.isPlayer()) {

                        if (gamePlayer.isPlayer() && !gamePlayer.isActionbarOverridden()) {
                            gamePlayer.getMlgActionbar().sendActionbar(player, (Component) null);
                        }

                        if (player.getInventory().contains(this.dumpItem)) {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 40, 0, false, false));
                        }

                        if (this.mlgWars.getData().getPlayers().size() > 1) {
                            Player nearest = this.getClosestEntity(player, player.getLocation());
                            if (nearest != null) {
                                player.setCompassTarget(nearest.getLocation());
                            }
                        }

                        /*if (gamePlayer.getSelectedKit() == Kit.STINKER) {
                            for (Entity nearbyEntity : player.getNearbyEntities(5, 2, 5)) {
                                if (!(nearbyEntity instanceof Player)) continue;
                                Player player1 = (Player) nearbyEntity;
                                GamePlayer gamePlayer1 = new GamePlayer(player1);

                                if (gamePlayer1.isSpectator() || player.getUniqueId().equals(player1.getUniqueId()))
                                    continue;

                                if (gamePlayer1.getSelectedKit() != Kit.STINKER) {
                                    player1.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 3, 0, true, true));
                                    player1.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 3, 0, true, true));
                                }
                            }
                        } else if (gamePlayer.getSelectedKit() == Kit.KANGAROO) {
                            if (!this.mlgWars.getData().getKangarooTask().containsKey(player.getUniqueId())) {
                                int currentEssences = 0;
                                if (player.hasMetadata("essence")) {
                                    currentEssences = player.getMetadata("essence").get(0).asInt();
                                }
                                Actionbar.sendActionbar(player, "§6Känguru §8● §a" + currentEssences + " §7Essenzen übrig");
                            }
                        } */
                    }
                });

                this.gameTime++;

            }, 0L, 20L);
        }

    }

    @Override
    public void stop() {
        if (this.running) {
            Bukkit.getScheduler().cancelTask(this.taskId);
            this.running = false;
        }
    }

    private Player getClosestEntity(Player player, Location center) {
        Player closestEntity = null;
        double closestDistance = 0.0;

        for (Player foundPlayer : center.getWorld().getNearbyPlayers(center, 100, 100, 100)) {
            if (foundPlayer.getUniqueId().equals(player.getUniqueId())) {
                continue;
            }
            GamePlayer gamePlayer = this.mlgWars.getData().getGamePlayers().get(foundPlayer.getUniqueId());
            if(gamePlayer == null) {
                continue;
            }
            if(!gamePlayer.isPlayer()) {
                continue;
            }
            int teamId = gamePlayer.getPlayerMlgWarsTeamId();
            if(this.mlgWars.getData().getGameTeams().get(teamId).getAlivePlayers().contains(player)) {
                continue;
            }

            double distance = foundPlayer.getLocation().distanceSquared(center);
            if (closestEntity == null || distance < closestDistance) {
                closestDistance = distance;
                closestEntity = foundPlayer;
            }
        }
        return closestEntity;
    }
}
