package com.monprojet.models;

import java.util.List;

public class Watchlist {
    private int userID ; 
	private List<Movie> movies ; 
	
	public Watchlist() {
		// TODO Auto-generated constructor stub
	}

	public Watchlist(int userID) {
		super();
		this.userID = userID;
	}

	public int getUserID() {
		return userID;
	}


	public Watchlist(int userID, List<Movie> movies) {
		super();
		this.userID = userID;
		this.movies = movies;
	}
    
	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	void addMovieToWatchlist(Movie movie)
	{
		movies.add(movie);
	}

	public List<Movie> getMovies() {
		return movies;
	}

	public void setMovies(List<Movie> movies) {
		this.movies = movies;
	}

}
