package de.fel1x.capturetheflag.kit;

import de.fel1x.capturetheflag.kit.kits.ArcherKit;
import de.fel1x.capturetheflag.kit.kits.MedicKit;
import de.fel1x.capturetheflag.kit.kits.TankKit;

public enum Kit {

    NONE(null),

    ARCHER(ArcherKit.class),
    TANK(TankKit.class),
    MEDIC(MedicKit.class);

    private Class<? extends IKit> clazz;

    Kit(Class<? extends IKit> clazz) {
        this.clazz = clazz;
    }

    public Class<? extends IKit> getClazz() {
        return clazz;
    }

}
