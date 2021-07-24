package de.fel1x.teamcrimx.crimxapi.cosmetic.database;

import de.fel1x.teamcrimx.crimxapi.cosmetic.CosmeticCategory;
import de.fel1x.teamcrimx.crimxapi.cosmetic.CosmeticRegistry;

import java.util.concurrent.CompletableFuture;

public interface ICosmeticPlayer {

    /**
     * Creates required data in database if not exist
     */
    void createPlayerData();

    /**
     * Updates cosmetic list in database if a newly added cosmetic isn't saved in database
     */
    void updateCosmeticList();

    /**
     * Unlocks a cosmetic sync
     * @param cosmeticRegistry the cosmetic to unlock
     * @return {@link java.lang.Boolean}
     */
    boolean unlockCosmeticSync(CosmeticRegistry cosmeticRegistry);

    /**
     * Unlocks a cosmetic sync
     * @param cosmeticRegistry the cosmetic to unlock
     * @return {@link java.util.concurrent.CompletableFuture}
     */
    CompletableFuture<Boolean> unlockCosmeticAsync(CosmeticRegistry cosmeticRegistry);

    /**
     * Save a selected cosmetic to database
     * @param cosmeticRegistry the cosmetic to save
     */
    void saveSelectedCosmeticToDatabase(CosmeticRegistry cosmeticRegistry);

    /**
     *
     */
    CosmeticRegistry[] getSelectedCosmeticsSync();

    /**
     *
     */
    CompletableFuture<CosmeticRegistry[]> getSelectedCosmeticsAsync();

    /**
     * Get the players selected cosmetic by category sync
     * @param cosmeticCategory the selected cosmetic
     */
    CosmeticRegistry getSelectedCosmeticByCategorySync(CosmeticCategory cosmeticCategory);

    /**
     * Get the players selected cosmetic by category async
     * @param cosmeticCategory the selected cosmetic
     */
    CompletableFuture<CosmeticRegistry> getSelectedCosmeticByCategoryAsync(CosmeticCategory cosmeticCategory);

    /**
     * Stops all active cosmetics
     */
    boolean stopAllActiveCosmeticsSync();

    /**
     * Stops all active cosmetics async
     */
    CompletableFuture<Boolean> stopAllActiveCosmeticsAsync();

}
