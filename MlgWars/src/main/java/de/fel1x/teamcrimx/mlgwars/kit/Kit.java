package de.fel1x.teamcrimx.mlgwars.kit;

import de.fel1x.teamcrimx.mlgwars.kit.kits.*;

public enum Kit {

    // NORMAL KIT's
    STARTER(StandardKit.class), // ja
    GRAPPLER(GrapplerKit.class), // ja
    EXPLODER(ExploderKit.class), // ja
    MINER(MinerKit.class), // nein
    ASTRONAUT(AstronautKit.class), // ja
    SAVER(SaverKit.class), // nein
    GHOST(GhostKit.class), // ja
    THROWER(ThrowerKit.class), // ja
    THOR(ThorKit.class), // ja
    KANGAROO(KangarooKit.class), // ja
    PULLER(PullerKit.class), // ja
    BOAT_GLIDER(BoatGliderKit.class), // ja
    CHICKEN_BRIDGE(ChickenBridgeKit.class),
    WEB_TRAPPER(WebTrapperKit.class), // ja in turtle
    BOT_PVP(NpcKit.class), // ja
    SOUPER(SouperKit.class), // nein
    FARMER(FarmerKit.class), // ja
    ENDER_MAN(EnderPearlKit.class),
    VALORANT(ValorantKit.class),
    ELYTRA(ElytraKit.class),

    // USER-SPECIFIC KIT's
    TORNADO(TornadoKit.class), // ja
    STINKER(StinkerKit.class), // ja
    TANK(TankKit.class), // ja
    TURTLE(TurtleKit.class), // ja
    CSGO(CSGOKit.class), // ja
    DUMP(DumpKit.class), // ja
    NUTS(NutsKit.class);

    // TESTING

    private Class<? extends IKit> clazz;

    Kit(Class<? extends IKit> clazz) {
        this.clazz = clazz;
    }

    public Class<? extends IKit> getClazz() {
        return this.clazz;
    }

    public void setClazz(Class<? extends IKit> clazz) {
        this.clazz = clazz;
    }
}
