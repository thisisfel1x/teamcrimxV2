package de.fel1x.teamcrimx.mlgwars.maphandler.gametype;

import de.fel1x.teamcrimx.crimxapi.utils.Cuboid;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.enums.Size;
import de.fel1x.teamcrimx.mlgwars.enums.Spawns;
import de.fel1x.teamcrimx.mlgwars.maphandler.ChestFiller;
import de.fel1x.teamcrimx.mlgwars.maphandler.MapHandler;
import de.fel1x.teamcrimx.mlgwars.objects.MlgWarsTeam;
import org.bukkit.*;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.A;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public abstract class GameType {

    public final MlgWars mlgWars;
    public final Random random = new Random();
    public final MapHandler mapHandler = new MapHandler();

    private int teamSize = -1;
    private int itemMultiplier = 1;

    public GameType(MlgWars mlgWars) {
        this.mlgWars = mlgWars;

        Bukkit.getConsoleSender().sendMessage(this.mlgWars.getPrefix() + "Gewählter GameType: " + this.getGameTypeName());
    }

    public String getGameTypeName() {
        return "FEHLER";
    }

    public int getTeamSize() {
        return this.teamSize;
    }

    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }

    public boolean shouldBeDefinedByVoting() {
        return false;
    }

    public void loadMap(String mapName) {
        Size size;

        try {
            size = this.mapHandler.getSize(mapName);
        } catch (Exception ignored) {
            this.mlgWars.setNoMap(true);
            return;
        }

        int totalPlayerSpawns = size.getMaxTeams();
        this.setTeamSize(size.getTeamSize());

        Location spectator = this.mapHandler.loadLocation(mapName, "spectator");
        if (spectator == null) {
            this.sendErrorMessage("spectator");
            this.mlgWars.setNoMap(true);
            return;
        } else {
            Bukkit.createWorld(new WorldCreator(this.mapHandler.getWorld(mapName, "spectator")));
            spectator = this.mapHandler.loadLocation(mapName, "spectator");
            Spawns.SPECTATOR.setLocation(spectator);
        }

        this.mlgWars.getData().getPlayerSpawns().clear();
        this.mlgWars.getData().getGameTeams().clear();

        if (totalPlayerSpawns > Bukkit.getServer().getMaxPlayers()) {
            if (totalPlayerSpawns < Bukkit.getOnlinePlayers().size()) {
                int count = 1;
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (count > totalPlayerSpawns) {
                        player.kickPlayer(this.mlgWars.getPrefix() + "§7Nach einem Forcemap sind nicht genügen Spawns übrig. " +
                                "Du wurdest gekickt");
                    }
                    count++;
                }
            }
        }

        for (int i = 0; i < totalPlayerSpawns; i++) {
            Location location = this.mapHandler.loadLocation(mapName, String.valueOf(i + 1));
            if (location == null) {
                this.sendErrorMessage("spawn " + (i + 1));
                this.mlgWars.setNoMap(true);
                return;
            } else {
                this.mlgWars.getData().getPlayerSpawns().add(location);
            }
        }

        Location loc1 = this.mapHandler.loadLocation(mapName, "loc1");
        Location loc2 = this.mapHandler.loadLocation(mapName, "loc2");
        Location middle1 = this.mapHandler.loadLocation(mapName, "middle1");
        Location middle2 = this.mapHandler.loadLocation(mapName, "middle2");

        if (loc1 == null || loc2 == null || middle1 == null || middle2 == null) {
            this.sendErrorMessage("map_region/middle_region");
            this.mlgWars.setNoMap(true);
            return;
        }

        Spawns.LOC_1.setLocation(loc1);
        Spawns.LOC_2.setLocation(loc2);

        this.mlgWars.getData().setMapRegion(new Cuboid(Spawns.LOC_1.getLocation(), Spawns.LOC_2.getLocation()));

        Spawns.MIDDLE_1.setLocation(middle1);
        Spawns.MIDDLE_2.setLocation(middle2);

        this.mlgWars.getData().setMiddleRegion(new Cuboid(Spawns.MIDDLE_1.getLocation(), Spawns.MIDDLE_2.getLocation()));

        World world = spectator.getWorld();
        world.setSpawnLocation((int) spectator.getX(), (int) spectator.getY(), (int) spectator.getZ());
        world.setDifficulty(Difficulty.EASY);
        world.setGameRule(GameRule.DO_MOB_LOOT, false);
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setDifficulty(Difficulty.NORMAL);
        world.setStorm(false);
        world.setThunderDuration(0);
        world.setThundering(false);
        world.setTime(1200);

        world.getEntities().forEach(Entity::remove);

        this.mlgWars.setTeamSize(size.getTeamSize());

        //if (this.mlgWars.getTeamSize() > 1) {
            int teamSize = this.mlgWars.getTeamSize();
            float hue = 0.0f;
            float step = 1.0f / totalPlayerSpawns;

            for (int i = 0; i < totalPlayerSpawns; i++) {
                hue += step * i;
                this.mlgWars.getData().getGameTeams().put(i, new MlgWarsTeam(i, (i + 1), teamSize,
                        new ArrayList<>(), new ArrayList<>(), Color.getHSBColor(hue, 1.0f, 1.0f)));
            }
        //}

        Bukkit.getConsoleSender().sendMessage(this.mlgWars.getPrefix() + "§aDie Map " + mapName +
                " wurde erfolgreich geladen");

        this.mlgWars.getData().getPlayers().forEach(player ->
                this.mlgWars.getData().getGamePlayers().get(player.getUniqueId()).getScoreboardHandler()
                        .updateBoard(player, "§e" + mapName, "map"));
    }

    public ArrayList<ItemStack> getTierOneItems() {
        return ChestFiller.getTier1Items();
    }

    public ArrayList<ItemStack> getTierTwoItems() {
        return ChestFiller.getTier2Items();
    }

    public void gameInit() {
        if(this.itemMultiplier > 1) {
            for (Player player : this.mlgWars.getData().getPlayers()) {
                ItemStack[] inventoryContents = player.getInventory().getContents();
                for (int i = 0; i < inventoryContents.length; i++) {
                    ItemStack loopStack = inventoryContents[i];
                    if(loopStack == null) {
                        continue;
                    }
                    loopStack.setAmount(loopStack.getAmount() * this.itemMultiplier);
                    inventoryContents[i] = loopStack;
                }
                player.getInventory().setContents(inventoryContents);
            }
        }
    }

    public void gameTick() {}

    public void fillChests() {
        if (this.mlgWars.isNoMap()) return;

        Random random = new Random(); // Will be the instance of the random chance to generate all our random numbers.

        ArrayList<ItemStack> items = this.getTierOneItems();
        ArrayList<ItemStack> itemsTier2 = this.getTierTwoItems();

        Cuboid cuboid = new Cuboid(Spawns.LOC_1.getLocation(), Spawns.LOC_2.getLocation());
        Cuboid middleCube = new Cuboid(Spawns.MIDDLE_1.getLocation(), Spawns.MIDDLE_2.getLocation());

        for (Chunk c : cuboid.getChunks()) {
            for (BlockState b : c.getTileEntities()) {

                if (b instanceof Chest chest) {
                    if (b.getBlock().getType() != Material.CHEST) continue;

                    chest.getBlockInventory().clear();

                    Collections.shuffle(items);

                    boolean[] chosen = new boolean[chest.getBlockInventory().getSize()]; // This checks which slots are already taken in the inventory.

                    int loot = random.nextInt(7);
                    for (int i = 0; i < (8 + loot); i++) {

                        Inventory chestInv = chest.getBlockInventory();

                        int slot;

                        do {
                            slot = random.nextInt(chestInv.getSize());
                        } while (chosen[slot]); // Make sure the slot does not already have an item in it.

                        chosen[slot] = true;
                        ItemStack is = items.get(random.nextInt(items.size()));
                        Material current = is.getType();

                        while (chest.getBlockInventory().contains(current)) {
                            is = items.get(random.nextInt(items.size()));
                            current = is.getType();
                        }

                        chestInv.setItem(random.nextInt(chestInv.getSize()), is); // Set the item in the chest to a random place (which is not taken).
                    }
                }
            }
        }

        for (Chunk c1 : middleCube.getChunks()) {
            for (BlockState b : c1.getTileEntities()) {

                if (b instanceof Chest chest) {

                    chest.getBlockInventory().clear();

                    Collections.shuffle(itemsTier2);

                    boolean[] chosen = new boolean[chest.getBlockInventory().getSize()]; // This checks which slots are already taken in the inventory.

                    int loot = random.nextInt(7);
                    for (int i = 0; i < (10 + loot); i++) {

                        Inventory chestInv = chest.getBlockInventory();

                        int slot;

                        do {
                            slot = random.nextInt(chestInv.getSize());
                        } while (chosen[slot]); // Make sure the slot does not already have an item in it.

                        chosen[slot] = true;
                        ItemStack is = itemsTier2.get(random.nextInt(itemsTier2.size()));
                        Material current = is.getType();

                        while (chest.getBlockInventory().contains(current)) {
                            is = items.get(random.nextInt(items.size()));
                            current = is.getType();
                        }

                        chestInv.setItem(random.nextInt(chestInv.getSize()), is); // Set the item in the chest to a random place (which is not taken).
                    }
                }
            }
        }
    }

    public void kill(Player killer, Player deadPlayer) {

    }

    public void death(Player player) {

    }

    public void quit(Player player) {
        this.death(player);

    }

    public void finish() {

    }

    public void setItemMultiplier(int itemMultiplier) {
        this.itemMultiplier = itemMultiplier;
    }

    private void sendErrorMessage(String error) {
        Bukkit.getConsoleSender().sendMessage(this.mlgWars.getPrefix() + String.format("§cDer Spawn '%s' existiert nicht!", error));
    }
}
