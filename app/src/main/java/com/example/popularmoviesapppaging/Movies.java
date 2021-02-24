package com.example.popularmoviesapppaging;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "popular_movies")
public class Movies {
    private String title;

    @NonNull
    @PrimaryKey
    private String id;
    private String overview;
    private String image;
    private String release_date;
    private String vote_average;
    private String popularity;
    private int markedAsFavorite;


    public Movies(String title, String id, String overview, String image, String release_date,
                  String vote_average, String popularity, int markedAsFavorite) {
        this.title = title;
        this.id = id;
        this.overview = overview;
        this.image = image;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.popularity = popularity;
        this.markedAsFavorite = markedAsFavorite;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public void setMarkedAsFavorite(int markedAsFavorite) {
        this.markedAsFavorite = markedAsFavorite;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getOverview() {
        return overview;
    }

    public String getImage() {
        return image;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getVote_average() {
        return vote_average;
    }

    public String getPopularity() {
        return popularity;
    }

    public int getMarkedAsFavorite() {
        return markedAsFavorite;
    }
}
