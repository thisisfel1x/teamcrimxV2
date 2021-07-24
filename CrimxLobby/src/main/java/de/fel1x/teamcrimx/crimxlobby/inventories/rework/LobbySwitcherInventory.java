package de.fel1x.teamcrimx.crimxlobby.inventories.rework;

import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.ext.bridge.BridgeServiceProperty;
import de.dytanic.cloudnet.ext.bridge.ServiceInfoSnapshotUtil;
import de.dytanic.cloudnet.wrapper.Wrapper;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.objects.LobbyPlayer;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

public class LobbySwitcherInventory implements InventoryProvider {

    private final String informationSkullBase64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDYxNzhhZDUxZmQ1MmIxOWQwYTM4ODg3MTBiZDkyMDY4ZTkzMzI1MmFhYzZiMTNjNzZlN2U2ZWE1ZDMyMjYifX19";
    private int count;

    public static final SmartInventory LOBBY_SWITCHER_INVENTORY = SmartInventory.builder()
            .id("LOBBY_SWITCHER_INVENTORY")
            .provider(new LobbySwitcherInventory())
            .size(6, 9)
            .title("§8● §eProfil")
            .manager(CrimxLobby.getInstance().getInventoryManager())
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {
        Pagination pagination = contents.pagination();

        contents.fillRow(0, ClickableItem.empty(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
                .setName(Component.empty()).toItemStack()));
        contents.fillRow(5, ClickableItem.empty(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
                .setName(Component.empty()).toItemStack()));

        // Pagination Items
        // TODO: global variables
        contents.set(5, 7, ClickableItem.of(new ItemBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ2OWUwNmU1ZGFkZmQ4NGU1ZjNkMWMyMTA2M2YyNTUzYjJmYTk0NWVlMWQ0ZDcxNTJmZGM1NDI1YmMxMmE5In19fQ==")
                .setName(Component.text("§8● §7Vorherige Seite")).toItemStack(), event -> {
            if(!pagination.isFirst()) {
                LOBBY_SWITCHER_INVENTORY.open(player, pagination.previous().getPage());
            }
        }));
        contents.set(5, 8, ClickableItem.of(new ItemBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTliZjMyOTJlMTI2YTEwNWI1NGViYTcxM2FhMWIxNTJkNTQxYTFkODkzODgyOWM1NjM2NGQxNzhlZDIyYmYifX19")
                .setName(Component.text("§8● §7Nächste Seite")).toItemStack(), event -> {
            if(!pagination.isLast()) {
                LOBBY_SWITCHER_INVENTORY.open(player, pagination.next().getPage());
            }
        }));

    }

    @Override
    public void update(Player player, InventoryContents contents) {
        int state = contents.property("state", 0);
        contents.setProperty("state", state + 1);

        if(state % 20 != 0) {
            return;
        }

        Pagination pagination = contents.pagination();

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

        contents.set(0, 4, ClickableItem.empty(new ItemBuilder(this.informationSkullBase64)
                .setName(Component.text("●", NamedTextColor.DARK_GRAY).append(Component.space())
                        .append(Component.text("Informationen", NamedTextColor.YELLOW))
                        .asComponent().decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                .setLore(Component.empty(),
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
                .toItemStack()));

        pagination.setItems((ClickableItem) null);

        ClickableItem[] servers = new ClickableItem[onlineServiceCount];
        this.count = 0;

        lobbyServiceInfoSnapshots.forEach(serviceInfoSnapshot -> {
            boolean sameServer = Wrapper.getInstance().getServiceId().getName().equalsIgnoreCase(serviceInfoSnapshot.getName());

            servers[this.count] = ClickableItem.of(new ItemBuilder(Material.LIME_DYE)
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
                                    .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
                            (sameServer) ? Component.newline()
                                    .append(Component.text("Du befindest dich hier", NamedTextColor.GREEN))
                                    .append(Component.newline()).asComponent()
                                    .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                                    : Component.empty())
                    .toItemStack(), event -> new LobbyPlayer(player).getCloudPlayer().getPlayerExecutor().connect(serviceInfoSnapshot.getName()));
            this.count++;
        });

        pagination.setItems(servers);
        pagination.setItemsPerPage(36);

        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 0));
    }
}
