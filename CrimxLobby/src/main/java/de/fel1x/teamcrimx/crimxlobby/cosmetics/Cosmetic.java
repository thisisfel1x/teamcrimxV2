package de.fel1x.teamcrimx.crimxlobby.cosmetics;

import de.fel1x.teamcrimx.crimxlobby.cosmetics.armor.RainbowArmor;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.dropTrails.MoneyShoeTrail;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.dropTrails.RolexShoeTrail;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.dropTrails.SnowShoeTrail;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.gadgets.FireworkGadget;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.gadgets.FunGunGadget;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.heads.FlowerHead;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.heads.GlassHead;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.heads.SlimeHead;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.trails.HeartShoeCosmetic;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.trails.LibraryShoeCosmetic;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.trails.NoteShoeCosmetic;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.trails.WitchShoeCosmetic;

public enum Cosmetic {

    FLOWER_HEAD(FlowerHead.class, CosmeticCategory.HEADS),
    SLIME_HEAD(SlimeHead.class, CosmeticCategory.HEADS),
    GLASS_HEAD(GlassHead.class, CosmeticCategory.HEADS),

    HEART_SHOE(HeartShoeCosmetic.class, CosmeticCategory.BOOTS),
    NOTE_SHOE(NoteShoeCosmetic.class, CosmeticCategory.BOOTS),
    SPELL_SHOE(WitchShoeCosmetic.class, CosmeticCategory.BOOTS),
    LIBRARY_SHOE(LibraryShoeCosmetic.class, CosmeticCategory.BOOTS),

    CLOCK_TRAIL(RolexShoeTrail.class, CosmeticCategory.TRAILS),
    DIAMOND_TRAIL(MoneyShoeTrail.class, CosmeticCategory.TRAILS),
    SNOW_TRAIL(SnowShoeTrail.class, CosmeticCategory.TRAILS),

    RGB_ARMOR(RainbowArmor.class, CosmeticCategory.GADGETS),
    FUNGUN(FunGunGadget.class, CosmeticCategory.GADGETS),
    FIREWORK(FireworkGadget.class, CosmeticCategory.GADGETS);


    private final Class<? extends ICosmetic> cosmeticClass;
    private final CosmeticCategory cosmeticCategory;

    Cosmetic(Class<? extends ICosmetic> cosmeticClass, CosmeticCategory cosmeticCategory) {
        this.cosmeticClass = cosmeticClass;
        this.cosmeticCategory = cosmeticCategory;
    }

    public Class<? extends ICosmetic> getCosmeticClass() {
        return this.cosmeticClass;
    }

    public CosmeticCategory getCosmeticCategory() {
        return this.cosmeticCategory;
    }
}
