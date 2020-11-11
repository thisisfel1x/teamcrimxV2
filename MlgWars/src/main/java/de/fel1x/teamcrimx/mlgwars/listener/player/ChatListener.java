package de.fel1x.teamcrimx.mlgwars.listener.player;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.IPermissionGroup;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import de.fel1x.teamcrimx.crimxapi.coins.CoinsAPI;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private final MlgWars mlgWars;

    public ChatListener(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
        this.mlgWars.getPluginManager().registerEvents(this, this.mlgWars);
    }

    @EventHandler
    public void on(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();
        GamePlayer gamePlayer = new GamePlayer(player);
        Gamestate currentState = this.mlgWars.getGamestateHandler().getGamestate();

        IPermissionUser iPermissionUser = CloudNetDriver.getInstance().getPermissionManagement().getUser(player.getUniqueId());

        if (iPermissionUser == null) return;

        IPermissionGroup permissionGroup = CloudNetDriver.getInstance().getPermissionManagement().getHighestPermissionGroup(iPermissionUser);

        if (currentState == Gamestate.DELAY || currentState == Gamestate.PREGAME || currentState == Gamestate.INGAME) {
            if (gamePlayer.isSpectator()) {
                String format = "§8§o[§4✖§8] " + ChatColor.translateAlternateColorCodes('&', permissionGroup.getDisplay()) + player.getName() + " §8» §f" + event.getMessage();
                for (Player spectator : this.mlgWars.getData().getPlayers()) {
                    spectator.sendMessage(format);
                }
                return;
            }
        }

        event.setFormat(ChatColor.translateAlternateColorCodes('&', permissionGroup.getDisplay()) + player.getName() + " §8» §f" + event.getMessage());

        if (currentState.equals(Gamestate.ENDING)) {
            String message = event.getMessage().toLowerCase();
            if (message.equalsIgnoreCase("gg")
                    || message.equalsIgnoreCase("bg")) {

                int coins = (message.equalsIgnoreCase("gg") ? 10 : -10);

                if (!this.mlgWars.getData().getPlayerGg().get(player.getUniqueId())) {
                    this.mlgWars.getData().getPlayerGg().put(player.getUniqueId(), true);

                    player.sendMessage(this.mlgWars.getPrefix() + "§7Du hast §e" + coins + " Coins §7erhalten!");
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2f, 1.75f);

                    CoinsAPI coinsAPI = new CoinsAPI(player.getUniqueId());
                    coinsAPI.addCoins(coins);

                }
            }
        }
    }
}
