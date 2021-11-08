package de.fel1x.teamcrimx.mlgwars.inventories.rework;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import de.fel1x.teamcrimx.mlgwars.objects.MlgWarsTeam;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class TeamReworkInventory {

    private final MlgWars mlgWars;
    private final Gui gui;

    public TeamReworkInventory(MlgWars mlgWars) {
        this.mlgWars = mlgWars;

        this.gui = Gui.gui()
                .title(Component.text("● Wähle dein Team"))
                .rows(2)
                .create();

        for (MlgWarsTeam mlgWarsTeam : this.mlgWars.getData().getGameTeams().values()) {
            this.gui.addItem(ItemBuilder.from(mlgWarsTeam.getGUIItemStack()).asGuiItem(event -> {
                if(event.getWhoClicked() instanceof Player player) {
                    GamePlayer gamePlayer = this.mlgWars.getData().getGamePlayers().get(player.getUniqueId());
                    int oldTeamSlot = gamePlayer.setTeam(mlgWarsTeam);

                    if(oldTeamSlot != -1) {
                        this.gui.updateItem(oldTeamSlot, this.mlgWars.getData().getGameTeams().get(oldTeamSlot)
                                .getGUIItemStack());
                    }
                    this.gui.updateItem(event.getSlot(), mlgWarsTeam.getGUIItemStack());
                }
            }));
        }

    }

    public void open(Player player) {
        this.gui.open(player);
    }

    public Gui getGui() {
        return this.gui;
    }
}
