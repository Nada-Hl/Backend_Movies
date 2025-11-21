package com.monprojet.models;

public class User {
    private int id ; 
	private boolean isAdmin ; 
	
	
	
	public User(int id, boolean isAdmin) {
		super();
		this.id = id;
		this.isAdmin = isAdmin;
	}


	public User() {
		// TODO Auto-generated constructor stub
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin =isAdmin;
	}

}
