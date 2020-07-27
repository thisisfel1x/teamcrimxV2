package de.fel1x.teamcrimx.crimxlobby.cosmetics;

import de.fel1x.teamcrimx.crimxlobby.cosmetics.armor.RainbowArmor;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.dropTrails.MoneyShoeTrail;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.dropTrails.RolexShoeTrail;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.dropTrails.SnowShoeTrail;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.trails.HeartShoeCosmetic;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.trails.NoteShoeCosmetic;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.trails.WitchShoeCosmetic;

public enum Cosmetic {

    HEART_SHOE(HeartShoeCosmetic.class, CosmeticCategory.TRAILS),
    NOTE_SHOE(NoteShoeCosmetic.class, CosmeticCategory.TRAILS),
    SPELL_SHOE(WitchShoeCosmetic.class, CosmeticCategory.TRAILS),
    CLOCK_TRAIL(RolexShoeTrail.class, CosmeticCategory.TRAILS),
    DIAMOND_TRAIL(MoneyShoeTrail.class, CosmeticCategory.TRAILS),
    SNOW_TRAIL(SnowShoeTrail.class, CosmeticCategory.TRAILS),

    RGB_ARMOR(RainbowArmor.class, CosmeticCategory.GADGETS);


    private Class<? extends ICosmetic> cosmeticClass;
    private CosmeticCategory cosmeticCategory;

    Cosmetic(Class<? extends ICosmetic> cosmeticClass, CosmeticCategory cosmeticCategory) {
        this.cosmeticClass = cosmeticClass;
        this.cosmeticCategory = cosmeticCategory;
    }

    public Class<? extends ICosmetic> getCosmeticClass() {
        return cosmeticClass;
    }

    public CosmeticCategory getCosmeticCategory() {
        return cosmeticCategory;
    }
}
