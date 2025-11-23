package com.monprojet.ressources;

import com.monprojet.models.Review;
import com.monprojet.services.ReviewServices;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/reviews")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReviewRessource {

    private final ReviewServices reviewService = new ReviewServices();

    /**
     * Payload pour ajouter un review
     */
    public static class ReviewPayload {
        private int userId;
        private int movieId;
        private String content;
        private int rating;
        private String viewingDate;
        private String viewingLocation;
        
        // Constructeur par défaut (obligatoire pour Jackson)
        public ReviewPayload() {}
        
        // Getters
        public int getUserId() { return userId; }
        public int getMovieId() { return movieId; }
        public String getContent() { return content; }
        public int getRating() { return rating; }
        public String getViewingDate() { return viewingDate; }
        public String getViewingLocation() { return viewingLocation; }
        
        // Setters
        public void setUserId(int userId) { this.userId = userId; }
        public void setMovieId(int movieId) { this.movieId = movieId; }
        public void setContent(String content) { this.content = content; }
        public void setRating(int rating) { this.rating = rating; }
        public void setViewingDate(String viewingDate) { this.viewingDate = viewingDate; }
        public void setViewingLocation(String viewingLocation) { this.viewingLocation = viewingLocation; }
    }

    /**
     * Ajouter un review
     * POST /reviews
     */
    @POST
    public Response addReview(ReviewPayload payload) {
        System.out.println("=== RECEIVED PAYLOAD ===");
        System.out.println("userId: " + payload.getUserId());
        System.out.println("movieId: " + payload.getMovieId());
        System.out.println("content: " + payload.getContent());
        System.out.println("rating: " + payload.getRating());
        System.out.println("viewingDate: " + payload.getViewingDate());
        System.out.println("viewingLocation: " + payload.getViewingLocation());
        System.out.println("========================");

        // Validation des champs obligatoires
        if (payload == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Request body is required.").build();
        }
        
        if (payload.userId <= 0 || payload.movieId <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Valid userId and movieId are required.").build();
        }
        
        if (payload.content == null || payload.content.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Comment is required.").build();
        }
        
        if (payload.content.length() > 800) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Comment must not exceed 800 characters.").build();
        }
        
        if (payload.rating < 1 || payload.rating > 5) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Rating must be between 1 and 5 stars.").build();
        }
        
        // Validation du lieu si présent
        if (payload.viewingLocation != null && !payload.viewingLocation.isBlank()) {
            String[] validLocations = {"cinema", "platform", "cd_bluray", "streaming", "other"};
            boolean isValid = false;
            for (String loc : validLocations) {
                if (loc.equalsIgnoreCase(payload.viewingLocation)) {
                    isValid = true;
                    break;
                }
            }
            if (!isValid) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Invalid viewing location. Accepted values: cinema, platform, cd_bluray, streaming, other").build();
            }
        }

        try {
            // Créer l'objet Review
            Review review = new Review();
            review.setUserId(payload.userId);
            review.setMovieId(payload.movieId);
            review.setContent(payload.content);
            review.setRating(payload.rating);
            
            // Champs facultatifs
            if (payload.viewingDate != null && !payload.viewingDate.isBlank()) {
                try {
                    review.setViewingDate(LocalDate.parse(payload.viewingDate));
                } catch (Exception e) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("Invalid date format. Use YYYY-MM-DD").build();
                }
            }
            
            if (payload.viewingLocation != null && !payload.viewingLocation.isBlank()) {
                review.setViewingLocation(payload.viewingLocation);
            }
            
            // Ajouter le review
            int result = reviewService.addReview(review);
            
            if (result == 1) {
                return Response.status(Response.Status.CREATED)
                        .entity("Review successfully added.").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Review validation failed.").build();
            }
            
        } catch (Exception ex) {
            System.err.println("Error in addReview endpoint: " + ex.getMessage());
            ex.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to add review.").build();
        }
    }

    /**
     * Récupérer tous les reviews d'un film
     * GET /reviews/movie/{movieId}
     */
    @GET
    @Path("/movie/{movieId}")
    public Response getReviewsForMovie(@PathParam("movieId") int movieId) {
        try {
            List<Review> reviews = reviewService.getReviewsForMovie(movieId);
            
            return Response.status(Response.Status.OK)
                    .entity(reviews)
                    .build();
            
        } catch (Exception ex) {
            System.err.println("Error fetching reviews for movie " + movieId + ": " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to retrieve reviews.").build();
        }
    }
    
    /**
     * Récupérer la note moyenne d'un film
     * GET /reviews/movie/{movieId}/average
     */
    @GET
    @Path("/movie/{movieId}/average")
    public Response getAverageRating(@PathParam("movieId") int movieId) {
        try {
            double average = reviewService.getAverageRating(movieId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("movieId", movieId);
            result.put("averageRating", Math.round(average * 10.0) / 10.0); // Arrondi à 1 décimale
            result.put("totalReviews", reviewService.getReviewsForMovie(movieId).size());
            
            return Response.status(Response.Status.OK)
                    .entity(result)
                    .build();
            
        } catch (Exception ex) {
            System.err.println("Error calculating average rating: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to calculate average rating.")
                    .build();
        }
    }
}