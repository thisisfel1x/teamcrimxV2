package de.fel1x.teamcrimx.crimxapi.cosmetic;

import de.fel1x.teamcrimx.crimxapi.cosmetic.cosmetics.effects.CelebrationEffect;
import de.fel1x.teamcrimx.crimxapi.cosmetic.cosmetics.effects.CompanionEffect;
import de.fel1x.teamcrimx.crimxapi.cosmetic.cosmetics.effects.HaloEffect;
import de.fel1x.teamcrimx.crimxapi.cosmetic.cosmetics.effects.RingEffect;
import de.fel1x.teamcrimx.crimxapi.cosmetic.cosmetics.gadgets.ChickenBomb;

public enum CosmeticRegistry {

    /* EFFECT */
    COMPANION_EFFECT(CompanionEffect.class, CosmeticCategory.EFFECT),
    RING_EFFECT(RingEffect.class, CosmeticCategory.EFFECT),
    CELEBRATION_EFFECT(CelebrationEffect.class, CosmeticCategory.EFFECT),
    HALO_EFFECT(HaloEffect.class, CosmeticCategory.EFFECT),

    CHICKEN_BOMB_GADGET(ChickenBomb.class, CosmeticCategory.GADGETS);

    private final Class<? extends ICosmetic> cosmeticClass;
    private final CosmeticCategory cosmeticCategory;

    CosmeticRegistry(Class<? extends ICosmetic> cosmeticClass, CosmeticCategory cosmeticCategory) {
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
