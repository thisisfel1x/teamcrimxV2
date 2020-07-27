package de.fel1x.teamcrimx.crimxapi;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDB;

import java.util.logging.Logger;

public final class CrimxAPI {

    private final IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);

    public static CrimxAPI instance;
    private String prefix = "§8| §bteamcrimx§lDE §8» §r";

    private Logger logger;

    private MongoDB mongoDB;

    public CrimxAPI() {

        instance = this;

        this.logger = Logger.getLogger(this.getClass().getName());
        this.mongoDB = new MongoDB();

    }

    public static CrimxAPI getInstance() {
        return instance;
    }

    public String getPrefix() {
        return prefix;
    }

    public MongoDB getMongoDB() {
        return mongoDB;
    }

    public Logger getLogger() {
        return logger;
    }

    public IPlayerManager getPlayerManager() {
        return playerManager;
    }
}
