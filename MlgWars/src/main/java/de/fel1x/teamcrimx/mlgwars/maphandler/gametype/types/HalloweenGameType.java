package de.fel1x.teamcrimx.mlgwars.maphandler.gametype.types;

import de.fel1x.teamcrimx.crimxapi.utils.Cuboid;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.enums.Spawns;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class HalloweenGameType extends SoloGameType {

    private int gameTick;

    private final Material[] possibleMaterials = {
            Material.JACK_O_LANTERN,
            Material.COBWEB,
            Material.TNT,
            Material.MOSSY_COBBLESTONE,
            Material.MOSSY_STONE_BRICKS,
            Material.INFESTED_MOSSY_STONE_BRICKS,
            Material.OBSIDIAN,
            Material.CRYING_OBSIDIAN,
            Material.MAGMA_BLOCK,
            Material.GRAVEL,
            Material.DEEPSLATE_EMERALD_ORE
    };

    public HalloweenGameType(MlgWars mlgWars) {
        super(mlgWars);
    }

    @Override
    public String getGameTypeName() {
        return "Halloween";
    }

    @Override
    public void gameInit() {
        super.gameInit();

        for (Player player : this.mlgWars.getData().getPlayers()) {
            player.getInventory().setHelmet(ItemBuilder.from(Material.JACK_O_LANTERN)
                    .enchant(Enchantment.BINDING_CURSE)
                    .build());
        }

        World arena = Spawns.SPECTATOR.getLocation().getWorld();
        if(arena == null) {
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {

                arena.setTime(arena.getTime() + 200);

                if(arena.getTime() >= 20500) {
                    this.cancel();
                }
            }
        }.runTaskTimer(this.mlgWars, 0L, 0L);
    }

    @Override
    public void gameTick() {
        super.gameTick();

        this.gameTick++;

        if(this.gameTick % 10 == 0) {
            for (Player player : this.mlgWars.getData().getPlayers()) {
                if(player.isDead() || !player.isValid() || !player.isOnline()) {
                    continue;
                }

                Cuboid area = new Cuboid(player.getLocation().clone().add(2, 2, 2),
                        player.getLocation().clone().add(-2, -2, -2));
                for(int i = 0; i < 5; i++) {
                    Block foundBlock = area.getBlocks().get(this.random.nextInt(area.getBlocks().size()));
                    while (!foundBlock.isSolid()
                            || foundBlock.getLocation() == player.getLocation().clone().add(0, -1,0)) {
                        foundBlock = area.getBlocks().get(this.random.nextInt(area.getBlocks().size()));
                    }
                    foundBlock.setType(this.possibleMaterials[this.random.nextInt(this.possibleMaterials.length)]);
                    player.getWorld().spawnParticle(Particle.BLOCK_CRACK, foundBlock.getLocation(), 3,
                            Bukkit.createBlockData(foundBlock.getType()));
                }
            }
        }
    }
}