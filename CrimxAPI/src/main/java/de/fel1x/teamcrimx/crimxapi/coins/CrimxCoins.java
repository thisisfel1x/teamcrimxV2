package de.fel1x.teamcrimx.crimxapi.coins;

import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.coins.events.AsyncPlayerCoinsChangeEvent;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CrimxCoins implements ICrimxCoins {

    private final CrimxAPI crimxAPI = new CrimxAPI();
    private final Document playerNetworkDocument;
    private final UUID uuid;

    public CrimxCoins(UUID uuid) {
        this.uuid = uuid;

        this.playerNetworkDocument = this.crimxAPI.getMongoDB().getUserCollection().
                find(new Document("_id", this.uuid.toString())).first();
    }

    @Override
    public int getCoinsSync() {
        return Objects.requireNonNull(this.crimxAPI.getMongoDB().getUserCollection().
                find(new Document("_id", this.uuid.toString())).first()).getInteger("coins");
    }

    @Override
    public CompletableFuture<Integer> getCoinsAsync() {
        return CompletableFuture.supplyAsync(this::getCoinsSync);
    }

    @Override
    public boolean setCoinsSync(int coins) {
        if (coins < 0) {
            coins = 0;
        }

        if(this.playerNetworkDocument == null) {
            return false;
        }

        Document document = new Document("coins", coins);
        Bson updateOperation = new Document("$set", document);
        this.crimxAPI.getMongoDB().getUserCollection().updateOne(this.playerNetworkDocument, updateOperation);

        // CrimxAPI - Call PlayerCoinsChangeEvent
        Bukkit.getPluginManager().callEvent(new AsyncPlayerCoinsChangeEvent(true, Bukkit.getPlayer(this.uuid), coins));
        return true;
    }

    @Override
    public CompletableFuture<Boolean> setCoinsAsync(int coins) {
        return CompletableFuture.supplyAsync(() -> this.setCoinsSync(coins));
    }

    @Override
    public boolean addCoinsSync(int coins) {
        // If coins are negative multiply by -1 that they are positive
        if(Math.signum(coins) == -1.0) {
            coins *= -1;
        }

        coins = this.getCoinsSync() + coins;
        return this.setCoinsSync(coins);
    }

    @Override
    public CompletableFuture<Boolean> addCoinsAsync(int coins) {
        return CompletableFuture.supplyAsync(() -> this.addCoinsSync(coins));
    }

    @Override
    public boolean removeCoinsSync(int coins) {
        // If coins are negative multiply by -1 that they are positive
        if(Math.signum(coins) == -1.0) {
            coins *= -1;
        }

        coins = this.getCoinsSync() - coins;
        return this.setCoinsSync(coins);
    }

    @Override
    public CompletableFuture<Boolean> removeCoinsAsync(int coins) {
        return CompletableFuture.supplyAsync(() -> this.removeCoinsSync(coins));
    }
}
