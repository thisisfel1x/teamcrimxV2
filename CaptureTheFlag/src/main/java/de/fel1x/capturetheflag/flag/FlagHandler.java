package de.fel1x.capturetheflag.flag;

import de.fel1x.capturetheflag.CaptureTheFlag;
import de.fel1x.capturetheflag.filehandler.SpawnHandler;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Banner;

public class FlagHandler {

    public FlagHandler() {

        this.initializeFlags();

    }

    private void initializeFlags() {

        Location blueFlag = SpawnHandler.loadLocation("blueFlag");
        Location redFlag = SpawnHandler.loadLocation("redFlag");

        try {

            blueFlag.getBlock().setType(Material.STANDING_BANNER);
            Banner banner = (Banner) blueFlag.getBlock().getState();
            banner.setBaseColor(DyeColor.BLUE);
            banner.update();

            CaptureTheFlag.getInstance().getData().setBlueFlagLocation(banner.getLocation());
            CaptureTheFlag.getInstance().getData().setBlueFlagBaseLocation(banner.getLocation());


            redFlag.getBlock().setType(Material.STANDING_BANNER);
            banner = (Banner) redFlag.getBlock().getState();
            banner.setBaseColor(DyeColor.RED);
            banner.update();

            CaptureTheFlag.getInstance().getData().setRedFlagLocation(banner.getLocation());
            CaptureTheFlag.getInstance().getData().setRedFlagBaseLocation(banner.getLocation());

        } catch (Exception ignored) {

            Bukkit.getConsoleSender().sendMessage("§cERROR: FLAG_INIT_FAILED_NPE");

        }

    }

}
