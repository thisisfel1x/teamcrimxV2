package de.fel1x.teamcrimx.mlgwars.utils;

import de.fel1x.teamcrimx.crimxapi.utils.actionbar.ActionbarImpl;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import net.kyori.adventure.text.Component;

import java.util.UUID;

public class MlgActionbar extends ActionbarImpl {

    private final MlgWars mlgWars;
    private final UUID playerUUID;

    public MlgActionbar(MlgWars mlgWars, UUID playerUUID) {
        this.mlgWars = mlgWars;
        this.playerUUID = playerUUID;
    }

    @Override
    public Component getAdditionalComponent() {
        int teamId = this.mlgWars.getData().getGamePlayers().get(this.playerUUID).getPlayerMlgWarsTeamId() + 1;
        return this.mlgWars.miniMessage().parse("<gray>Team <green>#" + teamId);
    }

}
