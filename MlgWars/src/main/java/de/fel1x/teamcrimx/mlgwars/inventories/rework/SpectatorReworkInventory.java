package de.fel1x.teamcrimx.mlgwars.inventories.rework;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class SpectatorReworkInventory {

    private final MlgWars mlgWars;

    public SpectatorReworkInventory(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
    }

    public void openInventory(Player player) {
        Gui gui = Gui.gui()
                .title(Component.text("● Übersicht"))
                .rows(Math.round(this.mlgWars.getSelectedMap().getSize().getSize() / 9f) + 1)
                .create();

        for (GamePlayer gamePlayer : this.mlgWars.getData().getGamePlayers().values()) {
            if(!gamePlayer.isPlayer()) {
                continue;
            }
            gui.addItem(ItemBuilder.skull()
                    .owner(gamePlayer.getPlayer())
                    .name(gamePlayer.getPlayer().displayName())
                            .lore(this.mlgWars.miniMessage().parse("<gray>Team <aqua>#" + (gamePlayer.getPlayerMlgWarsTeamId() + 1)))
                    .asGuiItem(event -> {
                        if(!event.getWhoClicked().getUniqueId().equals(player.getUniqueId())) {
                            return;
                        }
                        player.closeInventory();
                        if(!gamePlayer.isPlayer()) {
                            player.sendMessage(this.mlgWars.prefix().append(this.mlgWars.miniMessage()
                                    .parse("<red>Der Spieler " + gamePlayer.getPlayer().getName() + " lebt nicht mehr")));
                            return;
                        }
                        player.teleport(gamePlayer.getPlayer().getLocation());
            }));
        }
        gui.open(player);
    }
}
