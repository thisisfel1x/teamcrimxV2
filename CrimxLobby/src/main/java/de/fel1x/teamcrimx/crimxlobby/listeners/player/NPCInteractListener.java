package de.fel1x.teamcrimx.crimxlobby.listeners.player;

import com.github.juliarn.npc.NPC;
import com.github.juliarn.npc.event.PlayerNPCInteractEvent;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.inventories.NPCShopInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NPCInteractListener implements Listener {

    private final CrimxLobby crimxLobby;

    public NPCInteractListener(CrimxLobby crimxLobby) {
        this.crimxLobby = crimxLobby;

        this.crimxLobby.getPluginManager().registerEvents(this, this.crimxLobby);
    }

    @EventHandler
    public void on(PlayerNPCInteractEvent event) {

        NPC npc = event.getNPC();
        Player player = event.getPlayer();

        if (npc.getEntityId() == this.crimxLobby.getPerksNpc().getEntityId()) {
            NPCShopInventory.NPC_SHOP_INVENTORY.open(player);
        }
    }
}
