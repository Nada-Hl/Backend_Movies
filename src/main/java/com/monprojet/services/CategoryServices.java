package com.monprojet.services;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
// import com.mongodb.client.model.Filters;
import com.monprojet.db.MongoDBConnection;
import com.monprojet.models.Category;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class CategoryServices {
    private final MongoDatabase db = MongoDBConnection.getDatabase();
    private final MongoCollection<Document> categoryCollection = db.getCollection("categories");

    // Fetching all the categories
    public List<Category> getAllCategories() {
        List<Category> list = new ArrayList<>();
        for (Document d : categoryCollection.find()) {
            list.add(new Category(d.getInteger("category_id"), d.getString("category_name")));
        }
        return list;
    }
    
}
