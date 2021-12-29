package de.fel1x.teamcrimx.mlgwars.maphandler.gametype.types;

import de.fel1x.teamcrimx.crimxapi.utils.Cuboid;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.maphandler.ChestFiller;
import de.fel1x.teamcrimx.mlgwars.maphandler.gametype.GameType;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class TntMadnessGameType extends SoloGameType implements Listener {

    private int spawnTime;

    public TntMadnessGameType(MlgWars mlgWars) {
        super(mlgWars);
        this.mlgWars.getPluginManager().registerEvents(this, this.mlgWars);
    }

    @Override
    public String getGameTypeName() {
        return "TNT-Wahnsinn";
    }

    @Override
    public ArrayList<ItemStack> getTierOneItems() {
        return ChestFiller.getLaborItems();
    }

    @Override
    public ArrayList<ItemStack> getTierTwoItems() {
        return ChestFiller.getLaborItems();
    }

    @Override
    public void gameInit() {
        super.gameInit();
        for (Player player : this.mlgWars.getData().getPlayers()) {
            player.getInventory().setHelmet(new ItemBuilder(Material.CREEPER_HEAD, 1)
                    .addEnchant(Enchantment.PROTECTION_EXPLOSIONS, 1).toItemStack());
            player.getInventory().setChestplate(new ItemBuilder(Material.IRON_CHESTPLATE)
                    .addEnchant(Enchantment.PROTECTION_EXPLOSIONS, 1).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).setUnbreakable().toItemStack());
            player.getInventory().setLeggings(new ItemBuilder(Material.CHAINMAIL_LEGGINGS)
                    .addEnchant(Enchantment.PROTECTION_EXPLOSIONS, 1).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).setUnbreakable().toItemStack());
            player.getInventory().setBoots(new ItemBuilder(Material.IRON_BOOTS)
                    .addEnchant(Enchantment.PROTECTION_EXPLOSIONS, 1).addEnchant(Enchantment.PROTECTION_FALL, 2).setUnbreakable().toItemStack());
            player.getInventory().addItem(dev.triumphteam.gui.builder.item.ItemBuilder.from(Material.IRON_SWORD)
                    .enchant(Enchantment.DAMAGE_ALL).build());
            player.setGlowing(true);
        }
    }

    @Override
    public void gameTick() {
        super.gameTick();

        this.spawnTime++;

        if (this.spawnTime % 30 == 0) {
            Cuboid middleCuboid = this.mlgWars.getData().getMiddleRegion();
            Location center = middleCuboid.getCenter();

            for (int i = 0; i < 5; i++) {
                int x = this.random.nextInt(15) * (this.random.nextBoolean() ? 1 : -1);
                int z = this.random.nextInt(15) * (this.random.nextBoolean() ? 1 : -1);

                Block block = center.getWorld().getHighestBlockAt(x, z);

                while (block.getType() == Material.AIR) {
                    x = this.random.nextInt(15) * (this.random.nextBoolean() ? 1 : -1);
                    z = this.random.nextInt(15) * (this.random.nextBoolean() ? 1 : -1);

                    block = center.getWorld().getHighestBlockAt(x, z);
                }

                block.getWorld().spawnEntity(block.getLocation().clone().add(0, 2, 0), EntityType.CREEPER);
                block.getWorld().spawnEntity(block.getLocation().clone().add(0, 2, 0), EntityType.SILVERFISH);

            }

        }

        if (this.spawnTime % 60 == 0) {
            Cuboid middleCuboid = this.mlgWars.getData().getMiddleRegion();
            Location center = middleCuboid.getCenter();

            this.getCirclePoints(center.clone().add(0, 40, 0), 3, 5).forEach(block -> block.getWorld().spawnEntity(block.getBlock().getLocation(), EntityType.PRIMED_TNT));
            this.getCirclePoints(center.clone().add(0, 40, 0), 15, 30).forEach(block -> block.getWorld().spawnEntity(block.getBlock().getLocation(), EntityType.PRIMED_TNT));
        }
    }

    @EventHandler
    public void on(BlockBreakEvent event) {
        if(!this.validate(event.getPlayer())) {
            return;
        }
        event.getBlock().setType(Material.AIR);
        event.getBlock().getWorld().spawnEntity(event.getBlock().getLocation(), EntityType.PRIMED_TNT);
    }

    @EventHandler
    public void on(BlockPlaceEvent event) {
        if(!this.validate(event.getPlayer())) {
            return;
        }
        Block placedBlock = event.getBlockPlaced();
        if (placedBlock.getType() == Material.TNT) {
            if(event.getItemInHand().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(this.mlgWars, "instatnt"), PersistentDataType.STRING)) {
                placedBlock.setType(Material.AIR);
                TNTPrimed tntPrimed = (TNTPrimed) placedBlock.getWorld().spawnEntity(placedBlock.getLocation(), EntityType.PRIMED_TNT);
                tntPrimed.setFuseTicks(20);
            } else if(event.getItemInHand().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(this.mlgWars, "instaboost"), PersistentDataType.STRING)) {
                placedBlock.getWorld().spawnEntity(placedBlock.getLocation().clone().add(1, 0, 0), EntityType.PRIMED_TNT);
                placedBlock.getWorld().spawnEntity(placedBlock.getLocation().clone().subtract(1, 0, 0), EntityType.PRIMED_TNT);
                placedBlock.getWorld().spawnEntity(placedBlock.getLocation().clone().add(0, 0, 1), EntityType.PRIMED_TNT);
                placedBlock.getWorld().spawnEntity(placedBlock.getLocation().clone().subtract(0, 0, 1), EntityType.PRIMED_TNT);
            } if(event.getItemInHand().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(this.mlgWars, "velocitytnt"), PersistentDataType.STRING)) { {
                placedBlock.setType(Material.AIR);
                TNTPrimed tntPrimed = (TNTPrimed) placedBlock.getWorld().spawnEntity(placedBlock.getLocation(), EntityType.PRIMED_TNT);
                tntPrimed.setMetadata("velocity", new FixedMetadataValue(this.mlgWars, true));
                tntPrimed.setFuseTicks(20);
            }}
        }
    }

    @EventHandler
    public void on(ProjectileLaunchEvent event) {
        if(event.getEntity().getShooter() instanceof Player player) {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (event.getEntity() instanceof Arrow arrow) {
                if (!this.validate(player)) {
                    return;
                }
                if (itemStack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(this.mlgWars, "explodebow"), PersistentDataType.STRING)) {
                    arrow.setMetadata("explode", new FixedMetadataValue(this.mlgWars, true));
                } else if (itemStack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(this.mlgWars, "tntbow"), PersistentDataType.STRING)) {
                    Bukkit.getScheduler().runTaskLater(this.mlgWars, () -> {
                        TNTPrimed tntPrimed = (TNTPrimed) player.getWorld().spawnEntity(arrow.getLocation().clone().add(0, 1, 0), EntityType.PRIMED_TNT);
                        tntPrimed.setFuseTicks(10);
                        tntPrimed.setIsIncendiary(true);
                        tntPrimed.setTicksLived(5);
                        tntPrimed.setVelocity(arrow.getVelocity());
                        arrow.addPassenger(tntPrimed);
                    }, 3L);
                }
            } else if(event.getEntity() instanceof Snowball snowball) {
                if(itemStack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(this.mlgWars, "snowtnt"), PersistentDataType.STRING)) {
                    TNTPrimed tntPrimed = (TNTPrimed) player.getWorld().spawnEntity(snowball.getLocation().clone().add(0, 2, 0), EntityType.PRIMED_TNT);
                    tntPrimed.setFuseTicks(20);
                    tntPrimed.setIsIncendiary(true);
                    tntPrimed.setTicksLived(5);
                    tntPrimed.setVelocity(snowball.getVelocity());
                }
            }
        }
    }

    @EventHandler
    public void on(ProjectileHitEvent event) {
        Gamestate gamestate = this.mlgWars.getGamestateHandler().getGamestate();
        if(gamestate != Gamestate.PREGAME && gamestate != Gamestate.INGAME) {
            return;
        }
        if(event.getEntity() instanceof Arrow arrow) {
            if(arrow.hasMetadata("explode")) {
                arrow.getWorld().createExplosion(arrow, 3.5f);
            }
        }
    }

    @EventHandler
    public void on(EntityExplodeEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof TNTPrimed) {
            if (entity.hasMetadata("velocity")) {
                for (Entity nearbyEntity : entity.getNearbyEntities(3, 3, 3)) {
                    nearbyEntity.setVelocity(nearbyEntity.getVelocity().multiply(5D).setY(2D));
                }
            }
        }
    }

    private boolean validate(Player player) {
        Gamestate gamestate = this.mlgWars.getGamestateHandler().getGamestate();
        if(gamestate != Gamestate.PREGAME && gamestate != Gamestate.INGAME) {
            return false;
        }
        GamePlayer gamePlayer = this.mlgWars.getData().getGamePlayers().get(player.getUniqueId());
        return gamePlayer.isPlayer();
    }

    // TODO: move
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
