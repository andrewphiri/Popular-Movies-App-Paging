package com.example.popularmoviesapppaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.popularmoviesapppaging.adapters.MovieAdapter;
import com.example.popularmoviesapppaging.database.MoviesViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String IMAGE = "com.example.popularmoviesapp.IMAGE";
    public static final String TITLE = "com.example.popularmoviesapp.TITLE";
    public static final String ID = "com.example.popularmoviesapp.Id";
    public static final String RELEASE_DATE = "com.example.popularmoviesapp.RELEASE_DATE";
    public static final String POPULARITY = "com.example.popularmoviesapp.POPULARITY";
    public static final String USER_RATING = "com.example.popularmoviesapp.USER_RATING";
    public static final String OVERVIEW = "com.example.popularmoviesapp.OVERVIEW";
    public static final String MOVIE_LIST_STATE = "com.example.popularmoviesapp.MOVIE_LIST_STATE";
    public static final String FRAGMENT_LIST_STATE = "com.example.popularmoviesapp.FRAGMENT_LIST_STATE";
    public static String MARKED_FAV;

    public static final String MOST_POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    private String sharedPrefFile = "com.example.popularmoviesapp";

    public static List<Movies> movies;

    int listCurrentlyShowing;
    SharedPreferences sharedPreferences;

    MoviesViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            listCurrentlyShowing = savedInstanceState.getInt(MOVIE_LIST_STATE, 1);
        } else {
            sharedPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
            listCurrentlyShowing = sharedPreferences.getInt(MOVIE_LIST_STATE, 1);
            sharedPreferences.edit().clear().apply();
        }


        mViewModel = new ViewModelProvider(this).get(MoviesViewModel.class);


        if (listCurrentlyShowing == 1) {
            loadMostPopularMoviesData();
            listCurrentlyShowing = 1;

        } else if (listCurrentlyShowing == 2){
            loadTopRatedMoviesData();
            listCurrentlyShowing = 2;

        } else if (listCurrentlyShowing == 3){
            loadFavoritesList();
            listCurrentlyShowing = 3;
        }

    }

    public void loadMostPopularMoviesData() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        MostPopularMoviesFragment popularMoviesFragment = MostPopularMoviesFragment.newInstance();

        if (fragmentManager != null) {
            fragmentTransaction.replace(R.id.movie_fragment_container, popularMoviesFragment)
                    .commit();
        } else{
            fragmentTransaction.add(R.id.movie_fragment_container, popularMoviesFragment)
                    .commit();
        }
    }

    public void loadTopRatedMoviesData() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        TopRatedMoviesFragment topRatedMoviesFragment = TopRatedMoviesFragment.newInstance();

            if (fragmentManager != null) {
                fragmentTransaction.replace(R.id.movie_fragment_container, topRatedMoviesFragment).addToBackStack(FRAGMENT_LIST_STATE)
                        .commit();
            } else {
                fragmentTransaction.add(R.id.movie_fragment_container, topRatedMoviesFragment).addToBackStack(FRAGMENT_LIST_STATE)
                        .commit();
            }
    }

//    @Override
//    public void onItemClick(List<Movies> mData, int position) {
//        Intent intent = new Intent(this, DetailsActivity.class);
//        intent.putExtra(IMAGE, mData.get(position).getImage());
//        intent.putExtra(TITLE, mData.get(position).getTitle());
//        intent.putExtra(ID, mData.get(position).getId());
//        intent.putExtra(OVERVIEW, mData.get(position).getOverview());
//        intent.putExtra(USER_RATING, mData.get(position).getVote_average() + "/10");
//        intent.putExtra(RELEASE_DATE, mData.get(position).getRelease_date());
//        intent.putExtra(POPULARITY, mData.get(position).getPopularity());
//        intent.putExtra(MARKED_FAV, mData.get(position).getMarkedAsFavorite());
//
//        startActivity(intent);
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuItemSelected = item.getItemId();
         switch (menuItemSelected) {

             case R.id.action_refresh:
                 if(listCurrentlyShowing == 1) {
                     loadMostPopularMoviesData();
                 } else if (listCurrentlyShowing == 2){
                     loadTopRatedMoviesData();
                 } else if (listCurrentlyShowing == 3){
                     loadFavoritesList();
                 }
                 break;
             case R.id.action_most_popular:
                 loadMostPopularMoviesData();

                 listCurrentlyShowing = 1;
                 break;
             case R.id.action_highest_rated:
                 loadTopRatedMoviesData();
                listCurrentlyShowing = 2;

                 break;

             case R.id.action_favorites:
                 loadFavoritesList();
                 listCurrentlyShowing = 3;
             default:
         }
        return super.onOptionsItemSelected(item);
    }

    private void loadFavoritesList() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        FavoritesMoviesFragment favoritesMoviesFragment = FavoritesMoviesFragment.newInstance();

        if (fragmentManager != null) {
            fragmentTransaction.replace(R.id.movie_fragment_container, favoritesMoviesFragment).addToBackStack(FRAGMENT_LIST_STATE)
                    .commit();
        } else {
            fragmentTransaction.add(R.id.movie_fragment_container, favoritesMoviesFragment).addToBackStack(FRAGMENT_LIST_STATE)
                    .commit();
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(MOVIE_LIST_STATE, listCurrentlyShowing);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
            closeFragment();
    }

    public void closeFragment() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = (Fragment) manager.findFragmentById(R.id.movie_fragment_container);

        if (fragment == null) {
            finish();
        }
    }

        @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor preferences = sharedPreferences.edit();
        preferences.putInt(MOVIE_LIST_STATE, listCurrentlyShowing);
        preferences.apply();
    }
}