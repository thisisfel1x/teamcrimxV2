package de.fel1x.teamcrimx.mlgwars.commands;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.enums.Size;
import de.fel1x.teamcrimx.mlgwars.maphandler.MapHandler;
import de.fel1x.teamcrimx.mlgwars.maphandler.SpawnHandler;
import de.fel1x.teamcrimx.mlgwars.timer.IdleTimer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.material.Directional;

import java.util.HashSet;
import java.util.Set;

public class SetupCommand implements CommandExecutor {

    private final MlgWars mlgWars;
    private final MapHandler mapHandler = new MapHandler();
    private final SpawnHandler spawnHandler = new SpawnHandler();
    private final Set<Material> transparent = new HashSet<>();

    public SetupCommand(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
        this.mlgWars.getCommand("setup").setExecutor(this);

        this.transparent.add(Material.AIR);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] args) {

        if(!(commandSender instanceof Player)) {
            return false;
        }

        Player player = (Player) commandSender;

        if(!player.hasPermission("mlgwars.setup")) {
            return false;
        }

        if(args.length == 0) {
            if(!this.mlgWars.isInSetup()) {
                player.sendMessage(this.mlgWars.getPrefix() + "§7Dieser Server ist nun im Setup Modus!");
                Bukkit.getScheduler().cancelTasks(this.mlgWars);
                this.mlgWars.setInSetup(true);
                return true;
            } else {
             this.mlgWars.setInSetup(false);
             player.sendMessage(this.mlgWars.getPrefix() + "§7Dieser Server ist nun nicht mehr im Setup Modus");
             this.mlgWars.startTimerByClass(IdleTimer.class);
            }
        } else {
            if(this.mlgWars.isInSetup()) {
                if(args.length == 3) {
                    String mapName = args[1];
                    if(args[0].toLowerCase().equalsIgnoreCase("create")) {
                        String size = args[2];

                        if(!size.equalsIgnoreCase("8x1") && !size.equalsIgnoreCase("12x1")
                                && !size.equalsIgnoreCase("16x1") && !size.equalsIgnoreCase("24x1")
                                && !size.equalsIgnoreCase("8x2") && !size.equalsIgnoreCase("8x3")) {
                            player.sendMessage(this.mlgWars.getPrefix() + "§cBitte wähle eine passende Mapgröße aus " +
                                    "(8x1, 12x1, 16x1, 24x1, 8x2, 8x3)");
                            return false;
                        } else {
                            Size size1 = this.getSizeFromString(size);
                            if(size1 == null) {
                                player.sendMessage(this.mlgWars.getPrefix() + "§cEin Fehler ist aufgetreten!");
                                return false;
                            }
                            this.mapHandler.createMap(mapName, size1);
                            player.sendMessage(this.mlgWars.getPrefix() +
                                    String.format("§7Die Map §a%s (Größe: %s) §7wurde erfolgreich erstellt", mapName, size));
                            return true;
                        }
                    } else {
                        if (!this.mapHandler.exists(mapName)) {
                            player.sendMessage(this.mlgWars.getPrefix() + "§7Die Map §c" + mapName + " §7existiert nicht.");
                            return false;
                        } else {
                            if (args[0].toLowerCase().equalsIgnoreCase("setloc")) {
                                String loc = args[2].toLowerCase();

                                if (!loc.equalsIgnoreCase("loc1") && !loc.equalsIgnoreCase("loc2")) {
                                    player.sendMessage(this.mlgWars.getPrefix() + "§cBitte nutze entweder 'loc1' oder 'loc2'");
                                    return false;
                                } else {
                                    this.mapHandler.saveLocation(player.getLocation(), mapName, loc, player);
                                    return true;
                                }
                            } else if (args[0].toLowerCase().equalsIgnoreCase("setmiddle")) {
                                String middle = args[2].toLowerCase();

                                if (!middle.equalsIgnoreCase("middle1") && !middle.equalsIgnoreCase("middle2")) {
                                    player.sendMessage(this.mlgWars.getPrefix() + "§cBitte nutze entweder 'middle1' oder 'middle2'");
                                    return false;
                                } else {
                                    this.mapHandler.saveLocation(player.getLocation(), mapName, middle, player);
                                    return true;
                                }
                            } else if (args[0].toLowerCase().equalsIgnoreCase("setspawn")) {
                                int spawnNumber;

                                try {
                                    spawnNumber = Integer.parseInt(args[2]);
                                } catch (NumberFormatException ignored) {
                                    player.sendMessage(this.mlgWars.getPrefix() + "§cBitte gebe eine Zahl an!");
                                    return false;
                                }

                                int size = this.mapHandler.getSize(mapName).getSize();
                                if (spawnNumber > size) {
                                    player.sendMessage(this.mlgWars.getPrefix() + "§cBitte gebe eine Spawnnummer zwischen 1 und " + size + " an!");
                                    return false;
                                } else {
                                    this.mapHandler.saveLocation(player.getLocation(), mapName, String.valueOf(spawnNumber), player);
                                    return true;
                                }
                            }
                        }
                    }
                } else if(args.length == 2) {
                    if(args[0].toLowerCase().equalsIgnoreCase("setspectator")) {
                        String map = args[1];

                        if(!this.mapHandler.exists(map)) {
                            player.sendMessage(this.mlgWars.getPrefix() + "§7Die Map §c" + map + " §7existiert nicht.");
                            return false;
                        } else {
                            this.mapHandler.saveLocation(player.getLocation(), map, "spectator", player);
                            return true;
                        }
                    } else if(args[0].toLowerCase().equalsIgnoreCase("setsign")) {
                        int id;

                        try {
                            id = Integer.parseInt(args[1]);
                        } catch (NumberFormatException ignored) {
                            player.sendMessage(this.mlgWars.getPrefix() + "§cBitte gebe eine Zahl an!");
                            return false;
                        }

                        if(id < 1 || id > 5) {
                            player.sendMessage(this.mlgWars.getPrefix() + "§cBitte gebe eine Zahl zwischen 1 und 5 an!");
                            return false;
                        }

                        Block targetBlock = player.getTargetBlock(this.transparent, 10);

                        if(targetBlock.getType() != Material.WALL_SIGN) {
                            player.sendMessage(this.mlgWars.getPrefix() + "§cBitte schaue ein WALL_SIGN an!");
                            return false;
                        }

                        Sign sign = (Sign) targetBlock.getState();
                        this.spawnHandler.saveLocation(sign.getLocation(), "topSign" + id, player, sign.getBlock().getFace(sign.getBlock()));

                        player.sendMessage(this.mlgWars.getPrefix() + "§aSchild erfolgreich gespeichert!");
                        return true;
                    } else if(args[0].toLowerCase().equalsIgnoreCase("sethead")) {
                        int id;

                        try {
                            id = Integer.parseInt(args[1]);
                        } catch (NumberFormatException ignored) {
                            player.sendMessage(this.mlgWars.getPrefix() + "§cBitte gebe eine Zahl an!");
                            return false;
                        }

                        if(id < 1 || id > 5) {
                            player.sendMessage(this.mlgWars.getPrefix() + "§cBitte gebe eine Zahl zwischen 1 und 5 an!");
                            return false;
                        }

                        Block targetBlock = player.getTargetBlock(this.transparent, 10);

                        if(targetBlock.getType() != Material.SKULL) {
                            player.sendMessage(this.mlgWars.getPrefix() + "§cBitte schaue ein SKULL an!");
                            return false;
                        }

                        BlockState block = player.getTargetBlock((Set<Material>) null, 5).getState();
                        Directional directional = (Directional) block.getData();

                        this.spawnHandler.saveLocation(block.getLocation(), "topHead" + id, player, directional.getFacing());

                        player.sendMessage(this.mlgWars.getPrefix() + "§aKopf erfolgreich gespeichert!");
                        return true;
                    }
                } else if(args.length == 1) {
                    if(args[0].toLowerCase().equalsIgnoreCase("setlobby")) {
                        this.spawnHandler.saveLocation(player.getLocation(), "lobby", player);
                    }
                }
            } else {
                player.sendMessage(this.mlgWars.getPrefix() + "§7Der Server ist nicht im Setup Modus. Bitte aktiviere ihn zuerst §8(/setup)");
                return false;
            }
        }

        return true;
    }

    private Size getSizeFromString(String text) {
        for (Size size : Size.values()) {
            if (size.getName().equalsIgnoreCase(text)) {
                return size;
            }
        }
        return null;
    }

}
