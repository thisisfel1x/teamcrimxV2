package de.fel1x.teamcrimx.crimxlobby.listeners.player;

import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.Data;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.ICosmetic;
import de.fel1x.teamcrimx.crimxlobby.minigames.jumpandrun.JumpAndRunPlayer;
import de.fel1x.teamcrimx.crimxlobby.objects.LobbyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class MoveListener implements Listener {

    final CrimxLobby crimxLobby;
    final Data data;

    public MoveListener(CrimxLobby crimxLobby) {
        this.crimxLobby = crimxLobby;
        this.data = crimxLobby.getData();

        this.crimxLobby.getPluginManager().registerEvents(this, this.crimxLobby);
    }

    @EventHandler
    public void on(PlayerMoveEvent event) {

        Player player = event.getPlayer();
        LobbyPlayer lobbyPlayer = new LobbyPlayer(player);

        if (lobbyPlayer.getSelectedCosmetic() != null) {
            ICosmetic iCosmetic = lobbyPlayer.getSelectedCosmetic();

            switch (iCosmetic.cosmeticType()) {
                case TRAIL:
                    if (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getY() != event.getTo().getY() || event.getFrom().getZ() != event.getTo().getZ()) {
                        player.getWorld().spawnParticle(iCosmetic.getWalkEffect(),
                                player.getLocation().clone().add(0, 0.5, 0), iCosmetic.effectData());
                    }
                    break;

                case BLOCK_TRAIL:
                    if ((player.getLocation().clone().subtract(0, 1, 0).getBlock().getType() != Material.AIR
                            || player.getLocation().clone().subtract(0, 1, 0).getBlock().getType() != Material.SNOW)
                            && player.getLocation().getBlock().getType() == Material.AIR
                            && player.getLocation().subtract(0, 1, 0).getBlock().getType().isBlock()) {
                        Block old = player.getLocation().getBlock();
                        player.getLocation().getBlock().setType(Material.SNOW);
                        Bukkit.getScheduler().runTaskLater(this.crimxLobby, () -> old.setType(Material.AIR), 20 * 3L);
                    }
                    break;

                case DROP_TRAIL:
                    Item item = player.getWorld().dropItem(player.getLocation(), new ItemStack(iCosmetic.itemToDrop()));
                    Bukkit.getScheduler().runTaskLater(this.crimxLobby, item::remove, 20 * 2L);
                    break;
            }
        }

        if (lobbyPlayer.isInJumpAndRun()) {

            JumpAndRunPlayer jumpAndRunPlayer = this.crimxLobby.getData().getJumpAndRunPlayers().get(player.getUniqueId());

            Block block = player.getLocation().clone().add(0, -1, 0).getBlock();

            if (block.getType() == Material.LEGACY_WOOL) {

                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 7, 9);
                lobbyPlayer.generateNextJump();

            } else if (event.getTo().getY() < jumpAndRunPlayer.getCurrentBlock().getY()) {

                lobbyPlayer.endJumpAndRun();
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 5, 8);

            }
        }
    }

}
