package de.fel1x.capturetheflag.listener.player;

import de.dytanic.cloudnet.ext.cloudperms.bukkit.BukkitCloudNetCloudPermissionsPlugin;
import de.fel1x.capturetheflag.CaptureTheFlag;
import de.fel1x.capturetheflag.Data;
import de.fel1x.capturetheflag.gameplayer.GamePlayer;
import de.fel1x.capturetheflag.gamestate.Gamestate;
import de.fel1x.capturetheflag.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void on(PlayerJoinEvent event) {

        event.setJoinMessage(null);

        Player player = event.getPlayer();
        GamePlayer gamePlayer = new GamePlayer(player);
        Data data = CaptureTheFlag.getInstance().getData();

        Gamestate gamestate = CaptureTheFlag.getInstance().getGamestateHandler().getGamestate();

        //gamePlayer.cleanupInventory();
        CaptureTheFlag.getInstance().getScoreboardHandler().handleJoin(player);

        switch (gamestate) {

            case LOBBY:

                gamePlayer.teleportToLobby();
                gamePlayer.addToInGamePlayers();
                gamePlayer.showToAll();
                gamePlayer.cleanupInventory();
                gamePlayer.cleanupTeams();

                event.setJoinMessage("§a» " + player.getDisplayName() + " §7hat das Spiel betreten!");

                if(data.getPlayers().size() >= 2) {
                    CaptureTheFlag.getInstance().getLobbyTimer().start();
                }

                player.getInventory().setItem(0, new ItemBuilder(Material.BED).setName("§a§lWähle dein Team").addEnchant(Enchantment.DEPTH_STRIDER, 1).addGlow().toItemStack());
                player.getInventory().setItem(1, new ItemBuilder(Material.STORAGE_MINECART).setName("§e§lWähle dein Kit").addEnchant(Enchantment.DEPTH_STRIDER, 1).addGlow().toItemStack());

                break;

            case PREGAME: case INGAME:

                gamePlayer.setSpectator();
                player.sendMessage("§7Du bist nun Spectator");

                break;

            case ENDING:

                gamePlayer.teleportToLobby();

                break;

        }



    }

}
