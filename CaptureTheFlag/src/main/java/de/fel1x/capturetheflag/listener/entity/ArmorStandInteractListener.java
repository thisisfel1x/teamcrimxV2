package de.fel1x.capturetheflag.listener.entity;

import de.fel1x.capturetheflag.CaptureTheFlag;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;

public class ArmorStandInteractListener implements Listener {

    private CaptureTheFlag captureTheFlag;

    public ArmorStandInteractListener(CaptureTheFlag captureTheFlag) {
        this.captureTheFlag = captureTheFlag;
        this.captureTheFlag.getPluginManager().registerEvents(this, this.captureTheFlag);
    }

    @EventHandler
    public void on(PlayerArmorStandManipulateEvent event) {
        event.setCancelled(true);
    }

}
