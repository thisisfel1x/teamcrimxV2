package de.fel1x.teamcrimx.crimxlobby.npc.gamesupport;

import com.github.juliarn.npc.NPC;
import com.github.juliarn.npc.event.PlayerNPCInteractEvent;
import de.dytanic.cloudnet.common.document.gson.JsonDocument;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.*;
import de.dytanic.cloudnet.ext.bridge.BridgeServiceProperty;
import de.dytanic.cloudnet.ext.bridge.ServiceInfoSnapshotUtil;
import de.dytanic.cloudnet.wrapper.Wrapper;
import de.fel1x.teamcrimx.crimxapi.utils.npc.NPCCreator;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.objects.LobbyPlayer;
import de.fel1x.teamcrimx.crimxlobby.objects.Spawn;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MlgWarsNPC implements Listener {

    private final CrimxLobby crimxLobby;
    private NPC npc;
    private Gui gui;

    private final String value = "ewogICJ0aW1lc3RhbXAiIDogMTYxMjQ0MDgyODc4NiwKICAicHJvZmlsZUlkIiA6ICJlYjIwYWVkYTRiYTM0NzVmOTczZGNmZjkzNTE2OGZhYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJTa3lGYWxsZWVlIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2QyNjdiZjAxNjJhMzE2MDUwNDNjNjg5MDE0MTJmOTRlYjNlNzZiYzJkOTdiYjU1ZmI5MTY3ZDk1OGQ4NGNkMTMiCiAgICB9CiAgfQp9";
    private final String signature = "WO0PIZr8WouLUaOJzAjBAo7NXs+u2ezCQD+TdMNXUBf2Okq9/epjSjEu6grYGaRdUTImdm9X6ZyMO3LUbTi11SidJ8FBrPYtZg2rdpazolsV/9IJNz52/pKIhYliwXQnais1wHZmCv1Gb203KfDLmNsM/EYnSDb4tpuArAA2mOl1ITz3OtXCQaFVqNie74gScFtE+TX2Qd6mYhFCYYzjkCUGwIo/z0JpClyKAaYc0bDt9YuTP3hWJRz0jnZvK6vX2pzRgTmK9YebOXokCmMq0Zywt+sGkVzEhz/d9WmD1LVEuFahW7BgisWgNhUzxpUBJfGoJXJhjXg1Vb9giZW67FiEWmsShfo9ijyY8vaw/8jjLPDjqcekChohJwIOlV9kq3PHsDMlA7ULEhCTMUGcUQHw+XKnedFW8too8PfUhP4zNzV21FVl2TFv74MgRllsX9gTwTWJUsCaud7WCSC34/q0Ho+vvPC8slABXjbWdCDQTi09+uUW//f0rlTv3yRPcGNY9gDDK61KRMmJT03UT+skZ/UzxwVoHP098+VunRB2Dtl9dJCQCvsSUAXFHfeZwVq+aj0gzkRMg1RbuoVS0SHPpLMMrvossZQ4XJGYeY77LJdI92JAxK62OId+IOc9SXxz1Bl22r0/FDxxty17sj4S71ZWloUnztB/B1Zm4SY=";

    private final String soloSkull = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjUyN2ViYWU5ZjE1MzE1NGE3ZWQ0OWM4OGMwMmI1YTlhOWNhN2NiMTYxOGQ5OTE0YTNkOWRmOGNjYjNjODQifX19";
    private final String teamSkull = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmI0YzFkYmQ4NTQ0Y2ZhZWQ5NjNkZjRkMTM5YmM4YmFjNGI3NmFjNGJiYjMwYWI4NmY3NmZiYzMxYWI5YTcwIn19fQ==";

    private boolean canInteract = false;

    public MlgWarsNPC(CrimxLobby crimxLobby) {
        this.crimxLobby = crimxLobby;
        this.crimxLobby.getPluginManager().registerEvents(this, this.crimxLobby);

        if(Spawn.MLGWARS_NPC.getSpawn() == null) {
            return;
        }

        this.npc = new NPCCreator(Spawn.MLGWARS_NPC.getSpawn())
                .addHeaders(new Component[]{
                        Component.text("Keine Zeit?", NamedTextColor.GRAY),
                        Component.text("Quickjoin!", TextColor.fromHexString("#7417e4")),
                        Component.text("Falsche Größe?", NamedTextColor.GRAY),
                        Component.text("Server starten!", TextColor.fromHexString("#03f102")) // #6d66f8
                })
                .createProfile(this.value, this.signature)
                .shouldLookAtPlayer(true)
                .shouldImitatePlayer(false)
                .spawn();

        this.gui = Gui.gui()
                .title(Component.text("Wähle eine Teamgröße"))
                .rows(3)
                .create();

        this.gui.setItem(2, 4, ItemBuilder.skull().texture(this.soloSkull)
                        .name(Component.text("Starte einen ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                                .append(Component.text("Solo Server", TextColor.fromHexString("#4995fb"))))
                        .lore(Component.text("Falls verfügbar ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                                .append(Component.text("» ", NamedTextColor.DARK_GRAY))
                                .append(Component.text("Quickjoin!", TextColor.fromHexString("#6d66f8"))))
                .asGuiItem(event -> this.tryToStartServerOrConnect(MlgWarsServerType.SOLO, event.getWhoClicked())));

        this.gui.setItem(2, 6, ItemBuilder.skull().texture(this.teamSkull)
                .name(Component.text("Starte einen ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                        .append(Component.text("Team Server", TextColor.fromHexString("#4995fb"))))
                .lore(Component.text("Falls verfügbar ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                        .append(Component.text("» ", NamedTextColor.DARK_GRAY))
                        .append(Component.text("Quickjoin!", TextColor.fromHexString("#6d66f8"))))
                .asGuiItem(event -> this.tryToStartServerOrConnect(MlgWarsServerType.TEAM, event.getWhoClicked())));

        this.canInteract = true;
    }

    private void tryToStartServerOrConnect(MlgWarsServerType mlgWarsServerType, HumanEntity whoClicked) {
        if(whoClicked instanceof Player player) {
            player.closeInventory();

            int teamSizeRequired = mlgWarsServerType == MlgWarsServerType.SOLO ? 1 : 2;
            List<ServiceInfoSnapshot> possibleServers = Wrapper.getInstance().getCloudServiceProvider()
                    .getCloudServices("MlgWars").stream()
                    .filter(serviceInfoSnapshot -> serviceInfoSnapshot.getProperties().getInt("teamSize", 1) == teamSizeRequired
                            && !serviceInfoSnapshot.getProperty(BridgeServiceProperty.IS_IN_GAME).orElse(true)
                            && !serviceInfoSnapshot.getProperty(BridgeServiceProperty.IS_FULL).orElse(true))
                    .sorted(Comparator.comparingInt(o -> o.getProperty(BridgeServiceProperty.ONLINE_COUNT).orElse(0)))
                    .collect(Collectors.toList());
            Collections.reverse(possibleServers);

            if(!possibleServers.isEmpty()) {
                if(!possibleServers.get(0).getProperty(BridgeServiceProperty.IS_ONLINE).orElse(false)) { // Prevent starting too many services at once while the requested isn't online yet
                    possibleServers.get(0).provider().start();
                    player.sendMessage("§bCloud §8● §aEin passender Server wird gestartet...");
                    return;
                }

                new LobbyPlayer(player.getPlayer()).getCloudPlayer().getPlayerExecutor()
                        .connect(possibleServers.get(0).getName());
            } else {
                player.sendMessage("§bCloud §8● §aEin passender Server wird gestartet...");
                // Start new server
                ServiceTask serviceTask = CloudNetDriver.getInstance().getServiceTaskProvider().getServiceTask("MlgWars");
                if(serviceTask == null) {
                    return;
                }

                ServiceInfoSnapshot serviceInfoSnapshot = ServiceConfiguration.builder()
                        .task(serviceTask)
                        .properties(new JsonDocument("team", mlgWarsServerType != MlgWarsServerType.SOLO))
                        .build().createNewService();

                if(serviceInfoSnapshot != null) {
                    serviceInfoSnapshot.provider().start();
                }
            }
        }
    }

    @EventHandler
    public void on(PlayerNPCInteractEvent event) {
        if(!this.canInteract) {
            return;
        }

        if(event.getNPC().getEntityId() == this.npc.getEntityId()) {
            this.gui.open(event.getPlayer());
        }
    }

    public enum MlgWarsServerType {
        SOLO, TEAM;
    }
}
