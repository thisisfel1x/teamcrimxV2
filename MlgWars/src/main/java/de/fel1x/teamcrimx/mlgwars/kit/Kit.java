package de.fel1x.teamcrimx.mlgwars.kit;

import de.fel1x.teamcrimx.mlgwars.kit.kits.*;

public enum Kit {

    // NORMAL KIT's
    STARTER(StandardKit.class),
    GRAPPLER(GrapplerKit.class),
    EXPLODER(ExploderKit.class),
    MINER(MinerKit.class),
    ASTRONAUT(AstronautKit.class),
    SAVER(SaverKit.class),
    GHOST(GhostKit.class),
    THROWER(ThrowerKit.class),
    THOR(ThorKit.class),
    KANGAROO(KangarooKit.class),
    PULLER(PullerKit.class),
    BOAT_GLIDER(BoatGliderKit.class),
    CHICKEN_BRIDGE(ChickenBridgeKit.class),
    WEB_TRAPPER(WebTrapperKit.class),
    BOT_PVP(NpcKit.class),
    SOUPER(SouperKit.class),
    FARMER(FarmerKit.class),
    ENDER_MAN(EnderPearlKit.class),

    // USER-SPECIFIC KIT's
    TORNADO(TornadoKit.class),
    STINKER(StinkerKit.class),
    TANK(TankKit.class),
    TURTLE(TurtleKit.class),
    CSGO(CSGOKit.class);

    private Class<? extends IKit> clazz;

    Kit(Class<? extends IKit> clazz) {
        this.clazz = clazz;
    }

    public Class<? extends IKit> getClazz() {
        return clazz;
    }

    public void setClazz(Class<? extends IKit> clazz) {
        this.clazz = clazz;
    }
}
