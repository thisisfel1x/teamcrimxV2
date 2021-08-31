package de.fel1x.teamcrimx.crimxapi.cosmetic.database;

import de.fel1x.teamcrimx.crimxapi.cosmetic.BaseCosmetic;
import de.fel1x.teamcrimx.crimxapi.cosmetic.CosmeticCategory;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ActiveCosmetics {

    private final Map<CosmeticCategory, @Nullable BaseCosmetic> selectedCosmetic;

    public ActiveCosmetics() {
        this.selectedCosmetic = new HashMap<>();
    }

    public Map<CosmeticCategory, BaseCosmetic> getSelectedCosmetic() {
        return this.selectedCosmetic;
    }
}
