package de.fel1x.teamcrimx.mlgwars.listener.player;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.inventories.ForcemapInventory;
import de.fel1x.teamcrimx.mlgwars.inventories.KitInventory;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class InteractListener implements Listener {

    private final MlgWars mlgWars;

    public InteractListener(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
        this.mlgWars.getPluginManager().registerEvents(this, this.mlgWars);
    }

    @EventHandler
    public void on(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        GamePlayer gamePlayer = new GamePlayer(player);

        Material interactedMaterial = event.getMaterial();

        Gamestate gamestate = this.mlgWars.getGamestateHandler().getGamestate();

        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            if(gamestate == Gamestate.IDLE || gamestate == Gamestate.LOBBY) {
                if(event.hasItem()) {
                    if(event.getMaterial() == Material.STORAGE_MINECART) {
                        player.playSound(player.getLocation(), Sound.CHEST_OPEN, 2f, 0.75f);
                        KitInventory.KIT_OVERVIEW_INVENTORY.open(player);
                    } else if(event.getMaterial() == Material.REDSTONE_TORCH_ON) {
                        ForcemapInventory.FORCEMAP_INVENTORY.open(player);
                    } else {
                        event.setCancelled(true);
                    }
                }

            } else if(gamestate == Gamestate.DELAY || gamestate == Gamestate.ENDING) {
                event.setCancelled(true);
            } else {
                if(gamePlayer.isSpectator()) {
                    event.setCancelled(true);
                    return;
                }

                switch (gamePlayer.getSelectedKit()) {
                    case EXPLODER:
                        if(interactedMaterial == Material.LEVER) {
                            for (Block block : this.mlgWars.getData().getPlacedExploderTnt().get(player.getUniqueId())) {
                                if(block.getType() != Material.TNT) continue;
                                if(block.hasMetadata("owner")) {
                                    String owner = block.getMetadata("owner").get(0).asString();
                                    if(owner.equalsIgnoreCase(player.getName())) {
                                        block.setType(Material.AIR);

                                        TNTPrimed tnt = player.getWorld().spawn(block.getLocation(), TNTPrimed.class);
                                        tnt.setFuseTicks(0);
                                    }
                                }
                            }
                        }
                        break;
                    case ASTRONAUT:
                        if(interactedMaterial != Material.FIREWORK) {
                            return;
                        }

                        this.removeItem(player);

                        Firework firework = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
                        FireworkMeta fireworkMeta = firework.getFireworkMeta();

                        Random random = new Random();

                        fireworkMeta.setPower(3);
                        fireworkMeta.addEffect(FireworkEffect.builder().withColor(Color.fromBGR(random.nextInt(255),
                                random.nextInt(255), random.nextInt(255))).flicker(true).trail(true).build());
                        firework.setFireworkMeta(fireworkMeta);
                        firework.setPassenger(player);
                        break;
                    case SAVER:
                        if(interactedMaterial != Material.BLAZE_ROD) {
                            return;
                        }

                        this.removeItem(player);

                        int delay = 0;

                        if(player.getLocation().getY() < 0) {
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
                        if(gamestate == Gamestate.INGAME) {
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
                }
            }

        }
    }

    private void removeItem(Player player) {
        ItemStack inHand = player.getInventory().getItemInHand();
        if(inHand.getAmount() > 1) {
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
        for (int x = cx - radius; x <= cx +radius; x++) {
            for (int z = cz - radius; z <= cz +radius; z++) {
                if ((cx - x) * (cx - x) + (cz - z) * (cz - z) <= rSquared) {
                    w.getBlockAt(x, cy, z).setType(material);
                }
            }
        }
    }
}
