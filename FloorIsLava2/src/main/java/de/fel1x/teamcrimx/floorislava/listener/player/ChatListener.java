package de.fel1x.teamcrimx.floorislava.listener.player;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.IPermissionGroup;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import de.fel1x.teamcrimx.crimxapi.coins.CoinsAPI;
import de.fel1x.teamcrimx.floorislava.Data;
import de.fel1x.teamcrimx.floorislava.FloorIsLava;
import de.fel1x.teamcrimx.floorislava.gamehandler.Gamestate;
import de.fel1x.teamcrimx.floorislava.gameplayer.GamePlayer;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    private final FloorIsLava floorIsLava;

    public ChatListener(FloorIsLava floorIsLava) {
        this.floorIsLava = floorIsLava;
        Data data = floorIsLava.getData();
        floorIsLava.getPluginManager().registerEvents(this, floorIsLava);
    }

    @EventHandler
    public void on(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = new GamePlayer(player);
        Gamestate currentState = this.floorIsLava.getGamestateHandler().getGamestate();
        IPermissionUser iPermissionUser = CloudNetDriver.getInstance().getPermissionManagement().getUser(player.getUniqueId());
        if (iPermissionUser == null)
            return;
        IPermissionGroup permissionGroup = CloudNetDriver.getInstance().getPermissionManagement().getHighestPermissionGroup(iPermissionUser);
        if ((currentState == Gamestate.PREGAME || currentState == Gamestate.FARMING || currentState == Gamestate.RISING) &&
                gamePlayer.isSpectator()) {
            String format = "§8§o[§4✖§8] " + ChatColor.translateAlternateColorCodes('&', permissionGroup.getDisplay()) + player.getName() + " §8» §f" + event.getMessage();
            for (Player spectator : this.floorIsLava.getData().getPlayers())
                spectator.sendMessage(format);
            return;
        }
        event.setFormat(ChatColor.translateAlternateColorCodes('&', permissionGroup.getDisplay()) + player.getName() + " §8» §f" + event.getMessage());
        if (currentState.equals(Gamestate.ENDING)) {
            String message = event.getMessage().toLowerCase();
            if (message.equalsIgnoreCase("gg") || message
                    .equalsIgnoreCase("bg")) {
                int coins = message.equalsIgnoreCase("gg") ? 10 : -10;
                if (!(Boolean) this.floorIsLava.getData().getPlayerGG().get(player.getUniqueId())) {
                    this.floorIsLava.getData().getPlayerGG().put(player.getUniqueId(), Boolean.TRUE);
                    player.sendMessage(this.floorIsLava.getPrefix() + "§7Du hast §e" + coins + " Coins §7erhalten!");
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2.0F, 1.75F);
                    CoinsAPI coinsAPI = new CoinsAPI(player.getUniqueId());
                    coinsAPI.addCoins(coins);
                }
            }
        }
    }
}
