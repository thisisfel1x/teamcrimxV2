package de.fel1x.teamcrimx.crimxlobby.inventories.rework;

import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.ext.bridge.BridgeServiceProperty;
import de.dytanic.cloudnet.ext.bridge.ServiceInfoSnapshotUtil;
import de.dytanic.cloudnet.wrapper.Wrapper;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.objects.LobbyPlayer;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

public class LobbySwitcherInventory {
    private static PaginatedGui lobbySwitcherGui;

    private final String informationSkullBase64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDYxNzhhZDUxZmQ1MmIxOWQwYTM4ODg3MTBiZDkyMDY4ZTkzMzI1MmFhYzZiMTNjNzZlN2U2ZWE1ZDMyMjYifX19";
    private int count;
    private GuiItem[] servers;

    public LobbySwitcherInventory(CrimxLobby crimxLobby) {
        lobbySwitcherGui = Gui.paginated()
                .title(Component.text("● Lobby wechseln"))
                .rows(6)
                .pageSize(36)
                .create();

        lobbySwitcherGui.disableAllInteractions();

        lobbySwitcherGui.getFiller().fillTop(dev.triumphteam.gui.builder.item.ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).name(Component.empty()).asGuiItem());
        lobbySwitcherGui.getFiller().fillBottom(dev.triumphteam.gui.builder.item.ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).name(Component.empty()).asGuiItem());

        lobbySwitcherGui.setItem(6, 8, dev.triumphteam.gui.builder.item.ItemBuilder.skull()
                .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ2OWUwNmU1ZGFkZmQ4NGU1ZjNkMWMyMTA2M2YyNTUzYjJmYTk0NWVlMWQ0ZDcxNTJmZGM1NDI1YmMxMmE5In19fQ==")
                .name(Component.text("● ", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false).append(Component.text("Vorherige Seite", NamedTextColor.GRAY)))
                .asGuiItem(event -> {
                    lobbySwitcherGui.previous();
                }));

        lobbySwitcherGui.setItem(6, 9, dev.triumphteam.gui.builder.item.ItemBuilder.skull()
                .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTliZjMyOTJlMTI2YTEwNWI1NGViYTcxM2FhMWIxNTJkNTQxYTFkODkzODgyOWM1NjM2NGQxNzhlZDIyYmYifX19")
                .name(Component.text("● ", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false).append(Component.text("Nächste Seite", NamedTextColor.GRAY)))
                .asGuiItem(event -> {
                    lobbySwitcherGui.next();
                }));

        Bukkit.getScheduler().runTaskTimer(crimxLobby, () -> {
            try {
                Field list = PaginatedGui.class.getDeclaredField("pageItems");
                list.setAccessible(true);
                ArrayList<GuiItem> actualList = (ArrayList<GuiItem>) list.get(lobbySwitcherGui);
                actualList.clear();
                list.set(lobbySwitcherGui, actualList);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

            Collection<ServiceInfoSnapshot> lobbyServiceInfoSnapshots = Wrapper.getInstance()
                    .getCloudServiceProvider().getCloudServices("Lobby").stream().filter(snapshot -> snapshot.isConnected() &&
                            snapshot.getProperty(BridgeServiceProperty.IS_ONLINE).orElse(false) &&
                            snapshot.getProperty(BridgeServiceProperty.ONLINE_COUNT).isPresent() &&
                            snapshot.getProperty(BridgeServiceProperty.MAX_PLAYERS).isPresent() &&
                            snapshot.getProperty(BridgeServiceProperty.ONLINE_COUNT).orElse(0) <=
                                    snapshot.getProperty(BridgeServiceProperty.MAX_PLAYERS).orElse(0))
                    .sorted(Comparator.comparingInt(o -> o.getServiceId().getTaskServiceId()))
                    .collect(Collectors.toList());

            int onlinePlayerCount = ServiceInfoSnapshotUtil.getGroupOnlineCount("Lobby");
            int onlineServiceCount = lobbyServiceInfoSnapshots.size();

            lobbySwitcherGui.setItem(1, 5, dev.triumphteam.gui.builder.item.ItemBuilder.skull()
                    .texture(this.informationSkullBase64)
                    .name(Component.text("● ", NamedTextColor.DARK_GRAY)
                            .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                            .append(Component.text("Informationen", NamedTextColor.YELLOW)))
                    .lore(Component.empty(),
                            Component.text("Aktuelle Server", NamedTextColor.GRAY)
                                    .append(Component.space()).append(Component.text("●", NamedTextColor.DARK_GRAY))
                                    .append(Component.space()).append(Component.text(onlineServiceCount, NamedTextColor.GREEN))
                                    .asComponent().decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
                            Component.text("Aktuelle Spieler", NamedTextColor.GRAY)
                                    .append(Component.space()).append(Component.text("●", NamedTextColor.DARK_GRAY))
                                    .append(Component.space()).append(Component.text(onlinePlayerCount, NamedTextColor.GREEN)
                                    .append(Component.text("/", NamedTextColor.DARK_GRAY))
                                    .append(Component.text(onlineServiceCount * 25, NamedTextColor.RED)))
                                    .asComponent().decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
                            Component.empty())
                    .asGuiItem());

            this.servers = null;
            this.servers = new GuiItem[onlineServiceCount];
            this.count = 0;

            lobbyServiceInfoSnapshots.forEach(serviceInfoSnapshot -> {
                boolean sameServer = Wrapper.getInstance().getServiceId().getName().equalsIgnoreCase(serviceInfoSnapshot.getName());

                ItemBuilder itemBuilder = new ItemBuilder(Material.LIME_DYE)
                        .setName(Component.text("●", NamedTextColor.DARK_GRAY).append(Component.space())
                                .append(Component.text(serviceInfoSnapshot.getName(), NamedTextColor.GREEN))
                                .asComponent().decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                        .addGlow(sameServer)
                        .setLore(Component.empty(),
                                Component.text("Aktuelle Spieler", NamedTextColor.GRAY)
                                        .append(Component.space()).append(Component.text("●", NamedTextColor.DARK_GRAY))
                                        .append(Component.space())
                                        .append(Component.text(String.valueOf(
                                                serviceInfoSnapshot.getProperty(BridgeServiceProperty.ONLINE_COUNT).orElse(0)),
                                                NamedTextColor.GREEN)
                                                .append(Component.text("/", NamedTextColor.DARK_GRAY)))
                                        .append(Component.text(String.valueOf(
                                                serviceInfoSnapshot.getProperty(BridgeServiceProperty.MAX_PLAYERS).orElse(0)),
                                                NamedTextColor.RED))
                                        .asComponent()
                                        .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));

                itemBuilder.addLoreLine(Component.empty());

                if (sameServer) {
                    itemBuilder.addLoreLine(Component.text("Du befindest dich hier", NamedTextColor.GREEN)
                            .asComponent().decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                }

                this.servers[this.count] = dev.triumphteam.gui.builder.item.ItemBuilder.from(itemBuilder.toItemStack()).asGuiItem(event -> {
                    if (event.getWhoClicked() instanceof Player player) {
                        new LobbyPlayer(player).getCloudPlayer().getPlayerExecutor()
                                .connect(serviceInfoSnapshot.getName());
                    }
                });
                this.count++;
            });

            lobbySwitcherGui.addItem(this.servers);
            lobbySwitcherGui.update();

        }, 5 * 20L, 20L);

    }

    public static PaginatedGui getLobbySwitcherGui() {
        return lobbySwitcherGui;
    }
}
