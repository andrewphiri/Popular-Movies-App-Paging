package com.example.popularmoviesapppaging.utils;

import com.example.popularmoviesapppaging.Movies;
import com.example.popularmoviesapppaging.Reviews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonMovieUtils {

    public static List<Movies> parsedMovieJson(String movieJson) throws JSONException {

        final String MOVIES_LIST = "results";
        final String TITLE = "original_title";
        final String OVERVIEW = "overview";
        final String IMAGE = "poster_path";
        final String POPULARITY = "popularity";
        final String RATING = "vote_average";
        final String RELEASE_DATE = "release_date";
        final String MOVIE_ID = "id";
        int favorite = 0;

        String movieTitle= null;
        String movieOverview = null;
        String moviePoster = null;
        String dateReleased = null;
        String id = null;
        String moviePopularity = null;
        String movieRating = null;

        Movies popularMovies = null;
        List<Movies> moviesList = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(movieJson);

        JSONArray moviesArray = jsonObject.getJSONArray(MOVIES_LIST);

        for(int i = 0; i < moviesArray.length(); i++) {
            JSONObject movieJsonObject = moviesArray.getJSONObject(i);

            if (movieJsonObject.has(TITLE)) {
                movieTitle = movieJsonObject.getString(TITLE);
            } else {
                movieTitle = "N/A";
            }

            if (movieJsonObject.has(OVERVIEW)) {
                movieOverview = movieJsonObject.getString(OVERVIEW);
            } else {
                movieOverview = "N/A";
            }

            if (movieJsonObject.has(IMAGE)) {
                moviePoster = NetworkUtils.imageUrl(movieJsonObject.getString(IMAGE)).toString();
            } else {
                moviePoster = "N/A";
            }

            if (movieJsonObject.has(RELEASE_DATE)) {
                dateReleased = movieJsonObject.getString(RELEASE_DATE);
            } else {
                dateReleased = "N/A";
            }

            if (movieJsonObject.has(POPULARITY)) {
                moviePopularity = movieJsonObject.getString(POPULARITY);
            }

            if (movieJsonObject.has(RATING)) {
                movieRating = movieJsonObject.getString(RATING);
            }

            if (movieJsonObject.has(MOVIE_ID)) {
                id = movieJsonObject.getString(MOVIE_ID);
            }
            popularMovies = new Movies(movieTitle, id, movieOverview, moviePoster,
                    dateReleased, movieRating, moviePopularity, favorite);

            moviesList.add(popularMovies);
        }

        return moviesList;

    }

    public static List<String> trailerJson(String trailer) throws JSONException {
        String jsonArrayName = "results";
        String key = "key";

        List<String> trailerKeys = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(trailer);
        JSONArray jsonArray = jsonObject.getJSONArray(jsonArrayName);

        for (int i = 0 ; i < jsonArray.length(); i++) {
            JSONObject trailerObject = jsonArray.getJSONObject(i);

            if (trailerObject.has(key)) {
               trailerKeys.add(trailerObject.getString(key));
            }
        }

        return trailerKeys;
    }

    public static List<Reviews> reviewJson(String reviews) throws JSONException {
        String jsonArrayName = "results";
       final String AUTHOR = "author";
       final String CONTENT = "content";
       final String USERNAME = "username";
       final String DATE =  "created_at";

       String authorReview;
       String contentRev = null;
       String dateOfRev = null;

       Reviews movieReviews = null;

        List<Reviews> reviewContent = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(reviews);
        JSONArray jsonArray = jsonObject.getJSONArray(jsonArrayName);

        for (int i = 0 ; i < jsonArray.length(); i++) {
            JSONObject reviewObject = jsonArray.getJSONObject(i);

            if (reviewObject.has(AUTHOR)) {
                authorReview = reviewObject.getString(AUTHOR);
            } else {
                authorReview = reviewObject.getString(USERNAME);
            }

            if (reviewObject.has(CONTENT)) {
                contentRev = reviewObject.getString(CONTENT);
            }

            if (reviewObject.has(DATE)) {
                dateOfRev = reviewObject.getString(DATE);
            }

            movieReviews = new Reviews(contentRev, authorReview, dateOfRev);

            reviewContent.add(movieReviews);
        }

        return reviewContent;
    }
}
