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

    // Adding or creating a movies with all possible values
    public Movie createMovie(Movie m) {
        //Getting the movies category name and category id and checking if it exists
        int categoryId = m.getCategoryId();
        String categoryName = m.getCategoryName();

        if ((categoryId == 0) && (categoryName == null || categoryName.isBlank())) {
            throw new IllegalArgumentException("You must provide category_id or category_name");
        }
        // get next movie id from the counters database
        int newMovieId = CounterServices.getNextSequence("movie_id");

        Document doc = new Document("movie_id", newMovieId)
                .append("category_id", categoryId)
                .append("category_name", categoryName)
                .append("movie_name", m.getMovieName())
                .append("realisateur", m.getRealisateur())
                .append("poster", m.getPoster())
                .append("date_de_realisation", m.getDateRealisation())
                .append("date_de_sortie", m.getDateSortie())
                .append("synopsis", m.getSynopsis())
                .append("liste_acteurs_principales", m.getListeActeurs());

        movieCollection.insertOne(doc);

        m.setMovieId(newMovieId);
        return m;
    }

    
}
