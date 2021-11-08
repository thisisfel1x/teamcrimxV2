package de.fel1x.teamcrimx.mlgwars.kit.rework.kits;

import com.destroystokyo.paper.MaterialTags;
import com.destroystokyo.paper.ParticleBuilder;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.kit.rework.Kit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.Nullable;

public class ChickenBridge extends Kit {

    private final Material[] concreteTypes = MaterialTags.CONCRETES.getValues().toArray(new Material[0]);

    public ChickenBridge(Player player, MlgWars mlgWars) {
        super(player, mlgWars);
    }

    @Override
    public void initializeKit() {
        super.initializeKit();
        this.player.getInventory().setItem(0, this.getInteractionItemStack());
    }

    @Override
    public @Nullable ItemStack getInteractionItemStack() {
        return new ItemBuilder(Material.RAW_GOLD, 3)
                .setName(Component.text("● ", NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false)
                        .append(Component.text("Hühnerbrücke", TextColor.fromHexString("#8FE40D"))))
                .setPDC(this.mlgWars, "KIT", this.getClass().getName())
                .toItemStack();
    }

    @Override
    public boolean shouldConsiderCooldown() {
        return true;
    }

    @Override
    protected void onInteract(PlayerInteractEvent event) {
        this.removeItemByAmount(1);
        this.setCooldown(2000);
        this.player.setCooldown(Material.RAW_GOLD, 40); // 40 Ticks = 2 sec

        event.setCancelled(true);

        Egg egg = this.player.launchProjectile(Egg.class);
        new BukkitRunnable() {
            @Override
            public void run() {
                if(egg.isDead() || !egg.isValid() || egg.getLocation().getY() < 0) {
                    this.cancel();
                }

                switch (getCardinalDirection(egg)) {
                    case "X" -> {
                        for(int x = -1; x <= 1; x++) {
                            Block block = egg.getWorld().getBlockAt(egg.getLocation().clone().add(x, -2, 0));
                            block.setType(ChickenBridge.this.concreteTypes[ChickenBridge.this.random
                                    .nextInt(ChickenBridge.this.concreteTypes.length)]);
                        }
                    }
                    case "Z" -> {
                        for(int z = -1; z <= 1; z++) {
                            Block block = egg.getWorld().getBlockAt(egg.getLocation().clone().add(0, -2, z));
                            block.setType(ChickenBridge.this.concreteTypes[ChickenBridge.this.random
                                    .nextInt(ChickenBridge.this.concreteTypes.length)]);
                        }
                    }
                    default -> {
                        for(int x = -1; x <= 1; x++) {
                            for (int z = -1; z <= 1; z++) {
                                Block block = egg.getWorld().getBlockAt(egg.getLocation().clone().add(x, -2, z));
                                block.setType(ChickenBridge.this.concreteTypes[ChickenBridge.this.random
                                        .nextInt(ChickenBridge.this.concreteTypes.length)]);
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(this.mlgWars, 0L, 0L);
    }

    @Deprecated
    // BUGGY
    private void outlineBlock(Block block) {
        BoundingBox boundingBox = block.getBoundingBox();
        double particleDistance = 0.1;
        for (double x = boundingBox.getMinX(); x <= boundingBox.getMaxX(); x+=particleDistance) {
            for (double y = boundingBox.getMinY(); y <= boundingBox.getMaxY(); y+=particleDistance) {
                for (double z = boundingBox.getMinZ(); z <= boundingBox.getMaxZ(); z+=particleDistance) {
                    try {
                        String[] split = block.getType().name().split("_");
                        String name = split[0];
                        if(name.equalsIgnoreCase("LIGHT")) {
                            name = "BLUE";
                        }

                        new ParticleBuilder(Particle.REDSTONE).allPlayers()
                                .color(Color.fromRGB(net.md_5.bungee.api.ChatColor.of(name).getColor().getRGB()))
                                .location(new Location(block.getWorld(), x, y, z)).spawn();
                    } catch (Exception ignored) {
                        ignored.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void run() {

    }

    private String getCardinalDirection(Entity entity) {
        double rotation = (entity.getLocation().getYaw() - 180) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
        if (0 <= rotation && rotation < 22.5) {
            return "X";
        } else if (22.5 <= rotation && rotation < 67.5) {
            return "XZ";
        } else if (67.5 <= rotation && rotation < 112.5) {
            return "Z";
        } else if (112.5 <= rotation && rotation < 157.5) {
            return "XZ";
        } else if (157.5 <= rotation && rotation < 202.5) {
            return "X";
        } else if (202.5 <= rotation && rotation < 247.5) {
            return "XZ";
        } else if (247.5 <= rotation && rotation < 292.5) {
            return "Z";
        } else if (292.5 <= rotation && rotation < 337.5) {
            return "XZ";
        } else if (337.5 <= rotation && rotation < 360.0) {
            return "X";
        }
        return "X";
    }

}
