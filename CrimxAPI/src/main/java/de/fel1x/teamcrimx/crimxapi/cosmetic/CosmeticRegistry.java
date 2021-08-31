package de.fel1x.teamcrimx.crimxapi.cosmetic;

import de.fel1x.teamcrimx.crimxapi.cosmetic.cosmetics.effects.CelebrationEffect;
import de.fel1x.teamcrimx.crimxapi.cosmetic.cosmetics.effects.CompanionEffect;
import de.fel1x.teamcrimx.crimxapi.cosmetic.cosmetics.effects.HaloEffect;
import de.fel1x.teamcrimx.crimxapi.cosmetic.cosmetics.effects.RingEffect;
import de.fel1x.teamcrimx.crimxapi.cosmetic.cosmetics.gadgets.*;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum CosmeticRegistry {

    /* EFFECT */
    COMPANION_EFFECT(CompanionEffect.class, CosmeticCategory.EFFECT),
    RING_EFFECT(RingEffect.class, CosmeticCategory.EFFECT),
    CELEBRATION_EFFECT(CelebrationEffect.class, CosmeticCategory.EFFECT),
    HALO_EFFECT(HaloEffect.class, CosmeticCategory.EFFECT),

    CHICKEN_BOMB_GADGET(ChickenBombGadget.class, CosmeticCategory.GADGETS),
    FUN_GUN_GADGET(FunGunGadget.class, CosmeticCategory.GADGETS),
    MELON_THROWER_GADGET(MelonThrowerGadget.class, CosmeticCategory.GADGETS),
    GRAPPLING_HOOK(GrapplingHookGadget.class, CosmeticCategory.GADGETS),
    PAINTBALL_GUN_GADGET(PaintballGunGadget.class, CosmeticCategory.GADGETS);

    private final Class<? extends BaseCosmetic> cosmeticClass;
    private final CosmeticCategory cosmeticCategory;

    CosmeticRegistry(Class<? extends BaseCosmetic> cosmeticClass, CosmeticCategory cosmeticCategory) {
        this.cosmeticClass = cosmeticClass;
        this.cosmeticCategory = cosmeticCategory;
    }

    public Class<? extends BaseCosmetic> getCosmeticClass() {
        return this.cosmeticClass;
    }

    public CosmeticCategory getCosmeticCategory() {
        return this.cosmeticCategory;
    }

    public static Collection<CosmeticRegistry> filterByCategory(CosmeticCategory cosmeticCategory) {
        return Stream.of(CosmeticRegistry.values())
                .filter(cosmeticRegistry -> cosmeticRegistry.getCosmeticCategory() == cosmeticCategory)
                .collect(Collectors.toList());
    }

}
