package de.fel1x.teamcrimx.crimxapi.cosmetic.cosmetics.gadgets;

import com.destroystokyo.paper.MaterialSetTag;
import com.destroystokyo.paper.MaterialTags;
import de.fel1x.teamcrimx.crimxapi.cosmetic.CosmeticCategory;
import de.fel1x.teamcrimx.crimxapi.cosmetic.Gadget;
import de.fel1x.teamcrimx.crimxapi.support.CrimxSpigotAPI;
import de.fel1x.teamcrimx.crimxapi.utils.Cuboid;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;

public class PaintballGunGadget extends Gadget {

    private final ArrayList<BlockRestore> blockRestores;

    private final Material[] clayTypes = MaterialTags.STAINED_TERRACOTTA.getValues().toArray(new Material[0]);
    private final Material[] concreteTypes = MaterialTags.CONCRETES.getValues().toArray(new Material[0]);

    public PaintballGunGadget(Player player, CrimxSpigotAPI crimxSpigotAPI) {
        super(player, crimxSpigotAPI);
        this.blockRestores = new ArrayList<>();
    }

    @Override
    public Component getDisplayName() {
        return Component.text("Paintball Gun", NamedTextColor.YELLOW)
                .decoration(TextDecoration.ITALIC, false);
    }

    @Override
    public Component[] getDescription() {
        return new Component[]{
                Component.empty(), Component.text("Beschreibung folgt", NamedTextColor.GRAY)
                .decoration(TextDecoration.ITALIC, false), Component.empty()
        };
    }

    @Override
    public Material getDisplayMaterial() {
        return Material.IRON_NUGGET;
    }

    @Override
    public CosmeticCategory getCosmeticCategory() {
        return CosmeticCategory.GADGETS;
    }

    @Override
    public int getCost() {
        return 500;
    }

    @Override
    public double maxDiscount() {
        return 0.2;
    }

    @Override
    public void startCosmetic(Player player) {
        super.startCosmetic(player);
    }

    @Override
    public void stopCosmetic(Player player) {
        super.stopCosmetic(player);
    }

    @Override
    public ItemStack getGadgetItemStack() {
        return new ItemBuilder(Material.IRON_NUGGET)
                .setName(Component.text("● ", NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false)
                        .append(this.getDisplayName()))
                .setLore(this.getDescription())
                .toItemStack();
    }

    @Override
    protected void onRightClickInteract(Player player) {
        Snowball snowball = player.launchProjectile(Snowball.class);
        snowball.setMetadata(this.getClass().getName(), new FixedMetadataValue(CrimxSpigotAPI.getInstance(), null));
    }

    @EventHandler
    public void on(ProjectileHitEvent event) {
        Entity entity = event.getEntity();
        Location hitLocation = entity.getLocation().toBlockLocation();

        if (entity instanceof Snowball && entity.hasMetadata(this.getClass().getName())) {
            event.setCancelled(true);

            Cuboid cuboid = new Cuboid(hitLocation.clone().add(1, 1, 1),
                    hitLocation.clone().subtract(1, 1, 1));

            for (Block block : cuboid.getBlocks()) {
                if(block.getType() == Material.AIR || !block.isSolid()) {
                    continue;
                }

                if(this.random.nextInt(5) < 3) {
                    Material type = block.getType();
                    BlockData blockData = block.getBlockData();
                    BlockState blockState = block.getState();
                    BlockFace blockFace = block.getFace(block);

                    this.blockRestores.add(new BlockRestore(block, type, blockData, blockState, blockFace));

                    block.setType(this.random.nextBoolean()
                            ? this.concreteTypes[this.random.nextInt(this.concreteTypes.length)]
                            : this.clayTypes[this.random.nextInt(this.clayTypes.length)], false);
                }
            }

            Bukkit.getScheduler().runTaskLater(this.crimxSpigotAPI, () -> {
                this.blockRestores.forEach(BlockRestore::restoreToNormal);
                this.blockRestores.clear();
            }, 20L * 4);

        }
    }

    public class BlockRestore {
        Block block;
        Material type;
        BlockData blockData;
        BlockState blockState;
        BlockFace blockFace;

        public BlockRestore(Block block, Material type, BlockData blockData, BlockState blockState, BlockFace blockFace) {
            this.block = block;
            this.type = type;
            this.blockData = blockData;
            this.blockState = blockState;
            this.blockFace = blockFace;
        }

        public void restoreToNormal() {
            this.block.setType(this.type, false);
            this.block.setBlockData(this.blockData, false);
            this.block.getState().update(true, false);
        }

    }
}
