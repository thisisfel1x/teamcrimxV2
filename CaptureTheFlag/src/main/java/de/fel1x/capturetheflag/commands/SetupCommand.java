package de.fel1x.capturetheflag.commands;

import de.fel1x.capturetheflag.filehandler.SpawnHandler;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetupCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args) {

        if(!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        if(!player.hasPermission("ctf.setup")) return false;

        if(!command.equalsIgnoreCase("setup")) return false;

        Location location = player.getLocation();

        if(args.length == 1) {

            if(args[0].toLowerCase().equalsIgnoreCase("setlobby")) {

                SpawnHandler.saveLocation(location, "lobby", player);

            } else if(args[0].toLowerCase().equalsIgnoreCase("setspectator")) {

                SpawnHandler.saveLocation(location, "spectator", player);

            }  else if(args[0].toLowerCase().equalsIgnoreCase("setredspawn")) {

                SpawnHandler.saveLocation(location, "redSpawn", player);

            }  else if(args[0].toLowerCase().equalsIgnoreCase("setbluespawn")) {

                SpawnHandler.saveLocation(location, "blueSpawn", player);

            }  else if(args[0].toLowerCase().equalsIgnoreCase("setredflag")) {

                SpawnHandler.saveLocation(location, "redFlag", player);

            }  else if(args[0].toLowerCase().equalsIgnoreCase("setblueflag")) {

                SpawnHandler.saveLocation(location, "blueFlag", player);

            }

        }


        return true;
    }
}
