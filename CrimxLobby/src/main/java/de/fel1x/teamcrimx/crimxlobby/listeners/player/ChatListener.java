package de.fel1x.teamcrimx.crimxlobby.listeners.player;

import de.fel1x.teamcrimx.crimxapi.clanSystem.player.IClanPlayer;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {

    public ChatListener(CrimxLobby crimxLobby) {
        crimxLobby.getPluginManager().registerEvents(this, crimxLobby);
    }

    @EventHandler
    public void on(AsyncChatEvent event) {

        /*IClanPlayer clanPlayer = (IClanPlayer) event.getPlayer().getMetadata("iClanPlayer").get(0).value();
        Component clanComponent = Component.empty();

        if(clanPlayer != null) {
            clanComponent = clanComponent.append(Component.text("[", NamedTextColor.GRAY))
                    .append(Component.text(clanPlayer.getCurrentClan().getClanTag(), NamedTextColor.YELLOW)
                    .append(Component.text("] ", NamedTextColor.GRAY)));
        }

        Component finalClanComponent = clanComponent; */

        event.renderer((source, sourceDisplayName, message, viewer) -> sourceDisplayName
                .append(Component.text(": ", NamedTextColor.DARK_GRAY))
                .append(message.color(NamedTextColor.WHITE)));

    }
}
