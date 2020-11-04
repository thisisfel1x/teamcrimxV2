package de.fel1x.capturetheflag.flag;

import de.fel1x.capturetheflag.CaptureTheFlag;
import de.fel1x.capturetheflag.filehandler.SpawnHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

public class FlagHandler {

    public FlagHandler() {

        this.initializeFlags();

    }

    private void initializeFlags() {

        Location blueFlag = SpawnHandler.loadLocation("blueFlag");
        Location redFlag = SpawnHandler.loadLocation("redFlag");

        try {

            blueFlag.getBlock().setType(Material.BLUE_BANNER);
            CaptureTheFlag.getInstance().getData().setBlueFlagBaseLocation(blueFlag);


            redFlag.getBlock().setType(Material.RED_BANNER);
            CaptureTheFlag.getInstance().getData().setRedFlagBaseLocation(redFlag);

        } catch (Exception ignored) {

            Bukkit.getConsoleSender().sendMessage("§cERROR: FLAG_INIT_FAILED_NPE");

        }

    }

}
