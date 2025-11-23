package com.monprojet.models;

import java.time.LocalDate;

public class Review {
    private int userId;
    private int movieId;
    private String content;
    private int rating;
    private LocalDate viewingDate;
    private String viewingLocation;
    
    // Constructeurs
    public Review() {}
    
    public Review(int userId, int movieId, String content, int rating) {
        this.userId = userId;
        this.movieId = movieId;
        this.content = content;
        this.rating = rating;
    }
    
    public Review(int userId, int movieId, String content, int rating, 
                  LocalDate viewingDate, String viewingLocation) {
        this.userId = userId;
        this.movieId = movieId;
        this.content = content;
        this.rating = rating;
        this.viewingDate = viewingDate;
        this.viewingLocation = viewingLocation;
    }
    
    // Getters
    public int getUserId() { return userId; }
    public int getMovieId() { return movieId; }
    public String getContent() { return content; }
    public int getRating() { return rating; }
    public LocalDate getViewingDate() { return viewingDate; }
    public String getViewingLocation() { return viewingLocation; }
    
    // Setters
    public void setUserId(int userId) { this.userId = userId; }
    public void setMovieId(int movieId) { this.movieId = movieId; }
    public void setContent(String content) { this.content = content; }
    public void setRating(int rating) { this.rating = rating; }
    public void setViewingDate(LocalDate viewingDate) { this.viewingDate = viewingDate; }
    public void setViewingLocation(String viewingLocation) { this.viewingLocation = viewingLocation; }
    
    // Validation
    public boolean isValid() {
        // Commentaire obligatoire et limité à 800 caractères
        if (content == null || content.isBlank() || content.length() > 800) {
            return false;
        }
        
        // Note obligatoire entre 1 et 5
        if (rating < 1 || rating > 5) {
            return false;
        }
        
        // Validation du lieu si présent
        if (viewingLocation != null && !viewingLocation.isBlank()) {
            String[] validLocations = {"cinema", "platform", "cd_bluray", "streaming", "other"};
            boolean isValidLocation = false;
            for (String loc : validLocations) {
                if (loc.equalsIgnoreCase(viewingLocation)) {
                    isValidLocation = true;
                    break;
                }
            }
            if (!isValidLocation) {
                return false;
            }
        }
        
        return true;
    }
}