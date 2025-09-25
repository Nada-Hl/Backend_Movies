package com.monprojet.ressources;


import com.monprojet.models.Movie;
import com.monprojet.services.MovieServices;
import jakarta.ws.rs.*;

import java.util.List;

@Path("/movies")

public class MovieRessource {
    private final MovieServices service = new MovieServices();

    @GET
    public List<Movie> getAll() {
        return service.getAllMovies();
    }


}
