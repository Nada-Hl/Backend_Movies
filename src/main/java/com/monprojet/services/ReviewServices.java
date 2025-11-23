package com.monprojet.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.monprojet.db.MongoDBConnection;
import com.monprojet.models.Review;

public class ReviewServices {

    private final MongoDatabase db = MongoDBConnection.getDatabase();
    private final MongoCollection<Document> collection = db.getCollection("reviews");
    
    /**
     * Ajoute un review avec tous les champs
     * @return 1 si succès, 0 si échec
     */
    public int addReview(Review review) {
        System.out.println("=== Adding review for movie: " + review.getMovieId());
        
        // Validation
        if (!review.isValid()) {
            System.err.println("✗ Review validation failed!");
            return 0;
        }
        
        // Créer le document
        Document reviewDocument = new Document()
                .append("userId", review.getUserId())
                .append("movie_id", review.getMovieId())
                .append("content", review.getContent())
                .append("rating", review.getRating());
        
        // Ajouter les champs facultatifs s'ils sont présents
        if (review.getViewingDate() != null) {
            reviewDocument.append("viewing_date", review.getViewingDate().toString());
        }
        
        if (review.getViewingLocation() != null && !review.getViewingLocation().isBlank()) {
            reviewDocument.append("viewing_location", review.getViewingLocation());
        }
        
        try {
            collection.insertOne(reviewDocument);
            System.out.println("✓ Review successfully added!");
            
            long count = collection.countDocuments(Filters.eq("movie_id", review.getMovieId()));
            System.out.println("✓ Total reviews for this movie: " + count);
            
            return 1;
            
        } catch (Exception e) {
            System.err.println("✗ Error adding review: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
    
    /**
     * Récupère tous les reviews pour un film
     */
    public List<Review> getReviewsForMovie(int movieId) {
        System.out.println("=== Getting reviews for movie_id: " + movieId);
        
        Bson filter = Filters.eq("movie_id", movieId);
        List<Review> reviews = new ArrayList<>();
        
        try {
            for (Document doc : collection.find(filter)) {
                Review review = documentToReview(doc);
                if (review != null) {
                    reviews.add(review);
                }
            }
            
            System.out.println("✓ Found " + reviews.size() + " reviews");
            
        } catch (Exception e) {
            System.err.println("✗ Error fetching reviews: " + e.getMessage());
            e.printStackTrace();
        }
        
        return reviews;
    }
    
    /**
     * Calcule la note moyenne pour un film
     */
    public double getAverageRating(int movieId) {
        List<Review> reviews = getReviewsForMovie(movieId);
        
        if (reviews.isEmpty()) {
            return 0.0;
        }
        
        double sum = 0.0;
        for (Review review : reviews) {
            sum += review.getRating();
        }
        
        return sum / reviews.size();
    }
    
    /**
     * Convertit un Document MongoDB en objet Review
     */
    private Review documentToReview(Document doc) {
        try {
            Review review = new Review();
            
            review.setUserId(doc.getInteger("userId", 0));
            review.setMovieId(doc.getInteger("movie_id", 0));
            review.setContent(doc.getString("content"));
            review.setRating(doc.getInteger("rating", 0));
            
            // Champs facultatifs
            String viewingDateStr = doc.getString("viewing_date");
            if (viewingDateStr != null && !viewingDateStr.isBlank()) {
                review.setViewingDate(LocalDate.parse(viewingDateStr));
            }
            
            String viewingLocation = doc.getString("viewing_location");
            if (viewingLocation != null) {
                review.setViewingLocation(viewingLocation);
            }
            
            return review;
            
        } catch (Exception e) {
            System.err.println("Error converting document to Review: " + e.getMessage());
            return null;
        }
    }
}