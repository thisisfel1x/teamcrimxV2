package de.fel1x.bingo.generation;

import de.fel1x.bingo.objects.BingoDifficulty;
import de.fel1x.bingo.objects.BingoItem;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Items {

    private final List<BingoItem> items = new ArrayList<>();

    public Items() {

        //FOOD
        this.items.add(new BingoItem(Material.APPLE, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.MUSHROOM_STEW, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.BREAD, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.PORKCHOP, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.COOKED_PORKCHOP, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.GOLDEN_APPLE, BingoDifficulty.NORMAL));
        this.items.add(new BingoItem(Material.ENCHANTED_GOLDEN_APPLE, BingoDifficulty.HARDCORE));
        this.items.add(new BingoItem(Material.CAKE, BingoDifficulty.NORMAL));
        this.items.add(new BingoItem(Material.SWEET_BERRIES, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.DRIED_KELP, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.SHEARS, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.PUMPKIN_PIE, BingoDifficulty.NORMAL));
        this.items.add(new BingoItem(Material.COOKIE, BingoDifficulty.HARDCORE));
        this.items.add(new BingoItem(Material.CARROT, BingoDifficulty.NORMAL));
        this.items.add(new BingoItem(Material.COOKED_MUTTON, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.MUTTON, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.RABBIT, BingoDifficulty.NORMAL));
        this.items.add(new BingoItem(Material.COOKED_RABBIT, BingoDifficulty.NORMAL));
        this.items.add(new BingoItem(Material.COD, BingoDifficulty.NORMAL));
        this.items.add(new BingoItem(Material.SALMON, BingoDifficulty.NORMAL));
        this.items.add(new BingoItem(Material.NAME_TAG, BingoDifficulty.HARDCORE));

        // TOOLS & WEAPONS
        this.items.add(new BingoItem(Material.DIAMOND_PICKAXE, BingoDifficulty.NORMAL));
        this.items.add(new BingoItem(Material.DIAMOND_AXE, BingoDifficulty.NORMAL));
        this.items.add(new BingoItem(Material.CLOCK, BingoDifficulty.NORMAL));
        this.items.add(new BingoItem(Material.IRON_SHOVEL, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.FLINT_AND_STEEL, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.NAME_TAG, BingoDifficulty.HARDCORE));
        this.items.add(new BingoItem(Material.COMPASS, BingoDifficulty.NORMAL));

        this.items.add(new BingoItem(Material.BOW, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.LEATHER_CHESTPLATE, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.DIAMOND_CHESTPLATE, BingoDifficulty.HARDCORE));
        this.items.add(new BingoItem(Material.GOLDEN_LEGGINGS, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.LEATHER_CHESTPLATE, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.LEATHER_BOOTS, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.LEATHER_LEGGINGS, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.LEATHER_HELMET, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.DIAMOND_SWORD, BingoDifficulty.EASY));

        // TRANSPORTATION
        this.items.add(new BingoItem(Material.POWERED_RAIL, BingoDifficulty.NORMAL));
        this.items.add(new BingoItem(Material.DETECTOR_RAIL, BingoDifficulty.NORMAL));
        this.items.add(new BingoItem(Material.RAIL, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.MINECART, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.CHEST_MINECART, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.CARROT_ON_A_STICK, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.OAK_BOAT, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.BIRCH_BOAT, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.JUNGLE_BOAT, BingoDifficulty.HARDCORE));
        this.items.add(new BingoItem(Material.ACACIA_BOAT, BingoDifficulty.HARDCORE));
        this.items.add(new BingoItem(Material.DARK_OAK_BOAT, BingoDifficulty.NORMAL));

        // REDSTONE
        this.items.add(new BingoItem(Material.NOTE_BLOCK, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.PISTON, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.STICKY_PISTON, BingoDifficulty.HARDCORE));
        this.items.add(new BingoItem(Material.TNT, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.TRIPWIRE_HOOK, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.OAK_PRESSURE_PLATE, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.BIRCH_PRESSURE_PLATE, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.REDSTONE_LAMP, BingoDifficulty.NORMAL));
        this.items.add(new BingoItem(Material.SPRUCE_FENCE_GATE, BingoDifficulty.NORMAL));
        this.items.add(new BingoItem(Material.REDSTONE_TORCH, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.OAK_TRAPDOOR, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.BIRCH_TRAPDOOR, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.JUNGLE_TRAPDOOR, BingoDifficulty.HARDCORE));
        this.items.add(new BingoItem(Material.REDSTONE_LAMP, BingoDifficulty.NORMAL));

        // DECORATION
        this.items.add(new BingoItem(Material.CHEST, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.IRON_DOOR, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.TRAPPED_CHEST, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.CRAFTING_TABLE, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.FURNACE, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.LADDER, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.CACTUS, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.JUKEBOX, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.GLASS, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.GLASS_PANE, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.POPPY, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.DANDELION, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.RED_TULIP, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.BLUE_ORCHID, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.AZURE_BLUET, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.ORANGE_TULIP, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.ORANGE_TULIP, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.WHITE_TULIP, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.PINK_TULIP, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.ALLIUM, BingoDifficulty.NORMAL));
        this.items.add(new BingoItem(Material.ENCHANTING_TABLE, BingoDifficulty.NORMAL));
        this.items.add(new BingoItem(Material.LILY_PAD, BingoDifficulty.NORMAL));
        this.items.add(new BingoItem(Material.ANVIL, BingoDifficulty.NORMAL));
        this.items.add(new BingoItem(Material.BROWN_MUSHROOM, BingoDifficulty.NORMAL));
        this.items.add(new BingoItem(Material.RED_MUSHROOM, BingoDifficulty.NORMAL));
        this.items.add(new BingoItem(Material.OAK_SIGN, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.BIRCH_SIGN, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.BIRCH_SIGN, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.WHITE_BED, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.RED_BED, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.YELLOW_BED, BingoDifficulty.NORMAL));
        this.items.add(new BingoItem(Material.GREEN_BED, BingoDifficulty.NORMAL));
        this.items.add(new BingoItem(Material.BLUE_BED, BingoDifficulty.NORMAL));
        this.items.add(new BingoItem(Material.PURPLE_BED, BingoDifficulty.NORMAL));
        this.items.add(new BingoItem(Material.ITEM_FRAME, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.FLOWER_POT, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.ARMOR_STAND, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.CAMPFIRE, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.BLAST_FURNACE, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.LOOM, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.FLETCHING_TABLE, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.STONECUTTER, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.LANTERN, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.PAINTING, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.ROSE_BUSH, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.PEONY, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.LILAC, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.ANVIL, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.SCAFFOLDING, BingoDifficulty.HARDCORE));
        this.items.add(new BingoItem(Material.COAL, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.CHARCOAL, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.DIAMOND, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.IRON_INGOT, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.GOLD_INGOT, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.STICK, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.BRICK, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.SNOWBALL, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.WHEAT_SEEDS, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.WHEAT, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.FLINT_AND_STEEL, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.FLINT, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.SUGAR_CANE, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.CLAY_BALL, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.PAPER, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.BOOK, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.BOOKSHELF, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.EGG, BingoDifficulty.EASY));

        // SOME RANDOM BLOCKS
        this.items.add(new BingoItem(Material.STONE, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.WATER_BUCKET, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.LAVA_BUCKET, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.MILK_BUCKET, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.COMPOSTER, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.SAND, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.SANDSTONE, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.SANDSTONE_SLAB, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.SANDSTONE_STAIRS, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.SANDSTONE_WALL, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.IRON_BLOCK, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.GOLD_BLOCK, BingoDifficulty.NORMAL));
        this.items.add(new BingoItem(Material.LAPIS_BLOCK, BingoDifficulty.NORMAL));
        this.items.add(new BingoItem(Material.COBBLESTONE, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.COBBLESTONE_WALL, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.COBBLESTONE_STAIRS, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.COBBLESTONE_SLAB, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.DIRT, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.GRANITE, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.POLISHED_GRANITE, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.ANDESITE, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.POLISHED_ANDESITE, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.ACACIA_LOG, BingoDifficulty.HARDCORE));
        this.items.add(new BingoItem(Material.BIRCH_LOG, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.DARK_OAK_LOG, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.JUNGLE_LOG, BingoDifficulty.HARDCORE));
        this.items.add(new BingoItem(Material.OAK_LOG, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.ACACIA_PLANKS, BingoDifficulty.HARDCORE));
        this.items.add(new BingoItem(Material.BIRCH_PLANKS, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.DARK_OAK_PLANKS, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.JUNGLE_PLANKS, BingoDifficulty.HARDCORE));
        this.items.add(new BingoItem(Material.OAK_PLANKS, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.ACACIA_SLAB, BingoDifficulty.HARDCORE));
        this.items.add(new BingoItem(Material.BIRCH_SLAB, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.DARK_OAK_SLAB, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.JUNGLE_SLAB, BingoDifficulty.HARDCORE));
        this.items.add(new BingoItem(Material.OAK_SLAB, BingoDifficulty.NORMAL));
        this.items.add(new BingoItem(Material.ACACIA_STAIRS, BingoDifficulty.HARDCORE));
        this.items.add(new BingoItem(Material.BIRCH_STAIRS, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.DARK_OAK_STAIRS, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.JUNGLE_STAIRS, BingoDifficulty.HARDCORE));

        this.items.add(new BingoItem(Material.ACACIA_FENCE, BingoDifficulty.HARDCORE));
        this.items.add(new BingoItem(Material.BIRCH_FENCE, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.DARK_OAK_FENCE, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.JUNGLE_FENCE, BingoDifficulty.HARDCORE));
        this.items.add(new BingoItem(Material.OAK_FENCE, BingoDifficulty.EASY));

        this.items.add(new BingoItem(Material.ACACIA_FENCE_GATE, BingoDifficulty.HARDCORE));
        this.items.add(new BingoItem(Material.BIRCH_FENCE_GATE, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.DARK_OAK_FENCE_GATE, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.JUNGLE_FENCE_GATE, BingoDifficulty.HARDCORE));
        this.items.add(new BingoItem(Material.OAK_FENCE_GATE, BingoDifficulty.EASY));

        this.items.add(new BingoItem(Material.JACK_O_LANTERN, BingoDifficulty.NORMAL));
        this.items.add(new BingoItem(Material.PUMPKIN, BingoDifficulty.NORMAL));
        this.items.add(new BingoItem(Material.CARVED_PUMPKIN, BingoDifficulty.NORMAL));
        this.items.add(new BingoItem(Material.NETHERRACK, BingoDifficulty.NORMAL));
        this.items.add(new BingoItem(Material.OBSIDIAN, BingoDifficulty.NORMAL));
        this.items.add(new BingoItem(Material.STONE_BRICKS, BingoDifficulty.NORMAL));

        this.items.add(new BingoItem(Material.OAK_SAPLING, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.SPRUCE_SAPLING, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.BIRCH_SAPLING, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.JUNGLE_SAPLING, BingoDifficulty.HARDCORE));
        this.items.add(new BingoItem(Material.ACACIA_SAPLING, BingoDifficulty.HARDCORE));
        this.items.add(new BingoItem(Material.DARK_OAK_SAPLING, BingoDifficulty.NORMAL));

        this.items.add(new BingoItem(Material.OAK_LEAVES, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.SPRUCE_LEAVES, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.BIRCH_LEAVES, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.JUNGLE_LEAVES, BingoDifficulty.HARDCORE));
        this.items.add(new BingoItem(Material.ACACIA_LEAVES, BingoDifficulty.HARDCORE));
        this.items.add(new BingoItem(Material.DARK_OAK_LEAVES, BingoDifficulty.NORMAL));


        // COLOR
        this.items.add(new BingoItem(Material.WHITE_WOOL, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.ORANGE_WOOL, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.PINK_WOOL, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.LIGHT_BLUE_WOOL, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.YELLOW_WOOL, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.LIGHT_GRAY_WOOL, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.PINK_WOOL, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.GRAY_WOOL, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.LIGHT_GRAY_WOOL, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.CYAN_WOOL, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.PURPLE_WOOL, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.BLUE_WOOL, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.BROWN_WOOL, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.GREEN_WOOL, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.RED_WOOL, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.BLACK_WOOL, BingoDifficulty.EASY));

        this.items.add(new BingoItem(Material.WHITE_STAINED_GLASS, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.ORANGE_STAINED_GLASS, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.PINK_STAINED_GLASS, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.LIGHT_BLUE_STAINED_GLASS, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.YELLOW_STAINED_GLASS, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.LIGHT_GRAY_STAINED_GLASS, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.PINK_STAINED_GLASS, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.GRAY_STAINED_GLASS, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.LIGHT_GRAY_STAINED_GLASS, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.CYAN_STAINED_GLASS, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.PURPLE_STAINED_GLASS, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.BLUE_STAINED_GLASS, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.BROWN_STAINED_GLASS, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.GREEN_STAINED_GLASS, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.RED_STAINED_GLASS, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.BLACK_STAINED_GLASS, BingoDifficulty.EASY));

        this.items.add(new BingoItem(Material.WHITE_CARPET, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.ORANGE_CARPET, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.PINK_CARPET, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.LIGHT_BLUE_CARPET, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.YELLOW_CARPET, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.LIGHT_GRAY_CARPET, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.PINK_CARPET, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.GRAY_CARPET, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.LIGHT_GRAY_CARPET, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.CYAN_CARPET, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.PURPLE_CARPET, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.BLUE_CARPET, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.BROWN_CARPET, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.GREEN_CARPET, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.RED_CARPET, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.BLACK_CARPET, BingoDifficulty.EASY));

        this.items.add(new BingoItem(Material.WHITE_DYE, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.ORANGE_DYE, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.PINK_DYE, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.LIGHT_BLUE_DYE, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.YELLOW_DYE, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.LIGHT_GRAY_DYE, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.PINK_DYE, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.GRAY_DYE, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.LIGHT_GRAY_DYE, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.CYAN_DYE, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.PURPLE_DYE, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.BLUE_DYE, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.BROWN_DYE, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.GREEN_DYE, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.RED_DYE, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.BLACK_DYE, BingoDifficulty.EASY));

        this.items.add(new BingoItem(Material.BONE_MEAL, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.BONE_BLOCK, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.BONE, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.MAP, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.GRAVEL, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.IRON_NUGGET, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.GOLD_NUGGET, BingoDifficulty.EASY));
        this.items.add(new BingoItem(Material.RABBIT_HIDE, BingoDifficulty.HARDCORE));

        for (int i = 0; i < 10; i++) {
            Collections.shuffle(this.items);
        }


    }

    public List<BingoItem> getItems() {
        return this.items;
    }
}
