package de.fel1x.teamcrimx.crimxapi.cosmetic.runnable;

import de.fel1x.teamcrimx.crimxapi.cosmetic.ICosmetic;
import de.fel1x.teamcrimx.crimxapi.support.CrimxSpigotAPI;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class CosmeticTask extends BukkitRunnable {

    private HashMap<UUID, ICosmetic> activeCosmetics;

    public CosmeticTask(CrimxSpigotAPI crimxSpigotAPI) {
        this.activeCosmetics = new HashMap<>();

        this.runTaskTimer(crimxSpigotAPI, 0L, 0L);
    }

    @Override
    public void run() {
    }

    public HashMap<UUID, ICosmetic> getActiveCosmetics() {
        return this.activeCosmetics;
    }
}
