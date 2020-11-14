package de.fel1x.bingo.commands;

import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.generation.ItemGenerator;
import de.fel1x.bingo.objects.BingoPlayer;
import de.fel1x.bingo.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ItemsCommand implements CommandExecutor {

    Bingo bingo;
    ItemGenerator itemGenerator;

    public ItemsCommand(Bingo bingo) {
        this.bingo = bingo;
        this.itemGenerator = bingo.getItemGenerator();

        bingo.getCommand("items").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(this.bingo.getPrefix() + "§cDu musst ein Spieler sein um das zu tun");
            return false;
        }

        Player player = (Player) sender;
        BingoPlayer bingoPlayer = new BingoPlayer(player);

        player.openInventory(getBingoInventory(bingoPlayer));
        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 0.5f);

        return true;
    }

    public Inventory getBingoInventory(BingoPlayer bingoPlayer) {

        Inventory inventory = Bukkit.createInventory(null, InventoryType.DISPENSER, this.bingo.getPrefix() + "§7§lItems");

        for (int i = 0; i < this.itemGenerator.getPossibleItems().size(); i++) {

            ItemStack item = new ItemBuilder(this.itemGenerator.getPossibleItems().get(i).getMaterial())
                    .setLore("", "§7Schwierigkeit: §b§l" + this.itemGenerator.getPossibleItems().get(i).getBingoDifficulty().name(), "").toItemStack();

            if (bingoPlayer.isPlayer() && bingoPlayer.getTeam() != null) {

                boolean done = bingoPlayer.getTeam().getDoneItems()[i];

                if (done) {
                    item = new ItemBuilder(Material.BARRIER)
                            .setName("§a§l✔ §r§8- §7Bereits gefunden!").toItemStack();
                }

            }

            inventory.setItem(i, item);

        }

        return inventory;

    }

}
