package de.fel1x.teamcrimx.mlgwars.kit.rework;

import de.fel1x.teamcrimx.mlgwars.kit.rework.kits.*;

public enum KitRegistry {

    STARTER(StarterKit.class),
    GRAPPLER(GrapplerKit.class),
    JETPACK(JetPackKit.class),
    EXPLODER(ExploderKit.class),
    ASTRONAUT(AstronautKit.class),
    BOT_PVP(BotPvPKit.class),
    TRAPPER(TrapperKit.class),
    CHICKEN_BRIDGE(ChickenBridge.class),
    PULLER(PullerKit.class),
    FARMER(FarmerKit.class),
    THROWER(ThrowerKit.class),
    BOAT_GLIDER(BoatGliderKit.class),
    KANGAROO(KangarooKit.class),

    // CUSTOM
    TORNADO(TornadoKit.class),
    STINKER(StinkerKit.class),
    TANK(TankKit.class),
    DUMP(DumpKit.class);

    private final Class<? extends Kit> clazz;

    KitRegistry(Class<? extends Kit> clazz) {
        this.clazz = clazz;
    }

    public Class<? extends Kit> getClazz() {
        return this.clazz;
    }
}
