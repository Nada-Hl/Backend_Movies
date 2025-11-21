package com.monprojet.ressources;

import com.monprojet.models.Movie;
import com.monprojet.models.Watchlist;
import com.monprojet.services.WatchListServices;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import java.util.Collections;

@Path("/watchlist")
public class WatchlistRessource {

    private final WatchListServices watchlistService = new WatchListServices();

    @POST
    @Path("/user/{userId}")
    public Response addMovieToWatchlist(@PathParam("userId") int userId, Movie movie) {
        
        if (movie == null || movie.getMovieName() == null || movie.getMovieName().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Movie object and its title are required.").build();
        }

        try {
            int result = watchlistService.AddMovieToWatchList(userId, movie);
            
            if (result == 1) {
                // Movie successfully added
                return Response.status(Response.Status.CREATED)
                        .entity("Movie successfully added to watchlist for user: " + userId).build();
            } else if (result == 0) {
                // Movie already exists
                return Response.status(Response.Status.CONFLICT)
                        .entity("Movie already exists in watchlist for user: " + userId).build();
            } else if (result == -1) {
                // Watchlist is full
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Watchlist is full. Maximum 5 movies allowed.").build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Unexpected error occurred.").build();
            }
            
        } catch (Exception ex) {
            System.err.println("Error adding movie to watchlist: " + ex.getMessage());
            ex.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to add movie to watchlist.").build();
        }
    }

    @GET
    @Path("/user/{userId}")
    public Response getWatchlist(@PathParam("userId") int userId) {
        try {
            Watchlist watchlist = watchlistService.getWatchlist(userId); 
            
            if (watchlist == null || watchlist.getMovies().isEmpty()) {
                return Response.status(Response.Status.OK)
                        .entity(Collections.emptyList()) 
                        .build();
            }
            
            return Response.status(Response.Status.OK)
                    .entity(watchlist.getMovies())
                    .build();
            
        } catch (Exception ex) {
            System.err.println("Error fetching watchlist for user " + userId + ": " + ex.getMessage());
            ex.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to retrieve watchlist.").build();
        }
    }
}