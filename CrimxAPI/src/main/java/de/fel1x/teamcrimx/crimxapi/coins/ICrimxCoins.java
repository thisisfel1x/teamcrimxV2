package de.fel1x.teamcrimx.crimxapi.coins;

import java.util.concurrent.CompletableFuture;

public interface ICrimxCoins {

    /**
     * Get coins sync from player document
     *
     * @return Integer - the players coins
     */
    int getCoinsSync();

    /**
     * Get coins async from player document
     *
     * @return CompletableFuture<Integer> - the players coins
     */
    CompletableFuture<Integer> getCoinsAsync();

    /**
     * Set coins sync in player document
     *
     * @return boolean - success or not
     */
    boolean setCoinsSync(int coins);

    /**
     * Set coins async in player document
     *
     * @return CompletableFuture<Boolean> - success or not
     */
    CompletableFuture<Boolean> setCoinsAsync(int coins);

    /**
     * Add coins sync in player document
     *
     * @return boolean - success or not
     */
    boolean addCoinsSync(int coins);

    /**
     * Add coins async in player document
     *
     * @return CompletableFuture<Boolean> - success or not
     */
    CompletableFuture<Boolean> addCoinsAsync(int coins);

    /**
     * Remove coins sync in player document
     *
     * @return boolean - success or not
     */
    boolean removeCoinsSync(int coins);

    /**
     * Remove coins async in player document
     *
     * @return CompletableFuture<Boolean> - success or not
     */
    CompletableFuture<Boolean> removeCoinsAsync(int coins);

}
