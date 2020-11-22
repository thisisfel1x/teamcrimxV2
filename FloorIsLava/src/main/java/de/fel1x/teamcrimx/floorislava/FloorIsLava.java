package de.fel1x.teamcrimx.floorislava;

import org.bukkit.plugin.java.JavaPlugin;

public final class FloorIsLava extends JavaPlugin {

    public static FloorIsLava instance;
    private final String prefix = "§6TheFloorIsLava §8● §r";

    @Override
    public void onEnable() {

        instance = this;

    }

    @Override
    public void onDisable() {
    }

    public String getPrefix() {
        return this.prefix;
    }
}
