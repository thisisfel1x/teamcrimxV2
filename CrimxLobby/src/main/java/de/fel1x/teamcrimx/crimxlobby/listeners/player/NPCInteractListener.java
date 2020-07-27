package de.fel1x.teamcrimx.crimxlobby.listeners.player;

import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.inventories.NPCInventory;
import net.jitse.npclib.api.NPC;
import net.jitse.npclib.api.events.NPCInteractEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NPCInteractListener implements Listener {

    private CrimxLobby crimxLobby;

    public NPCInteractListener(CrimxLobby crimxLobby) {
        this.crimxLobby = crimxLobby;

        this.crimxLobby.getPluginManager().registerEvents(this, this.crimxLobby);
    }

    @EventHandler
    public void on(NPCInteractEvent event) {

        NPC npc = event.getNPC();
        Player player = event.getWhoClicked();

        NPC playerNpc = this.crimxLobby.getData().getPlayerNPCs().get(player.getUniqueId());

        if (npc.getId().equalsIgnoreCase(playerNpc.getId())) {
            NPCInventory.NPC_INVENTORY.open(player);
        }

    }

}
