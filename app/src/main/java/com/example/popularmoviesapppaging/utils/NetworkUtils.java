package com.example.popularmoviesapppaging.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    //this requires an API_KEY from TMDB
    private static String TAG = NetworkUtils.class.getSimpleName();
    private static String BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static String API_KEY = "provide own key";
    private static String API_PARAM = "api_key";
    private static String   LANGUAGE = "en-US";
    private static String LANG_PARAM = "language";
    private static String ID_PARAM = "movie_id";


    private static String IMAGE_URL ="http://image.tmdb.org/t/p/";
    private static String SIZE = "w500";

    /**
     * Build image URl
     */
    public static URL imageUrl(String imageUrl){
        Uri builtImageUrl = Uri.parse(IMAGE_URL).buildUpon()
                .appendEncodedPath(SIZE)
                .appendEncodedPath(imageUrl)
                .build();

        URL url = null;

        try {
            url = new URL(builtImageUrl.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
      //  Log.i(TAG, "BUILT IMAGE URL " + url);

        return url;

    }

    /**
     * Builds URL used using an API key
     */

    public static URL buildUrl (String sortOrder) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(sortOrder)
                .appendQueryParameter(API_PARAM,API_KEY)
                .appendQueryParameter(LANG_PARAM, LANGUAGE)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //Log.i(TAG, "BUILT URL " + url);

        return url;
    }

    public static URL trailerReviewsUrl (String id, String sortOrder) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(id)
                .appendEncodedPath(sortOrder)
                .appendQueryParameter(API_PARAM,API_KEY)
                .appendQueryParameter(LANG_PARAM, LANGUAGE)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();

            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }

    }
}
