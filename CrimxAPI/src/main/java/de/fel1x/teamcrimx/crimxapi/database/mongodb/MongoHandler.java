package de.fel1x.teamcrimx.crimxapi.database.mongodb;

import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class MongoHandler {

    private MongoDB mongoDB;

    /**
     * Constructor for MongoHandler
     */
    public MongoHandler() {
    }

    public void initMongoDBClass() {
        this.mongoDB = CrimxAPI.getInstance().getMongoDB();
    }

    /**
     * Returns a Document out of a collection sync
     * @param uuid Player UUID
     * @param mongoDBCollection identifier for Collection
     * @return found Document if not null
     */
    public Document getDocumentSync(UUID uuid, MongoDBCollection mongoDBCollection) {
        return this.mongoDB.getNetworkDatabase().getCollection(mongoDBCollection.getCollectionName())
                .find(new Document("_id", uuid.toString())).first();
    }

    /**
     * Returns a Document out of a collection sync
     * @param uuid Player UUID
     * @param mongoDBCollection identifier for Collection
     * @return CompletableFuture<Document>
     */
    public CompletableFuture<Document> getDocumentAsync(UUID uuid, MongoDBCollection mongoDBCollection) {
        return CompletableFuture.supplyAsync(() -> this.getDocumentSync(uuid, mongoDBCollection));
    }

    /**
     * Returns an Object in a Document if not null
     * @param uuid Player UUID
     * @param mongoDBCollection identifier for Collection
     * @param key Key for Value in Document
     * @return Object
     */
    public Object getObjectFromDocumentSync(UUID uuid, MongoDBCollection mongoDBCollection, String key) {
        Document found = this.getDocumentSync(uuid, mongoDBCollection);

        if(found != null) {
            return found.get(key);
        }
        return null;
    }

    /**
     * Returns an Object in a Document if not null
     * @param uuid Player UUID
     * @param mongoDBCollection identifier for Collection
     * @param key Key for Value in Document
     * @return ArrayList
     */
    public ArrayList<String> getStringArrayListFromDocumentSync(UUID uuid, MongoDBCollection mongoDBCollection, String key) {
        Document found = this.getDocumentSync(uuid, mongoDBCollection);

        if(found != null) {
            Object foundObject = found.get(key);
            if(foundObject instanceof ArrayList) {
                return (ArrayList<String>) foundObject;
            }
        }
        return null;
    }

    /**
     * Returns an Object in a Document if not null
     * @param uuid Player UUID
     * @param mongoDBCollection identifier for Collection
     * @param key Key for Value in Document
     * @param defaultValue Default Value if key is null
     * @return Object
     */
    public @Nullable Object getObjectFromDocumentSyncOrDefault(UUID uuid, MongoDBCollection mongoDBCollection, String key, Object defaultValue) {
        Document found = this.getDocumentSync(uuid, mongoDBCollection);

        if(found != null) {
            return found.get(key, defaultValue);
        }
        return null;
    }

    /**
     * Returns an Object in a Document if not null async
     * @param uuid Player UUID
     * @param mongoDBCollection identifier for Collection
     * @param key Key for Value in Document
     * @return CompletableFuture<Object>
     */
    @Nullable
    public CompletableFuture<Object> getObjectFromDocumentAsync(UUID uuid, MongoDBCollection mongoDBCollection, String key) {
        return CompletableFuture.supplyAsync(() -> this.getDocumentSync(uuid, mongoDBCollection).get(key));
    }

    /**
     * Returns an Object in a Document if not null async
     * @param uuid Player UUID
     * @param mongoDBCollection identifier for Collection
     * @param key Key for Value in Document
     * @param defaultValue Default Value if key is null
     * @return CompletableFuture<Object>
     */
    @Nullable
    public CompletableFuture<Object> getObjectFromDocumentAsyncOrDefault(UUID uuid, MongoDBCollection mongoDBCollection, String key, Object defaultValue) {
        return CompletableFuture.supplyAsync(() -> this.getDocumentSync(uuid, mongoDBCollection).get(key, defaultValue));
    }

    /**
     * Checks if a Document exists in a specific Collection
     * @param uuid Player UUID
     * @param mongoDBCollection identifier for Collection
     * @return boolean
     */
    public boolean checkIfDocumentExistsSync(UUID uuid, MongoDBCollection mongoDBCollection) {
        return this.mongoDB.getNetworkDatabase().getCollection(mongoDBCollection.getCollectionName())
                .countDocuments(new Document("_id", uuid.toString())) > 0;
    }

    /**
     * Checks if a Document exists in a specific Collection async
     * @param uuid Player UUID
     * @param mongoDBCollection identifier for Collection
     * @return CompletableFuture<Boolean>
     */
    public CompletableFuture<Boolean> checkIfDocumentExistsAsync(UUID uuid, MongoDBCollection mongoDBCollection) {
        return CompletableFuture.supplyAsync(() -> this.checkIfDocumentExistsSync(uuid, mongoDBCollection));
    }

    /**
     * Inserts a document in a collection
     * @param document The document to insert
     * @param mongoDBCollection The target collection
     * @return boolean indicates success or not
     */
    public boolean insertDocumentInCollectionSync(Document document, MongoDBCollection mongoDBCollection) {
        try {
            this.mongoDB.getNetworkDatabase().getCollection(mongoDBCollection.getCollectionName()).insertOne(document);
        } catch (Exception ignored) {
            // TODO: save exception in bugtracker
            return false;
        }
        return true;
    }

    /**
     * Updates a value in a document sync
     * @param uuid Player UUID
     * @param mongoDBCollection The target collection
     * @param updateDocument the document with update content
     */
    public void updateDocumentInCollectionSync(UUID uuid, MongoDBCollection mongoDBCollection, Document updateDocument) {
        Document toInsert = this.getDocumentSync(uuid, mongoDBCollection);
        Bson updateOperation = new Document("$set", updateDocument);

        if(toInsert != null) {
            this.mongoDB.getNetworkDatabase().getCollection(mongoDBCollection.getCollectionName())
                    .updateOne(toInsert, updateOperation);
        }
    }

    /**
     * Inserts an object in a document
     * @param uuid Player UUID
     * @param mongoDBCollection the target collection
     * @param key the key for the value
     * @param value the specific value
     * @return boolean indicates success or not
     */
    public boolean insertObjectInDocument(UUID uuid, MongoDBCollection mongoDBCollection, String key, Object value) {
        Document toInsert = this.getDocumentSync(uuid, mongoDBCollection);
        Document document = new Document(key, value);
        Bson updateOperation = new Document("$set", document);

        if(toInsert == null) {
            return false;
        }

        this.mongoDB.getNetworkDatabase().getCollection(mongoDBCollection.getCollectionName()).updateOne(toInsert, updateOperation);
        return true;
    }

    /**
     * Get a nested document in another document sync
     * @param uuid Player UUID
     * @param mongoDBCollection the target collection
     * @param key the nested document name or key
     * @return nested document
     */
    public Document getNestedDocumentSync(UUID uuid, MongoDBCollection mongoDBCollection, String key) {
        return (Document) this.getDocumentSync(uuid, mongoDBCollection).get(key);
    }

    /**
     * Get a nested document in another document sync
     * @param uuid Player UUID
     * @param mongoDBCollection the target collection
     * @param key the nested document name or key
     * @return CompletableFuture
     */
    public CompletableFuture<Document> getNestedDocumentAsync(UUID uuid, MongoDBCollection mongoDBCollection, String key) {
        return CompletableFuture.supplyAsync(() -> this.getNestedDocumentSync(uuid, mongoDBCollection, key));
    }

    public void updateValueInNestedDocument(UUID uuid, MongoDBCollection mongoDBCollection, String nestedDocumentName, String updateKey, Object updateValue) {
        Document rootDocument = this.getDocumentSync(uuid, mongoDBCollection);
        Document nestedDocument = (Document) rootDocument.get(nestedDocumentName);
        nestedDocument.put(updateKey, updateValue);
        rootDocument.put(nestedDocumentName, nestedDocument);
        this.updateDocumentInCollectionSync(uuid, MongoDBCollection.FRIENDS, rootDocument);
    }

}
