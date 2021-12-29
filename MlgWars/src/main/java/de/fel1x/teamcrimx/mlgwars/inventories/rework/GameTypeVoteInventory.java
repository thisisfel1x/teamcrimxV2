package de.fel1x.teamcrimx.mlgwars.inventories.rework;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.maphandler.gametype.GameType;
import de.fel1x.teamcrimx.mlgwars.maphandler.gametype.types.*;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class GameTypeVoteInventory {

    private final MlgWars mlgWars;

    private final List<ImplementedMode> activeModes;
    private final HashMap<ImplementedMode, ArrayList<Player>> votes;

    public GameTypeVoteInventory(MlgWars mlgWars) {
        this.mlgWars = mlgWars;

        this.activeModes = new ArrayList<>(Arrays.stream(ImplementedMode.values())
                .filter(mode -> mode.active && mode.isVotable()).toList());
        this.votes = new HashMap<>();

        for (ImplementedMode activeMode : this.activeModes) {
            this.votes.put(activeMode, new ArrayList<>());
        }
    }

    public void openInventory(Player player) {
        Gui gui = Gui.gui()
                .title(Component.text("● Übersicht"))
                .rows(1)
                .create();

        for (ImplementedMode activeMode : this.activeModes) {
            int votes = this.votes.get(activeMode).size();
            gui.addItem(ItemBuilder.from(activeMode.skull)
                    .lore(this.mlgWars.miniMessage().parse("<gray>Stimmen: <aqua>"
                            + votes))
                    .glow(this.votes.get(activeMode).contains(player))
                    .asGuiItem(event -> {
                if(!event.getWhoClicked().getUniqueId().equals(player.getUniqueId())) {
                    return;
                }
                player.closeInventory();
                if (this.mlgWars.getLobbyCountdown() <= 10) {
                    player.sendMessage(this.mlgWars.getPrefix() + "§7Du kannst nicht mehr abstimmen");
                    return;
                }
                this.votes.forEach((implementedMode, players) -> players.remove(player));
                this.votes.get(activeMode).add(player);
            }));
        }
        gui.open(player);
    }

    public ImplementedMode calculateMode() {
        ImplementedMode toReturn = ImplementedMode.NORMAL;
        int highestVotes = 0;
        for (ImplementedMode activeMode : this.activeModes) {
            int toCompare = this.votes.get(activeMode).size();
            if(toCompare != 0 && toCompare > highestVotes) {
                if(ThreadLocalRandom.current().nextBoolean())       {
                    toReturn = activeMode;
                    highestVotes = toCompare;
                }
            }
        }
        return toReturn;
    }

    public void quit(Player player) {
        this.votes.forEach((implementedMode, players) -> players.remove(player));
    }

    public enum ImplementedMode {
        NORMAL(SoloGameType.class, true, true, ItemBuilder.skull()
                .name(MlgWars.getInstance().miniMessage().parse("<yellow>Normal"))
                .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzQ5MmNhOTQwNzkxMzZkMjUyNTcwM2QzNzVjMjU1N2VhYzIwMWVlN2RkMzljZTExYzY0YTljMzgxNDdlY2M0ZCJ9fX0=")
                .build()),
        TNT_MADNESS(TntMadnessGameType.class, true, true, ItemBuilder.skull()
                .name(MlgWars.getInstance().miniMessage().parse("<#7F3060>TNT-Wahnsinn"))
                .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2FmNTk3NzZmMmYwMzQxMmM3YjU5NDdhNjNhMGNmMjgzZDUxZmU2NWFjNmRmN2YyZjg4MmUwODM0NDU2NWU5In19fQ==")
                .build()),
        HALLOWEEN(HalloweenGameType.class, true, true, ItemBuilder.skull()
                .name(MlgWars.getInstance().miniMessage().parse("<#FF7E24>Halloween"))
                .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTMyMWNlNDcwZjIzMmI5ZTczZmNlMDkxY2U4NDhkZTkzODYwYTAzMzgxZDMxMDBjNDJiMzk2YzAyNTRiZTBlZiJ9fX0=")
                .build()),
        TOURNAMENT(Tournament.class, false, true, ItemBuilder.skull()
                .name(MlgWars.getInstance().miniMessage().parse("<#32b8e7>Turnier"))
                .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWE2OTZlMTUyMzYzYWQ0NzdlZDZjYjVhNTE4YmZlYTg0YjAzNjE0OGZlY2FjMDU4MWYzMmM1YjUzOTY5NTgifX19")
                .build()),
        TOURNAMENT_TNT(TournamentTntMadnessGameType.class, false, true, ItemBuilder.skull()
                .name(MlgWars.getInstance().miniMessage().parse("<#32b8e7>Turnier (TNT-Wahnsinn)"))
                .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2FmNTk3NzZmMmYwMzQxMmM3YjU5NDdhNjNhMGNmMjgzZDUxZmU2NWFjNmRmN2YyZjg4MmUwODM0NDU2NWU5In19fQ==")
                .build()),
        TEST(null, false, false, ItemBuilder.from(Material.PAPER).build());

        private final Class<? extends GameType> gameType;
        private boolean votable;
        private boolean active;
        private final ItemStack skull;

        ImplementedMode(Class<? extends GameType> gameType, boolean votable, boolean active, ItemStack skull) {
            this.gameType = gameType;
            this.votable = votable;
            this.active = active;
            this.skull = skull;
        }

        public Class<? extends GameType> getGameType() {
            return this.gameType;
        }

        public boolean isVotable() {
            return votable;
        }

        public boolean isActive() {
            return this.active;
        }

        // TODO: webinterface
        public void setActive(boolean active) {
            this.active = active;
        }
    }
}
