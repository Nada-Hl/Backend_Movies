package com.monprojet.models;

public class Category {

    private int category_id;
    private String category_name;

    public Category(){}

    public Category(int category_id, String category_name){
        this.category_id=category_id;
        this.category_name=category_name;
    }

    // Getters
    public int getCategoryID(){return this.category_id;}
    public String getCategoryName(){return this.category_name;}

    // Setters
    public void setCategoryName(String category_name){this.category_name=category_name;}
    public void setCategoryID(int category_id){this.category_id=category_id;}

    
}
