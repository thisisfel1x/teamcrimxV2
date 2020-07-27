package de.fel1x.teamcrimx.mlgwars.kit;

import de.fel1x.teamcrimx.mlgwars.kit.kits.StandardKit;

public enum Kit {

    STARTER(StandardKit.class);

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
