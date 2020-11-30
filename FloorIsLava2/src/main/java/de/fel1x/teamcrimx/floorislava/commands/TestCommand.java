package de.fel1x.teamcrimx.floorislava.commands;

import de.fel1x.teamcrimx.floorislava.FloorIsLava;
import de.fel1x.teamcrimx.floorislava.gameevents.lootdrop.LootDrop;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TestCommand implements CommandExecutor {

    private final FloorIsLava floorIsLava;

    public TestCommand(FloorIsLava floorIsLava) {
        this.floorIsLava = floorIsLava;
        this.floorIsLava.getCommand("test").setExecutor(this::onCommand);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        if(!sender.hasPermission("test")) {
            return false;
        }

        LootDrop lootDrop = new LootDrop(player.getLocation(), Material.valueOf(args[0])).build();
        player.sendMessage("§aspawned!");

        return true;
    }
}
