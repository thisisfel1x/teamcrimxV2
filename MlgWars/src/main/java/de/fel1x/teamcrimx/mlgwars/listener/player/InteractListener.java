package de.fel1x.teamcrimx.mlgwars.listener.player;

import de.fel1x.teamcrimx.crimxapi.utils.Actionbar;
import de.fel1x.teamcrimx.crimxapi.utils.Cuboid;
import de.fel1x.teamcrimx.crimxapi.utils.ProgressBar;
import de.fel1x.teamcrimx.mlgwars.Data;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.inventories.ForcemapInventory;
import de.fel1x.teamcrimx.mlgwars.inventories.KitInventory;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import de.fel1x.teamcrimx.mlgwars.utils.Tornado;
import de.fel1x.teamcrimx.mlgwars.utils.entites.CustomZombie;
import de.fel1x.teamcrimx.mlgwars.utils.entites.ZombieEquipment;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class InteractListener implements Listener {

    private final MlgWars mlgWars;
    private final Data data;
    private final Set<Material> transparent = new HashSet<>();

    private int count;

    private final Random random = new Random();
    private final ZombieEquipment zombieEquipment = new ZombieEquipment();
    
    public InteractListener(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
        this.data = this.mlgWars.getData();
        this.mlgWars.getPluginManager().registerEvents(this, this.mlgWars);

        this.transparent.add(Material.AIR);
    }

    @EventHandler
    public void on(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        GamePlayer gamePlayer = new GamePlayer(player);

        Material interactedMaterial = event.getMaterial();

        Gamestate gamestate = this.mlgWars.getGamestateHandler().getGamestate();

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            if (gamestate == Gamestate.IDLE || gamestate == Gamestate.LOBBY) {
                if (event.hasItem()) {
                    if (event.getMaterial() == Material.STORAGE_MINECART) {
                        player.playSound(player.getLocation(), Sound.CHEST_OPEN, 2f, 0.75f);
                        KitInventory.KIT_OVERVIEW_INVENTORY.open(player);
                    } else if (event.getMaterial() == Material.REDSTONE_TORCH_ON) {
                        ForcemapInventory.FORCEMAP_INVENTORY.open(player);
                    } else {
                        event.setCancelled(true);
                    }
                }

            } else if (gamestate == Gamestate.DELAY || gamestate == Gamestate.ENDING) {
                event.setCancelled(true);
            } else {
                if (gamePlayer.isSpectator()) {
                    event.setCancelled(true);
                    return;
                }
                if (event.hasItem()) {
                    if(event.getMaterial() == Material.MUSHROOM_SOUP) {
                        if(player.getHealth() < 19) {
                            event.getItem().setType(Material.BOWL);

                            double health;
                            if(player.getHealth() + 3.5 >= 20) {
                                health = 20;
                            } else {
                                health = player.getHealth() + 3.5;
                            }

                            player.setHealth(health);
                            return;
                        }
                    }

                    switch (gamePlayer.getSelectedKit()) {
                        case EXPLODER:
                            if (interactedMaterial == Material.LEVER) {
                                for (Block block : this.mlgWars.getData().getPlacedExploderTnt().get(player.getUniqueId())) {
                                    if (block.getType() != Material.TNT) continue;
                                    if (block.hasMetadata("owner")) {
                                        String owner = block.getMetadata("owner").get(0).asString();
                                        if (owner.equalsIgnoreCase(player.getName())) {
                                            block.setType(Material.AIR);

                                            TNTPrimed tnt = player.getWorld().spawn(block.getLocation(), TNTPrimed.class);
                                            tnt.setFuseTicks(0);
                                        }
                                    }
                                }
                            }
                            break;
                        case ASTRONAUT:
                            if (interactedMaterial != Material.FIREWORK) {
                                return;
                            }

                            this.removeItem(player);

                            Firework firework = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
                            FireworkMeta fireworkMeta = firework.getFireworkMeta();

                            fireworkMeta.setPower(3);
                            fireworkMeta.addEffect(FireworkEffect.builder().withColor(Color.fromBGR(random.nextInt(255),
                                    random.nextInt(255), random.nextInt(255))).flicker(true).trail(true).build());
                            firework.setFireworkMeta(fireworkMeta);
                            firework.setPassenger(player);
                            break;
                        case SAVER:
                            if (interactedMaterial != Material.BLAZE_ROD) {
                                return;
                            }

                            this.removeItem(player);

                            int delay = 0;

                            if (player.getLocation().getY() < 0) {
                                player.setVelocity(player.getVelocity().setY(4.5D));
                                delay = 40;
                            }

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    placeCylinder(player.getLocation().clone().subtract(0, 10, 0),
                                            Material.SLIME_BLOCK, 2);
                                    player.sendMessage(mlgWars.getPrefix() + "§7Die Rettungsplattform §cverschwindet " +
                                            "§7in §e30 Sekunden");
                                }
                            }.runTaskLater(this.mlgWars, delay);

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    placeCylinder(player.getLocation().clone().subtract(0, 10, 0),
                                            Material.AIR, 2);
                                }
                            }.runTaskLater(this.mlgWars, delay + (20 * 30));

                            break;
                        case THROWER:
                            if (gamestate == Gamestate.INGAME) {
                                switch (interactedMaterial) {
                                    case TNT:
                                        this.removeItem(player);
                                        TNTPrimed tnt = player.getWorld().spawn(player.getEyeLocation(), TNTPrimed.class);
                                        tnt.setVelocity(player.getEyeLocation().getDirection().multiply(1.75).setY(0.25D));
                                        tnt.setFuseTicks(50);
                                        break;
                                    case SKULL_ITEM:
                                        this.removeItem(player);
                                        player.launchProjectile(WitherSkull.class);
                                        break;
                                    case FIREBALL:
                                        this.removeItem(player);
                                        player.launchProjectile(Fireball.class);
                                        break;
                                }
                            }
                            break;
                        case THOR:
                            if (interactedMaterial != Material.GOLD_AXE) {
                                return;
                            }

                            Block targetBlock = player.getTargetBlock(this.transparent, 100);

                            if (player.hasMetadata("thor")) {
                                long nextUse = player.getMetadata("thor").get(0).asLong();
                                if (nextUse > System.currentTimeMillis()) {
                                    player.sendMessage(this.mlgWars.getPrefix() + "§7Bitte warte einen Moment bis du Thors Hammer wieder nutzen kannst!");
                                    return;
                                }
                            }

                            targetBlock.getWorld().strikeLightning(targetBlock.getLocation());
                            targetBlock.getWorld().getNearbyEntities(targetBlock.getLocation(), 3, 3, 3).forEach(entity -> {
                                if (entity instanceof Player) {
                                    ((Player) entity).setLastDamage(3D);
                                }
                            });

                            player.setMetadata("thor", new FixedMetadataValue(this.mlgWars, System.currentTimeMillis() + (1000 * 10)));
                            player.setMetadata("thorcount", new FixedMetadataValue(this.mlgWars, 0));

                            this.data.getThorTask().put(player.getUniqueId(), new BukkitRunnable() {
                                @Override
                                public void run() {

                                    count = player.getMetadata("thorcount").get(0).asInt();

                                    Actionbar.sendActiobar(player, "§6Thor §8● " + ProgressBar.getProgressBar(count, 10, 10,
                                            '█', ChatColor.GREEN, ChatColor.DARK_GRAY));

                                    if (count == 10) {
                                        data.getThorTask().get(player.getUniqueId()).cancel();
                                        data.getThorTask().remove(player.getUniqueId());
                                        Actionbar.sendActiobar(player, "§6Thor §8● §7Hammer wieder §abereit!");
                                    } else {
                                        player.setMetadata("thorcount", new FixedMetadataValue(mlgWars, count + 1));
                                    }
                                }
                            });

                            this.data.getThorTask().get(player.getUniqueId()).runTaskTimer(this.mlgWars, 0L, 20L);

                            break;
                        case BOAT_GLIDER:
                            if (interactedMaterial != Material.BOAT) {
                                return;
                            }

                            this.removeItem(player);

                            Boat boat = (Boat) player.getWorld().spawnEntity(player.getLocation(), EntityType.BOAT);
                            boat.setPassenger(player);
                            boat.setVelocity(boat.getVelocity().setY(7.5D));
                            break;

                        case TORNADO:
                            if (interactedMaterial != Material.DEAD_BUSH) {
                                return;
                            }

                            this.removeItem(player);

                            Block block = player.getTargetBlock(this.transparent, 150);
                            Location location = block.getLocation();

                            Tornado.spawnTornado(MlgWars.getInstance(), location, block.getType(), block.getData(),
                                    null, 0.6, 250, 20 * 20, true, true);

                            location.getWorld().strikeLightning(location.clone().add(1, 0, 0));
                            location.getWorld().strikeLightning(location.clone().add(-1, 0, 0));
                            location.getWorld().strikeLightning(location.clone().add(0, 0, 1));
                            location.getWorld().strikeLightning(location.clone().add(0, 0, -1));

                            for (Player player1 : MlgWars.getInstance().getData().getPlayers()) {
                                if (player1.equals(player)) return;

                                GamePlayer gamePlayer1 = new GamePlayer(player1);
                                if (gamePlayer1.isSpectator()) return;

                                player1.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 5, 1, false, false), true);
                                player1.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 5, 1, false, false), true);

                                Actionbar.sendTitle(player1, "§4" + player.getDisplayName(), 5, 20, 5);
                                Actionbar.sendSubTitle(player1, "§7eskaliert komplett!", 5, 20, 5);
                            }

                            break;

                        case CHICKEN_BRIDGE:
                            if (interactedMaterial != Material.EGG) {
                                return;
                            }

                            if(!event.getItem().getItemMeta().hasDisplayName()) {
                                return;
                            }

                            if (!event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§8● §eHühnerbrücke")) {
                                return;
                            }

                            event.setCancelled(true);
                            this.removeItem(player);
                            Egg egg = player.launchProjectile(Egg.class);

                            this.data.getEggTask().computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>());

                            BukkitRunnable bridgeTask = new BukkitRunnable() {
                                @Override
                                public void run() {
                                    Block block = egg.getWorld().getBlockAt(egg.getLocation().clone().add(0, -2, 0));

                                    if (block.getType() != Material.WOOL && block.getType() == Material.AIR) {

                                        block.setType(Material.WOOL);
                                        block.setData((byte) new Random().nextInt(15));
                                        block.getState().update();

                                    }

                                    block = egg.getWorld().getBlockAt(egg.getLocation().clone().add(1, -2, 0));

                                    if (block.getType() != Material.WOOL && block.getType() == Material.AIR) {

                                        block.setType(Material.WOOL);
                                        block.setData((byte) new Random().nextInt(15));
                                        block.getState().update();

                                    }

                                    block = egg.getWorld().getBlockAt(egg.getLocation().clone().add(0, -2, 1));

                                    if (block.getType() != Material.WOOL && block.getType() == Material.AIR) {

                                        block.setType(Material.WOOL);
                                        block.setData((byte) new Random().nextInt(15));
                                        block.getState().update();

                                    }

                                    if (egg.isDead() || egg.isOnGround() || egg.getLocation().getY() < 0) {
                                        this.cancel();
                                        data.getEggTask().get(player.getUniqueId()).remove(this);
                                    }
                                }
                            };

                            bridgeTask.runTaskTimer(this.mlgWars, 0L, 0L);
                            this.data.getEggTask().get(player.getUniqueId()).add(bridgeTask);

                            break;

                        case WEB_TRAPPER:
                            if (interactedMaterial != Material.EGG) {
                                return;
                            }

                            if(!event.getItem().getItemMeta().hasDisplayName()) {
                                return;
                            }

                            if (!event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§8● §fWebtrap")) {
                                return;
                            }

                            event.setCancelled(true);
                            this.removeItem(player);
                            Egg webTrap = player.launchProjectile(Egg.class);

                            this.data.getEggTask().computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>());

                            BukkitRunnable trapperTask = new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (webTrap.isDead()) {
                                        Location location = webTrap.getLocation();
                                        if (location.getY() < 0) return;
                                        Cuboid cuboid = new Cuboid(location.clone().add(1, 3, 1),
                                                location.clone().subtract(1, 1, 1));

                                        cuboid.getBlocks().forEach(block -> {
                                            if (block.getType() != Material.AIR) return;
                                            boolean shouldPlace = ThreadLocalRandom.current().nextBoolean();
                                            if (shouldPlace) {
                                                block.setType(Material.WEB);
                                            }
                                        });
                                    }

                                    if (webTrap.isDead() || webTrap.isOnGround() || webTrap.getLocation().getY() < 0) {
                                        this.cancel();
                                        data.getWebTrap().get(player.getUniqueId()).remove(this);
                                    }
                                }
                            };

                            trapperTask.runTaskTimer(this.mlgWars, 0L, 5L);
                            this.data.getEggTask().get(player.getUniqueId()).add(trapperTask);
                            break;

                        case BOT_PVP:
                            if (interactedMaterial != Material.EGG) {
                                return;
                            }

                            if(!event.getItem().getItemMeta().hasDisplayName()) {
                                return;
                            }

                            if (!event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§8● §bBotgranate")) {
                                return;
                            }

                            event.setCancelled(true);
                            this.removeItem(player);
                            Egg botDecoy = player.launchProjectile(Egg.class);

                            this.data.getBotTask().computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>());

                            BukkitRunnable botTask = new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (botDecoy.isDead()) {
                                        LivingEntity entity = CustomZombie.EntityTypes.spawnEntity(new CustomZombie(botDecoy.getWorld()),
                                                botDecoy.getLocation().clone().add(0, 1.5, 0));

                                        entity.setCustomName(player.getDisplayName());
                                        entity.setCustomNameVisible(true);
                                        entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 3, true, false));

                                        entity.setCustomName(player.getDisplayName());
                                        entity.setMaxHealth(10D);
                                        entity.getEquipment().setItemInHand(zombieEquipment.getSwords()[random.nextInt(zombieEquipment.getSwords().length)]);

                                        entity.getEquipment().setHelmet(zombieEquipment.getHelmets()[random.nextInt(zombieEquipment.getHelmets().length)]);
                                        entity.getEquipment().setChestplate(zombieEquipment.getChestplates()[random.nextInt(zombieEquipment.getChestplates().length)]);
                                        entity.getEquipment().setLeggings(zombieEquipment.getLeggins()[random.nextInt(zombieEquipment.getLeggins().length)]);
                                        entity.getEquipment().setBoots(zombieEquipment.getShoes()[random.nextInt(zombieEquipment.getShoes().length)]);

                                        entity.setMetadata("owner", new FixedMetadataValue(mlgWars, player.getName()));

                                        Disguise disguise = new PlayerDisguise(player.getName(), player.getName());
                                        disguise.setShowName(true);
                                        disguise.setReplaceSounds(true);
                                        DisguiseAPI.disguiseEntity(entity, disguise);
                                    }

                                    if (botDecoy.isDead() || botDecoy.isOnGround() || botDecoy.getLocation().getY() < 0) {
                                        this.cancel();
                                        data.getBotTask().get(player.getUniqueId()).remove(this);
                                    }
                                }
                            };

                            botTask.runTaskTimer(this.mlgWars, 0L, 5L);
                            this.data.getBotTask().get(player.getUniqueId()).add(botTask);
                            break;
                    }
                }
            }
        }
    }

    private void removeItem(Player player) {
        ItemStack inHand = player.getInventory().getItemInHand();
        if (inHand.getAmount() > 1) {
            inHand.setAmount(inHand.getAmount() - 1);
        } else {
            player.getInventory().remove(inHand);
        }
    }

    private void placeCylinder(Location location, Material material, int radius) {
        int cx = location.getBlockX();
        int cy = location.getBlockY();
        int cz = location.getBlockZ();
        World w = location.getWorld();
        int rSquared = radius * radius;
        for (int x = cx - radius; x <= cx + radius; x++) {
            for (int z = cz - radius; z <= cz + radius; z++) {
                if ((cx - x) * (cx - x) + (cz - z) * (cz - z) <= rSquared) {
                    w.getBlockAt(x, cy, z).setType(material);
                }
            }
        }
    }
}
