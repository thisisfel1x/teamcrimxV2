package de.fel1x.capturetheflag.listener.block;

import de.fel1x.capturetheflag.CaptureTheFlag;
import de.fel1x.capturetheflag.Data;
import de.fel1x.capturetheflag.flag.Flag;
import de.fel1x.capturetheflag.gameplayer.GamePlayer;
import de.fel1x.capturetheflag.gamestate.Gamestate;
import de.fel1x.capturetheflag.inventories.KitSelector;
import de.fel1x.capturetheflag.inventories.TeamSelector;
import de.fel1x.capturetheflag.team.Teams;
import de.fel1x.capturetheflag.utils.ItemBuilder;
import de.fel1x.capturetheflag.utils.Utils;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InteractListener implements Listener {

    Data data = CaptureTheFlag.getInstance().getData();

    @EventHandler
    public void on(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        GamePlayer gamePlayer = new GamePlayer(player);
        Teams teams = gamePlayer.getTeam();

        if (!gamePlayer.isPlayer()) return;

        Gamestate gamestate = CaptureTheFlag.getInstance().getGamestateHandler().getGamestate();

        Flag flag;

        if (gamestate.equals(Gamestate.LOBBY)) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {

                if (event.getMaterial().equals(Material.BED)) {
                    if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§a§lWähle dein Team")) {

                        TeamSelector.TEAM_SELECTOR.open(player);

                    }
                } else if (event.getMaterial().equals(Material.STORAGE_MINECART)) {
                    if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§e§lWähle dein Kit")) {

                        KitSelector.KIT_SELECTOR.open(player);

                    }
                }

            }

        } else if (gamestate.equals(Gamestate.INGAME)) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {

                if (event.getClickedBlock() == null || event.getClickedBlock().getState() == null) return;

                if (event.getClickedBlock().getState() instanceof Banner) {

                    Banner banner = (Banner) event.getClickedBlock().getState();

                    if (banner.getBaseColor().equals(DyeColor.RED)) {
                        flag = Flag.RED_FLAG;
                    } else if (banner.getBaseColor().equals(DyeColor.BLUE)) {
                        flag = Flag.BLUE_FLAG;
                    } else if (banner.getBaseColor().equals(DyeColor.WHITE)) {
                        if (banner.getLocation().equals(data.getRedFlagBaseLocation())) {
                            flag = Flag.RED_FLAG;
                        } else if (banner.getLocation().equals(data.getBlueFlagBaseLocation())) {
                            flag = Flag.BLUE_FLAG;
                        } else {
                            return;
                        }
                    } else {
                        player.sendMessage("§cDiese Flagge ist bereits gestohlen!");
                        return;
                    }

                    switch (flag) {
                        case BLUE_FLAG:

                            if (teams.equals(Teams.BLUE)) {

                                if (!banner.getLocation().equals(data.getBlueFlagBaseLocation())  && !player.equals(data.getRedFlagHolder())) {
                                    player.sendMessage("§bDu hast die Flagge deines Teams - sichere sie!");
                                    data.setBlueFlagHolder(player);

                                    ItemStack bannerStack = new ItemBuilder(Material.BANNER, 1, (byte) 4).toItemStack();
                                    player.getInventory().setHelmet(bannerStack);

                                    try {

                                        banner.getBlock().setType(Material.AIR);
                                        banner.getBlock().getState().update(true);

                                    } catch (Exception ignored) {
                                    }

                                    return;

                                }

                                if(player.equals(data.getBlueFlagHolder()) && banner.getLocation().equals(data.getBlueFlagBaseLocation())) {

                                    player.sendMessage("§aDu hast erfolgreich deine Flagge gesichert!");

                                    player.getInventory().setHelmet(null);
                                    data.setBlueFlagHolder(null);

                                    try {

                                        banner.setBaseColor(DyeColor.BLUE);
                                        banner.update();

                                    } catch (Exception ignored) {

                                    }

                                    return;


                                }

                                if (!player.equals(data.getRedFlagHolder()) && banner.getLocation().equals(data.getBlueFlagBaseLocation())) {
                                    player.sendMessage("§cDu kannst deine eigene Flagge nicht aufheben!");
                                    return;
                                } else if(player.equals(data.getRedFlagHolder()) && banner.getLocation().equals(data.getBlueFlagBaseLocation())){

                                    player.getInventory().setHelmet(null);
                                    player.sendMessage("§7Du hast die Flagge gesichert. GG");
                                    Utils.win(Teams.BLUE);

                                    try {

                                        banner.setBaseColor(DyeColor.RED);
                                        banner.update();

                                    } catch (Exception ignored) {

                                    }


                                } else {
                                    return;
                                }

                            } else if (teams.equals(Teams.RED)) {

                                if(player.equals(data.getRedFlagHolder())) {
                                    return;
                                }

                                if (banner.getBaseColor().equals(DyeColor.BLUE)) {

                                    ItemStack bannerStack = new ItemBuilder(Material.BANNER, 1, (byte) 4).toItemStack();
                                    player.getInventory().setHelmet(bannerStack);

                                    data.setBlueFlagHolder(player);

                                    try {

                                        if (banner.getLocation().equals(data.getBlueFlagBaseLocation())) {
                                            banner.setBaseColor(DyeColor.WHITE);
                                            banner.update();
                                        } else {
                                            banner.getBlock().setType(Material.AIR);
                                            banner.getBlock().getState().update(true);
                                        }

                                    } catch (Exception exception) {

                                        exception.printStackTrace();

                                    }
                                }


                            }

                            break;

                        case RED_FLAG:

                            if (teams.equals(Teams.RED)) {

                                if (!banner.getLocation().equals(data.getRedFlagBaseLocation()) && !player.equals(data.getBlueFlagHolder())) {
                                    player.sendMessage("§bDu hast die Flagge deines Teams - sichere sie!");
                                    data.setRedFlagHolder(player);

                                    ItemStack bannerStack = new ItemBuilder(Material.BANNER, 1, (byte) 1).toItemStack();
                                    player.getInventory().setHelmet(bannerStack);

                                    try {

                                        banner.getBlock().setType(Material.AIR);
                                        banner.getBlock().getState().update(true);

                                    } catch (Exception ignored) {
                                    }

                                    return;

                                }

                                if(player.equals(data.getRedFlagHolder()) && banner.getLocation().equals(data.getRedFlagBaseLocation())) {

                                    player.sendMessage("§aDu hast erfolgreich deine Flagge gesichert!");

                                    player.getInventory().setHelmet(null);
                                    data.setRedFlagHolder(null);

                                    try {

                                        banner.setBaseColor(DyeColor.RED);
                                        banner.update();

                                    } catch (Exception ignored) {

                                    }

                                    return;


                                }

                                if (!player.equals(data.getBlueFlagHolder()) && banner.getLocation().equals(data.getRedFlagBaseLocation())) {
                                    player.sendMessage("§cDu kannst deine eigene Flagge nicht aufheben!");
                                    return;
                                } else if(player.equals(data.getBlueFlagHolder()) && banner.getLocation().equals(data.getRedFlagBaseLocation())){

                                    player.getInventory().setHelmet(null);
                                    player.sendMessage("§7Du hast die Flagge gesichert. GG");
                                    Utils.win(Teams.RED);

                                    try {

                                        banner.setBaseColor(DyeColor.BLUE);
                                        banner.update();

                                    } catch (Exception ignored) {

                                    }

                                } else {
                                    return;
                                }


                            } else if (teams.equals(Teams.BLUE)) {

                                if(player.equals(data.getBlueFlagHolder())) {
                                    return;
                                }

                                if (banner.getBaseColor().equals(DyeColor.RED)) {

                                    ItemStack bannerStack = new ItemBuilder(Material.BANNER, 1, (byte) 1).toItemStack();
                                    player.getInventory().setHelmet(bannerStack);

                                    data.setRedFlagHolder(player);

                                    try {

                                        if (banner.getLocation().equals(data.getRedFlagBaseLocation())) {
                                            banner.setBaseColor(DyeColor.WHITE);
                                            banner.update();
                                        } else {
                                            banner.getBlock().setType(Material.AIR);
                                            banner.getBlock().getState().update(true);
                                        }


                                    } catch (Exception exception) {

                                        exception.printStackTrace();

                                    }
                                }

                            }

                            break;

                    }

                }

            }

        }
    }

}
