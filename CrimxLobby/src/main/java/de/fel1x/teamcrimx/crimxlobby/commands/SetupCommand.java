package de.fel1x.teamcrimx.crimxlobby.commands;

import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.manager.SpawnManager;
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

        if (!(commandSender instanceof Player)) return false;

        Player player = (Player) commandSender;

        if (!player.hasPermission("crimxlobby.setup")) return false;

        if (args.length == 0) {

            player.sendMessage(this.crimxLobby.getPrefix() + "§cNutze /setup <setspawn>|<setspawnnpc>|<mlgwars>|<mlgwarsnpc> etc.");

        } else if (args.length == 1) {

            String spawn = args[0].toLowerCase();
            Location location = player.getLocation();

            switch (spawn) {

                case "setspawn":
                    this.spawnManager.saveLocation(location, "spawn", player);
                    break;

                case "setmlgwars":
                    this.spawnManager.saveLocation(location, "mlgwars", player);
                    break;

                case "setfloorislava":
                    this.spawnManager.saveLocation(location, "floorislava", player);
                    break;

                case "setmasterbuilders":
                    this.spawnManager.saveLocation(location, "masterbuilders", player);
                    break;

                case "setbedwars":
                    this.spawnManager.saveLocation(location, "bedwars", player);
                    break;

                case "setctf":
                    this.spawnManager.saveLocation(location, "ctf", player);
                    break;
            }

        }

        return true;
    }
}
