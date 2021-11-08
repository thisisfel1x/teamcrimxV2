package de.fel1x.teamcrimx.mlgwars.listener.entity;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.maphandler.gametype.types.TntMadnessGameType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class EntityExplodeListener implements Listener {

    private final MlgWars mlgWars;

    private final Collection<ItemStack> drops = new ArrayList<>();

    public EntityExplodeListener(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
        this.mlgWars.getPluginManager().registerEvents(this, this.mlgWars);

        Random random = new Random();
        this.drops.add(new ItemBuilder(Material.TNT, 3 + random.nextInt(5))
                .setName("§cInstant TNT")
                .toItemStack());
        this.drops.add(new ItemBuilder(Material.TNT, random.nextInt(3) + 1)
                .setName("§cVelocity TNT").addGlow()
                .toItemStack());
        this.drops.add(new ItemBuilder(Material.TNT, 1)
                .setName("§cInstant TNT Boost").addGlow()
                .toItemStack());
    }
}
