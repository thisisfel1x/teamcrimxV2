package de.fel1x.teamcrimx.crimxapi.support.listener;

import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.cosmetic.BaseCosmetic;
import de.fel1x.teamcrimx.crimxapi.cosmetic.CosmeticCategory;
import de.fel1x.teamcrimx.crimxapi.cosmetic.CosmeticRegistry;
import de.fel1x.teamcrimx.crimxapi.cosmetic.database.ActiveCosmetics;
import de.fel1x.teamcrimx.crimxapi.cosmetic.database.CosmeticPlayer;
import de.fel1x.teamcrimx.crimxapi.support.CrimxSpigotAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class JoinQuitListener implements Listener {

    private final CrimxSpigotAPI crimxSpigotAPI;

    public JoinQuitListener(CrimxSpigotAPI crimxSpigotAPI) {
        this.crimxSpigotAPI = crimxSpigotAPI;
        crimxSpigotAPI.getPluginManager().registerEvents(this, this.crimxSpigotAPI);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        /*String taskName = CrimxAPI.getInstance().getPlayerManager().getOnlinePlayer(player.getUniqueId()).getConnectedService().getTaskName();

        if (taskName != null && taskName.equalsIgnoreCase("Lobby")) {
            IClanPlayer clanPlayer = new ClanPlayer(player.getUniqueId());
            clanPlayer.sendClanRequestMessage();
        } */

        Bukkit.getScheduler().runTaskLater(CrimxSpigotAPI.getInstance(), () -> {
            UUID uuid = player.getUniqueId();
            CosmeticPlayer cosmeticPlayer = new CosmeticPlayer(uuid);

            ActiveCosmetics activeCosmetics = new ActiveCosmetics();
            for (CosmeticCategory cosmeticCategory : CosmeticCategory.values()) {
                CosmeticRegistry cosmeticRegistry = cosmeticPlayer.getSelectedCosmeticByCategorySync(cosmeticCategory);
                if(cosmeticRegistry == null) {
                    activeCosmetics.getSelectedCosmetic().put(cosmeticCategory, null);
                    continue;
                }
                try {
                    BaseCosmetic baseCosmetic = cosmeticRegistry.getCosmeticClass()
                            .getDeclaredConstructor(Player.class, CrimxSpigotAPI.class)
                            .newInstance(Bukkit.getPlayer(uuid), CrimxSpigotAPI.getInstance());
                    baseCosmetic.startCosmetic(player);
                    activeCosmetics.getSelectedCosmetic().put(cosmeticCategory, baseCosmetic);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) {
                    activeCosmetics.getSelectedCosmetic().put(cosmeticCategory, null);
                }
            }

            CrimxSpigotAPI.getInstance().getActiveCosmeticsHashMap().put(uuid, activeCosmetics);
        }, 10L);
    }

    @EventHandler
    public void on(PlayerQuitEvent event) {
        for (CosmeticCategory cosmeticCategory : CosmeticCategory.values()) {
            ActiveCosmetics activeCosmetics = CrimxSpigotAPI.getInstance().getActiveCosmeticsHashMap().get(event.getPlayer().getUniqueId());
            BaseCosmetic baseCosmetic = activeCosmetics.getSelectedCosmetic().get(cosmeticCategory);
            if(baseCosmetic == null) {
                continue;
            }
            baseCosmetic.stopCosmetic(event.getPlayer());

            activeCosmetics.getSelectedCosmetic().remove(cosmeticCategory);
            CrimxSpigotAPI.getInstance().getActiveCosmeticsHashMap().put(event.getPlayer().getUniqueId(), activeCosmetics);
        }
    }

}
