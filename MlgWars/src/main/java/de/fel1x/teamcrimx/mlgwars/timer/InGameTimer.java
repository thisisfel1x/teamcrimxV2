package de.fel1x.teamcrimx.mlgwars.timer;

import de.fel1x.teamcrimx.crimxapi.utils.Actionbar;
import de.fel1x.teamcrimx.crimxapi.utils.Cuboid;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.kit.Kit;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
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
            .setName("§8● §6Müll §7(umtauschen verboten!)")
            .setLore("", "", "", "", "", "", "", "", "§7§o(kann Gift verursachen)")
            .toItemStack();
    private boolean running = false;
    private int taskId;
    private int gameTime = 0;
    private int spawnTime = 0;

    @Override
    public void start() {

        if (!this.running) {
            this.running = true;
            this.mlgWars.getGamestateHandler().setGamestate(Gamestate.INGAME);

            if(this.mlgWars.isLabor()) {
                this.mlgWars.getData().getPlayers().forEach(player -> player.setGlowing(true));
            }

            this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.mlgWars, () -> {

                if (this.mlgWars.isLabor()) {
                    spawnTime++;

                    if (spawnTime % 30 == 0) {
                        Cuboid middleCuboid = this.mlgWars.getData().getMiddleRegion();
                        Location center = middleCuboid.getCenter();

                        for (int i = 0; i < 5; i++) {
                            int x = random.nextInt(15) * (random.nextBoolean() ? 1 : -1);
                            int z = random.nextInt(15) * (random.nextBoolean() ? 1 : -1);

                            Block block = center.getWorld().getHighestBlockAt(x, z);

                            while (block.getType() == Material.AIR) {
                                x = random.nextInt(15) * (random.nextBoolean() ? 1 : -1);
                                z = random.nextInt(15) * (random.nextBoolean() ? 1 : -1);

                                block = center.getWorld().getHighestBlockAt(x, z);
                            }

                            block.getWorld().spawnEntity(block.getLocation().clone().add(0, 2, 0), EntityType.CREEPER);
                            block.getWorld().spawnEntity(block.getLocation().clone().add(0, 2, 0), EntityType.SILVERFISH);

                        }

                    }

                    if (spawnTime % 60 == 0) {
                        Cuboid middleCuboid = this.mlgWars.getData().getMiddleRegion();
                        Location center = middleCuboid.getCenter();

                        this.getCirclePoints(center.clone().add(0, 40, 0), 3, 5).forEach(block -> block.getWorld().spawnEntity(block.getBlock().getLocation(), EntityType.PRIMED_TNT));
                        this.getCirclePoints(center.clone().add(0, 40, 0), 15, 30).forEach(block -> block.getWorld().spawnEntity(block.getBlock().getLocation(), EntityType.PRIMED_TNT));
                    }

                }

                this.mlgWars.getData().getPlayers().forEach(player -> {
                    GamePlayer gamePlayer = new GamePlayer(player);
                    if (gamePlayer.isPlayer()) {

                        if (player.hasMetadata("team")) {
                            int team = player.getMetadata("team").get(0).asInt() + 1;
                            Actionbar.sendActionbar(player, "§7Team §a#" + team);
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

                        if (gamePlayer.getSelectedKit() == Kit.STINKER) {
                            for (Entity nearbyEntity : player.getNearbyEntities(5, 2, 5)) {
                                if (!(nearbyEntity instanceof Player)) continue;
                                Player player1 = (Player) nearbyEntity;
                                GamePlayer gamePlayer1 = new GamePlayer(player1);

                                if (gamePlayer1.isSpectator() || player.getUniqueId().equals(player1.getUniqueId()))
                                    continue;

                                if (gamePlayer1.getSelectedKit() != Kit.STINKER) {
                                    player1.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 3, 0, true, true), true);
                                    player1.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 3, 0, true, true), true);
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
                        }
                    }
                });

                this.gameTime++;

            }, 0L, 20L);
        }

    }

    @Override
    public void stop() {
        if (this.running) {
            Bukkit.getScheduler().cancelTask(taskId);
            this.running = false;
        }
    }

    private Player getClosestEntity(Player owner, Location center) {
        Player closestEntity = null;
        double closestDistance = 0.0;

        for (Entity entity : center.getWorld().getNearbyEntities(center, 200, 200, 200)) {
            if (!(entity instanceof Player)) continue;
            if (entity.getUniqueId().equals(owner.getUniqueId())) continue;

            double distance = entity.getLocation().distanceSquared(center);
            if (closestEntity == null || distance < closestDistance) {
                closestDistance = distance;
                closestEntity = (Player) entity;
            }
        }
        return closestEntity;
    }

    public ArrayList<Location> getCirclePoints(Location center, double radius, int amount) {

        ArrayList<Location> points = new ArrayList<>();

        double increment = (2 * Math.PI) / amount;
        for (int i = 0; i < amount; i++) {

            double angle = i * increment;
            double x = center.getX() + (radius * Math.cos(angle));
            double z = center.getZ() + (radius * Math.sin(angle));

            Location current = new Location(center.getWorld(), x, center.getY(), z);
            points.add(current);

        }

        return points;

    }

}
