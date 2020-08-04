package de.fel1x.teamcrimx.mlgwars.listener.player;

import de.fel1x.teamcrimx.crimxapi.utils.*;
import de.fel1x.teamcrimx.mlgwars.Data;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.enums.Spawns;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.inventories.ForcemapInventory;
import de.fel1x.teamcrimx.mlgwars.inventories.KitInventory;
import de.fel1x.teamcrimx.mlgwars.inventories.SpectatorInventory;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import de.fel1x.teamcrimx.mlgwars.timer.LobbyTimer;
import de.fel1x.teamcrimx.mlgwars.utils.Tornado;
import de.fel1x.teamcrimx.mlgwars.utils.entites.ZombieEquipment;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
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

public class InteractListener implements Listener {

    private final MlgWars mlgWars;
    private final Data data;
    private final Set<Material> transparent = new HashSet<>();
    private final Random random = new Random();
    private final ZombieEquipment zombieEquipment = new ZombieEquipment();
    private int count;

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
                        LobbyTimer lobbyTimer = new LobbyTimer();
                        if(lobbyTimer.getCountdown() <= 10) {
                            player.sendMessage(this.mlgWars.getPrefix() + "§7Du kannst die Map nicht mehr ändern");
                        } else {
                            ForcemapInventory.FORCEMAP_INVENTORY.open(player);
                        }
                    } else {
                        event.setCancelled(true);
                    }
                }

            } else if (gamestate == Gamestate.DELAY || gamestate == Gamestate.ENDING) {
                event.setCancelled(true);
            } else {
                if (gamePlayer.isSpectator()) {
                    event.setCancelled(true);
                    if (event.hasItem() && event.getMaterial() == Material.COMPASS) {
                        SpectatorInventory.INVENTORY.open(player);
                    }
                    return;
                }
                if (event.hasItem()) {
                    if (event.getMaterial() == Material.MUSHROOM_SOUP) {
                        if (player.getHealth() < 19) {
                            event.getItem().setType(Material.BOWL);

                            double health;
                            if (player.getHealth() + 3.5 >= 20) {
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
                                if (player1.equals(player)) continue;

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

                            if (!event.getItem().getItemMeta().hasDisplayName()) {
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

                            if (!event.getItem().getItemMeta().hasDisplayName()) {
                                return;
                            }

                            if (!event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§8● §fWebtrap")) {
                                return;
                            }

                            event.setCancelled(true);
                            this.removeItem(player);
                            Egg webTrap = player.launchProjectile(Egg.class);
                            webTrap.setMetadata("webTrap", new FixedMetadataValue(this.mlgWars, true));
                            break;

                        case BOT_PVP:
                            if (interactedMaterial != Material.EGG) {
                                return;
                            }

                            if (!event.getItem().getItemMeta().hasDisplayName()) {
                                return;
                            }

                            if (!event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§8● §bBotgranate")) {
                                return;
                            }

                            event.setCancelled(true);
                            this.removeItem(player);
                            Egg botDecoy = player.launchProjectile(Egg.class);
                            botDecoy.setMetadata("botDecoy", new FixedMetadataValue(this.mlgWars, true));
                            break;

                        case FARMER:
                            if (interactedMaterial != Material.GOLD_NUGGET) {
                                return;
                            }

                            if (!event.getItem().getItemMeta().hasDisplayName()) {
                                return;
                            }

                            if (!event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§8● §dVerwandler")) {
                                return;
                            }

                            if (this.data.getFarmerTask().containsKey(player.getUniqueId())) {
                                return;
                            }

                            this.removeItem(player);

                            DisguiseType[] disguiseType = {
                                    DisguiseType.CHICKEN,
                                    DisguiseType.COW,
                                    DisguiseType.SHEEP,
                                    DisguiseType.PIG
                            };

                            for (int i = 0; i < 3; i++) {

                                player.getWorld().spawnEntity(player.getLocation(), EntityType.SHEEP);
                                player.getWorld().spawnEntity(player.getLocation(), EntityType.PIG);
                                player.getWorld().spawnEntity(player.getLocation(), EntityType.CHICKEN);
                                player.getWorld().spawnEntity(player.getLocation(), EntityType.COW);

                            }

                            DisguiseType toDisguise = disguiseType[random.nextInt(disguiseType.length)];
                            player.sendMessage(this.mlgWars.getPrefix() + "§7Du bist nun als §a" + toDisguise.name()
                                    + " §7versteckt!");

                            Disguise disguise = new MobDisguise(toDisguise);
                            disguise.setShowName(false);
                            disguise.setVelocitySent(false);
                            disguise.setViewSelfDisguise(false);
                            disguise.setReplaceSounds(true);
                            DisguiseAPI.disguiseEntity(player, disguise);

                            player.setMetadata("farmer", new FixedMetadataValue(this.mlgWars, 60));

                            this.data.getFarmerTask().put(player.getUniqueId(), new BukkitRunnable() {
                                @Override
                                public void run() {

                                    count = player.getMetadata("farmer").get(0).asInt();

                                    Actionbar.sendActiobar(player, "§6Farmer (versteckt) §8● " + ProgressBar.getProgressBar(count, 60, 15,
                                            '█', ChatColor.GREEN, ChatColor.DARK_GRAY));

                                    if (count == 0) {
                                        data.getFarmerTask().get(player.getUniqueId()).cancel();
                                        data.getFarmerTask().remove(player.getUniqueId());
                                        if (DisguiseAPI.isDisguised(player)) {
                                            DisguiseAPI.undisguiseToAll(player);
                                        }
                                        Actionbar.sendActiobar(player, "§6Farmer §8● §7Du bist nun wieder §asichtbar");
                                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 2.5f, 0.5f);

                                        Actionbar.sendTitle(player, " ", 5, 20, 5);
                                        Actionbar.sendSubTitle(player, "§aWieder sichtbar!", 5, 20, 5);
                                    } else {
                                        player.setMetadata("farmer", new FixedMetadataValue(mlgWars, count - 1));
                                    }
                                }
                            });

                            this.data.getFarmerTask().get(player.getUniqueId()).runTaskTimer(this.mlgWars, 0L, 20L);

                            break;

                        case TURTLE:
                            if (interactedMaterial != Material.EGG) {
                                return;
                            }

                            if (!event.getItem().getItemMeta().hasDisplayName()) {
                                return;
                            }

                            if (!event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§8● §aTurtle")) {
                                return;
                            }

                            event.setCancelled(true);
                            this.removeItem(player);
                            Egg turtleEgg = player.launchProjectile(Egg.class);

                            this.data.getTurtleTask().computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>());

                            if (!turtleEgg.hasMetadata("state")) {
                                turtleEgg.setMetadata("state", new FixedMetadataValue(this.mlgWars, 0));
                            }

                            if (!turtleEgg.hasMetadata("height")) {
                                turtleEgg.setMetadata("height", new FixedMetadataValue(this.mlgWars, -2));
                            }

                            BukkitRunnable turtleTask = new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (turtleEgg.isDead()) {

                                        int turtleBuildState = turtleEgg.getMetadata("state").get(0).asInt();
                                        int turtleHeight = turtleEgg.getMetadata("height").get(0).asInt();

                                        if (buildTurtle(turtleEgg.getLocation(), player, turtleBuildState, turtleHeight)) {
                                            this.cancel();
                                            data.getTurtleTask().get(player.getUniqueId()).remove(this);
                                        }

                                        turtleEgg.setMetadata("state",
                                                new FixedMetadataValue(mlgWars, turtleBuildState + 1));
                                        turtleEgg.setMetadata("height",
                                                new FixedMetadataValue(mlgWars, turtleHeight + 1));
                                    }

                                    if (turtleEgg.getLocation().getY() < 0) {
                                        this.cancel();
                                        data.getTurtleTask().get(player.getUniqueId()).remove(this);
                                    }
                                }
                            };

                            turtleTask.runTaskTimer(this.mlgWars, 0L, 5L);
                            this.data.getTurtleTask().get(player.getUniqueId()).add(turtleTask);
                            break;

                        case ENDER_MAN:
                            if (interactedMaterial != Material.ENDER_PEARL) {
                                return;
                            }

                            if (!event.getItem().getItemMeta().hasDisplayName()) {
                                return;
                            }

                            if (!event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§8● §5Funkelnde Enderperle")) {
                                return;
                            }

                            if (this.data.getTeleporterTask().containsKey(player.getUniqueId())) {
                                event.setCancelled(true);
                                player.sendMessage(this.mlgWars.getPrefix() + "§cEs läuft bereits ein Teleportvorgang!");
                                return;
                            }

                            Player nearestPlayer = this.getClosestEntity(player, player.getLocation());

                            if (nearestPlayer != null) {

                                event.setCancelled(true);
                                this.removeItem(player);

                                count = 0;
                                Location toTeleport = nearestPlayer.getLocation();

                                this.data.getTeleporterTask().put(player.getUniqueId(), new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        if (count < 3) {
                                            ParticleUtils.drawParticleLine(player.getLocation().clone().add(0, 0.5, 0),
                                                    nearestPlayer.getLocation().clone().add(0, 0.5, 0), Particles.SPELL_WITCH,
                                                    500, 0, 0, 0);
                                            player.getWorld().playSound(player.getLocation(), Sound.NOTE_PLING, 2f, 4.5f);
                                        } else {
                                            if (nearestPlayer.isDead() || nearestPlayer.getLocation().getY() < 0) {
                                                player.sendMessage(mlgWars.getPrefix() + "§cDer angepeilte Spieler ist Tod! " +
                                                        "Du wirst in die Mitte teleportiert");
                                                player.teleport(Spawns.SPECTATOR.getLocation());
                                            } else {
                                                player.teleport(toTeleport);
                                                player.getWorld().playSound(toTeleport, Sound.ENDERMAN_TELEPORT, 4f, 1.5f);
                                                player.getWorld().playEffect(toTeleport, Effect.ENDER_SIGNAL, 1);
                                            }

                                            data.getTeleporterTask().remove(player.getUniqueId());
                                            this.cancel();
                                        }
                                        count++;
                                    }
                                });

                                this.data.getTeleporterTask().get(player.getUniqueId()).runTaskTimer(this.mlgWars, 0L, 20L);
                            } else {
                                player.sendMessage(this.mlgWars.getPrefix() + "§cEs konnte kein Gegner gefunden werden... Spielst du alleine?");
                            }
                            break;

                        case CSGO:
                            if(!event.getItem().getItemMeta().hasDisplayName()) {
                                return;
                            }

                            this.data.getCsgoTasks().computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>());

                            if(interactedMaterial == Material.GLOWSTONE_DUST
                                    && event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§8● §eFlash")) {

                                event.setCancelled(true);
                                this.removeItem(player);

                                Item item = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.GLOWSTONE_DUST));
                                item.setPickupDelay(Integer.MAX_VALUE);
                                item.setVelocity(player.getEyeLocation().getDirection().multiply(1.25D).setY(0.25D));

                                BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        if(item.isOnGround()) {
                                            new InstantFirework(FireworkEffect.builder()
                                                    .withColor(Color.WHITE, Color.YELLOW, Color.GRAY).build(),
                                                    item.getLocation().clone().add(0, 1, 0));

                                            item.getNearbyEntities(5, 5, 5).forEach(entity -> {
                                                if(!(entity instanceof LivingEntity)) return;
                                                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,
                                                        20 * 5, 4, true, true));
                                                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,
                                                        20 * 5, 1, true, true));
                                            });
                                        }

                                        if(item.isOnGround() || item.getLocation().getY() < 0 || item.isDead() || !item.isValid()) {
                                            this.cancel();
                                            data.getCsgoTasks().get(player.getUniqueId()).remove(this);
                                            item.remove();
                                        }
                                    }
                                };

                                bukkitRunnable.runTaskTimer(this.mlgWars, 0L, 1L);
                                this.data.getCsgoTasks().get(player.getUniqueId()).add(bukkitRunnable);

                            } else if(interactedMaterial == Material.FIREBALL
                                    && event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§8● §cMolotowcocktail")) {

                                event.setCancelled(true);
                                this.removeItem(player);

                                Fireball fireball = player.launchProjectile(Fireball.class);
                                fireball.setMetadata("moli", new FixedMetadataValue(this.mlgWars, true));

                            } else if(interactedMaterial == Material.FIREWORK_CHARGE
                                    && event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§8● §7Smoke")) {

                                event.setCancelled(true);
                                this.removeItem(player);

                                Item item = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.FIREWORK_CHARGE));
                                item.setPickupDelay(Integer.MAX_VALUE);
                                item.setVelocity(player.getEyeLocation().getDirection().multiply(1.25D).setY(0.25D));

                                BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        if(item.isOnGround()) {
                                            new InstantFirework(FireworkEffect.builder()
                                                    .withColor(Color.WHITE, Color.YELLOW, Color.GRAY).build(),
                                                    item.getLocation().clone().add(0, 0.5, 0));
                                            new SmokeGrenade(mlgWars, item.getLocation(), 10, item.getWorld());
                                        }

                                        if(item.isOnGround() || item.getLocation().getY() < 0 || item.isDead() || !item.isValid()) {
                                            this.cancel();
                                            data.getCsgoTasks().get(player.getUniqueId()).remove(this);
                                            item.remove();
                                        }
                                    }
                                };
                                bukkitRunnable.runTaskTimer(this.mlgWars, 0L, 1L);
                                data.getCsgoTasks().get(player.getUniqueId()).add(bukkitRunnable);
                            }
                            break;
                    }
                }
            }
        }
    }

    private boolean buildTurtle(Location location, Player player, int turtleBuildState, int turtleHeight) {
        switch (turtleBuildState) {
            case 1:
            case 5:
                for (int x = -2; x < 3; x++) {
                    for (int y = -2; y < 3; y++) {
                        location.clone().add(x, turtleHeight, y).getBlock().setType(Material.WOOD);
                    }
                }
                break;

            case 2:
            case 3:
            case 4:
                location.clone().add(-2, turtleHeight, -2).getBlock().setType(Material.WOOD);
                location.clone().add(-2, turtleHeight, -1).getBlock().setType(Material.WOOD);
                location.clone().add(-2, turtleHeight, 0).getBlock().setType(Material.WOOD);
                location.clone().add(-2, turtleHeight, 1).getBlock().setType(Material.WOOD);
                location.clone().add(-2, turtleHeight, 2).getBlock().setType(Material.WOOD);

                location.clone().add(2, turtleHeight, -2).getBlock().setType(Material.WOOD);
                location.clone().add(2, turtleHeight, -1).getBlock().setType(Material.WOOD);
                location.clone().add(2, turtleHeight, 0).getBlock().setType(Material.WOOD);
                location.clone().add(2, turtleHeight, 1).getBlock().setType(Material.WOOD);
                location.clone().add(2, turtleHeight, 2).getBlock().setType(Material.WOOD);

                location.clone().add(1, turtleHeight, 2).getBlock().setType(Material.WOOD);
                location.clone().add(0, turtleHeight, 2).getBlock().setType(Material.WOOD);
                location.clone().add(-1, turtleHeight, 2).getBlock().setType(Material.WOOD);

                location.clone().add(1, turtleHeight, -2).getBlock().setType(Material.WOOD);
                location.clone().add(0, turtleHeight, -2).getBlock().setType(Material.WOOD);
                location.clone().add(-1, turtleHeight, -2).getBlock().setType(Material.WOOD);

                break;

            case 6:
                for (int x = -1; x < 2; x++) {
                    for (int y = -1; y < 2; y++) {
                        location.clone().add(x, turtleHeight, y).getBlock().setType(Material.WOOD);
                    }
                }
                break;


            case 7:
                location.clone().add(0, 5, 0).getBlock().setType(Material.WOOD);
                location.clone().add(0, 3, 0).getBlock().setType(Material.GLOWSTONE);

                location.clone().add(0, 1, 2).getBlock().setType(Material.AIR);
                location.clone().add(0, 1, -2).getBlock().setType(Material.AIR);
                location.clone().add(2, 1, 0).getBlock().setType(Material.AIR);
                location.clone().add(-2, 1, 0).getBlock().setType(Material.AIR);

                player.playSound(player.getLocation(), Sound.LEVEL_UP, 14, 15);
                break;
        }

        if (turtleBuildState != 7) {
            player.playSound(player.getLocation(), Sound.CLICK, 10, 17);
            Particles.PORTAL.display(1.5f, 1.5f, 1.5f, 0f, 80,
                    location.clone().add(0, 1.5, 0), new ArrayList<>(Bukkit.getOnlinePlayers()));
            return false;
        } else {
            return true;
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
}
