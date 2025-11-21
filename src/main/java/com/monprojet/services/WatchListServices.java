package com.monprojet.services;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;

import com.monprojet.db.MongoDBConnection;
import com.monprojet.models.Movie;
import com.monprojet.models.Watchlist;

public class WatchListServices {

    private final MongoDatabase db = MongoDBConnection.getDatabase();
    private final MongoCollection<Document> Collection = db.getCollection("watchlist");
    
    private static final int MAX_MOVIES = 5;
    
    /**
     * Adds a movie to the watchlist. Returns:
     * - 1 if movie was added successfully
     * - 0 if movie already exists in watchlist
     * - -1 if watchlist is full (5 movies max)
     */
    public int AddMovieToWatchList(int userId, Movie m) {
        Bson filter = Filters.eq("userID", userId);
        
        // First, check if watchlist exists and get current movie count
        Document existingWatchlist = Collection.find(filter).first();
        
        if (existingWatchlist != null) {
            @SuppressWarnings("unchecked")
            List<Document> movies = (List<Document>) existingWatchlist.get("movies");
            
            if (movies != null) {
                // Check if watchlist is full
                if (movies.size() >= MAX_MOVIES) {
                    System.out.println("Watchlist is full for user: " + userId);
                    return -1;
                }
                
                // Check if movie already exists
                for (Document movieDoc : movies) {
                    String existingMovieName = movieDoc.getString("movie_name");
                    if (existingMovieName != null && existingMovieName.equals(m.getMovieName())) {
                        System.out.println("Movie already exists in watchlist for user: " + userId);
                        return 0;
                    }
                }
            }
        }
        
        // Create movie document
        Document movieDocument = new Document()
                .append("category_id", m.getCategoryId())
                .append("category_name", m.getCategoryName())
                .append("movie_name", m.getMovieName())
                .append("realisateur", m.getRealisateur())
                .append("poster", m.getPoster())
                .append("date_de_realisation", m.getDateRealisation())
                .append("date_de_sortie", m.getDateSortie())
                .append("synopsis", m.getSynopsis())
                .append("liste_acteurs_principales", m.getListeActeurs());
        
        // Use $push instead of $addToSet for better control
        Bson update = Updates.push("movies", movieDocument);
        
        UpdateResult result = Collection.updateOne(
            filter,
            update,
            new com.mongodb.client.model.UpdateOptions().upsert(true)
        );
        
        if (result.getUpsertedId() != null) {
            System.out.println("Watchlist created and movie added for user: " + userId);
            return 1;
        } else if (result.getModifiedCount() > 0) {
            System.out.println("Movie added to existing watchlist for user: " + userId);
            return 1;
        }
        
        return 0;
    }
    
    public Watchlist getWatchlist(int userId) {
        Bson filter = Filters.eq("userID", userId);
        
        Document watchlistDoc = Collection.find(filter).first();
        
        if (watchlistDoc == null) {
            System.out.println("Watchlist not found for user: " + userId);
            return null;
        }
        
        Watchlist watchlist = new Watchlist();
        watchlist.setUserID(watchlistDoc.getInteger("userID"));
        
        @SuppressWarnings("unchecked")
        List<Document> movieDocs = (List<Document>) watchlistDoc.get("movies");
        List<Movie> movies = new ArrayList<>();
        
        if (movieDocs != null) {
            for (Document doc : movieDocs) {
                Movie movie = new Movie();
                
                // Map all fields from the document
                movie.setCategoryId(doc.getInteger("category_id", 0));
                movie.setCategoryName(doc.getString("category_name"));
                movie.setMovieName(doc.getString("movie_name"));
                movie.setRealisateur(doc.getString("realisateur"));
                movie.setPoster(doc.getString("poster"));
                movie.setDateRealisation(doc.getString("date_de_realisation"));
                movie.setDateSortie(doc.getString("date_de_sortie"));
                movie.setSynopsis(doc.getString("synopsis"));
                
                // Handle liste_acteurs_principales
                Object acteurs = doc.get("liste_acteurs_principales");
                if (acteurs instanceof List<?>) {
                    @SuppressWarnings("unchecked")
                    List<String> acteursList = (List<String>) acteurs;
                    movie.setListeActeurs(acteursList);
                }
                
                movies.add(movie);
            }
        }
        
        watchlist.setMovies(movies);
        return watchlist;
    }
}