package com.movieapp;

public class Films {

    private  int id;

    private String filmtitle;

    private String filmcategory;

    private String filmcomments;

    private String filmdate;

    Films(){

    }

    public String getFilmcomments() {
        return filmcomments;
    }

    public void setFilmcomments(String filmcomments) {
        this.filmcomments = filmcomments;
    }

    Films(int id, String filmtitle, String filmcategory, String filmcomments, String filmdate){
        this.id = id;
        this.filmtitle = filmtitle;
        this.filmcategory = filmcategory;
        this.filmcomments = filmcomments;
        this.filmdate = filmdate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilmtitle() {
        return filmtitle;
    }

    public void setFilmtitle(String filmtitle) {
        this.filmtitle = filmtitle;
    }

    public String getFilmcategory() {
        return filmcategory;
    }

    public void setFilmcategory(String filmcategory) {
        this.filmcategory = filmcategory;
    }

    public String getFilmdate() {
        return filmdate;
    }

    public void setFilmdate(String filmdate) {
        this.filmdate = filmdate;
    }
}
