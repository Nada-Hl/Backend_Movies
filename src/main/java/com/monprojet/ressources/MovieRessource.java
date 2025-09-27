package com.monprojet.ressources;


import com.monprojet.models.Movie;
import com.monprojet.services.MovieServices;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;


import java.util.List;

@Path("/movies")

public class MovieRessource {
    private final MovieServices service = new MovieServices();

    @GET
    public List<Movie> getAll() {
        return service.getAllMovies();
    }


    @POST
    public Response create(Movie movie) {
        if (movie == null ||
            movie.getCategoryId() == 0 && (movie.getCategoryName() == null || movie.getCategoryName().isBlank()) ||
            movie.getMovieName() == null || movie.getMovieName().isBlank() ||
            movie.getRealisateur() == null || movie.getRealisateur().isBlank() ||
            movie.getPoster() == null || movie.getPoster().isBlank() ||
            movie.getDateRealisation() == null || movie.getDateRealisation().isBlank() ||
            movie.getDateSortie() == null || movie.getDateSortie().isBlank() ||
            movie.getSynopsis() == null || movie.getSynopsis().isBlank() ||
            movie.getListeActeurs() == null || movie.getListeActeurs().isEmpty()) 
        {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("All fields are required (category, movieName, realisateur, poster, dates, synopsis, listeActeurs)").build();
        }
        try {
            Movie created = service.createMovie(movie);
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

}
