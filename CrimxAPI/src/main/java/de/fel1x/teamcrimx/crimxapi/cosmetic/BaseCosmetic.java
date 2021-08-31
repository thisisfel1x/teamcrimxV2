package de.fel1x.teamcrimx.crimxapi.cosmetic;

import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.cosmetic.database.ActiveCosmetics;
import de.fel1x.teamcrimx.crimxapi.cosmetic.database.CosmeticPlayer;
import de.fel1x.teamcrimx.crimxapi.support.CrimxSpigotAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;


import java.util.Random;

public abstract class BaseCosmetic extends InventoryCosmetic implements Listener {

    public Player player;
    public CrimxSpigotAPI crimxSpigotAPI;
    public ICloudPlayer cloudPlayer;

    public Random random = new Random();

    public BaseCosmetic(Player player, CrimxSpigotAPI crimxSpigotAPI) {
        this.player = player;
        this.crimxSpigotAPI = crimxSpigotAPI;
        this.cloudPlayer = CrimxAPI.getInstance().getPlayerManager().getOnlinePlayer(player.getUniqueId());
    }

    public void startCosmetic(Player player) {
        // register listener
        this.crimxSpigotAPI.getPluginManager().registerEvents(this, this.crimxSpigotAPI);

        ActiveCosmetics activeCosmetics = this.crimxSpigotAPI.getActiveCosmeticsHashMap().get(this.player.getUniqueId());
        if(activeCosmetics != null) {
            activeCosmetics.getSelectedCosmetic().put(this.getCosmeticCategory(), this);
            this.crimxSpigotAPI.getActiveCosmeticsHashMap().put(this.player.getUniqueId(), activeCosmetics);
        }
    }

    public void stopCosmetic(Player player) {
        // Unregister all listeners
        HandlerList.unregisterAll(this);

        try {
            this.cancel(); // Cancel all active tasks
        } catch (Exception ignored) {
        }

        ActiveCosmetics activeCosmetics = this.crimxSpigotAPI.getActiveCosmeticsHashMap().get(this.player.getUniqueId());
        if (activeCosmetics != null) {
            activeCosmetics.getSelectedCosmetic().remove(this.getCosmeticCategory());
            this.crimxSpigotAPI.getActiveCosmeticsHashMap().put(this.player.getUniqueId(), activeCosmetics);
        }
    }
}
