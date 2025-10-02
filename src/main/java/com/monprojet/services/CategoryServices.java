package com.monprojet.services;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.monprojet.db.MongoDBConnection;
import com.monprojet.models.Category;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class CategoryServices {
    private final MongoDatabase db = MongoDBConnection.getDatabase();
    private final MongoCollection<Document> categoryCollection = db.getCollection("categories");
    private final MongoCollection<Document> movieCollection = db.getCollection("movies");
    // Find a specific category using it's name
    public Category findByName(String name) {
        Document d = categoryCollection.find(Filters.eq("category_name", name)).first();
        if (d == null) return null;
        int id = d.getInteger("category_id");
        String nm = d.getString("category_name");
        return new Category(id, nm);
    }

    // Fetching all the categories
    public List<Category> getAllCategories() {
        List<Category> list = new ArrayList<>();
        for (Document d : categoryCollection.find()) {
            list.add(new Category(d.getInteger("category_id"), d.getString("category_name")));
        }
        return list;
    }

    // Creating a new category
    public Category createCategory(String name) {
        // Checking if the category name already exists
        Category existing = findByName(name);
        if (existing != null) return existing;

        int newId = CounterServices.getNextSequence("category_id");
        Document doc = new Document("category_id", newId)
                .append("category_name", name);
        categoryCollection.insertOne(doc);
        return new Category(newId, name);
    }
    // Deleting categry by ID
    public boolean deleteCategory(int categoryId) {
        movieCollection.deleteMany(Filters.eq("category_id", categoryId));
        DeleteResult result = categoryCollection.deleteOne(Filters.eq("category_id", categoryId));
        long a=result.getDeletedCount();
        return a>0;
    }
}
