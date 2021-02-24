package com.example.popularmoviesapppaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.popularmoviesapppaging.database.MoviesViewModel;
import com.example.popularmoviesapppaging.utils.AppExecutors;
import com.example.popularmoviesapppaging.utils.JsonMovieUtils;
import com.example.popularmoviesapppaging.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    public static final String VND_YOUTUBE = "vnd.youtube";
    public static final String FRAGMENT_DISPLAYED = "FRAGMENT_DISPLAYED";
    ImageView mImageView;
    TextView titleTextView;
    TextView overviewTextView;
    TextView releaseDateTextView;
    TextView popularityTextView;
    TextView userRatingTextView;
    ImageView markFav;

    public final String REVIEWS = "reviews";
    public final String VIDEOS = "videos";
    public final String PREFS_FILES = "com.example.popularmoviesapp.PREFS";

    private static String TRAILER_BASE_URL = "https://www.youtube.com/watch?v=";

    private String image;
    private String title;
    private String overview;
    private String releaseDate;
    private String popularity;
    private String userRating;
    private String id;
    boolean isMarkedAsFavorite;
    int marked;
    MoviesViewModel moviesViewModel;
    static SharedPreferences preferences;
    private Movies favMovie;
    List<String> trailerKeys = new ArrayList<>();
    boolean isFragmentDisplayed = false;

    LinearLayout trailer1;
    LinearLayout trailer2;

    TextView trailer1TextView;
    TextView trailer2TextView;
    TextView borderTextView;

    FrameLayout frameLayout;

    public void mark() {
        //mark movie as favorite
        // marked has value of 1
        //not marked has value of 0
        if (marked == 0) {
            isMarkedAsFavorite = false;
            marked = 1;
            markFav.setImageResource(R.drawable.mark_as_favorite);
            // isMarkedAsFavorite = true;
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    favMovie = new Movies(title, id, overview, image, releaseDate, userRating, popularity, marked);
                    moviesViewModel.insert(favMovie);
                }
            });
        } else {
            isMarkedAsFavorite = true;
            marked = 0;
            markFav.setImageResource(R.drawable.ic_baseline_star_rate_24);
            //isMarkedAsFavorite = false;
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    favMovie = new Movies(title, id, overview, image, releaseDate, userRating, popularity, marked);
                    moviesViewModel.delete(favMovie);
                }
            });
        }
    }

    public void loadVideoKeys() {
        //load Youtube trailer  keys
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                URL url = NetworkUtils.trailerReviewsUrl(id, VIDEOS);

                List<String> resultFromJson = null;

                try {
                    String networkResponse = NetworkUtils.getResponseFromHttpUrl(url);
                    resultFromJson = JsonMovieUtils.trailerJson(networkResponse);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

                trailerKeys = resultFromJson;

                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (trailerKeys != null) {
                            if (trailerKeys.size() == 0) {
                                trailer1TextView.setText(R.string.trailer_not_available);
                                trailer1.setEnabled(false);
                                trailer2.setVisibility(View.GONE);
                                borderTextView.setVisibility(View.GONE);

                            } else if (trailerKeys.size() == 1) {
                                trailer1.setEnabled(true);
                                trailer2.setVisibility(View.GONE);
                                borderTextView.setVisibility(View.GONE);

                            } else {
                                trailer1.setEnabled(true);
                                trailer2.setEnabled(true);
                            }
                        } else {
                            return;
                        }

                    }
                });
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        //initialize views
        mImageView = findViewById(R.id.poster_iv);
        titleTextView = findViewById(R.id.tv_title);
        overviewTextView = findViewById(R.id.tv_overview);
        releaseDateTextView = findViewById(R.id.tv_date_released);
        popularityTextView = findViewById(R.id.tv_popularity);
        userRatingTextView = findViewById(R.id.tv_rating);
        markFav = findViewById(R.id.markFav);
        trailer1 = findViewById(R.id.trailer1);
        trailer2 = findViewById(R.id.trailer2);
        trailer1TextView = findViewById(R.id.textViewTrailer1);
        trailer2TextView = findViewById(R.id.textViewTrailer2);
        borderTextView = findViewById(R.id.borderline);

        frameLayout = findViewById(R.id.fragment_container);

        //frameLayout.setVisibility(View.GONE);

        moviesViewModel = new ViewModelProvider(this).get(MoviesViewModel.class);

        preferences = getSharedPreferences(PREFS_FILES, MODE_PRIVATE);

        if (savedInstanceState != null) {
            Picasso.get().load(savedInstanceState.getString(MainActivity.IMAGE)).into(mImageView);
            titleTextView.setText(savedInstanceState.getString(MainActivity.TITLE));
            overviewTextView.setText(savedInstanceState.getString(MainActivity.OVERVIEW));
            releaseDateTextView.setText(savedInstanceState.getString(MainActivity.RELEASE_DATE));
            popularityTextView.setText(savedInstanceState.getString(MainActivity.POPULARITY));
            userRatingTextView.setText(savedInstanceState.getString(MainActivity.USER_RATING));
            id = savedInstanceState.getString(MainActivity.ID);
            isFragmentDisplayed = savedInstanceState.getBoolean(FRAGMENT_DISPLAYED);
        }

        //check if fragment is showing
        if (isFragmentDisplayed) {
            displayFragment();
        } else {
            closeFragment();
        }

        //get intent values of selected movie from MainActivity
        Intent movieSelected = getIntent();

        image = movieSelected.getStringExtra(MainActivity.IMAGE);
        title = movieSelected.getStringExtra(MainActivity.TITLE);
        overview = movieSelected.getStringExtra(MainActivity.OVERVIEW);
        releaseDate = movieSelected.getStringExtra(MainActivity.RELEASE_DATE);
        popularity = movieSelected.getStringExtra(MainActivity.POPULARITY);
        userRating = movieSelected.getStringExtra(MainActivity.USER_RATING);
        id = movieSelected.getStringExtra(MainActivity.ID);
        marked = preferences.getInt(id, 0);

        favMovie = new Movies(title, id, overview, image, releaseDate, userRating, popularity, marked);


        //check if movie is selected as favourite
        if (marked == 1) {
            markFav.setImageResource(R.drawable.mark_as_favorite);
        } else {
            markFav.setImageResource(R.drawable.ic_baseline_star_rate_24);
        }

        setTitle(title);

        //populate views
        Picasso.get().load(image).into(mImageView);
        titleTextView.setText(title);
        overviewTextView.setText(overview);
        releaseDateTextView.setText(releaseDate);
        popularityTextView.setText(popularity);
        userRatingTextView.setText(userRating);

        loadVideoKeys();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(MainActivity.IMAGE, image);
        outState.putString(MainActivity.TITLE, title);
        outState.putString(MainActivity.OVERVIEW, overview);
        outState.putString(MainActivity.RELEASE_DATE, releaseDate);
        outState.putString(MainActivity.POPULARITY, popularity);
        outState.putString(MainActivity.USER_RATING, userRating);
        outState.putBoolean(FRAGMENT_DISPLAYED, isFragmentDisplayed);
        outState.putString(MainActivity.ID, id);
    }


    public void markAsFavorite(View view) {
        mark();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor sharedPreferences = preferences.edit();
        sharedPreferences.putInt(id, marked);
        sharedPreferences.apply();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
         onBackPressed();
         return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        closeFragment();
        super.onBackPressed();
    }

    public void trailerOne(View view){
        playTrailer(trailerKeys.get(0));
    }

    public void trailerTwo(View view){
        playTrailer(trailerKeys.get(1));
    }

    private void playTrailer(String keys) {
        Uri appUri = Uri.parse(VND_YOUTUBE + keys);
        Uri webUri = Uri.parse(TRAILER_BASE_URL + keys);
        Intent appIntent = new Intent(Intent.ACTION_VIEW, appUri);
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webUri);

        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex){
            startActivity(webIntent);
        }
    }

    public void readViews(View view) {
        displayFragment();
    }

    public void displayFragment() {
        frameLayout.setVisibility(View.VISIBLE);
        ReviewsFragment fragment = ReviewsFragment.newInstance(id);

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager != null) {
            fragmentManager.popBackStackImmediate();
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null);

        fragmentTransaction.add(R.id.fragment_container, fragment)
                .commit();

        isFragmentDisplayed = true;
    }

    public void closeFragment() {
        FragmentManager manager = getSupportFragmentManager();

        ReviewsFragment reviewsFragment = (ReviewsFragment) manager
                .findFragmentById(R.id.fragment_container);

        if (reviewsFragment != null) {
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
                    fragmentTransaction.remove(reviewsFragment).commit();
                    isFragmentDisplayed = false;
        }
       frameLayout.setVisibility(View.GONE);
    }
}