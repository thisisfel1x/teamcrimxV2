package de.fel1x.teamcrimx.mlgwars.kit.rework.kits;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import de.fel1x.teamcrimx.crimxapi.utils.Cuboid;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.crimxapi.utils.ParticleUtils;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.kit.rework.Kit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ThreadLocalRandom;

public class TrapperKit extends Kit {

    private final ItemStack trapperStick;
    private final ItemStack webTrapperItem;
    private final ItemStack trapperBullets;

    public TrapperKit(Player player, MlgWars mlgWars) {
        super(player, mlgWars);

        this.trapperStick = new ItemBuilder(Material.STICK)
                .setName(Component.text("Fallen-Stick").decoration(TextDecoration.ITALIC, false))
                .setPDC(this.mlgWars, "KIT", this.getClass().getName()).toItemStack();
        this.webTrapperItem = new ItemBuilder(Material.FERMENTED_SPIDER_EYE)
                .setName(Component.text("Spinnenetz-Falle").decoration(TextDecoration.ITALIC, false))
                .setPDC(this.mlgWars, "KIT", this.getClass().getName()).toItemStack();
        this.trapperBullets = new ItemBuilder(Material.NETHERITE_SCRAP, 10)
                .setName(Component.text("Fallenleger-Patronen").decoration(TextDecoration.ITALIC, false))
                .setPDC(this.mlgWars, "KIT", this.getClass().getName()).toItemStack();
    }

    @Override
    public void initializeKit() {
        super.initializeKit();
        this.player.getInventory().addItem(this.trapperStick, this.webTrapperItem, this.trapperBullets);
    }

    @Override
    public @Nullable ItemStack getInteractionItemStack() {
        return null;
    }

    @Override
    public boolean shouldConsiderCooldown() {
        return true;
    }

    @Override
    protected void onInteract(PlayerInteractEvent event) {
        if(event.getItem() == null) {
            return;
        }

        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        ItemStack item = event.getItem();

        if(!item.equals(this.trapperStick) && !item.equals(this.webTrapperItem)) {
            return;
        }

        Block targetBlock = this.player.getTargetBlock(100);
        Entity targetEntity = this.player.getTargetEntity(100);

        if (targetBlock == null && targetEntity == null) {
            return;
        }

        if (targetBlock != null && targetBlock.getType() == Material.AIR) {
            targetBlock = null;
        }

        Location targetLocation = null;

        if (targetEntity instanceof Player) {
            targetLocation = targetEntity.getLocation();
        } else if (targetBlock != null) {
            targetLocation = targetBlock.getLocation();
        }

        if (targetLocation == null) {
            return;
        }

        Player closestPlayer = null;
        if (targetEntity == null) {
            double closestDistance = -1.0;
            for (Player possiblePlayer : targetLocation.getNearbyEntitiesByType(Player.class, 5)) {
                if(this.mlgWars.getData().getSpectators().contains(possiblePlayer)) {
                    continue;
                }
                double distance = targetLocation.distanceSquared(possiblePlayer.getLocation());
                if (closestDistance != -1.0 && !(distance < closestDistance)) {
                    continue;
                }
                closestDistance = distance;
                closestPlayer = possiblePlayer;
            }
        }

        Location finalLocation = closestPlayer != null ? closestPlayer.getLocation() : targetLocation;

        TrapperType trapperType = null;

        if (item.equals(this.trapperStick)) {
            trapperType = TrapperType.CAGE;
        } else if(item.equals(this.webTrapperItem)) {
            trapperType = TrapperType.WEB;
        }

        this.execute(trapperType, finalLocation);
    }

    private void execute(TrapperType trapperType, Location location) {
        if(trapperType == null) {
            return;
        }

        if(!this.removeBullets(trapperType)) {
            this.player.sendMessage(this.mlgWars.prefix().append(Component.text("Du nicht mehr genügend Patronen!",
                    NamedTextColor.RED)));
            this.player.playSound(this.player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1f, 2f);
            return;
        }

        Color color;
        long cooldown;

        switch (trapperType) {
            case WEB -> {
                color = Color.WHITE;
                cooldown = 2000;

                if (location.getY() < 0) return;
                Cuboid cuboid = new Cuboid(location.clone().add(1, 3, 1),
                        location.clone().subtract(1, 1, 1));

                cuboid.getBlocks().forEach(block -> {
                    if (block.getType() != Material.AIR) return;
                    boolean shouldPlace = ThreadLocalRandom.current().nextBoolean();
                    if (shouldPlace) {
                        block.setType(Material.COBWEB);
                    }
                });
            }
            default -> {
                color = Color.AQUA;
                cooldown = 5000;

                try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(location.getWorld()))) {
                    Operation operation = new ClipboardHolder(this.mlgWars.getAllPossibleTrapperCages()
                            .get(this.random.nextInt(this.mlgWars.getAllPossibleTrapperCages().size())))
                            .createPaste(editSession)
                            .to(BlockVector3.at(location.getX(), location.getY(), location.getZ()))
                            .ignoreAirBlocks(true)
                            .build();
                    Operations.complete(operation);
                }
            }
        }

        this.setCooldown(cooldown);
        ParticleUtils.drawLine(this.player.getEyeLocation(), location, 0.5, Particle.REDSTONE,
                new Particle.DustOptions(color, 1f));

    }

    private boolean removeBullets(TrapperType trapperType) {
        int possibleSlot = this.player.getInventory().first(Material.NETHERITE_SCRAP);
        if(possibleSlot == -1) {
            return false;
        }

        ItemStack itemStack = this.player.getInventory().getItem(possibleSlot);
        if(itemStack == null) {
            return false;
        }

        if(itemStack.getAmount() < trapperType.bulletsToRemove) {
            return false;
        }

        itemStack.setAmount(itemStack.getAmount() - trapperType.bulletsToRemove);
        if(itemStack.getAmount() <= 0) {
            itemStack = null;
        }

        this.player.getInventory().setItem(possibleSlot, itemStack);
        this.player.updateInventory();

        return true;
    }

    @Override
    public void run() {

    }

    public enum TrapperType {
        CAGE(2),
        WEB(1);

        int bulletsToRemove;

        TrapperType(int bulletsToRemove) {
            this.bulletsToRemove = bulletsToRemove;
        }
    }
}
