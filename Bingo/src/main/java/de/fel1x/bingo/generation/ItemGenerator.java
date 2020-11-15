package de.fel1x.bingo.generation;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.objects.BingoDifficulty;
import de.fel1x.bingo.objects.BingoItem;

import java.util.List;
import java.util.Random;

public class ItemGenerator {

    BiMap<Integer, BingoItem> possibleItems = HashBiMap.create();

    BingoDifficulty bingoDifficulty = BingoDifficulty.EASY;
    Bingo bingo = Bingo.getInstance();

    List<BingoItem> items;
    Random random;

    public ItemGenerator() {

        this.items = this.bingo.getItems().getItems();
        this.random = new Random();

        for (int i = 0; i < 9; i++) {

            this.generateNewItem(i);

        }

    }

    public BiMap<Integer, BingoItem> getPossibleItems() {
        return this.possibleItems;
    }

    public BingoItem generateNewItem(int finalI) {

        int possibility;
        BingoItem bingoItem;

        if (this.bingoDifficulty.equals(BingoDifficulty.NORMAL)) {

            possibility = this.random.nextInt(20);
            bingoItem = this.items.get(this.random.nextInt(this.items.size()));

            if (possibility <= 3) {
                while (bingoItem.getBingoDifficulty() != BingoDifficulty.HARDCORE) {
                    bingoItem = this.items.get(this.random.nextInt(this.items.size()));
                }
            } else if (possibility <= 10) {
                while (bingoItem.getBingoDifficulty() != BingoDifficulty.EASY) {
                    bingoItem = this.items.get(this.random.nextInt(this.items.size()));
                }
            } else {
                while (bingoItem.getBingoDifficulty() != BingoDifficulty.NORMAL) {
                    bingoItem = this.items.get(this.random.nextInt(this.items.size()));
                }
            }

        } else if (this.bingoDifficulty.equals(BingoDifficulty.HARDCORE)) {

            possibility = this.random.nextInt(20);
            bingoItem = this.items.get(this.random.nextInt(this.items.size()));
            if (possibility <= 3) {
                while (bingoItem.getBingoDifficulty() != BingoDifficulty.EASY) {
                    bingoItem = this.items.get(this.random.nextInt(this.items.size()));
                }
            } else if (possibility <= 10) {
                while (bingoItem.getBingoDifficulty() != BingoDifficulty.NORMAL) {
                    bingoItem = this.items.get(this.random.nextInt(this.items.size()));
                }
            } else {
                while (bingoItem.getBingoDifficulty() != BingoDifficulty.HARDCORE) {
                    bingoItem = this.items.get(this.random.nextInt(this.items.size()));
                }
            }

        } else {

            possibility = this.random.nextInt(20);
            bingoItem = this.items.get(this.random.nextInt(this.items.size()));

            if (possibility <= 3) {
                while (bingoItem.getBingoDifficulty() != BingoDifficulty.NORMAL) {
                    bingoItem = this.items.get(this.random.nextInt(this.items.size()));
                }
            } else {
                while (bingoItem.getBingoDifficulty() != BingoDifficulty.EASY) {
                    bingoItem = this.items.get(this.random.nextInt(this.items.size()));
                }
            }
        }

        this.items.remove(bingoItem);

        String name = bingoItem.getMaterial().name();
        this.possibleItems.put(finalI, bingoItem);

        if (name.contains("glass") || name.contains("wool")) {
            this.items.stream().filter(item -> item.getMaterial().name().contains(name)).forEach(this.items::remove);
        }

        return bingoItem;

    }
}
