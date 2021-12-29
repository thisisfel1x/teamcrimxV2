package de.fel1x.teamcrimx.mlgwars.maphandler.gametype.types;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class TournamentTntMadnessGameType extends Tournament {

    private final TntMadnessGameType tntMadnessGameType;

    public TournamentTntMadnessGameType(MlgWars mlgWars) {
        super(mlgWars);
        this.tntMadnessGameType = new TntMadnessGameType(mlgWars);
    }

    @Override
    public ArrayList<ItemStack> getTierOneItems() {
        return this.tntMadnessGameType.getTierOneItems();
    }

    @Override
    public ArrayList<ItemStack> getTierTwoItems() {
        return this.tntMadnessGameType.getTierTwoItems();
    }

    @Override
    public void gameInit() {
        this.tntMadnessGameType.gameInit();
    }

    @Override
    public void gameTick() {
        this.tntMadnessGameType.gameTick();
    }
}
