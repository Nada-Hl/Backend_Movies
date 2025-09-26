package com.monprojet.services;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.monprojet.db.MongoDBConnection;
import com.monprojet.models.Movie;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MovieServices {
    private final MongoDatabase db = MongoDBConnection.getDatabase();
    private final MongoCollection<Document> movieCollection = db.getCollection("movies");
    //private final CategoryServices categoryService = new CategoryServices();


    // Fetching all the movies
    public List<Movie> getAllMovies() {
        List<Movie> list = new ArrayList<>();
        for (Document d : movieCollection.find()) {
            int movieId = d.getInteger("movie_id", 0);
            int categoryId = d.getInteger("category_id", 0);
            String categoryName = d.getString("category_name");
            String movieName = d.getString("movie_name");
            String realisateur = d.getString("realisateur");
            String poster = d.getString("poster");
            String dateReal = d.getString("date_de_realisation");
            String dateSortie = d.getString("date_de_sortie");
            String synopsis = d.getString("synopsis");
            List<String> acteurs = d.getList("liste_acteurs_principales", String.class);

            Movie m = new Movie(movieId, categoryId, categoryName, movieName,
                                realisateur,poster, dateReal, dateSortie, synopsis,
                                acteurs != null ? acteurs : new ArrayList<>());
            list.add(m);
        }
        return list;
    }

    
}
