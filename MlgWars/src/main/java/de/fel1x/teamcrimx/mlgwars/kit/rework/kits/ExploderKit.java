package de.fel1x.teamcrimx.mlgwars.kit.rework.kits;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.kit.rework.Kit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ExploderKit extends Kit {

    private final List<Block> tntBlocks;

    public ExploderKit(Player player, MlgWars mlgWars) {
        super(player, mlgWars);

        this.tntBlocks = new ArrayList<>();
    }

    @Override
    public @Nullable ItemStack getInteractionItemStack() {
        return new ItemBuilder(Material.LEVER)
                .setName(Component.text("● ", NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false)
                        .append(Component.text("Fernzünder", NamedTextColor.RED)))
                .setPDC(this.mlgWars, "KIT", this.getClass().getName())
                .toItemStack();
    }

    @Override
    public void initializeKit() {
        super.initializeKit();
        this.player.getInventory().setItem(0, this.getInteractionItemStack());
        this.player.getInventory().setItem(1, new ItemStack(Material.TNT, 12));
    }

    @Override
    public void disableKit() {
        super.disableKit();
    }

    @Override
    protected void onInteract(PlayerInteractEvent event) {
        for (Block tntBlock : this.tntBlocks) {
            if(tntBlock == null || tntBlock.getType() != Material.TNT || !tntBlock.hasMetadata(this.player.getName())) {
                continue;
            }
            TNTPrimed tnt = this.player.getWorld().spawn(tntBlock.getLocation(), TNTPrimed.class);
            tnt.setFuseTicks(0);

            tntBlock.setType(Material.AIR);
        }
        this.tntBlocks.clear();
    }

    @EventHandler
    public void on(BlockPlaceEvent event) {
        if(!this.compareUniqueIDs(event.getPlayer().getUniqueId())) {
            return;
        }

        if(event.getBlockPlaced().getType() == Material.TNT) {
            event.getBlockPlaced().setMetadata(this.player.getName(), new FixedMetadataValue(this.mlgWars, null));
            this.tntBlocks.add(event.getBlockPlaced());
        }

    }

    @Override
    public void run() {
    }
}
