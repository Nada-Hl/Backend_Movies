package com.monprojet.db;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDBConnection {
    private static MongoClient mongoClient = null;
    private static MongoDatabase database = null;

    // Connecting
    public static MongoDatabase getDatabase() {
        if (mongoClient == null) {
            mongoClient = MongoClients.create(
                "mongodb+srv://danaelhlaissi_db_user:2003719Nada@cluster0.zieirbm.mongodb.net/movies"
            );
            database = mongoClient.getDatabase("movies"); // nom de ta base
        }
        return database;
    }

    //Fetch the movies collection
    public static MongoCollection<Document> getMoviesCollection() {
        return getDatabase().getCollection("movies");
    }
}
