package com.monprojet.models;
import java.util.List;

public class Movie {
    private int movie_id;
    private int category_id;
    private String category_name;
    private String movie_name;
    private String realisateur;
    private String poster;
    private String date_de_realisation;
    private String date_de_sortie;
    private String synopsis;
    private List<String> liste_des_acteurs_principales;


    public Movie() {}

    public Movie(int movie_id, int category_id, String category_name, String movie_name,
                    String realisateur,String poster,String date_de_realisation, String date_de_sortie, String synopsis, List<String> liste_des_acteurs_principales) {

        this.movie_id=movie_id;
        this.category_id=category_id;
        this.category_name=category_name;
        this.movie_name=movie_name;
        this.realisateur=realisateur;
        this.poster=poster;
        this.date_de_realisation=date_de_realisation;
        this.date_de_sortie=date_de_sortie;
        this.synopsis=synopsis;
        this.liste_des_acteurs_principales=liste_des_acteurs_principales;
    }

    //Getters
    public int getMovieId() {return this.movie_id;}
    public int getCategoryId() {return this.category_id;}
    public String getCategoryName() {return this.category_name;}
    public String getMovieName() {return this.movie_name;}
    public String getRealisateur() {return this.realisateur;}
    public String getPoster() {return this.poster;}
    public String getDateRealisation() {return this.date_de_realisation;}
    public String getDateSortie() {return this.date_de_sortie;}
    public String getSynopsis() {return this.synopsis;}
    public List<String> getListeActeurs() {return this.liste_des_acteurs_principales;}


    //Setters
    public void setMovieId(int movie_id){this.movie_id=movie_id;}
    public void setCategoryId(int category_id){this.category_id=category_id;}
    public void setCategoryName(String category_name){this.category_name=category_name;}
    public void setMovieName(String movie_name){this.movie_name=movie_name;}
    public void setRealisateur(String realisateur){this.realisateur=realisateur;}
    public void setPoster(String poster){this.poster=poster;}
    public void setDateRealisation(String date_de_realisation){this.date_de_realisation=date_de_realisation;}
    public void setDateSortie(String date_de_sortie){this.date_de_sortie=date_de_sortie;}
    public void setSynopsis(String synopsis){this.synopsis=synopsis;}
    public void setListeActeurs(List<String> liste_des_acteurs_principales){this.liste_des_acteurs_principales=liste_des_acteurs_principales;}



}
