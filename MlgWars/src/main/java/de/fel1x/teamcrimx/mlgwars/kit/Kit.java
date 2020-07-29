package de.fel1x.teamcrimx.mlgwars.kit;

import de.fel1x.teamcrimx.mlgwars.kit.kits.ExploderKit;
import de.fel1x.teamcrimx.mlgwars.kit.kits.GrapplerKit;
import de.fel1x.teamcrimx.mlgwars.kit.kits.MinerKit;
import de.fel1x.teamcrimx.mlgwars.kit.kits.StandardKit;

public enum Kit {

    STARTER(StandardKit.class),
    GRAPPLER(GrapplerKit.class),
    EXPLODER(ExploderKit.class),
    MINER(MinerKit.class);

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
