package de.fel1x.capturetheflag.listener.block;

import de.fel1x.capturetheflag.CaptureTheFlag;
import de.fel1x.capturetheflag.Data;
import de.fel1x.capturetheflag.gameplayer.GamePlayer;
import de.fel1x.capturetheflag.gamestate.Gamestate;
import de.fel1x.capturetheflag.inventories.KitSelector;
import de.fel1x.capturetheflag.inventories.TeamSelector;
import de.fel1x.capturetheflag.team.Team;
import de.fel1x.capturetheflag.utils.WinDetection;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class InteractListener implements Listener {

    private final CaptureTheFlag captureTheFlag;
    private final Data data;

    public InteractListener(CaptureTheFlag captureTheFlag) {
        this.captureTheFlag = captureTheFlag;
        this.data = this.captureTheFlag.getData();

        this.captureTheFlag.getPluginManager().registerEvents(this, this.captureTheFlag);
    }

    @EventHandler
    public void on(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        GamePlayer gamePlayer = new GamePlayer(player);
        Team playerTeam = gamePlayer.getTeam();

        if (!gamePlayer.isPlayer()) {
            return;
        }

        Gamestate gamestate = this.captureTheFlag.getGamestateHandler().getGamestate();

        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        switch (gamestate) {
            case IDLE:
            case LOBBY:
                if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (event.getMaterial().equals(Material.RED_BED)) {
                        TeamSelector.TEAM_SELECTOR.open(player);
                    } else if (event.getMaterial().equals(Material.CHEST_MINECART)) {
                        KitSelector.KIT_SELECTOR.open(player);
                    }
                }
                break;

            case PREGAME:
            case INGAME:
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.hasBlock()) {

                    Block block = event.getClickedBlock();
                    Location blockLocation = block.getLocation();

                    /*
                    Check if player interacts with Blue Banner
                     */
                    if (block.getType() == Material.BLUE_BANNER) {
                        switch (playerTeam) {
                            /*
                            If the player is in team red, he tries to pickup the flag
                             */
                            case RED:
                                /*
                                First of all we have to check if the player already holds a flag
                                 */
                                if (player.equals(this.data.getRedFlagHolder()) && this.data.getRedFlagHolder() != null) {
                                    player.sendMessage(this.captureTheFlag.getPrefix() +
                                            "§7Du hast bereits die Flagge deines Teams aufgehoben!");
                                    return;
                                }
                                /*
                                Now we have to check if the flag is at the base location or
                                somewhere on the map
                                 */
                                if (blockLocation.equals(this.data.getBlueFlagBaseLocation())) {
                                    /*
                                    The blue flag was at the base location, now we set the base banner color to white
                                     */
                                    this.data.getBlueFlagBaseLocation().getBlock().setType(Material.WHITE_BANNER);
                                } else {
                                    /*
                                    In this case, the flag isn't at the base location, we set the block to air
                                     */
                                    block.setType(Material.AIR);
                                }
                                /*
                                In both cases, we can give the player a blue banner as helmet
                                 */
                                player.getInventory().setHelmet(new ItemStack(Material.BLUE_BANNER));
                                player.setGlowing(true);
                                this.data.setBlueFlagHolder(player);

                                break;

                            case BLUE:
                                /*
                                Here we have to check if the player is holding the red flag
                                If true, we have to check if the player interacts with the base banner
                                 */
                                if (player.equals(this.data.getRedFlagHolder()) && this.data.getRedFlagHolder() != null) {
                                    if (blockLocation.equals(this.data.getBlueFlagBaseLocation())) {
                                        new WinDetection(Team.BLUE);
                                        return;
                                    }
                                }
                                /*
                                This is a bit trickier. Team blue can only pick up their flag if the flag isn't at
                                the base location
                                 */
                                else if (!blockLocation.equals(this.data.getBlueFlagBaseLocation())
                                        && this.data.getBlueFlagHolder() == null) {
                                    /*
                                    Now we set the helmet and remove the banner from the ground
                                     */
                                    player.getInventory().setHelmet(new ItemStack(Material.BLUE_BANNER));
                                    block.setType(Material.AIR);

                                    player.setGlowing(true);
                                    this.data.setBlueFlagHolder(player);
                                } else {
                                    /*
                                    If they try, we send them a message
                                     */
                                    player.sendMessage(this.captureTheFlag.getPrefix()
                                            + "§cDu kannst deine eigene Flagge nicht aufheben!");
                                    return;
                                }

                                break;

                        }
                        /*
                        Check if player interacts with Red Banner
                        */
                    } else if (block.getType() == Material.RED_BANNER) {
                        switch (playerTeam) {
                                /*
                                 If the player is in team red, he tries to pickup the flag
                                */
                            case BLUE:
                                    /*
                                    First of all we have to check if the player already holds a flag
                                    */
                                if (player.equals(this.data.getBlueFlagHolder()) && this.data.getBlueFlagHolder() != null) {
                                    player.sendMessage(this.captureTheFlag.getPrefix() +
                                            "§7Du hast bereits die Flagge deines Teams aufgehoben!");
                                    return;
                                }
                                    /*
                                    Now we have to check if the flag is at the base location or
                                    somewhere on the map
                                    */
                                if (blockLocation.equals(this.data.getRedFlagBaseLocation())) {
                                        /*
                                        The red flag was at the base location, now we set the base banner color to white
                                         */
                                    this.data.getRedFlagBaseLocation().getBlock().setType(Material.WHITE_BANNER);
                                } else {
                                        /*
                                        In this case, the flag isn't at the base location, we set the block to air
                                         */
                                    block.setType(Material.AIR);
                                }
                                    /*
                                    In both cases, we can give the player a red banner as helmet
                                    */
                                player.getInventory().setHelmet(new ItemStack(Material.RED_BANNER));

                                player.setGlowing(true);
                                this.data.setRedFlagHolder(player);

                                break;

                            case RED:
                                 /*
                                    Here we have to check if the player is holding the blue flag
                                    If true, we have to check if the player interacts with the base banner
                                    */
                                if (player.equals(this.data.getBlueFlagHolder()) && this.data.getBlueFlagHolder() != null) {
                                    if (blockLocation.equals(this.data.getRedFlagBaseLocation())) {
                                        new WinDetection(Team.RED);
                                        return;
                                    }
                                }
                                /*
                                    This is a bit trickier. Team red can only pick up their flag if the flag isn't at
                                    the base location
                                    */
                                else if (!blockLocation.equals(this.data.getRedFlagBaseLocation())
                                        && this.data.getRedFlagHolder() == null) {
                                        /*
                                        Now we set the helmet and remove the banner from the ground
                                         */
                                    player.getInventory().setHelmet(new ItemStack(Material.RED_BANNER));
                                    block.setType(Material.AIR);

                                    this.data.setRedFlagHolder(player);
                                    player.setGlowing(true);
                                } else {
                                        /*
                                        If they try, we send them a message
                                        */
                                    player.sendMessage(this.captureTheFlag.getPrefix()
                                            + "§cDu kannst deine eigene Flagge nicht aufheben!");
                                    return;
                                }

                                break;
                        }
                    } else if (block.getType() == Material.WHITE_BANNER) {
                        /*
                        Now we have to check what happens if the player interacts with a white banner
                         */
                        switch (playerTeam) {
                            case RED:
                                /*
                                if he is in team red he can either save his own flag or win with the flag of team blue
                                 */
                                // CASE HE HOLDS BLUE FLAG
                                if (player.equals(this.data.getBlueFlagHolder()) && this.data.getBlueFlagHolder() != null) {
                                    if (blockLocation.equals(this.data.getRedFlagBaseLocation())) {
                                        new WinDetection(Team.RED);
                                        return;
                                    }
                                } else if (player.equals(this.data.getRedFlagHolder()) && this.data.getRedFlagHolder() != null) {
                                    if (blockLocation.equals(this.data.getRedFlagBaseLocation())) {
                                        /*
                                        He saved his own flag. setting the color back to red
                                         */
                                        this.data.getRedFlagBaseLocation().getBlock().setType(Material.RED_BANNER);

                                        /*
                                        remove player as red flag holder - give him back a red leather helmet
                                         */
                                        this.data.setRedFlagHolder(null);
                                        player.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET)
                                                .setLeatherArmorColor(Color.RED).toItemStack());
                                    }
                                }
                                break;
                            case BLUE:
                                /*
                                if he is in team blue he can either save his own flag or win with the flag of team red
                                 */
                                // CASE HE HOLDS RED FLAG
                                if (player.equals(this.data.getRedFlagHolder()) && this.data.getRedFlagHolder() != null) {
                                    if (blockLocation.equals(this.data.getBlueFlagBaseLocation())) {
                                        new WinDetection(Team.BLUE);
                                        return;
                                    }
                                } else if (player.equals(this.data.getBlueFlagHolder()) && this.data.getBlueFlagHolder() != null) {
                                    if (blockLocation.equals(this.data.getBlueFlagBaseLocation())) {
                                        /*
                                        He saved his own flag. setting the color back to blue
                                         */
                                        this.data.getBlueFlagBaseLocation().getBlock().setType(Material.BLUE_BANNER);

                                        /*
                                        remove player as blue flag holder - give him back a blue leather helmet
                                         */
                                        this.data.setBlueFlagHolder(null);
                                        player.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET)
                                                .setLeatherArmorColor(Color.BLUE).toItemStack());
                                    }
                                }
                                break;
                        }
                    }
                }
        }
    }
}
