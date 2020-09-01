package de.fel1x.capturetheflag.listener.player;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.IPermissionGroup;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import de.fel1x.capturetheflag.CaptureTheFlag;
import de.fel1x.capturetheflag.Data;
import de.fel1x.capturetheflag.gameplayer.GamePlayer;
import de.fel1x.capturetheflag.gamestate.Gamestate;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    Data data = CaptureTheFlag.getInstance().getData();

    @EventHandler
    public void on(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();
        GamePlayer gamePlayer = new GamePlayer(player);
        Gamestate gamestate = CaptureTheFlag.getInstance().getGamestateHandler().getGamestate();

        if (gamestate.equals(Gamestate.LOBBY) || gamestate.equals(Gamestate.ENDING)) {

            IPermissionUser iPermissionUser = CloudNetDriver.getInstance().getPermissionManagement().getUser(player.getUniqueId());

            if (iPermissionUser == null) return;

            IPermissionGroup permissionGroup = CloudNetDriver.getInstance().getPermissionManagement().getHighestPermissionGroup(iPermissionUser);

            event.setFormat(ChatColor.translateAlternateColorCodes('&', permissionGroup.getDisplay()) + player.getName() + " §8» §f" + event.getMessage());
        } else {

            if (data.getSpectators().contains(player)) {

                event.setCancelled(true);
                data.getSpectators().forEach(spectator -> {
                    spectator.sendMessage("§7§o" + player.getName() + " §8» §f" + event.getMessage());
                });

            } else if (data.getPlayers().contains(player)) {
                if (!event.getMessage().startsWith("@a ") && !event.getMessage().startsWith("@all ")) {
                    gamePlayer.getTeam().getTeamPlayers().forEach(teamplayer -> {

                        event.setCancelled(true);
                        teamplayer.sendMessage(player.getDisplayName() + " §8» §f" + event.getMessage());

                    });

                } else {

                    String message = event.getMessage();
                    String[] split = null;

                    if (message.startsWith("@a ")) {
                        split = message.split("@a ");
                    } else if (message.startsWith("@all ")) {
                        split = message.split("@all ");
                    }

                    if (split != null) event.setFormat("§7[@all] " + player.getDisplayName() + " §8» §f" + split[1]);
                }

            }

        }

    }

}
