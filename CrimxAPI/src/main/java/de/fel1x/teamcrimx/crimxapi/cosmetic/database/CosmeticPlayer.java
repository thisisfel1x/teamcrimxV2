package de.fel1x.teamcrimx.crimxapi.cosmetic.database;

import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.cosmetic.BaseCosmetic;
import de.fel1x.teamcrimx.crimxapi.cosmetic.CosmeticCategory;
import de.fel1x.teamcrimx.crimxapi.cosmetic.CosmeticRegistry;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.crimxapi.support.CrimxSpigotAPI;
import org.apache.commons.lang.WordUtils;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CosmeticPlayer {

    private final CrimxAPI crimxAPI = CrimxAPI.getInstance();
    private final UUID uuid;

    public CosmeticPlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public void createPlayerData() {
        Document cosmeticDocument = new Document("_id", this.uuid.toString());

        Document registryMap = new Document();
        for (CosmeticCategory cosmeticCategory : CosmeticCategory.values()) {
            registryMap.put(cosmeticCategory.name(), new Document());
        }

        for (CosmeticRegistry cosmeticRegistry : CosmeticRegistry.values()) {
            Document cosmeticCategoryMap = (Document) registryMap.get(cosmeticRegistry.getCosmeticCategory().name());
            cosmeticCategoryMap.put(cosmeticRegistry.name(), false);
            registryMap.put(cosmeticRegistry.getCosmeticCategory().name(), cosmeticCategoryMap);
        }

        cosmeticDocument.append("registry", registryMap);

        for (CosmeticCategory cosmeticCategory : CosmeticCategory.values()) {
            cosmeticDocument.append("selected" + WordUtils.capitalizeFully(cosmeticCategory.name()), null);
        }

        this.crimxAPI.getMongoDB().insertDocumentInCollectionSync(cosmeticDocument, MongoDBCollection.COSMETIC);
    }

    public void updateCosmeticList() {
        if (!this.crimxAPI.getMongoDB().checkIfDocumentExistsSync(this.uuid, MongoDBCollection.COSMETIC)) {
            this.createPlayerData();
            return;
        }

        Document registryDocument = (Document) this.crimxAPI.getMongoDB()
                .getObjectFromDocumentSync(this.uuid, MongoDBCollection.COSMETIC, "registry");

        if (registryDocument == null) {
            this.createPlayerData();
            return;
        }

        boolean updateDocument = false;

        for (CosmeticRegistry cosmeticRegistry : CosmeticRegistry.values()) {
            Document registryCategory = (Document) registryDocument.get(cosmeticRegistry.getCosmeticCategory().name());

            if (!registryCategory.containsKey(cosmeticRegistry.name())) {
                updateDocument = true;

                ((Document) registryDocument.get(cosmeticRegistry.getCosmeticCategory().name()))
                        .put(cosmeticRegistry.name(), false);
            }
        }

        if (updateDocument) {
            this.crimxAPI.getMongoDB().insertObjectInDocument(this.uuid, MongoDBCollection.COSMETIC, "registry", registryDocument);
        }
    }

    public boolean unlockCosmeticSync(CosmeticRegistry cosmeticRegistry) {
        Document cosmeticDocument = this.crimxAPI.getMongoDB()
                .getDocumentSync(this.uuid, MongoDBCollection.COSMETIC);

        if (cosmeticDocument == null) {
            this.createPlayerData();
            return false;
        }

        Document registryDocument = (Document) cosmeticDocument.get("registry");
        Document categoryDocument = (Document) registryDocument.get(cosmeticRegistry.getCosmeticCategory().name());

        categoryDocument.put(cosmeticRegistry.name(), true);
        registryDocument.put(cosmeticRegistry.getCosmeticCategory().name(), categoryDocument);
        cosmeticDocument.put("registry", registryDocument);

        this.crimxAPI.getMongoDB().insertObjectInDocument(this.uuid, MongoDBCollection.COSMETIC, "registry", registryDocument);

        return true;
    }

    public CompletableFuture<Boolean> unlockCosmeticAsync(CosmeticRegistry cosmeticRegistry) {
        return CompletableFuture.supplyAsync(() -> this.unlockCosmeticSync(cosmeticRegistry));
    }

    public CosmeticRegistry[] getSelectedCosmeticsSync() {
        CosmeticRegistry[] selectedCosmetics = new CosmeticRegistry[CosmeticRegistry.values().length];

        for (int i = 0; i < CosmeticCategory.values().length; i++) {
            try {
                selectedCosmetics[i] = CosmeticRegistry.valueOf((String) this.crimxAPI.getMongoDB()
                        .getObjectFromDocumentSync(this.uuid, MongoDBCollection.COSMETIC,
                                "selected" + WordUtils.capitalizeFully(CosmeticCategory.values()[i].name())));
            } catch (NullPointerException ignored) {
                selectedCosmetics[i] = null;
            }

        }
        return selectedCosmetics;
    }

    public void saveSelectedCosmeticToDatabase(CosmeticRegistry cosmeticRegistry) {
        this.crimxAPI.getMongoDB().insertObjectInDocument(this.uuid, MongoDBCollection.COSMETIC,
                "selected" + WordUtils.capitalizeFully(cosmeticRegistry.getCosmeticCategory().name()),
                cosmeticRegistry.name());
    }

    public CompletableFuture<CosmeticRegistry[]> getSelectedCosmeticsAsync() {
        return CompletableFuture.supplyAsync(this::getSelectedCosmeticsSync);
    }

    public CosmeticRegistry getSelectedCosmeticByCategorySync(CosmeticCategory cosmeticCategory) {
        try {
            return CosmeticRegistry.valueOf(String.valueOf(this.crimxAPI.getMongoDB()
                    .getObjectFromDocumentSync(this.uuid, MongoDBCollection.COSMETIC,
                            "selected" + WordUtils.capitalizeFully(cosmeticCategory.name()))));
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    public CompletableFuture<CosmeticRegistry> getSelectedCosmeticByCategoryAsync(CosmeticCategory cosmeticCategory) {
        return CompletableFuture.supplyAsync(() -> this.getSelectedCosmeticByCategorySync(cosmeticCategory));
    }

    public boolean stopAllActiveCosmeticsSync(Player player) {
        for (CosmeticCategory cosmeticCategory : CosmeticCategory.values()) {
            this.stopCosmeticByType(cosmeticCategory, player);
        }
        return true;
    }

    public CompletableFuture<Boolean> stopAllActiveCosmeticsAsync(Player player) {
        return CompletableFuture.supplyAsync(() -> this.stopAllActiveCosmeticsSync(player));
    }

    public void stopCosmeticByType(CosmeticCategory cosmeticCategory, Player player) {

        ActiveCosmetics activeCosmetics = CrimxSpigotAPI.getInstance().getActiveCosmeticsHashMap().get(this.uuid);
        if(activeCosmetics == null) {
            return;
        }

        BaseCosmetic baseCosmetic = activeCosmetics.getSelectedCosmetic().get(cosmeticCategory);
        if(baseCosmetic == null) {
            return;
        }
        baseCosmetic.stopCosmetic(player);

        activeCosmetics.getSelectedCosmetic().remove(cosmeticCategory);
        CrimxSpigotAPI.getInstance().getActiveCosmeticsHashMap().put(this.uuid, activeCosmetics);

        this.crimxAPI.getMongoDB().insertObjectInDocument(this.uuid,
                MongoDBCollection.COSMETIC,
                "selected" + WordUtils.capitalizeFully(cosmeticCategory.name()),
                null);
    }

    public UUID getUuid() {
        return this.uuid;
    }
}
