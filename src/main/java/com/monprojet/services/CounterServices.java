package com.monprojet.services;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import org.bson.Document;
import com.monprojet.db.MongoDBConnection;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.inc;

public class CounterServices {
    private static final MongoDatabase db = MongoDBConnection.getDatabase();
    private static final MongoCollection<Document> counters = db.getCollection("counters");

    public static int getNextSequence(String name) {
        Document updated = counters.findOneAndUpdate(
                eq("_id", name),
                inc("sequence_value", 1),
                new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER).upsert(true)
        );
        return updated.getInteger("sequence_value");
    }
}
