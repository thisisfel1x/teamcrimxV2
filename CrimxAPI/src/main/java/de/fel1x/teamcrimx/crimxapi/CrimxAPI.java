package de.fel1x.teamcrimx.crimxapi;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDB;

import java.util.logging.Logger;

public final class CrimxAPI {

    public static CrimxAPI instance;
    private final IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);

    private final Logger logger;

    private final MongoDB mongoDB;

    public CrimxAPI() {

        instance = this;

        this.logger = Logger.getLogger(this.getClass().getName());
        this.mongoDB = new MongoDB();

    }

    public static CrimxAPI getInstance() {
        return instance;
    }

    public String getPrefix() {
        String prefix = "§bteamcrimx§lDE §8● §r";
        return prefix;
    }

    public MongoDB getMongoDB() {
        return this.mongoDB;
    }

    public Logger getLogger() {
        return this.logger;
    }

    public IPlayerManager getPlayerManager() {
        return this.playerManager;
    }
}
