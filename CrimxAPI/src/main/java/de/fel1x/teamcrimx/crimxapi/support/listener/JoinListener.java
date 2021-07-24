package de.fel1x.teamcrimx.crimxapi.support.listener;

import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.clanSystem.player.ClanPlayer;
import de.fel1x.teamcrimx.crimxapi.clanSystem.player.IClanPlayer;
import de.fel1x.teamcrimx.crimxapi.cosmetic.CosmeticCategory;
import de.fel1x.teamcrimx.crimxapi.cosmetic.CosmeticRegistry;
import de.fel1x.teamcrimx.crimxapi.cosmetic.database.CosmeticPlayer;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.crimxapi.support.CrimxSpigotAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class JoinListener implements Listener {

    public JoinListener(CrimxSpigotAPI crimxSpigotAPI) {
        crimxSpigotAPI.getPluginManager().registerEvents(this, crimxSpigotAPI);
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
            new CosmeticPlayer(player.getUniqueId())
                    .getSelectedCosmeticByCategoryAsync(CosmeticCategory.EFFECT)
                    .thenAccept(cosmeticRegistry -> {
                        if (cosmeticRegistry == null) {
                            return;
                        }
                        try {
                            cosmeticRegistry.getCosmeticClass().getDeclaredConstructor().newInstance().initializeCosmetic(player);
                        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ignored) {
                            player.sendMessage(CrimxAPI.getInstance().getPrefix() + "§cEin Fehler beim Laden deines Cosmetics aufgetreten!");
                        }

                    });
        }, 20L);
    }

    // TODO: very dirty
    @EventHandler
    public void on2(PlayerQuitEvent event) {
        if(CrimxSpigotAPI.getInstance().getCosmeticTask().getActiveCosmetics().containsKey(event.getPlayer().getUniqueId())) {
            CrimxSpigotAPI.getInstance().getCosmeticTask().getActiveCosmetics().remove(event.getPlayer().getUniqueId()).stopCosmetic(event.getPlayer());
        }
    }

}
