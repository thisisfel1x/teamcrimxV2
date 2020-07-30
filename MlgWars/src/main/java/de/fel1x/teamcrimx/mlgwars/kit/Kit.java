package de.fel1x.teamcrimx.mlgwars.kit;

import de.fel1x.teamcrimx.mlgwars.kit.kits.*;

public enum Kit {

    STARTER(StandardKit.class),
    GRAPPLER(GrapplerKit.class),
    EXPLODER(ExploderKit.class),
    MINER(MinerKit.class),
    ASTRONAUT(AstronautKit.class),
    SAVER(SaverKit.class);

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
