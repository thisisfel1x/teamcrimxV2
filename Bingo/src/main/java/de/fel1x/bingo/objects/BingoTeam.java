package de.fel1x.bingo.objects;

import de.fel1x.bingo.Bingo;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;

public enum BingoTeam {

    RED("Rot", "[#1]", "", 2, 0, new ArrayList<>(), new Boolean[9], Color.RED,
            Bukkit.createInventory(null, 9 * 3, Bingo.getInstance().getPrefix() + "§7Backpack")),
    GREEN("Grün", "[#2]", "", 2, 0, new ArrayList<>(), new Boolean[9], Color.GREEN,
            Bukkit.createInventory(null, 9 * 3, Bingo.getInstance().getPrefix() + "§7Backpack")),
    YELLOW("Gelb", "[#3]", "", 2, 0, new ArrayList<>(), new Boolean[9], Color.YELLOW,
            Bukkit.createInventory(null, 9 * 3, Bingo.getInstance().getPrefix() + "§7Backpack")),
    BLUE("Blau", "[#4]", "", 2, 0, new ArrayList<>(), new Boolean[9], Color.BLUE,
            Bukkit.createInventory(null, 9 * 3, Bingo.getInstance().getPrefix() + "§7Backpack")),
    MAGENTA("Violett", "[#5]", "", 2, 0, new ArrayList<>(), new Boolean[9], Color.PURPLE,
            Bukkit.createInventory(null, 9 * 3, Bingo.getInstance().getPrefix() + "§7Backpack")),
    ORANGE("Orange", "[#6]", "", 2, 0, new ArrayList<>(), new Boolean[9], Color.ORANGE,
            Bukkit.createInventory(null, 9 * 3, Bingo.getInstance().getPrefix() + "§7Backpack"));

    String name;
    String shortId;
    String customName;
    int teamSize;
    int doneItemsSize;
    ArrayList<Player> teamPlayers;
    Boolean[] doneItems;
    Color color;
    Inventory backpack;

    BingoTeam(String name, String shortId, String customName, int teamSize, int doneItemsSize, ArrayList<Player> teamPlayers, Boolean[] doneItems, Color color, Inventory inventory) {
        this.name = name;
        this.shortId = shortId;
        this.customName = customName;
        this.teamSize = teamSize;
        this.doneItemsSize = doneItemsSize;
        this.teamPlayers = teamPlayers;
        this.doneItems = doneItems;
        this.color = color;
        this.backpack = inventory;

        Arrays.fill(doneItems, false);

    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Player> getTeamPlayers() {
        return this.teamPlayers;
    }

    public void setTeamPlayers(ArrayList<Player> teamPlayers) {
        this.teamPlayers = teamPlayers;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getShortId() {
        return this.shortId;
    }

    public void setShortId(String shortId) {
        this.shortId = shortId;
    }

    public String getCustomName() {
        return this.customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public int getTeamSize() {
        return this.teamSize;
    }

    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }

    public Boolean[] getDoneItems() {
        return this.doneItems;
    }

    public void setDoneItems(Boolean[] doneItems) {
        this.doneItems = doneItems;
    }

    public int getDoneItemsSize() {
        return this.doneItemsSize;
    }

    public void setDoneItemsSize(int doneItemsSize) {
        this.doneItemsSize = doneItemsSize;
    }

    public void increaseByOne() {
        this.doneItemsSize += 1;
    }

    public boolean isEmpty() {
        return this.teamPlayers.size() == 0;
    }

    public boolean hasPlayers() {
        return this.teamPlayers.size() > 0;
    }

    public Inventory getBackpack() {
        return this.backpack;
    }

    public void setBackpack(Inventory backpack) {
        this.backpack = backpack;
    }
}
