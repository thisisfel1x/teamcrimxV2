package de.fel1x.teamcrimx.crimxlobby.commands;

import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.manager.SpawnManager;
import de.fel1x.teamcrimx.crimxlobby.objects.Spawn;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetupCommand implements CommandExecutor {

    private final CrimxLobby crimxLobby;
    private final SpawnManager spawnManager;

    public SetupCommand(CrimxLobby crimxLobby) {
        this.crimxLobby = crimxLobby;
        this.crimxLobby.getCommand("setup").setExecutor(this);

        this.spawnManager = this.crimxLobby.getSpawnManager();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] args) {

        if (!(commandSender instanceof Player player)) return false;

        if (!player.hasPermission("crimxlobby.setup")) return false;

        if (args.length == 0) {

            StringBuilder stringBuilder = new StringBuilder("§cMögliche Spawnpunkte: ");

            for (Spawn spawn : Spawn.values()) {
                stringBuilder.append(spawn.name()).append(" ");
            }

            player.sendMessage(stringBuilder.toString());

        } else if (args.length == 1) {
            try {
                Spawn spawn = Spawn.valueOf(args[0].toUpperCase());
                Location location = player.getLocation();

                this.spawnManager.saveLocation(location, spawn.name(), player);

            } catch (IllegalArgumentException ignored) {
                player.sendMessage("§cFehler");
                return false;
            }
        }
        return true;
    }
}
