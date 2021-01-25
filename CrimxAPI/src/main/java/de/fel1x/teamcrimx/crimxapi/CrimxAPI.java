package de.fel1x.teamcrimx.crimxapi;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.fel1x.teamcrimx.crimxapi.clanSystem.clan.Clan;
import de.fel1x.teamcrimx.crimxapi.clanSystem.database.ClanDatabase;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDB;

import java.util.logging.Logger;

public final class CrimxAPI {

    public static CrimxAPI instance;
    private final IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);

    private final Logger logger;

    private final MongoDB mongoDB;
    private final ClanDatabase clanDatabase;

    public CrimxAPI() {

        instance = this;

        this.logger = Logger.getLogger(this.getClass().getName());
        this.mongoDB = new MongoDB();
        this.clanDatabase = new ClanDatabase(this);

    }

    public static CrimxAPI getInstance() {
        return instance;
    }

    public String getPrefix() {
        return "§bteamcrimx§lDE §8● §r";
    }

    public String getSuggestionPrefix() {
        return "§aVorschläge §8● §r";
    }

    public String getBugreportPrefix() {
        return "§cBugreport §8● §r";
    }

    public String getClanPrefix() {
        return "§4Clan §8● §r";
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

    public ClanDatabase getClanDatabase() {
        return this.clanDatabase;
    }
}
